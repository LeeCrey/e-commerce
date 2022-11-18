package org.ethio.gpro.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

// non null
@Entity(tableName = "categories_tbl")
@JsonRootName("category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    @ColumnInfo(name = "id", index = true)
    @PrimaryKey
    @JsonProperty("id")
    private Integer id;

    @ColumnInfo(name = "priority", index = true, defaultValue = "2")
    @JsonProperty("priority")
    private Integer priority;

    @ColumnInfo(name = "name", index = true)
    @JsonProperty("name")
    private String name;

    @ColumnInfo(name = "amharic")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
