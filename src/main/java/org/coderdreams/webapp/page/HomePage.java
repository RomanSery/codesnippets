package org.coderdreams.webapp.page;

import java.time.LocalDateTime;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.PanelFactory;
import org.coderdreams.webapp.panels.WelcomeMsgPanel;

public class HomePage extends BasePage {
    @SpringBean private PanelFactory panelFactory;

    public HomePage() {
        super();
        add(new Label("now", LocalDateTime.now().toString()));
        addOrReplace(panelFactory.newInstance(WelcomeMsgPanel.class, "welcomePanel", () -> new WelcomeMsgPanel("welcomePanel")));
    }
}
