package org.coderdreams.site.func;

import org.coderdreams.conditions.CustomerConditions;
import org.coderdreams.dom.User;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Conditional(CustomerConditions.FbCondition.class)
@Service
class FbSiteFunctions implements SiteFunctions {

    @Override
    public boolean canDeleteUserAccounts(User u) {
        //FB wants to be able to delete accounts regardless of status
        return u != null;
    }
}
