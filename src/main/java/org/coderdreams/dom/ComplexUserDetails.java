package org.coderdreams.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComplexUserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double height;
    private Double weight;
    private String nickname;
    private List<UserAddress> addresses;


    public ComplexUserDetails() {
        super();
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<UserAddress> getAddresses() {
        if(addresses == null) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }

    public void setAddresses(List<UserAddress> addresses) {
        this.addresses = addresses;
    }
}