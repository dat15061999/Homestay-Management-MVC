package org.example.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ObjectOutputStream;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
public class Response implements Serializable {
    private String method;
    private String statusCode;
    private Object data;
    private String action;
    private String controller;
    private transient ObjectOutputStream objectOutputStream;

    public Response(String method, String statusCode, Object data) {
        this.method = method;
        this.statusCode = statusCode;
        this.data = data;
    }
    public Response(String method, String statusCode, String action, String controller, Object data) {
        this.method = method;
        this.statusCode = statusCode;
        this.action = action;
        this.controller = controller;
        this.data = data;
    }
    @Override
    public String toString() {
        return "Response{" +
                "method='" + method + '\'' +
                "action='" + action + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", data=" + data +
                '}';
    }
}