package com.minis.web.http;

import com.minis.util.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mqz
 */
public class DefaultHttpMessageConverter implements HttpMessageConverter{


    String defaultContentType = "text/json;charset=UTF-8";

    String defaultCharacterEncoding = "UTF-8";

    ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DefaultHttpMessageConverter() {}

    @Override
    public void write(Object obj, HttpServletResponse resp) throws IOException {
        resp.setContentType(defaultContentType);
        resp.setCharacterEncoding(defaultCharacterEncoding);
        writeInternal(obj, resp);
        resp.flushBuffer();
    }


    private void writeInternal(Object obj, HttpServletResponse resp) throws IOException {
        String jsonStr = this.objectMapper.writeValuesAsString(obj);
        resp.getWriter().write(jsonStr);
    }
}
