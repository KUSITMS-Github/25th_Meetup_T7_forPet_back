package com.kusitms.forpet.dto.response;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse<T> {
    private final static int SUCCESS = 200;
    public static final int CREATED = 201;
    private final static String SUCCESS_MESSAGE = "SUCCESS";
    private final static String CREATED_MESSAGE = "CREATED";
    private final static String UPDATED_MESSAGE = "UPDATED";
    private final static String BAD_REQUEST_MESSAGE = "Bad Request";

    private final ApiResponseHeader header;
    private final Map<String, T> body;

    public static <T> ApiResponse<T> success(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);

        return new ApiResponse(new ApiResponseHeader(SUCCESS, SUCCESS_MESSAGE), map);
    }

    public static <T> ApiResponse<T> created(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);

        return new ApiResponse(new ApiResponseHeader(CREATED, CREATED_MESSAGE), map);
    }
    public static <T> ApiResponse<T> updated(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);

        return new ApiResponse(new ApiResponseHeader(CREATED, UPDATED_MESSAGE), map);
    }

}
