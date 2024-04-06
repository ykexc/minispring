package com.minis.web.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mqz
 */
public interface HttpMessageConverter {

    void write(Object obj, HttpServletResponse resp) throws IOException;

}
