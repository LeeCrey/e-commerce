package org.ethio.gpro.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "products_tbl")
@JsonRootName("product")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Product {
    @ColumnInfo(name = "id")
    @PrimaryKey
    @JsonProperty("id")
    private Integer id;

    @ColumnInfo(name = "name")
    @JsonProperty("name")
    private String name;

    @ColumnInfo(name = "rates")
    @JsonProperty("rates")
    private Float rate;

    @ColumnInfo(name = "price", index = true)
    @JsonProperty("price")
    private Double price;

    @ColumnInfo(name = "discount")
    @JsonProperty("discount")
    private Double discount;

    //    @ColumnInfo(name = "images")
    @Ignore
    @JsonProperty("images")
    private List<String> images;

    @ColumnInfo(name = "origin")
    @JsonProperty("origin")
    private String origin;

    @ColumnInfo(name = "description")
    @JsonProperty("description")
    private String description;

    @Ignore
    @JsonProperty("shop")
    private Shop shop;

    @ColumnInfo(name = "numberOfRates")
    @JsonProperty("numberOfRates")
    private Integer numberOfRates;

    // id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // price
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // discount
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    // shop
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    // rate
    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //
    public Integer getNumberOfRates() {
        return numberOfRates;
    }

    public void setNumberOfRates(Integer numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    @JsonIgnore
    public boolean isSimilar(Product newProduct) {
        return Objects.equals(getId(), newProduct.getId());
    }

    @JsonIgnore
    public boolean isContentTheSame(Product newProduct) {
        boolean nameTheSame = Objects.equals(getName(), newProduct.getName());
        boolean priceSame = Objects.equals(getPrice(), newProduct.getPrice());
        boolean idSame = Objects.equals(getId(), newProduct.getId());

        return nameTheSame && priceSame && idSame;
    }

    @JsonIgnore
    //
    public double getLastPrice() {
        if (discount != null) {
            return price - discount;
        }
        return price;
    }

    @JsonIgnore
    public List<String> getImages() {
        return images;
    }

    @JsonIgnore
    public void setImages(List<String> images) {
        this.images = images;
    }
}
