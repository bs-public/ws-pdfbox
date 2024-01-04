package com;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateNonBlankPDF {

  private static final Logger logger = LoggerFactory.getLogger(CreatePDF.class);

  public static final String TEXT = "Test Document";
  public static final String OUT_FILE = "pdfs/test.pdf";

  public static void main(String[] args) {

    try (PDDocument doc = new PDDocument()) {
      PDPage page = new PDPage();
      doc.addPage(page);
      doc.setVersion(2.0f);

      try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
        contents.beginText();
        PDFont font = new PDType1Font(FontName.HELVETICA);
        contents.setFont(font, 20);
        contents.newLineAtOffset(200, 750);
        contents.showText(TEXT);
        contents.endText();
      }

      doc.save(OUT_FILE);
    } catch (IOException e) {
      logger.error("Exception while creating a PDF", e);
    }
    logger.info("PDF is created");
  }

}
