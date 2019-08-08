package org.coderdreams.webapp.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.coderdreams.util.CustomerSpecific;

@CustomerSpecific(replace = WelcomeMsgPanel.class, customer = "FB")
public class WelcomeMsgPanelFB extends WelcomeMsgPanel {
    public WelcomeMsgPanelFB(String id) {
        super(id);
        add(new Label("fbStatus", "FB-PENDING"));
    }
}
