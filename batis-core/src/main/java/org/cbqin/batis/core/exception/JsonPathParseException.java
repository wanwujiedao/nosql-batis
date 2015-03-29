package org.cbqin.batis.core.exception;

/**
 * @author qinchuanbao
 * @version 0.1.0
 * @email cbqin@gmail.com
 * @date 2015/3/24
 */
public class JsonPathParseException extends RuntimeException {
    private String jsonPathExpression;


    public JsonPathParseException(String message) {
        super(message);
    }

    public JsonPathParseException(String message, Exception e) {
        super(message, e);
    }

    public String getJsonPathExpression() {
        return jsonPathExpression;
    }

    public void setJsonPathExpression(String jsonPathExpression) {
        this.jsonPathExpression = jsonPathExpression;
    }
}
