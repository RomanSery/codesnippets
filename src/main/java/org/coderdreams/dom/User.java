package org.coderdreams.dom;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.coderdreams.enums.CountryCode;
import org.coderdreams.enums.State;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String lastName;
    private String firstName;
    private boolean isActive;
    private String displayName;
    private List<String> favGenres;
    private State state;
    private CountryCode countryCode;

    public User() {

    }

    public User(int id, String lastName, String firstName, boolean isActive) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getFavGenres() {
        if(favGenres == null) {
            favGenres = new ArrayList<>();
        }
        return favGenres;
    }

    public void setFavGenres(List<String> favGenres) {
        this.favGenres = favGenres;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }
}
