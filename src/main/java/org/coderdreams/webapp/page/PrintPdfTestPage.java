package org.coderdreams.webapp.page;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.coderdreams.service.PdfService;
import org.coderdreams.util.UIHelpers;

public class PrintPdfTestPage extends WebPage {

    @SpringBean private PdfService pdfService;
    private Label usersCountLbl;

    public PrintPdfTestPage() throws IOException {
        super();

        try (AbstractResourceStreamWriter resourceStream = new AbstractResourceStreamWriter() {
            @Override
            public void write(OutputStream output) {
                String html = UIHelpers.renderPageToString(new PageProvider(PageSizeTest2.class, new PageParameters()));
                pdfService.printUrlToPdf(html, output);
            }
        }) {
            RequestCycle.get().scheduleRequestHandlerAfterCurrent(
                    new ResourceStreamRequestHandler(resourceStream).setFileName("export.pdf").setContentDisposition(ContentDisposition.ATTACHMENT));
        }
    }

}
