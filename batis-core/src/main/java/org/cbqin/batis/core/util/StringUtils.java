package org.cbqin.batis.core.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/22
 */
public class StringUtils {
    /**
     * 正则表达式中具有特殊含义的字符
     */
    public static final List<Character> SPECIAL_CHARACTERS = Arrays.asList(
            '-'
            , '['
            , ']'
            , '/'
            , '{'
            , '}'
            , '('
            , ')'
            , '*'
            , '+'
            , '?'
            , '.'
            , '\\'
            , '^'
            , '$'
            , '|');

    /**
     * 转义普通字符串中的特殊字符
     * 大多数情况可以使用 {@link java.util.regex.Pattern#quote(String str)}方法代替
     * 少数情况下使用此方法, 如原本字符串的一部分是正则表达式，另一部分是运行时生成的
     *
     * @param string 原始字符串
     * @return 处理过后的字符串
     */
    public static String escapeRegularString(String string) {

        char[] chars = string.toCharArray();
        StringBuilder builder = new StringBuilder();

        for (char c : chars) {
            if (SPECIAL_CHARACTERS.contains(c)) {
                builder.append("\\").append(c);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
