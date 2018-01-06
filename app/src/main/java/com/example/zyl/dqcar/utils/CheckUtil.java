package com.example.zyl.dqcar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查工具类
 */
public class CheckUtil {

    /**
     * 判断两个string是否相等
     */
    public static boolean checkEquels(Object strObj0, Object strObj1) {
        String str0 = strObj0 + "";
        String str1 = strObj1 + "";
        if (str0.equals(str1)) {
            return true;
        }
        return false;
    }

    /**
     * 同时判断多个参数是否为空
     *
     * @param strArray
     * @return
     */
    public static boolean isNull(Object... strArray) {
        for (Object obj : strArray) {
            if (!"".equals(obj + "")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否为空
     */
    public static boolean isNull(Object strObj) {
        String str = strObj + "";
        if (!"".equals(str) && !"null".equals(str)) {
            return false;
        }
        return true;
    }

    /**
     * 检查邮箱
     *
     * @param strObj
     * @return
     */
    public static boolean checkEmail(Object strObj) {
        String str = strObj + "";
        String match = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 是否为电话号码
     */
    public static boolean checkPhone(Object phoneNumber) {
        boolean isValid = false;
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber + "";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 检查str的长度是否达到要求
     *
     * @param strObj
     * @param length
     * @return
     */
    public static boolean checkLength(Object strObj, int length) {
        String str = strObj + "";
        if (str.length() >= length) {
            return true;
        }
        return false;
    }

    /**
     * 检查字符串的长度
     *
     * @param strObj
     * @param length
     * @return
     */
    public static boolean checkLengthEq(Object strObj, int length) {
        String str = strObj + "";
        if (str.length() == length) {
            return true;
        }
        return false;
    }

    public static boolean checkLength(Object strObj, int start, int end) {
        String str = strObj + "";
        if (str.length() >= start && str.length() <= end) {
            return true;
        }
        return false;
    }

    /**
     * string是否为电话号码
     *
     * @param value
     * @return
     */
    public static boolean checkIsPhone(String value) {
        if (value.startsWith("1")) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否Url
     *
     * @param url
     * @return
     */
    public static boolean checkURL(String url) {
        String regular = "(http|https)://[\\S]*";
        return Pattern.matches(regular, url);
    }

    /**
     * 检查是否卡号
     *
     * @param idCardNumber
     * @return
     */
    public static boolean checkIdCardNumber(String idCardNumber) {
        String regular = "^(\\d{15}|\\d{17}[\\dxX])$";
        return Pattern.matches(regular, idCardNumber);
    }


    /**
     * 检查密码等级
     *
     * @param pwd
     * @return
     */
    public static String checkPWDLevel(String pwd) {
        //【邀请注册】密码强度判断不正确，6-7位的密码一定为弱；
        // 8-10位输入类型有3种以上为强，不足2种的为中，1种为弱；
        // 11-12位一种输入类型为中，两种以上为强
        String low = "弱";
        String middle = "中";
        String hight = "强";
        if (CheckUtil.isNull(pwd)) {
            return low;
        }


        int pwdLength = pwd.length();
        if (pwdLength < 8) {
            return low;
        } else if (pwd.length() >= 8 && pwd.length() < 11) {

            if (checkContainChar(pwd) && checkContainDigit(pwd) && checkContainLetter(pwd)) {
                return hight;
            } else if (
                    (checkContainChar(pwd) && checkContainDigit(pwd)) ||
                            (checkContainDigit(pwd) && checkContainLetter(pwd)) ||
                            (checkContainChar(pwd) && checkContainLetter(pwd))) {
                return middle;

            } else {
                return low;
            }


        } else {
            if ((checkContainChar(pwd) && checkContainDigit(pwd)) ||
                    (checkContainDigit(pwd) && checkContainLetter(pwd)) ||
                    (checkContainChar(pwd) && checkContainLetter(pwd))) {
                return hight;
            } else {
                return middle;
            }

        }

    }


    /**
     * 检查是否包含字符
     *
     * @return true 包含字符
     */
    public static boolean checkContainChar(String pwd) {
        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否包含数字
     *
     * @return true 包含字符
     */
    public static boolean checkContainDigit(String pwd) {
        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查是否包含字母
     *
     * @return true 包含字符
     */
    public static boolean checkContainLetter(String pwd) {
        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }
}
