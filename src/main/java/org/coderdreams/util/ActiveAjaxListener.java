package org.coderdreams.util;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;


/**
 * Changes the default ajax QUEUE behavior to ACTIVE for all ajax requests
 */
public class ActiveAjaxListener implements AjaxRequestTarget.IListener {

    private static final AjaxChannel ACTIVE_CHANNEL = new AjaxChannel(AjaxChannel.DEFAULT_NAME, AjaxChannel.Type.ACTIVE);

    @Override
    public void updateAjaxAttributes(AbstractDefaultAjaxBehavior behavior, AjaxRequestAttributes attributes) {
        attributes.setChannel(ACTIVE_CHANNEL);
    }
}
