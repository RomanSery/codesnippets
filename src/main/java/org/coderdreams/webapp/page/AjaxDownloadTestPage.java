package org.coderdreams.webapp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.AJAXDownload;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class AjaxDownloadTestPage extends BasePage implements IBasePage {

    private AJAXDownload download;

    public AjaxDownloadTestPage() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        download = new AJAXDownload() {
            private static final long serialVersionUID = 1L;
            @Override
            protected IResourceStream getResourceStream() {
                String csvContents = "id,first,last\n1,Roman,Sery\n2,Mike,Wicket\n";
                return new StringResourceStream(csvContents, "text/csv");
            }
            @Override protected String getFileName() { return "export.csv"; }
        };


        testForm.add(new SingleClickAjaxButton("singleClickBtn", testForm, true, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                download.initiate(target);
            }
        }.add(download));

    }


}
