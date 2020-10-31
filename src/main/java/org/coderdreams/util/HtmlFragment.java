package org.coderdreams.util;


import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

public abstract class HtmlFragment extends Fragment implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {
    private static final long serialVersionUID = 1L;

    public HtmlFragment(String id) {
        super(id, "", null);
    }

    protected abstract String getHtml();

    @Override
    protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
        return new PanelMarkupSourcingStrategy(false);
    }

    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return containerClass.isAnonymousClass() ? containerClass.getSuperclass().getSimpleName() : containerClass.getSimpleName();
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        String str = "<wicket:panel>" + getHtml() + "</wicket:panel>";
        return new StringResourceStream(str);
    }
}
