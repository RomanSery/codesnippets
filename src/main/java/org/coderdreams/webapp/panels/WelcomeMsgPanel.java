package org.coderdreams.webapp.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.service.UserService;

public class WelcomeMsgPanel extends Panel {
    @SpringBean private UserService userService;
    public WelcomeMsgPanel(String id) {
        super(id);
        add(new Label("userName", userService.getUserName()));
    }
}
