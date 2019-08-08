package org.coderdreams.webapp;

import org.apache.wicket.markup.html.WebPage;
import org.coderdreams.util.Utils;


public abstract class BasePage extends WebPage {

    @Override
    public String getVariation() {
        String customer = Utils.getCustomer();
        if(customer != null) {
            return customer;
        }
        return super.getVariation();
    }
}
