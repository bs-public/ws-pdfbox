package com;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePDFMetaData {

  private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePDFMetaData.class);
  public static final String SRC = "pdfs/document.pdf";
  public static final String DEST = "pdfs/document-metadata.pdf";

  public static void main(String[] args) {
    try (PDDocument document = Loader.loadPDF(new File(SRC))) {
      PDDocumentInformation info = new PDDocumentInformation();
      info.setTitle("Test Title");
      info.setSubject("Test Subject");
      info.setAuthor("Test Author");
      info.setCreator("Test Creator");
      info.setProducer("Test Producer");
      info.setKeywords("Apache, PdfBox, PDF");
      info.setCreationDate(Calendar.getInstance());
      info.setModificationDate(Calendar.getInstance());
      document.setDocumentInformation(info);

      document.save(new File(DEST));
    } catch (IOException e) {
      LOGGER.error("Exception while updating the metadata", e);
    }
  }

}
