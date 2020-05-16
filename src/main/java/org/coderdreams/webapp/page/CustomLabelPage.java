package org.coderdreams.webapp.page;

import org.apache.wicket.markup.html.basic.Label;
import org.coderdreams.webapp.BasePage;

public class CustomLabelPage extends BasePage implements IBasePage {

    public CustomLabelPage() {
        super();

        add(new Label("lbl1", getLbl("user.address.zip")));
        add(new Label("lbl2", getLbl("user.address.state")));

    }
}
