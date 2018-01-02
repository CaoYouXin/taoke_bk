package com.taoke.miquaner.util;

import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.view.AliMaMaSubmit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DivideByTenthUtil {

    private static final Logger logger = LogManager.getLogger(DivideByTenthUtil.class);

    public static Tenth get(ConfigRepo configRepo) {
        EConfig one = configRepo.findByKeyEquals(AliMaMaSubmit.DIVIDE_BY_TENTHS);
        if (null == one) {
            logger.warn("数据库中没有配置，使用默认5:2:3配置");
            return new Tenth(0.2, 0.3, 0.5);
        }

        String[] tenths = one.getValue().split(":");
        if (tenths.length != 3) {
            logger.warn("数据库中配置格式不正确，使用默认5:2:3配置");
            return new Tenth(0.2, 0.3, 0.5);
        }

        int first = 0;
        int second = 0;
        int platform = 0;
        try {
            first = Integer.parseInt(tenths[1]);
            second = Integer.parseInt(tenths[2]);
            platform = Integer.parseInt(tenths[0]);
        } catch (NumberFormatException e) {
            logger.warn("数据库中配置数字不正确，使用默认5:2:3配置");
            return new Tenth(0.2, 0.3, 0.5);
        }

        if (100 != first + second + platform) {
            logger.warn("数据库中配置总和不正确，使用默认5:2:3配置");
            return new Tenth(0.2, 0.3, 0.5);
        }

        return new Tenth(
                (double) first / 100,
                (double) second / 100,
                (double) platform / 100
        );
    }

    public static class Tenth {
        public double first;
        public double second;
        public double platform;

        public Tenth(double first, double second, double platform) {
            this.first = first;
            this.second = second;
            this.platform = platform;
        }
    }

}
