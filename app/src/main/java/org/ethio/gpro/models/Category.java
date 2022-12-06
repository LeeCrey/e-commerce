package org.ethio.gpro.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

// non null
@JsonRootName("category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    @JsonProperty("id")
    private Integer categoryId;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("name")
    private String name;

    @JsonProperty("amharic")
    private String amharic;

    @JsonProperty("selected")
    private Boolean isSelected = false;

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public void setName(String cars) {
        name = cars;
    }

    @JsonIgnore
    public String getAmharic() {
        return amharic;
    }

    public void setAmharic(String amharic) {
        this.amharic = amharic;
    }

    @JsonIgnore
    public boolean isSelected() {
        return isSelected;
    }

    @JsonIgnore
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
