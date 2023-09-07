package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Messenger implements Serializable {
    private User user;
    private Object data;
    private String clientID;
    private String comment;

    public Messenger(String clientID, String comment) {
        this.clientID = clientID;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Messenger{" +
                "data=" + data +
                ", clientID='" + clientID + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
