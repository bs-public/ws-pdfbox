package com;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePDF {

  private static final Logger logger = LoggerFactory.getLogger(CreatePDF.class);

  public static final String OUT_FILE = "pdfs/document.pdf";

  public static void main(String[] args) {

    try (PDDocument doc = new PDDocument()) {
      PDPage blankPage = new PDPage();
      doc.addPage(blankPage);
      doc.setVersion(2.0f);
      doc.save(OUT_FILE);
    } catch (IOException e) {
      logger.error("Exception while creating a PDF", e);
    }
    logger.info("PDF is created");
  }

}
