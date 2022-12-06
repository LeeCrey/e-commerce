package org.ethio.gpro.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.ethio.gpro.models.Product;

import java.util.List;

public class ProductShowResponse {
    @JsonProperty("product")
    private Product product;

    @JsonProperty("related_products")
    private List<Product> relatedProducts;

    public Product getProduct() {
        return product;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }
}
