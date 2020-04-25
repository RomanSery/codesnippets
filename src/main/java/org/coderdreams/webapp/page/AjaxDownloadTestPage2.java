package org.coderdreams.webapp.page;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.coderdreams.service.PdfService;
import org.coderdreams.util.UIHelpers;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.webapp.components.AJAXDownload;
import org.coderdreams.webapp.components.SingleClickAjaxButton;

public class AjaxDownloadTestPage2 extends BasePage implements IBasePage {
    @SpringBean private PdfService pdfService;
    private AJAXDownload download;

    public AjaxDownloadTestPage2() {
        super();

        Form<Void> testForm = new Form<Void>("testForm");
        addOrReplace(testForm);

        download = new AJAXDownload() {
            @Override
            public void onRequest() {
                try (AbstractResourceStreamWriter resourceStream = new AbstractResourceStreamWriter() {
                    @Override
                    public void write(OutputStream output) {
                        String html = UIHelpers.renderPageToString(new PageProvider(PageSizeTest2.class, new PageParameters()));
                        pdfService.printUrlToPdf(html, output);
                    }
                }) {
                    getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(
                            new ResourceStreamRequestHandler(resourceStream).setFileName("export.pdf").setContentDisposition(ContentDisposition.ATTACHMENT));
                } catch (IOException e) {

                }
            }
        };

        testForm.add(new SingleClickAjaxButton("singleClickBtn", testForm, true, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                download.initiate(target);
            }
        }.add(download));
    }
}
