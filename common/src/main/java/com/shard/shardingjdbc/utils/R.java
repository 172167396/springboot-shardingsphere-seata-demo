package com.shard.shardingjdbc.utils;

import com.shard.shardingjdbc.exception.AppRuntimeException;

public class R<T> {
    private int code;
    private T data;
    private String msg;

    private static final String SUCCESS_MSG = "操作成功";
    private static final String ERROR_MSG = "操作失败";

    public R() {

    }

    public R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> R<T> success() {
        return new R<>(0, null, SUCCESS_MSG);
    }

    public static <T> R<T> success(T data) {
        return new R<>(0, data, SUCCESS_MSG);
    }

    public static <T> R<T> success(T data, String msg) {
        return new R<>(0, data, msg);
    }

    public static <T> R<T> error() {
        return new R<>(-1, null, ERROR_MSG);
    }

    public static <T> R<T> error(int code) {
        return new R<>(code, null, ERROR_MSG);
    }

    public static <T> R<T> error(T data) {
        return new R<>(-1, data, ERROR_MSG);
    }

    public static <T> R<T> error(T data, String msg) {
        return new R<>(-1, data, msg);
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public T requireApiData() {
        if (!isSuccess()) {
            throw new AppRuntimeException(msg);
        }
        return data;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
