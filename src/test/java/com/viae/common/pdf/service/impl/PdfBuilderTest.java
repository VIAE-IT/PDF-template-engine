package com.viae.common.pdf.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.StringJoiner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.viae.common.pdf.model.PdfContext;
import com.viae.common.pdf.service.impl.PdfBuilder.PageSize;

public class PdfBuilderTest {

    private static final String COURIER_FONT = "COURIER";

    private static PdfBuilder builder;
    private PDDocument document;
    private PDPage page;
    private StringJoiner defaultJoiner;
    private PDPageContentStream contentStream;
    private final ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
    private final ArgumentCaptor<PDFont> fontFamilyCaptor = ArgumentCaptor.forClass(PDFont.class);
    private final ArgumentCaptor<Float> fontSizeCaptor = ArgumentCaptor.forClass(Float.class);
    private final ArgumentCaptor<Float> positionXCaptor = ArgumentCaptor.forClass(Float.class);
    private final ArgumentCaptor<Float> positionYCaptor = ArgumentCaptor.forClass(Float.class);

    @BeforeClass
    public static void setupSharedFixture(){
        builder = new PdfBuilder();
    }

    @Before
    public void setupFreshFixture(){
        document = new PDDocument();
        defaultJoiner = new StringJoiner("\n");
        contentStream = mock(PDPageContentStream.class);
        page = new PDPage();
    }

    @Test
    public void testWriteStringWithoutText() throws Throwable {
        final PdfContext context = PdfContext.builder()
                .create()
                .fontFamily(COURIER_FONT)
                .build();

        builder.context = context;
        builder.contentStream = contentStream;

        final String expectedWrittenString = "";
        doWriteText(expectedWrittenString);

        verify(contentStream, times(1)).beginText();
        verify(contentStream, times(1)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(1)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(1)).drawString(textCaptor.capture());
        verify(contentStream, times(1)).endText();

        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 0);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    @Test
    public void testWriteStringWithSingleLineText() throws Throwable {
        final PdfContext context = PdfContext.builder()
                .create()
                .fontFamily(COURIER_FONT)
                .build();

        builder.context = context;
        builder.contentStream = contentStream;

        defaultJoiner.add("single line");
        final String expectedWrittenString = "single line";
        doWriteText(expectedWrittenString);

        verify(contentStream, times(1)).beginText();
        verify(contentStream, times(1)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(1)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(1)).drawString(textCaptor.capture());
        verify(contentStream, times(1)).endText();

        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 0);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    @Test
    public void testWriteStringWithSingleRowSingleColumn() throws Throwable {
        final PdfContext context = PdfContext.builder()
                .create()
                .fontFamily(COURIER_FONT)
                .build();

        builder.context = context;
        builder.contentStream = contentStream;

        defaultJoiner.add("column1");
        final String expectedWrittenString = "single line";
        final String[][] content = new String[][]{{"column1"}};
        builder.writeTable(page, content);

        verify(contentStream, times(1)).beginText();
        verify(contentStream, times(1)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(1)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(1)).drawString(textCaptor.capture());
        verify(contentStream, times(1)).endText();

        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 0);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    @Test
    public void testWriteStringWithSingleRowMultipleColumns() throws Throwable {
        final PdfContext context = PdfContext.builder()
                .create()
                .fontFamily(COURIER_FONT)
                .fontSize(1f)
                .build();

        builder.context = context;
        builder.contentStream = contentStream;

        defaultJoiner.add("column1");
        final String expectedWrittenString = "single line";
        final String[][] content = new String[][]{{"column1", "column2"}};
        builder.writeTable(page, content);

        verify(contentStream, times(2)).beginText();
        verify(contentStream, times(2)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(2)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(2)).drawString(textCaptor.capture());
        verify(contentStream, times(2)).endText();

        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 0);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    @Test
    public void testWriteStringWithMultiLineText() throws Throwable {
        final PdfContext context = PdfContext.builder()
                .create()
                .fontFamily(COURIER_FONT)
                .fontSize(1f)
                .build();

        builder.context = context;
        builder.contentStream = contentStream;

        defaultJoiner.add(DefaultString.LOREM_IPSUM);
        final String expectedWrittenString = "single line";
        doWriteText(expectedWrittenString);

        verify(contentStream, times(1)).beginText();
        verify(contentStream, times(1)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(1)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(1)).drawString(textCaptor.capture());
        verify(contentStream, times(1)).endText();

        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 0);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    private void doWriteText(final String expectedWrittenString) throws IOException {
        final String toBeWritten = defaultJoiner.toString();
        builder.writeText(toBeWritten, document, builder.getPageSize(PageSize.A4));
        assertEquals(expectedWrittenString, toBeWritten);
    }

    private void validateWriteTextResult(final String expectedWrittenString, final PDType1Font font, final Float fontSize, final Float positionX, final Float positionY) {
        assertEquals(font, fontFamilyCaptor.getValue());
        assertEquals(fontSize, fontSizeCaptor.getValue());
        assertEquals(positionX, positionXCaptor.getValue());
        assertEquals(positionY, positionYCaptor.getValue());
        assertEquals(expectedWrittenString, textCaptor.getValue());
    }

}
