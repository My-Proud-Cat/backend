package com.study.proudcat.infra.utils;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class HttpResponseUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
		throws IOException {
		String responseBody = objectMapper.writeValueAsString(body);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(httpStatus.value());
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseBody);
	}

	public static void writeErrorResponse(HttpServletResponse response, HttpStatus httpStatus, Object data) throws
		IOException {
		String json = objectMapper.writeValueAsString(data);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(httpStatus.value());
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
}
