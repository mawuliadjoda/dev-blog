package com.esprit.controller;

import com.esprit.producer.ProductProducer;
import com.esprit.proto.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductProducer productProducer;



    @GetMapping("/send")
    public String sendProduct() {

        for (int i = 0; i < 20; i++) {
            Product person = Product.newBuilder()
                    .setProductId(i)
                    .setName("Product" + i)
                    .setDescription("Product descriptoin" + i)
                    .build();
            productProducer.sendProduct(person);
        }

        return "Person sent!";
    }
}
