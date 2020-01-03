package org.coderdreams.webapp.page;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dao.ComplexUserRepository;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.enums.StatusType;
import org.coderdreams.webapp.BasePage;

public class GenerateUsersPage extends BasePage implements IBasePage {

    @SpringBean
    private ComplexUserRepository complexUserRepository;

    public GenerateUsersPage() {
        super();

        for(int i = 0; i < 100; i++) {

            String email = RandomStringUtils.random(10, true, false) + "@coderdreams.com";
            String name = RandomStringUtils.random(10, true, false);

            ComplexUser u = new ComplexUser();
            u.setStatus(StatusType.ACTIVE);
            u.setEmail(email);
            u.setDisplayName(name);
            u = complexUserRepository.save(u);
        }

        setMarkup(Markup.of("DONE"));
    }

}
