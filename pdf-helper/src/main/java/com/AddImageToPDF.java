package com;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddImageToPDF {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddImageToPDF.class);

  private static final String IMAGE_PATH = "pdfs/addimagetopdf/image.png";
  private static final String SRC = "pdfs/addimagetopdf/test.pdf";
  private static final String DEST = "pdfs/addimagetopdf/test_out.pdf";

  public static void main(String[] args) {

    try (PDDocument document = Loader.loadPDF(new File(SRC))) {
      PDPage page = document.getPage(0);

      PDImageXObject pdImage = PDImageXObject.createFromFile(IMAGE_PATH, document);

      try (PDPageContentStream contentStream =
          new PDPageContentStream(document, page, AppendMode.APPEND, true, true)) {
        float scale = 1f;
        contentStream.drawImage(pdImage, 60, 400, pdImage.getWidth() * scale,
            pdImage.getHeight() * scale);
      }

      document.save(DEST);
      LOGGER.info("Image added");
    } catch (IOException e) {
      LOGGER.error("Exception while adding image", e);
    }
  }

}
