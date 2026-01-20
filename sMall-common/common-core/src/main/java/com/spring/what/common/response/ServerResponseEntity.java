package com.spring.what.common.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class ServerResponseEntity<T> implements Serializable {

    private String msg;

    private String code;

    private T data;

    @Override
    public String toString() {
        return "ServerResponseEntity{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
        serverResponseEntity.setCode(ResponseEnum.OK.value());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
        serverResponseEntity.setCode(ResponseEnum.OK.value());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> showFail(String message) {
        log.error(message);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ResponseEnum.SHOW_FAIL.value());
        serverResponseEntity.setMsg(message);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.value());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T failData) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.value());
        serverResponseEntity.setData(failData);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> transfer(ServerResponseEntity<?> serverResponse) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(serverResponse.getCode());
        serverResponseEntity.setMsg(serverResponseEntity.getMsg());
        log.error(serverResponseEntity.toString());
        return serverResponseEntity;
    }

}
