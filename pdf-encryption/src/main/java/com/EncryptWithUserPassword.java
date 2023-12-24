package com;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptWithUserPassword {

  private static final Logger LOGGER = LoggerFactory.getLogger(EncryptWithUserPassword.class);

  private static final String SRC = "pdfs/test.pdf";
  private static final String DEST = "pdfs/user/test-user-encrypted.pdf";
  private static final String OWNER_PASSWORD = "owner";
  private static final String USER_PASSWORD = "user";

  public static void main(String[] args) {

    try (PDDocument doc = Loader.loadPDF(new File(SRC))) {
      AccessPermission permission = new AccessPermission();
      permission.setCanAssembleDocument(false);
      permission.setCanExtractContent(false);
      permission.setCanExtractForAccessibility(false);
      permission.setCanFillInForm(false);
      permission.setCanModify(false);
      permission.setCanModifyAnnotations(false);
      permission.setCanPrint(false);
      permission.setCanPrintFaithful(false);
      permission.setReadOnly();

      StandardProtectionPolicy spp =
          new StandardProtectionPolicy(OWNER_PASSWORD, USER_PASSWORD, permission);
      spp.setEncryptionKeyLength(256);
      spp.setPermissions(permission);
      doc.protect(spp);

      doc.save(DEST);
      LOGGER.info("Document encrypted successfully");
    } catch (IOException e) {
      LOGGER.error("Exception while encrypting the pdf", e);
    }
  }

}
