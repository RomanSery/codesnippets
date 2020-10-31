package org.coderdreams.webapp.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.coderdreams.dom.User;
import org.coderdreams.util.HtmlFragment;

public class UserNameFragment extends HtmlFragment {
    private static final long serialVersionUID = 1L;

    public UserNameFragment(String id, IModel<User> userModel) {
        super(id);

        add(new Label("fname", new PropertyModel<>(userModel, "firstName")));
        add(new Label("lname", new PropertyModel<>(userModel, "lastName")));
    }

    @Override
    protected String getHtml() {
        return "<p>first name: <span wicket:id=\"fname\"></span></p>" +
                "<p>last name: <span wicket:id=\"lname\"></span></p>";
    }
}
