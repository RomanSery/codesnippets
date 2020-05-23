package org.coderdreams.webapp.components;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dom.UserLock;
import org.coderdreams.locking.LockingService;

public class DisplayLocksPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private @SpringBean LockingService lockingService;

    public DisplayLocksPanel(String id, int listenerId, int objectId) {
        super(id);

        WebMarkupContainer locksContainer = new WebMarkupContainer("locksContainer") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(lockingService.findUserLocks(objectId).stream().anyMatch(ul -> ul.getListenerId() != listenerId));
            }
        };
        addOrReplace(locksContainer.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

        locksContainer.addOrReplace(new ListView<UserLock>("locksListView", new LoadableDetachableModel<List<UserLock>>() {
            @Override
            protected List<UserLock> load() {
                return lockingService.findUserLocks(objectId);
            }
        }) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<UserLock> item) {
                UserLock l = item.getModelObject();
                if (l.getListenerId() == listenerId) {
                    item.setVisible(false);
                    return;
                }
                item.add(new Label("lockMessage", "User Id: " + l.getUserId() + " - " + l.getListenerId()));
            }
        });
    }

}