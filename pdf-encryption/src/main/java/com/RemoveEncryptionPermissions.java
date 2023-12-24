package com;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveEncryptionPermissions {

  private static final Logger LOGGER = LoggerFactory.getLogger(RemoveEncryptionPermissions.class);

  public static final String SRC = "pdfs/removeEncryption/encrypted.pdf";
  public static final String DEST = "pdfs/removeEncryption/unencrypted.pdf";
  private static final String OWNER_PASSWORD = "owner";

  public static void main(String[] args) {
    File file = new File(SRC);
    try (PDDocument doc = Loader.loadPDF(file, OWNER_PASSWORD)) {
      doc.setAllSecurityToBeRemoved(true);
      doc.save(DEST);
      LOGGER.info("Document decrypted successfully");
    } catch (IOException e) {
      LOGGER.error("Exception while decrypting the pdf document", e);
    }
  }
  
}
