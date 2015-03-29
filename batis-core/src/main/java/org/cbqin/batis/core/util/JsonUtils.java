package org.cbqin.batis.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.cbqin.batis.core.exception.JsonPathParseException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/20
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /*
    * 默认日期格式: ISO8601格式(兼容ECMAScript的实现), 时区默认为UTC
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final TimeZone DEFAULT_DATETIME_TIMEZONE = TimeZone.getTimeZone("UTC");

    /**
     * 将对象转换为json
     * <p>如果为基本数据类型，则返回其字符串形式</p>
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String objectToString(Object obj) {
        if (obj == null) return null;
        if (ClassUtils.isBasicType(obj.getClass())) {
            //基础数据类型和他们的包装类直接返回其字符串形式
            return obj.toString();

        } else if (obj instanceof Date) {
            DateTime dateTime = new DateTime(obj);
            dateTime = dateTime.toDateTime(DateTimeZone.forTimeZone(DEFAULT_DATETIME_TIMEZONE));
            return dateTime.toString(DEFAULT_DATETIME_FORMAT);
            //return JSON.toJSONStringWithDateFormat(dateTime.toDate(), DEFAULT_DATETIME_FORMAT).replace("\"", "");
        } else {
            //复杂数据类型序列化后返回
            return JSON.toJSONString(obj, SerializerFeature.UseISO8601DateFormat);
        }
    }

    /**
     * 将json字符串转换为对象
     *
     * @param jsonString json形式的字符串或者基础数据类型的字符串
     * @return 反序列化后的对象
     */
    public static Object getObjectFromString(String jsonString) {
        //基础数据类型
        if (!jsonString.contains("{")) {
            //不带"{"字符的肯定不是json
            return jsonString;
        }

        //反序列化
        try {
            return JSON.parse(jsonString);
        } catch (RuntimeException e) {
            //TODO: 如果是“1”呢，怎么处理
            return jsonString;
        }
    }


    /**
     * 解析JSON-Path的值
     * <p>JsonPath的语法可以参考<a href="https://github.com/alibaba/fastjson/wiki/JSONPath">JSON-Path</a></p>
     *
     * @param jsonPath   json path
     * @param rootObject 目标对象
     * @return 解析后的值
     */
    public static Object resolveJsonPath(String jsonPath, Object rootObject) {
        return JSONPath.eval(rootObject, jsonPath);
    }


    /**
     * 解析带命名根节点的json-path表达式的值
     * <p>所谓带顶点对象的表示式是指根节点是一个命名对象(而非传统的$)，该对象的相关信息存放在上下文参数中，如user.address.city</p>
     *
     * @param jsonPath json-path表达式
     * @param context  上下文
     * @return 解析后的值
     */
    public static Object resolveJsonPathWithNamedRoot(String jsonPath, Map<String, Object> context) {

        int pos = jsonPath.indexOf(".");

        //情况1: 没有通过点来获取深层数据,则直接获取对象本身
        if (pos == -1) {
            return context.get(jsonPath);
        }

        //情况2: 解析json-path，获取内部对象

        //2.1 先读取顶层对象
        String rootObjectName = jsonPath.substring(0, pos).trim();
        Object rootObject = context.get(rootObjectName);
        if (rootObject == null) {
            throw new JsonPathParseException("无法从参数中读取到顶层对象" + rootObjectName + "的值");
        }

        //2.2 根据json-path，从对象中读取path对应的内部对象值
        String standardJsonPathExpression = jsonPath.replaceFirst(Pattern.quote(rootObjectName), Matcher.quoteReplacement("$"));
        return JsonUtils.resolveJsonPath(standardJsonPathExpression, rootObject);
    }


    /**
     * 解析并替换指定字符串中的json-path表达式
     */
    public static String parseAndReplaceJsonPathExpression(Method method, Object[] args, String originalString) {
        Map<String, Object> context = ParameterUtils.getParameterContext(method, args);
        return JsonUtils.parseAndReplaceJsonPathExpression(context, originalString);
    }

    /**
     * 解析并替换指定字符串中的json-path表达式
     * <p>此处的json-path带有命名的根节点，即将$替换为了具体的对象名称</p>
     *
     * @param originalString expression的基本形式类似 #{obj.filed.subField}
     * @return 解析后的字符串
     */
    public static String parseAndReplaceJsonPathExpression(Map<String, Object> context, final String originalString) {
        logger.info("解析字符串 {} ", originalString);

        String resolvedString = originalString; //解析之后的字符串

        final Matcher matcher = Context.JSON_PATH_WRAPPER_PATTERN.matcher(originalString);
        while (matcher.find()) {

            final String jsonPath = matcher.group(1);
            logger.debug("--解析表达式 {}", jsonPath);

            //解析json-path, 获取目标对象
            Object targetObject = JsonUtils.resolveJsonPathWithNamedRoot(jsonPath.trim(), context);

            //校验
            if (targetObject == null) {
                throw new JsonPathParseException("无法从参数中读取到json-path表达式\"" + jsonPath + "\"的值");
            }

            //转换成string
            String resolvedValueFromJsonPath = JsonUtils.objectToString(targetObject);
            logger.debug("--表达式{}的值为: {}", jsonPath, resolvedValueFromJsonPath);

            //替换,获取解析之后的

            //转义json-path-wrapper字符串中在正则表达式中具有特殊含义的字符，目前包括 "{"和"}"
            String jsonPathWrapper = Context.PREFIX + jsonPath + Context.SUFFIX;
            String safeRegex = "\\s*" + StringUtils.escapeRegularString(jsonPathWrapper) + "\\s*";
            resolvedString = resolvedString.replaceFirst(safeRegex, Matcher.quoteReplacement(resolvedValueFromJsonPath));
        }

        logger.info("语句{}解析结果为: {}", originalString, resolvedString);
        return resolvedString;
    }
}
