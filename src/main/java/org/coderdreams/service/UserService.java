package org.coderdreams.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.coderdreams.dom.User;
import org.coderdreams.enums.UsernameDisplayType;
import org.coderdreams.util.SiteConfig;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private Random rand = new Random();

    public List<User> getAllUsers() {
        List<User> arr = new ArrayList<>();
        for(int i = 1; i <= 5000; i++) {
            User u = new User();
            u.setId(i);
            u.setFirstName("fname_"+i);
            u.setLastName("lname_"+i);
            arr.add(u);
        }
        return arr;
    }

    public Integer getUserCount() {
        return rand.nextInt((100 - 10) + 1) + 10;
    }

    public String getUserName() {
        return getUserName(getLoggedInUser());
    }

    public String getUserName(User usr) {
        //for some, if not active, return "Temp-user-${userId}"
        if(!usr.isActive() && SiteConfig.SHOW_INACTIVE_USERS_AS_TEMP.getBoolean()) {
            return "Temp-user-(" + usr.getId() + ")";
        }

        UsernameDisplayType displayType = SiteConfig.USER_DISPLAY_TYPE.getEnum(UsernameDisplayType.class);

        if(UsernameDisplayType.LAST_FIRST == displayType) {
            //last name, first name
            return usr.getLastName() + ", " + usr.getFirstName();
        } else if(UsernameDisplayType.FIRST_LAST_INITIAL == displayType) {
            //first name, last initial
            return usr.getFirstName() + ", " + usr.getLastName().charAt(0);
        } else if(UsernameDisplayType.LAST_USERID == displayType) {
            //last name (user ID)
            return usr.getLastName() + "(" + usr.getId() + ")";
        }
        return null;
    }


    public User getLoggedInUser() {
        //dummy method
        return new User(1, "Wicket", "Wicked", true);
    }

    public Integer getCurrUserId() {
        return 1;
    }
}
