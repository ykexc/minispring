package com.minis.web.servlet.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
	void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	default String getContentType() {
		return null;
	}
	void setContentType(String contentType);

	void setUrl(String url);
	String getUrl();

	void setRequestContextAttribute(String requestContextAttribute);
	String getRequestContextAttribute();

}