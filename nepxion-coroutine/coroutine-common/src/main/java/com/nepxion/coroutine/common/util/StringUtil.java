package com.nepxion.coroutine.common.util;

/**
 * <p>Title: Nepxion Coroutine</p>
 * <p>Description: Nepxion Coroutine For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class StringUtil {
    public static String firstLetterToUpper(String value) {
        Character character = Character.toUpperCase(value.charAt(0));

        return character.toString().concat(value.substring(1));
    }

    public static String firstLetterToLower(String value) {
        Character character = Character.toLowerCase(value.charAt(0));

        return character.toString().concat(value.substring(1));
    }
}