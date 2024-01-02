package com;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageToPDF {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageToPDF.class);
  private static final String SRC = "pdfs/imagetopdf/image.png";
  private static final String DEST = "pdfs/imagetopdf/image.pdf";

  public static void main(String[] args) {

    try (PDDocument doc = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.A4);

      doc.addPage(page);

      PDImageXObject pdImage = PDImageXObject.createFromFile(SRC, doc);
      float pageWidth = page.getMediaBox().getWidth();
      float pageHeight = page.getMediaBox().getHeight();
      float imageWidth = pdImage.getWidth();
      float imageHeight = pdImage.getHeight();

      // Calculate scale to fit the image within the page
      float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);

      float newWidth = imageWidth * scale;
      float newHeight = imageHeight * scale;

      // Position the image in the center of the page
      float x = (pageWidth - newWidth) / 2;
      float y = (pageHeight - newHeight) / 2;

      try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
        contents.drawImage(pdImage, x, y, newWidth, newHeight);
      }
      doc.save(DEST);
      LOGGER.info("Image converted to PDF");
    } catch (IOException e) {
      LOGGER.error("Exception while Image to PDF conversion", e);
    }
  }

}
