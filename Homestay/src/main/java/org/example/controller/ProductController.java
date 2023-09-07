package org.example.controller;

import org.example.models.*;
import org.example.server.Server;
import org.example.services.ProductService;

import java.io.IOException;
import java.util.List;

public class ProductController extends Controller {
    ProductService productService;
    private static ProductController INSTANCE_PRODUCT_CONTROLLER;

    public static ProductController getInstance() {
        if (INSTANCE_PRODUCT_CONTROLLER == null) {
            INSTANCE_PRODUCT_CONTROLLER = new ProductController();
        }
        return INSTANCE_PRODUCT_CONTROLLER;
    }
    @Override
    public void doGet(Request request, Response response) throws IOException {
        if(request.getAction().equals("show")) {
            response.setStatusCode("OK");
            response.setAction("show");

            User user = request.getUser();
            System.out.println("SERVER: ("+user.getRole()+") "+user.getName()+" : Xem danh sách sản phẩm.");
            List<Product> productList = List.copyOf(productService.readFile());
            response.setData(productList);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {

    }

    @Override
    public void doPut(Request request, Response response) throws IOException {

    }

    @Override
    public void doDelete(Request request, Response response) throws IOException {

    }

    @Override
    public void init(Server context, Request request, Response response) {
        productService = ProductService.getInstance();
        this.context = context;
        this.request = request;
        this.response = response;
        this.response.setController("/product");
    }
}
