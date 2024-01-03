package com;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationRubberStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RubberStamp {

  private static final Logger LOGGER = LoggerFactory.getLogger(RubberStamp.class);

  private static final String SRC = "pdfs/rubberstamp/documents.pdf";
  private static final String DEST = "pdfs/rubberstamp/documents_out.pdf";

  public static void main(String[] args) {

    try (PDDocument document = Loader.loadPDF(new File(SRC))) {
      for (PDPage page : document.getPages()) {
        List<PDAnnotation> annotations = page.getAnnotations();

        PDAnnotationRubberStamp rs = new PDAnnotationRubberStamp();
        rs.setName(PDAnnotationRubberStamp.NAME_CONFIDENTIAL);
        rs.setRectangle(new PDRectangle(100, 600, 100, 100));
        rs.setContents("Test content");

        annotations.add(rs);
      }

      document.save(DEST);
      LOGGER.info("Rubber stamping done");
    } catch (IOException e) {
      LOGGER.error("Exception while Rubber stamping", e);
    }
  }

}
