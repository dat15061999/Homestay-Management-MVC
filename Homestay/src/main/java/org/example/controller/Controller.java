package org.example.controller;



import lombok.Getter;
import lombok.Setter;
import org.example.models.Request;
import org.example.models.Response;
import org.example.server.Server;

import java.io.IOException;
@Getter
@Setter
public abstract class Controller {

    protected Request request;
    protected Response response;
    protected Server context;
    public abstract void doGet(Request request, Response response) throws IOException;

    public abstract void doPost(Request request, Response response) throws IOException;
    public abstract void doPut(Request request, Response response) throws IOException;
    public abstract void doDelete(Request request, Response response) throws IOException;


    public abstract void init(Server context,Request request, Response response);

}
