package org.coderdreams.webapp.page;

import java.util.Optional;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dao.ComplexUserRepository;
import org.coderdreams.dom.ComplexUser;
import org.coderdreams.dom.ComplexUserDetails;
import org.coderdreams.dom.PhysicalAddress;
import org.coderdreams.webapp.BasePage;

public class HibernateTestPage extends BasePage implements IBasePage {

    @SpringBean
    private ComplexUserRepository complexUserRepository;

    public HibernateTestPage() {
        super();

        ComplexUserDetails details = new ComplexUserDetails();
        details.setHeight(95.0d);
        details.setWeight(220.0d);
        details.setNickname("micky");

        details.getFavoriteMovies().add("Terminator");
        details.getFavoriteMovies().add("Star Trek");

        PhysicalAddress a1 = new PhysicalAddress();
        a1.setId(System.nanoTime());
        a1.setAddress1("123 test");
        a1.setCity("NY");
        details.getAddresses().add(a1);

        PhysicalAddress a2 = new PhysicalAddress();
        a2.setId(System.nanoTime());
        a2.setPrimary(true);
        a2.setAddress1("444 primary st");
        a2.setCity("NY");
        details.getAddresses().add(a2);

        ComplexUser u = new ComplexUser();
        u.setEmail("test@test.com");
        u.setDisplayName("roman");
        u.setUserDetails(details);
        u = complexUserRepository.save(u);


        Optional<ComplexUser> u2 = complexUserRepository.findById(u.getId());
        if(u2.isPresent()) {
            u2.get().getUserDetails().setMobilePhone("123-666-6666");
            complexUserRepository.save(u2.get());
        }

        setMarkup(Markup.of(String.valueOf(u.getId())));
    }

}
