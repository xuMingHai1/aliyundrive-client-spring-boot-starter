/*
 * Copyright (c) [2021] [xuMingHai]
 * [aliyundrive-client-spring-boot-starter] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package xyz.xuminghai.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import xyz.xuminghai.cache.Cache;
import xyz.xuminghai.core.ReactiveFileDao;
import xyz.xuminghai.core.ReactiveRecycleDao;
import xyz.xuminghai.core.impl.ReactiveFileDaoImpl;
import xyz.xuminghai.core.impl.ReactiveRecycleDaoImpl;
import xyz.xuminghai.exception.InvalidRefreshTokenException;
import xyz.xuminghai.template.BlockClientTemplate;
import xyz.xuminghai.template.ReactiveClientTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * webClient???????????????
 *
 * @author xuMingHai
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ClientProperties.class)
@ConditionalOnClass(WebClient.class)
@AutoConfigureAfter(CacheAutoConfiguration.class)
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ClientAutoConfiguration {

    private final WebClient webClient;
    /**
     * ?????????????????????
     */
    private final ClientProperties clientProperties;
    /**
     * ????????????????????????
     */
    private String refreshToken;

    /**
     * ??????????????????????????????
     *
     * @param clientProperties client?????????
     */
    public ClientAutoConfiguration(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
        this.refreshToken = Objects.requireNonNull(clientProperties.getRefreshToken(), "???????????????refreshToken????????????????????????????????????");
        this.webClient = WebClient.builder().codecs(clientCodecConfigurer -> {
                    final ClientCodecConfigurer.ClientDefaultCodecs clientDefaultCodecs = clientCodecConfigurer.defaultCodecs();
                    clientDefaultCodecs.maxInMemorySize(clientProperties.getMaxInMemorySize());
                    clientDefaultCodecs.enableLoggingRequestDetails(clientProperties.isEnableLoggingRequestDetails());
                })
                .build();
        scheduled(clientProperties.getFixedDelay());
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return this.webClient;
    }

    @Bean
    public ClientAspect clientAspect() {
        return new ClientAspect();
    }

    /*
        ----------?????????????????????????????????????????????????????????????????????????????????????????????------
     */

    @Bean
    @ConditionalOnBean(WebClient.class)
    public ReactiveFileDao reactiveFileDao(WebClient webClient) {
        return new ReactiveFileDaoImpl(webClient, clientProperties.getUploadRetries(), clientProperties.getUploadFragmentation());
    }

    @Bean
    @ConditionalOnBean(WebClient.class)
    public ReactiveRecycleDao reactiveRecycleDao(WebClient webClient) {
        return new ReactiveRecycleDaoImpl(webClient);
    }

    /*
        ----------?????????????????????????????????????????????????????????list??????????????????????????????------------
     */

    @Bean
    @ConditionalOnBean({Cache.class, ReactiveFileDao.class, ReactiveRecycleDao.class})
    public BlockClientTemplate blockClientTemplate(Cache cache, ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        return new BlockClientTemplate(cache, reactiveFileDao, reactiveRecycleDao);
    }

    @Bean
    @ConditionalOnBean({Cache.class, ReactiveFileDao.class, ReactiveRecycleDao.class})
    public ReactiveClientTemplate reactiveClientTemplate(Cache cache, ReactiveFileDao reactiveFileDao, ReactiveRecycleDao reactiveRecycleDao) {
        return new ReactiveClientTemplate(cache, reactiveFileDao, reactiveRecycleDao);
    }

    private void scheduled() {
        log.info("?????????????????????????????????????????????????????????????????????");
        final TokenResponse tokenResponse = refresh();

        this.refreshToken = tokenResponse.getRefreshToken();

        // ??????????????????????????????????????????
        try {
            // ????????????????????????
            final Class<TokenStatic> tokenStaticClass = TokenStatic.class;
            final Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            for (Field field : tokenStaticClass.getDeclaredFields()) {
                modifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
                final String[] split = field.getName().toLowerCase(Locale.ROOT).split("_");
                final StringBuilder methodName = new StringBuilder("get");
                for (String s : split) {
                    methodName.append(Character.toUpperCase(s.charAt(0)))
                            .append(s.substring(1));
                }
                try {
                    final Method method = tokenResponse.getClass().getMethod(methodName.toString());
                    field.set(tokenStaticClass, method.invoke(tokenResponse));
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                modifiers.setInt(field, field.getModifiers() | Modifier.FINAL);

            }
            modifiers.setAccessible(false);

            // ??????webClient???????????????
            final Field defaultRequest = this.webClient.getClass().getDeclaredField("defaultRequest");
            defaultRequest.setAccessible(true);
            Consumer<WebClient.RequestHeadersSpec<?>> consumer = requestHeadersSpec -> requestHeadersSpec.accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                    .headers(httpHeaders -> {
                        httpHeaders.setBearerAuth(TokenStatic.ACCESS_TOKEN);
                        httpHeaders.set(HttpHeaders.REFERER, "https://www.aliyundrive.com/");
                        httpHeaders.setOrigin("https://www.aliyundrive.com");
                        httpHeaders.set(HttpHeaders.USER_AGENT, "aliyundrive-client-spring-boot-starter");
                    });
            defaultRequest.set(this.webClient, consumer);
            defaultRequest.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        log.info("???????????????????????????????????????-----------------------------");
    }

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private void scheduled(int fixedDelay) {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this::scheduled, 0, fixedDelay, TimeUnit.SECONDS);
    }

    private TokenResponse refresh() {
        return WebClient.create()
                .post()
                .uri("https://api.aliyundrive.com/token/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(new InvalidRefreshTokenException("????????????????????????????????????????????????????????????????????????refreshToken???" + this.refreshToken)))
                .bodyToMono(TokenResponse.class)
                .doOnError(Throwable::printStackTrace)
                .block();

    }


    @Override
    public String toString() {
        return "{\"refresh_token\": \"" + this.refreshToken + "\"}";
    }


}
