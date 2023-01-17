package com.example.oohq3ebeadando.model;

import javax.persistence.Entity;

import javax.persistence.*;

@Entity
@Table(name = "cars")
    public class Car {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;
        @Column(name="brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name="horsePower")
    private int horsePower;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (brand != null){
            this.brand = brand;
        }
    }

    public String getModel() {
        return model;
    }

    public Car() {
        super();
    }

    public Car(String brand, String model, int horsePower) {
        super();
        this.brand = brand;
        this.model = model;
        this.horsePower = horsePower;
    }

    public Car(long id, String brand, String model, int horsePower) {
        super();
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.horsePower = horsePower;
    }

    public void setModel(String model) {
        if (model != null){
            this.model = model;
        }
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        if (horsePower > 0 && horsePower < 2000){
            this.horsePower = horsePower;
        }
    }




}
