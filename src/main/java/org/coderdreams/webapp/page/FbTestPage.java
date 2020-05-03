package org.coderdreams.webapp.page;

import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.behaviors.InitFbSdkBehavior;

public class FbTestPage extends BasePage implements IBasePage {

    public FbTestPage() {
        super();
        add(new InitFbSdkBehavior());
    }
}
