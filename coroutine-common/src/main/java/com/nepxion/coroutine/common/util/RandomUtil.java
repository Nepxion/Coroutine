package com.nepxion.coroutine.common.util;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {
    public static int random(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max value can't be less than min value");
        }
        Random random = new Random();

        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static String numericRandom(int max) {
        return RandomStringUtils.randomNumeric(max);
    }

    public static String uuidRandom() {
        return UUID.randomUUID().toString();
    }
}