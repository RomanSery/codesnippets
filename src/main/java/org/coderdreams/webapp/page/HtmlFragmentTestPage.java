package org.coderdreams.webapp.page;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.coderdreams.dom.User;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.panels.UserNameFragment;

public class HtmlFragmentTestPage extends BasePage implements IBasePage {

    public HtmlFragmentTestPage() {
        super();

        addOrReplace(new ListView<User>("userList", LoadableDetachableModel.of(this::getUsers)) {
            @Override
            protected void populateItem(ListItem<User> item) {
                item.addOrReplace(new UserNameFragment("nameFrag", item.getModel()));
            }
        });
    }

    private List<User> getUsers() {
        User u1 = new User(1, "John", "James", true);
        User u2 = new User(2, "Mike", "Jones", true);
        return List.of(u1, u2);
    }
}
