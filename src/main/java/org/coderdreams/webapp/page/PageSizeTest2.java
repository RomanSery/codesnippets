package org.coderdreams.webapp.page;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.coderdreams.webapp.BasePage;

public class PageSizeTest2 extends BasePage implements IBasePage {

    private static List<Integer> range = IntStream.rangeClosed(1, 500).boxed().collect(Collectors.toList());

    private static Random rand = new Random();

    public PageSizeTest2(PageParameters params) {
        super();

        final boolean useAttributeModifier = params.get("am") != null && "1".equals(params.get("am").toString());

        add(new DebugBar("debug"));

        add(new ListView<Integer>("listView", range) {
            @Override
            protected void populateItem(ListItem<Integer> item) {
                if(useAttributeModifier) {
                    Label lbl = new Label("num", new RandomStringModel());
                    lbl.add(new AttributeModifier("style", "background-color: "+getRandomColor()+";"));
                    item.add(lbl);
                } else {
                    item.add(new RandomColorLabel("num", new RandomStringModel()));
                }
            }
        });
    }

    private static final class RandomColorLabel extends Label {
        RandomColorLabel(String id, IModel<String> model) {
            super(id, model);
        }
        @Override
        protected void onComponentTag(final ComponentTag tag) {
            super.onComponentTag(tag);
            tag.put("style", "background-color: "+getRandomColor()+";");
        }
    }


    private static String getRandomString() {
        return RandomStringUtils.random(100, true, true);
    }

    private static String getRandomColor() {
        int rand_num = rand.nextInt(0xffffff + 1);
        return String.format("#%06x", rand_num);
    }

    private static final class RandomStringModel extends LoadableDetachableModel<String> {
        @Override
        protected String load() {
            return getRandomString();
        }
    }
}
