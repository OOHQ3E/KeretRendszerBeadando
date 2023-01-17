package com.example.oohq3ebeadando.model;

import javax.persistence.*;

@Entity
@Table(name = "rollerscooters")
public class RollerScooter {
    public long getId() {
        return id;
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

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
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

    public RollerScooter(long id, String color, int maxSpeed, String brand, String type) {
        this.id = id;
        this.color = color;
        this.maxSpeed = maxSpeed;
        this.brand = brand;
        this.type = type;
    }
    public RollerScooter(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="color")
    private String color;
    @Column(name="maxSpeed")
    private int maxSpeed;
    @Column(name="brand")
    private String brand;
    @Column(name="type")
    private String type;
}
