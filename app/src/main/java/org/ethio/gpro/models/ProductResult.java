package org.ethio.gpro.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductResult {
    @JsonProperty("products")
    private List<Product> productList;
    @JsonProperty("trending")
    private List<Product> trendingProducts;
    @JsonProperty("recommended")
    private List<Product> recommendedProducts;

    public List<Product> getProductList() {
        return productList;
    }

    public List<Product> getTrendingProducts() {
        return trendingProducts;
    }

    public List<Product> getRecommendedProducts() {
        return recommendedProducts;
    }
}
