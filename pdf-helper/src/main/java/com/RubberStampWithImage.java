package com;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationRubberStamp;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubberStampWithImage {

  private static final Logger LOGGER = LoggerFactory.getLogger(RubberStamp.class);

  private static final String SRC = "pdfs/rubberstampwithimage/documents.pdf";
  private static final String DEST = "pdfs/rubberstampwithimage/documents_out.pdf";

  private static final String IMAGE = "pdfs/rubberstampwithimage/rubber-stamp.png";

  private static final String SAVE_GRAPHICS_STATE = "q\n";
  private static final String RESTORE_GRAPHICS_STATE = "Q\n";
  private static final String CONCATENATE_MATRIX = "cm\n";
  private static final String XOBJECT_DO = "Do\n";
  private static final String SPACE = " ";

  private static final NumberFormat FORMATDECIMAL = NumberFormat.getNumberInstance(Locale.US);

  public static void main(String[] args) {

    try (PDDocument document = Loader.loadPDF(new File(SRC))) {
      for (PDPage page : document.getPages()) {
        List<PDAnnotation> annotations = page.getAnnotations();

        PDAnnotationRubberStamp rs = new PDAnnotationRubberStamp();
        rs.setName(PDAnnotationRubberStamp.NAME_CONFIDENTIAL);
        rs.setReadOnly(true);

        PDImageXObject xObject = PDImageXObject.createFromFile(IMAGE, document);

        // define and set the rectangle
        float lowerLeftX = 250;
        float lowerLeftY = 600;
        float formWidth = 100;
        float formHeight = 100;

        float imgWidth = 100;
        float imgHeight = 100;

        PDRectangle rect = new PDRectangle();
        rect.setLowerLeftX(lowerLeftX);
        rect.setLowerLeftY(lowerLeftY);
        rect.setUpperRightX(lowerLeftX + formWidth);
        rect.setUpperRightY(lowerLeftY + formHeight);

        PDFormXObject form = new PDFormXObject(document);
        form.setResources(new PDResources());
        form.setBBox(rect);
        form.setFormType(1);

        // Fit rubber stamp to rectangle
        try (OutputStream os = form.getStream().createOutputStream()) {
          drawXObject(xObject, form.getResources(), os, lowerLeftX, lowerLeftY, imgWidth,
              imgHeight);
        }

        PDAppearanceStream myDic = new PDAppearanceStream(form.getCOSObject());
        PDAppearanceDictionary appearance = new PDAppearanceDictionary(new COSDictionary());
        appearance.setNormalAppearance(myDic);
        rs.setAppearance(appearance);
        rs.setRectangle(rect);

        annotations.add(rs);
      }

      document.save(DEST);
      LOGGER.info("Rubber stamping done");
    } catch (IOException e) {
      LOGGER.error("Exception while Rubber stamping", e);
    }
  }

  private static void drawXObject(PDImageXObject xobject, PDResources resources, OutputStream os,
      float x, float y, float width, float height) throws IOException {
    COSName xObjectId = resources.add(xobject);
    appendRawCommands(os, SAVE_GRAPHICS_STATE);
    appendRawCommands(os, FORMATDECIMAL.format(width));
    appendRawCommands(os, SPACE);
    appendRawCommands(os, FORMATDECIMAL.format(0));
    appendRawCommands(os, SPACE);
    appendRawCommands(os, FORMATDECIMAL.format(0));
    appendRawCommands(os, SPACE);
    appendRawCommands(os, FORMATDECIMAL.format(height));
    appendRawCommands(os, SPACE);
    appendRawCommands(os, FORMATDECIMAL.format(x));
    appendRawCommands(os, SPACE);
    appendRawCommands(os, FORMATDECIMAL.format(y));
    appendRawCommands(os, SPACE);
    appendRawCommands(os, CONCATENATE_MATRIX);
    appendRawCommands(os, SPACE);
    appendRawCommands(os, "/");
    appendRawCommands(os, xObjectId.getName());
    appendRawCommands(os, SPACE);
    appendRawCommands(os, XOBJECT_DO);
    appendRawCommands(os, SPACE);
    appendRawCommands(os, RESTORE_GRAPHICS_STATE);
  }

  private static void appendRawCommands(OutputStream os, String commands) throws IOException {
    os.write(commands.getBytes(StandardCharsets.ISO_8859_1));
  }

}
