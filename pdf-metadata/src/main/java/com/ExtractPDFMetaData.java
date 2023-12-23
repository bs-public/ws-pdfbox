package com;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractPDFMetaData {

  private static final Logger logger = LoggerFactory.getLogger(ExtractPDFMetaData.class);

  public static final String SRC = "pdfs/document.pdf";

  public static void main(String[] args) {

    try (PDDocument document = Loader.loadPDF(new File(SRC))) {
      logger.info("Page Count: {}", document.getNumberOfPages());

      PDDocumentInformation info = document.getDocumentInformation();
      logger.info("Title: {}", info.getTitle());
      logger.info("Author: {}", info.getAuthor());
      logger.info("Subject: {}", info.getSubject());
      logger.info("Keywords: {}", info.getKeywords());
      logger.info("Creator: {}", info.getCreator());
      logger.info("Producer: {}", info.getProducer());
      logger.info("Creation Date: {}", formatDate(info.getCreationDate()));
      logger.info("Modification Date: {}", formatDate(info.getModificationDate()));
      
      PDDocumentCatalog cat = document.getDocumentCatalog();
      PDMetadata pdMetadata = cat.getMetadata();
      if (pdMetadata != null) {
        String metadata = new String(pdMetadata.toByteArray(), StandardCharsets.UTF_8);
        logger.info("Metadata: {}", metadata);
      }
    } catch (IOException e) {
      logger.error("Exception while reading a PDF", e);
    }
  }

  private static String formatDate(Calendar calendar) {
    String date = null;
    if (calendar != null) {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      date = format.format(calendar.getTime());
    }
    return date;
  }

}
