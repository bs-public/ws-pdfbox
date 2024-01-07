package com;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddWatermarkTextToPDF {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddWatermarkTextToPDF.class);

  private static final String SRC = "pdfs/addwatermarktexttopdf/test.pdf";
  private static final String DEST = "pdfs/addwatermarktexttopdf/test_out.pdf";

  private static final String TEXT = "This is Watermark Text";

  public static void main(String[] args) {

    try (PDDocument document = Loader.loadPDF(new File(SRC))) {

      for (PDPage page : document.getPages()) {
        try (PDPageContentStream cs = new PDPageContentStream(document, page,
            PDPageContentStream.AppendMode.APPEND, true, true)) {
          float fontHeight = 80;
          float width = page.getMediaBox().getWidth();
          float height = page.getMediaBox().getHeight();

          int rotation = page.getRotation();
          switch (rotation) {
            case 90:
              width = page.getMediaBox().getHeight();
              height = page.getMediaBox().getWidth();
              cs.transform(Matrix.getRotateInstance(Math.toRadians(90), height, 0));
              break;
            case 180:
              cs.transform(Matrix.getRotateInstance(Math.toRadians(180), width, height));
              break;
            case 270:
              width = page.getMediaBox().getHeight();
              height = page.getMediaBox().getWidth();
              cs.transform(Matrix.getRotateInstance(Math.toRadians(270), 0, width));
              break;
            default:
              break;
          }

          PDFont font = new PDType1Font(FontName.HELVETICA);
          float stringWidth = font.getStringWidth(TEXT) / 1000 * fontHeight;
          float diagonalLength = (float) Math.sqrt(width * width + height * height);
          float angle = (float) Math.atan2(height, width);
          float x = (diagonalLength - stringWidth) / 2;
          float y = -fontHeight / 4;
          cs.transform(Matrix.getRotateInstance(angle, 0, 0));
          cs.setFont(font, fontHeight);

          PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
          gs.setNonStrokingAlphaConstant(0.2f);
          gs.setStrokingAlphaConstant(0.2f);
          gs.setBlendMode(BlendMode.MULTIPLY);
          gs.setLineWidth(3f);
          cs.setGraphicsStateParameters(gs);

          cs.setNonStrokingColor(Color.red);
          cs.setStrokingColor(Color.red);

          cs.beginText();
          cs.newLineAtOffset(x, y);
          cs.showText(TEXT);
          cs.endText();
        }
      }
      document.save(DEST);
      LOGGER.error("Added watermark text");
    } catch (IOException e) {
      LOGGER.error("Exception while adding watermark text", e);
    }

  }

}
