package com.example.shoppingflow.DTO;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Address implements Serializable {
    private String streetName;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
