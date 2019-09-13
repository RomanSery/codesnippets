package org.coderdreams.webapp.page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.Literal;

public class PageSizeTest extends BasePage implements IBasePage {

    private static List<Integer> range = IntStream.rangeClosed(1, 500).boxed().collect(Collectors.toList());

    public PageSizeTest(PageParameters params) {
        super();

        final boolean useLiteral = params.get("literal") != null && "1".equals(params.get("literal").toString());
        final boolean useLdm = params.get("ldm") != null && "1".equals(params.get("ldm").toString());

        add(new DebugBar("debug"));

        add(new ListView<Integer>("listView", range) {
            @Override
            protected void populateItem(ListItem<Integer> item) {
                if(useLiteral) {
                    item.add(new Literal("num", getRandomString()));
                } else if(useLdm) {
                    item.add(new Label("num", new LoadableDetachableModel<String>() {
                        @Override
                        protected String load() {
                            return getRandomString();
                        }
                    }));
                } else {
                    item.add(new Label("num", getRandomString()));
                }
            }
        });

    }

    private String getRandomString() {
        return RandomStringUtils.random(100, true, true);
    }
}
