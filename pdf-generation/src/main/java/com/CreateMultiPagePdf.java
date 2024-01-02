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

public class CreateMultiPagePdf {

  private static final Logger logger = LoggerFactory.getLogger(CreateMultiPagePdf.class);
  public static final String OUT_FILE = "pdfs/documents.pdf";

  public static void main(String[] args) throws IOException {
    try (PDDocument document = new PDDocument()) {
      for (int i = 1; i <= 2; i++) {
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        String text = "This is page " + i;
        PDFont font = new PDType1Font(FontName.HELVETICA);

        // Distance from top of the page
        float marginY = 40;
        float fontSize = 12;

        float textWidth = font.getStringWidth(text) * fontSize / 1000f;

        float startX = (page.getMediaBox().getWidth() - textWidth) / 2;
        float startY = page.getMediaBox().getHeight() - marginY;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(startX, startY);
        contentStream.showText(text);
        contentStream.endText();

        contentStream.close();
      }
      document.save(OUT_FILE);
      document.close();
    } catch (IOException e) {
      logger.error("Exception while creating a PDF", e);
    }
    logger.info("PDF with multiple pages is created");
  }

}
