package org.example.services;
import org.example.models.Product;
import org.example.untils.CR;
import org.example.untils.GetValue;
import org.example.untils.SerializationUtil;
import java.util.ArrayList;
import java.util.List;


public class ProductService implements CR<Product> {
    public List<Product> productList;
    private final String fileName = "products.txt";
    public static ProductService INSTANCE_PRODUCT_SERVICE;

    public static ProductService getInstance() {
        if (INSTANCE_PRODUCT_SERVICE == null) {
            INSTANCE_PRODUCT_SERVICE = new ProductService();
        }
        return INSTANCE_PRODUCT_SERVICE;
    }
    public ProductService() {
        productList = (List<Product>) SerializationUtil.deserialize(fileName);
        if (productList != null) {
            productList = new ArrayList<>();
        }
    }
    @Override
    public void save() {
        SerializationUtil.serialize(productList, fileName);
    }


    @Override
    public void update(Product product) {
        List<Product> productsList =  ProductService.INSTANCE_PRODUCT_SERVICE.readFile();
        if(productsList != null) {
            for (Product p: productsList) {
                if (product.getId() == p.getId()) {
                    p.setQuantity(product.getQuantity());
                    p.setQuantityInStock(product.getQuantityInStock());
                    break;
                }
            }
            SerializationUtil.serialize(productsList, fileName);
        }
    }
    public void update(List<Product> products) {
        List<Product> productsList =  ProductService.INSTANCE_PRODUCT_SERVICE.readFile();
        if (productsList != null) {
            int k = 0;
            System.out.println(productsList);
            for (Product product : productsList) {
                if (k < products.size() && products.get(k).getId() == product.getId()) {
                    double a = product.getQuantityInStock() - products.get(k).getQuantity();
                    product.setQuantityInStock(a);
                    k++;
                }
            }
            SerializationUtil.serialize(productsList, fileName);
        }
    }

    @Override
    public List<Product> readFile() {
        return (List<Product>) SerializationUtil.deserialize(fileName);
    }


    @Override
    public Product find(int idT) {
        List<Product> products = ProductService.INSTANCE_PRODUCT_SERVICE.readFile();
        if (products != null) {
            if (products.stream().anyMatch(product -> product.getId()==idT && product.getStatus().equals("Còn hàng"))) {
                for (Product product: products) {
                    if (product.getId() == idT) {
                        return product;
                    }
                }
            } else {
                System.out.println("Sản phẩm có ID đã đã hết hàng hoặc ID đã nhập không chính xác.");
                return find(idT);
            }
        }
        return find(idT);
    }
    public Product find(List<Product> productLists,int idProduct) {
        if (productLists != null) {
            for (Product product: productLists) {
                if (product.getId() == idProduct) {
                    return product;
                }
            }
        }
        return find(productLists,GetValue.getInt("Nhập lại ID sản phẩm: "));
    }

}
