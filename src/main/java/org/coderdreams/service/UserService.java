package org.coderdreams.service;

import org.coderdreams.dom.User;
import org.coderdreams.enums.UsernameDisplayType;
import org.coderdreams.util.SiteConfig;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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
}
