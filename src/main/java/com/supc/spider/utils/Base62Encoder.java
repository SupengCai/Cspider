package com.supc.spider.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:xyang@wisorg.com">xyang</a>
 * @version V1.0, 15/1/9
 */
public class Base62Encoder {

    private static final char[] CHARS = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    public static String encode(long num) {
        StringBuilder builder = new StringBuilder(10);
        if (num < 0) {
            builder.append(CHARS[0]);
            num = -num;
        }
        int i;
        do {
            i = (int) (num % 62l);
            builder.append(CHARS[i]);
            num = num / 62;
        } while (num > 0);
        return builder.toString();
    }

    public static String encode(long... nums) {
        StringBuilder builder = new StringBuilder(nums.length * 10);
        for (long number : nums) {
            if (number < 0) {
                builder.append(CHARS[0]);
                number = -number;
            }
            int remainder;
            do {
                remainder = (int) (number % 62l);
                builder.append(CHARS[remainder]);
                number = number / 62;
            } while (number > 0);
        }
        return builder.toString();
    }

    public static long decode(String string) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalArgumentException("Cannot decode null/empty string");
        }
        char[] array = string.toCharArray();
        long num = 0;
        boolean negative = false;
        for (int index = array.length - 1; index >= 0; index--) {
            char c = array[index];
            if (index == 0 && c == CHARS[0]) {
                negative = true;
                break;
            }
            num = num * 62;
            if ('A' <= c && c <= 'Z') {
                num += (c - 'A');
                continue;
            }
            if ('0' <= c && c <= '9') {
                num += (c - '0' + 26);
                continue;
            }
            if ('a' <= c && c <= 'z') {
                num += (c - 'a' + 36);
                continue;
            }
            throw new IllegalArgumentException("String is not Base62 encoded");
        }
        return negative ? -num : num;
    }

}
