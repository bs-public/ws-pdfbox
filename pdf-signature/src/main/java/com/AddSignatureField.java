package com;

import java.io.IOException;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddSignatureField {
  private static final Logger LOG = LoggerFactory.getLogger(AddSignatureField.class);

  public static final String OUT_FILE = "pdfs/addsignaturefield/test.pdf";

  public static void main(String[] args) {
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.A4);
      document.addPage(page);
      document.setVersion(2.0f);

      PDFont font = new PDType1Font(FontName.HELVETICA);
      PDResources resources = new PDResources();
      resources.put(COSName.HELV, font);

      try (PDPageContentStream contents = new PDPageContentStream(document, page)) {
        contents.beginText();
        contents.setFont(font, 20);
        contents.newLineAtOffset(200, 750);
        contents.showText("Test Document");
        contents.endText();
      }

      PDAcroForm acroForm = new PDAcroForm(document);
      document.getDocumentCatalog().setAcroForm(acroForm);
      acroForm.setDefaultResources(resources);

      String defaultAppearanceString = "/Helv 0 Tf 0 g";
      acroForm.setDefaultAppearance(defaultAppearanceString);

      PDSignatureField signatureField = new PDSignatureField(acroForm);
      PDAnnotationWidget widget = signatureField.getWidgets().get(0);
      PDRectangle rect = new PDRectangle(50, 600, 200, 50);
      widget.setRectangle(rect);
      widget.setPage(page);
      widget.setPrinted(true);

      page.getAnnotations().add(widget);

      acroForm.getFields().add(signatureField);

      document.save(OUT_FILE);
    } catch (IOException e) {
      LOG.error("Exception while creating & adding signature field to a PDF", e);
    }
    LOG.info("PDF is created");
  }

}
