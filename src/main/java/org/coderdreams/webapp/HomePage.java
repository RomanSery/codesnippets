package org.coderdreams.webapp;

import java.time.LocalDateTime;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class HomePage extends WebPage {

    public HomePage() {
        super();

        add(new Label("now", LocalDateTime.now().toString()));
    }
}
