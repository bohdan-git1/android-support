package com.rapidzz.yourmusicmap.other.util;

/*
 * Created by ZubairAhmed on 1/6/18.
 */

import com.rapidzz.mymusicmap.other.util.SessionManager;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int toInt(String text, int defaultValue) {
        if (isDigitsOnly(text)) {
            return Integer.parseInt(text);
        }
        return defaultValue;

    }

    public static int toInt(Object object, int defaultValue) {
        try {
            if (object != null) {
                return (int) object;
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String toString(Float rating) {
        return String.valueOf(rating);

    }


    public static String toString(CharSequence string) {
        return string != null ? String.valueOf(string) : "";

    }


    /*public static String roundDoubleTwoDecimalPlaces(double d) {
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    public static String roundDoubleOneDecimalPlaces(double d) {
        return String.format(Locale.getDefault(), "%.1f", d);
    }*/

    public static double toDouble(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {

            return defaultValue;
        }
    }


    public static String string(Object object) {
        if (object != null)
            return String.valueOf(object);
        return "";
    }

    public static boolean isDigitsOnly(String text) {
        return !isEmpty(text) && text.matches("[0-9]+");
    }


    /**
     * 判断字符串是否为 null 或长度为 0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为 null 或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null 或全空格<br> {@code false}: 不为 null 且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为 null 或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null 或全空白字符<br> {@code false}: 不为 null 且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串 a
     * @param b 待校验字符串 b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串 a
     * @param b 待校验字符串 b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null 转为长度为 0 的字符串
     *
     * @param s 待转字符串
     * @return s 为 null 转为长度为 0 字符串，否则不改变
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null 返回 0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(final String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    public static String priorityHandler(double factor) {
        String value = Double.toString(factor);
        int index = value.indexOf(".");
        if (index != -1) return value.substring(0, index + 3);
        return String.valueOf(Math.ceil(factor));
    }

    public static String checkAndAddPlus(String phone) {
        return phone.contains("+") ? phone : "+".concat(phone);
    }

}