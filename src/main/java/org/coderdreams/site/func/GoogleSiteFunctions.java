package org.coderdreams.site.func;

import org.coderdreams.conditions.CustomerConditions;
import org.coderdreams.dom.User;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Conditional(CustomerConditions.GoogleCondition.class)
@Service
class GoogleSiteFunctions implements SiteFunctions {

    @Override
    public boolean canDeleteUserAccounts(User u) {
        //Google doesnt want to ever delete user accounts
        return false;
    }

}