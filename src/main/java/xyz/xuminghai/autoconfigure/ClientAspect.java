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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import xyz.xuminghai.util.DurationUtils;

import java.time.Instant;

/**
 * 2021/11/5 15:00 星期五<br/>
 * 客户端切面
 *
 * @author xuMingHai
 */
@Aspect
@Slf4j
class ClientAspect {

    @Around("target(xyz.xuminghai.template.ReactiveClientTemplate)")
    private Object reactiveClientTemplateArdound(ProceedingJoinPoint pjp) throws Throwable {
        // 执行开始的时间戳
        final Instant startTime = Instant.now();
        // 执行方法
        final Mono<?> mono = (Mono<?>) pjp.proceed();
        // 获取执行对象的签名
        final Signature signature = pjp.getSignature();

        return mono.doOnError(throwable -> {
            WebClientResponseException e = (WebClientResponseException) throwable;
            log.error("发生的错误请求：{}", e.getMessage());
            log.error("阿里云盘响应的错误信息：{}", e.getResponseBodyAsString());
        }).doOnNext(o -> log.info("【{}#{}】执行成功耗时：{}", signature.getDeclaringTypeName(), signature.getName(), DurationUtils.between(startTime, Instant.now())));
    }

    @Pointcut("target(xyz.xuminghai.template.BlockClientTemplate)")
    private void blockClientTemplatePointcut() {
    }

    @AfterThrowing(pointcut = "blockClientTemplatePointcut()", throwing = "e")
    private void blockClientTemplateThrowing(WebClientResponseException e) {
        log.error("发生的错误请求：{}", e.getMessage());
        log.error("阿里云盘响应的错误信息：{}", e.getResponseBodyAsString());
    }

    @Around("blockClientTemplatePointcut()")
    private Object blockClientTemplateAround(ProceedingJoinPoint pjp) throws Throwable {
        // 执行开始的时间戳
        final Instant startTime = Instant.now();
        final Object o = pjp.proceed();
        // 获取执行对象的签名
        final Signature signature = pjp.getSignature();
        log.info("【{}#{}】执行成功耗时：{}", signature.getDeclaringTypeName(), signature.getName(), DurationUtils.between(startTime, Instant.now()));
        return o;
    }

}
