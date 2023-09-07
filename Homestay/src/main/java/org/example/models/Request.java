package org.example.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.User;

import java.io.ObjectInputStream;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor

public class Request implements Serializable,Cloneable {
    private String requestId;
    private String method;
    private String action;
    private String controller;
    private User user;
    private transient ObjectInputStream objectInputStream = null;
    private Object data;
    public Request(String requestId, String method, String action, String controller, ObjectInputStream objectInputStream, Object data) {
        this.requestId = requestId;
        this.method = method;
        this.action = action;
        this.controller = controller;
        this.objectInputStream = objectInputStream;
        this.data = data;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Request user = (Request) super.clone();
        user.setUser((User) user.getUser().clone());
        return super.clone();
    }

    @Override
    public String toString() {
        return
                " RequestId='" + requestId + '\'' +
                ", method='" + method + '\'' +
                ", action='" + action + '\'' +
                ", controller='" + controller + '\'' +
                ", user=" + user +
                ", data=" + data ;
    }
}
