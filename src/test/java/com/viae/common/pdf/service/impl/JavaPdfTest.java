package com.viae.common.pdf.service.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.StringJoiner;

import org.junit.Ignore;
import org.junit.Test;

import com.viae.common.pdf.model.PdfContext;
import com.viae.common.pdf.model.PdfContext.FontFamily;
import com.viae.common.pdf.service.impl.PdfBuilder.ImageType;
import com.viae.common.pdf.service.impl.PdfBuilder.PageSize;

public class JavaPdfTest {
    @Test
    public void test3() throws Throwable {
        final String imagePath = "src/test/resources/schoonheidsspecialiste.png";
        final File image = new File(imagePath);
        assertTrue(image.exists());

        final JavaPdfBuilder.BuilderClient client = new JavaPdfBuilder.BuilderClient() {

            @Override
            public void whenBuilding(final JavaPdfBuilder builder) {
                PdfContext context = PdfContext.builder().create().build();

                final StringJoiner joiner = new StringJoiner("\n");
                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.HELVETICA_BOLD)
                        .fontSize(15)
                        .lineHeight(-1)
                        .marginLeft(100)
                        .marginRight(100)
                        .textLinePadding(2f)
                        .build();
                builder.setContext(context);
                builder.writeImage(image, ImageType.OTHER);
                builder.writeText("Big header title");
                context = PdfContext.builder()
                        .deepCopy(context)
                        .marginLeft(100)
                        .build();
                builder.setContext(context);

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.HELVETICA_BOLD)
                        .fontSize(10)
                        .build();
                builder.setContext(context);
                builder.writeText("Subtitle 1");

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.COURIER)
                        .fontSize(5)
                        .cellMarginLeft(10)
                        .cellMarginTop(10)
                        .cellMarginBottom(10)
                        .build();
                builder.setContext(context);
                builder.writeTableRow("Table header");
                builder.writeTableRow("naam1", "maarten", "vandeperre");
                builder.writeTableRow("naam2", "VIAE is a company actively involved in IT development (going from mobile applications and webdevelopmen to providing libraries like the pdf rendering engine. It is founded by Maarten Vandeperre (software engineer, Gent).");
                builder.writeTableRow("naam3", "maarten vandeperre");
                builder.writeTableRow("naam4", "maarten vandeperre");

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.HELVETICA_BOLD)
                        .fontSize(10)
                        .build();
                builder.setContext(context);
                builder.writeNewLine();
                builder.writeText("Subtitle2");

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.HELVETICA_BOLD)
                        .fontSize(5)
                        .build();
                builder.setContext(context);
                builder.writeText(DefaultString.LOREM_IPSUM);
                builder.writeNewLine();

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.HELVETICA)
                        .fontSize(5)
                        .build();
                builder.setContext(context);
                builder.writeText(DefaultString.LOREM_IPSUM + DefaultString.LOREM_IPSUM + DefaultString.LOREM_IPSUM + DefaultString.LOREM_IPSUM + " ; the end");
                builder.writeNewLine();

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.COURIER)
                        .fontSize(5)
                        .build();
                builder.setContext(context);
                builder.writeText(DefaultString.LOREM_IPSUM);
                builder.writeNewLine();

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.COURIER)
                        .fontSize(5)
                        .cellMarginLeft(10)
                        .cellMarginTop(10)
                        .cellMarginBottom(10)
                        .build();
                builder.setContext(context);
                builder.writeTableRow("Table header");
                builder.writeTableRow("naam1", "maarten", "vandeperre");
                builder.writeTableRow("naam2", "VIAE is a company actively involved in IT development (going from mobile applications and webdevelopmen to providing libraries like the pdf rendering engine. It is founded by Maarten Vandeperre (software engineer, Gent).");
                builder.writeTableRow("naam3", "maarten vandeperre");
                builder.writeTableRow("naam4", "maarten vandeperre");

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.HELVETICA_BOLD)
                        .fontSize(10)
                        .build();
                builder.setContext(context);
                builder.writeNewLine();
                builder.writeText("Subtitle3");

                joiner.add("");
            }
        };

        final JavaPdfBuilder builder = new JavaPdfBuilder();
        builder.buildPdf(client, PageSize.A4, "voorbeeld.pdf");
    }

    @Test
    @Ignore
    public void test4() {
        final JavaPdfBuilder.BuilderClient client = new JavaPdfBuilder.BuilderClient() {

            @Override
            public void whenBuilding(final JavaPdfBuilder builder) {
                PdfContext context = PdfContext.builder().create().build();

                context = PdfContext.builder()
                        .deepCopy(context)
                        .fontFamily(FontFamily.COURIER)
                        .fontSize(5)
                        .cellMarginLeft(10)
                        .cellMarginTop(10)
                        .cellMarginBottom(10)
                        .build();
                builder.setContext(context);
                builder.writeTableRow("naam1", "maarten", "vandeperre");
                builder.writeTableRow("naam2", "Zolder vormt zondag het decor voor de strijd om de regenboogtrui in het veld. Volgt Mathieu van der Poel zichzelf op? Troeft Wout van Aert de Nederlandse titelverdediger af? Of is er een derde hond? Breng hier uw stem uit.");
                builder.writeTableRow("naam3", "maarten vandeperre");
                builder.writeTableRow("naam4", "maarten vandeperre");
            }
        };

        final JavaPdfBuilder builder = new JavaPdfBuilder();
        builder.buildPdf(client, PageSize.A4, "voorbeeld.pdf");
    }
}
