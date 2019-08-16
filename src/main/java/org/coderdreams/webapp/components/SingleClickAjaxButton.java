package org.coderdreams.webapp.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public abstract class SingleClickAjaxButton extends IndicatingAjaxButton {
	private static final long serialVersionUID = 1L;

	private final IModel<String> confirmationMessageModel;
    private final boolean enableButtonAfterSubmit;

	public SingleClickAjaxButton(String id, Form<?> form, boolean enableButtonAfterSubmit, IModel<String> confirmationMessageModel) {
		super(id, form);
        this.enableButtonAfterSubmit = enableButtonAfterSubmit;
		this.confirmationMessageModel = confirmationMessageModel;
	}


    @Override
    protected void onError(AjaxRequestTarget target) {
        error(target);
        target.appendJavaScript("$('#"+this.getMarkupId()+"').prop('disabled', false);");
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target) {
        if(submit(target)) {
            target.appendJavaScript("$('#"+this.getMarkupId()+"').prop('disabled', false);");
        }
    }

    @Override
    protected void onAfterSubmit(AjaxRequestTarget target) {
        if(enableButtonAfterSubmit) {
            target.appendJavaScript("$('#"+this.getMarkupId()+"').prop('disabled', false);");
        }
    }

    /**
     * @return true to re-enable the button
     */
    protected abstract boolean submit(AjaxRequestTarget target);

    @Override
	protected String getOnClickScript() {
        return "$('#"+this.getMarkupId()+"').prop('disabled', true); setTimeout(function() { $('#"+this.getMarkupId()+"').prop('disabled', false); }, 10000);";
    }

    protected void error(AjaxRequestTarget target) { }

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		if(confirmationMessageModel != null && confirmationMessageModel.getObject() != null) {
			attributes.getAjaxCallListeners().add(new AjaxCallListener(){
				private static final long serialVersionUID = 1L;
				@Override
		    	public CharSequence getPrecondition(Component component) {
					if(enableButtonAfterSubmit) {
						String str = "$('#"+ SingleClickAjaxButton.this.getMarkupId()+"').prop('disabled', false);";
						return "if (!confirm('" + confirmationMessageModel.getObject() + "')) { "+str+" return false; }";
					} else {
						return "if (!confirm('" + confirmationMessageModel.getObject() + "')) { return false; }";
					}
										
		    	}
		    });
		}		
	}
}
