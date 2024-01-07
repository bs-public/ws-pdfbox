package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.pdfbox.CreateSignatureBase;
import org.pdfbox.SigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignDocument extends CreateSignatureBase {

  private static final Logger LOG = LoggerFactory.getLogger(SignDocument.class);

  public static final String SRC = "pdfs/signdocument/test.pdf";
  public static final String DEST = "pdfs/signdocument/test_signed.pdf";

  public static final String KEYSTORE = "keystore.p12";
  public static final String KEYSTORE_PASSWORD = "password";


  public SignDocument(KeyStore keystore, char[] pin) throws KeyStoreException,
      UnrecoverableKeyException, NoSuchAlgorithmException, IOException, CertificateException {
    super(keystore, pin);
  }

  public static void main(String[] args) {

    try (InputStream is = SignDocument.class.getResourceAsStream("/" + KEYSTORE);
        PDDocument document = Loader.loadPDF(new File(SRC))) {
      char[] password = KEYSTORE_PASSWORD.toCharArray();
      KeyStore keystore = KeyStore.getInstance("PKCS12");
      keystore.load(is, password);

      OutputStream os = new FileOutputStream(DEST);
      SignDocument signing = new SignDocument(keystore, password);
      signing.signDetached(document, os);

      LOG.info("Signing done");
    } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException
        | UnrecoverableKeyException e) {
      LOG.error("Exception while signing a documment", e);
    }

  }


  public void signDetached(PDDocument document, OutputStream output) throws IOException {
    int accessPermissions = SigUtils.getMDPPermission(document);
    if (accessPermissions == 1) {
      throw new IllegalStateException(
          "No changes to the document are permitted due to DocMDP transform parameters dictionary");
    }

    // Signature dictionary
    PDSignature signature = new PDSignature();
    signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
    signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
    signature.setName("Test User");
    signature.setLocation("Test");
    signature.setReason("Testing");
    signature.setSignDate(Calendar.getInstance());

    if (accessPermissions == 0) {
      SigUtils.setMDPPermission(document, signature, 2);
    }

    SignatureOptions signatureOptions = new SignatureOptions();
    signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);

    document.addSignature(signature, this, signatureOptions);

    document.saveIncremental(output);
  }


}
