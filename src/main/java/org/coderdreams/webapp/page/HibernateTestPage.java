package org.coderdreams.webapp.page;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.ComplexUserDetails;
import org.coderdreams.service.CrudService;
import org.coderdreams.webapp.BasePage;

public class HibernateTestPage extends BasePage implements IBasePage {

    @SpringBean private CrudService crudService;

    public HibernateTestPage() {
        super();

        ComplexUserDetails details = new ComplexUserDetails();
        details.setHeight(95.0d);
        details.setWeight(220.0d);
        details.setNickname("micky");

        ComplexUser u = new ComplexUser();
        u.setEmail("test@test.com");
        u.setDisplayName("roman");
        u.setUserDetails(details);
        u = crudService.create(u);

        setMarkup(Markup.of(String.valueOf(u.getId())));
    }

}
