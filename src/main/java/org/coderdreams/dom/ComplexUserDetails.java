package org.coderdreams.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComplexUserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double height;
    private Double weight;
    private String nickname;
    private String officeFax;
    private String officePhone;
    private String officePhoneExt;
    private String mobilePhone;
    private String altEmail;

    private List<PhysicalAddress> addresses;
    private List<String> favoriteMovies;
    private List<String> favoriteQuotes;


    public ComplexUserDetails() {
        super();
    }

    public PhysicalAddress getPrimaryAddress() {
        return getAddresses().stream().filter(PhysicalAddress::isPrimary).findFirst().orElse(null);
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

    public List<PhysicalAddress> getAddresses() {
        if(addresses == null) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }

    public void setAddresses(List<PhysicalAddress> addresses) {
        this.addresses = addresses;
    }

    public String getOfficeFax() {
        return officeFax;
    }

    public void setOfficeFax(String officeFax) {
        this.officeFax = officeFax;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficePhoneExt() {
        return officePhoneExt;
    }

    public void setOfficePhoneExt(String officePhoneExt) {
        this.officePhoneExt = officePhoneExt;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAltEmail() {
        return altEmail;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public List<String> getFavoriteMovies() {
        if(favoriteMovies == null) {
            favoriteMovies = new ArrayList<>();
        }
        return favoriteMovies;
    }

    public void setFavoriteMovies(List<String> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public List<String> getFavoriteQuotes() {
        if(favoriteQuotes == null) {
            favoriteQuotes = new ArrayList<>();
        }
        return favoriteQuotes;
    }

    public void setFavoriteQuotes(List<String> favoriteQuotes) {
        this.favoriteQuotes = favoriteQuotes;
    }
}