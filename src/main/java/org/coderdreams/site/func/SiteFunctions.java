package org.coderdreams.site.func;

import org.coderdreams.dom.User;

public interface SiteFunctions {

	default boolean canDeleteUserAccounts(User u) {
		//by default only allow deleting user accounts if inactive
		return u != null && !u.isActive();
    }


}
