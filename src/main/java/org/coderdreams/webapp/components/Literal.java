package org.coderdreams.webapp.components;


import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;

public class Literal extends Component {
    private static final long serialVersionUID = 1L;
    private transient String lbl;
    public Literal(String id, String lbl) {
        super(id);
        this.lbl = lbl;
    }

    @Override
    public void onConfigure() {
        super.onConfigure();
        setEscapeModelStrings(false);
        setRenderBodyOnly(true);
    }

    @Override
    protected void onRender() {
        if(!StringUtils.isBlank(lbl)) {
            getResponse().write(lbl);
        }
    }
}
