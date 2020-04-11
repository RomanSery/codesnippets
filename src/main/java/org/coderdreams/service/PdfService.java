package org.coderdreams.service;

import java.io.OutputStream;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Service
public class PdfService {

	public void printUrlToPdf(String html, OutputStream output) {
		try {
			String xhtml = convertHtmlToXHTML(html);

			Document document = new Document(PageSize.LETTER.rotate());
			PdfWriter writer = PdfWriter.getInstance(document, output);
			writer.setInitialLeading(12.5f);
			document.open();

			document.newPage();

			Paragraph p1 = new Paragraph();

			ElementList list = XMLWorkerHelper.parseToElementList(xhtml, null);
			p1.addAll(list);
			document.add(p1);

			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private String convertHtmlToXHTML(String input) {
		org.jsoup.nodes.Document d = Jsoup.parse(input, "US-ASCII");
		d.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
		return d.html();
	}



}
