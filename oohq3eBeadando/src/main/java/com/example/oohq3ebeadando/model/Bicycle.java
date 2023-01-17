package com.example.oohq3ebeadando.model;

import javax.persistence.*;

@Entity
@Table(name = "bicycles")
public class Bicycle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="color")
    private String color;
    public Bicycle() {
    }

    @Column(name="brand")
    private String brand;

    public long getId() {
        return id;
    }
    @Column(name="type")
    private String type;
    @Column(name="tyreSize")
    private int tyreSize;

    public Bicycle(long id, String color, String brand, String type, int tyreSize) {
        this.id = id;
        this.color = color;
        this.brand = brand;
        this.type = type;
        this.tyreSize = tyreSize;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTyreSize() {
        return tyreSize;
    }

    public void setTyreSize(int tyreSize) {
        this.tyreSize = tyreSize;
    }


}
