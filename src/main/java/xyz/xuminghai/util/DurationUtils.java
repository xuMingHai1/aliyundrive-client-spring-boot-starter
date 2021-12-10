package xyz.xuminghai.util;

import java.time.Duration;
import java.time.temporal.Temporal;

/**
 * 两个时间个时间差文本
 *
 * @author xuMingHai
 */
public final class DurationUtils {

    private DurationUtils() {
    }

    /**
     * 求两个时间的差 返回示例：5小时，30分钟，12秒，332毫秒<br/>
     * 开始时间不能大于结束时间！！！
     *
     * @param startInclusive 开始时间
     * @param endExclusive   结束时间
     * @return 几小时，几分钟，几秒，几毫秒
     */
    public static String between(Temporal startInclusive, Temporal endExclusive) {
        final Duration between = Duration.between(startInclusive, endExclusive);
        if (between.isNegative()) {
            throw new IllegalArgumentException("开始时间大于结束时间！");
        }
        // 大部分情况下，都只有毫秒级别的信息
        if (between.isZero()) {
            return "0毫秒";
        }
        final StringBuilder sb = new StringBuilder();
        // 如果天不为0
        final long days = between.toDays();
        if (days > 0) {
            sb.append(days).append("天，");
        }

        // 如果小时不为0
        final long hours = between.toHours();
        if (hours > 0) {
            sb.append(hours & (24 - 1)).append("小时，");
        }

        // 如果分钟不为0
        final long minutes = between.toMinutes();
        if (minutes > 0) {
            sb.append(minutes & (60 - 1)).append("分钟，");
        }

        // 如果秒不为0
        final long seconds = between.getSeconds();
        if (seconds > 0) {
            sb.append(seconds & (60 - 1)).append("秒，");
        }

        final long millis = between.getNano() / 1000_000;
        sb.append(millis).append("毫秒");

        // 当毫秒等于0的时候，展示纳秒
        if (millis == 0) {
            sb.append("，").append(between.getNano() % 1000_000).append("纳秒");
        }
        return sb.toString();
    }

}
