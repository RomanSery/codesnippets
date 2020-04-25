package org.coderdreams.webapp.page;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dom.User;
import org.coderdreams.site.func.SiteFunctions;

public class CustomerTestPage extends WebPage {
    @SpringBean private SiteFunctions siteFunctions;

    public CustomerTestPage() {
        super();

        User u = new User();
        u.setActive(false);

        setMarkup(Markup.of("Can delete user: " + siteFunctions.canDeleteUserAccounts(u)));
    }
}
