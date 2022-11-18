package org.ethio.gpro.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity(tableName = "carts_tbl")
@JsonRootName("cart")
public class Cart {
    @ColumnInfo(index = true)
    @PrimaryKey
    @JsonProperty("id")
    private Integer id;

    @ColumnInfo(index = true)
    @JsonProperty("name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
