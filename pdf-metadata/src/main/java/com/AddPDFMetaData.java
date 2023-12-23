package com;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddPDFMetaData {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddPDFMetaData.class);
  public static final String DEST = "pdfs/document-addmetadata.pdf";

  public static void main(String[] args) throws Exception {
    try (PDDocument document = new PDDocument()) {
      PDDocumentInformation info = new PDDocumentInformation();
      info.setTitle("Test Title");
      info.setSubject("Test Subject");
      info.setAuthor("Test Author");
      info.setCreator("Test Creator");
      info.setProducer("Test Producer");
      info.setKeywords("Apache, PdfBox, PDF");
      info.setCreationDate(Calendar.getInstance());
      info.setModificationDate(Calendar.getInstance());
      // Add custom properties
      info.setCustomMetadataValue("Email", "test@pdf.com");
      document.setDocumentInformation(info);

      PDPage page = new PDPage();
      document.addPage(page);
      document.setVersion(2.0f);
      document.save(new File(DEST));
    } catch (IOException e) {
      LOGGER.error("Exception while creating document", e);
    }
  }

}
