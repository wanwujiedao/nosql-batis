package org.cbqin.batis.core.util;

import java.util.regex.Pattern;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/25
 */
public class Context {
    /**
     * json-path表达式的前缀
     */
    public static final String PREFIX = "#{";

    /**
     * json-path表达式的后缀
     */
    public static final String SUFFIX = "}";

    /**
     * 本类库中json-path表达式的正则表达式
     */
    //书写格式， 如 #{obj.f1.f2}， #{0.f1.f2}
    public static final Pattern JSON_PATH_WRAPPER_PATTERN;

    static {
        String regex = StringUtils.escapeRegularString(PREFIX)
                + "(\\s*([\\w0-9]+.{1,2})*[\\w0-9]+?\\s*)"
                + StringUtils.escapeRegularString(SUFFIX);
        JSON_PATH_WRAPPER_PATTERN = Pattern.compile(regex);
    }

}
