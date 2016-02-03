package com.viae.common.pdf.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
import com.viae.common.pdf.model.PdfContext.FontFamily;
import com.viae.common.pdf.service.impl.PdfBuilder.PageSize;

public class PdfBuilder_WriteText_Test {

    private static final FontFamily COURIER_FONT = FontFamily.COURIER;
    private static final PDFont DEFAULT_FONT = PDType1Font.COURIER;
    private static float DEFAULT_FONT_SIZE = 10f;
    private static float DEFAULT_Y_POSITION_1 = Float.valueOf(831.3398f);
    private static float DEFAULT_Y_POSITION_2 = Float.valueOf(810.2398f);
    private static float DEFAULT_Y_POSITION_3 = Float.valueOf(789.1398f);
    private static float DEFAULT_Y_POSITION_4 = Float.valueOf(768.03973f);
    private static float DEFAULT_Y_POSITION_5 = Float.valueOf(746.93976f);
    private static float DEFAULT_Y_POSITION_6 = Float.valueOf(725.8398f);
    private static final PdfContext DEFAULT_CONTEXT = PdfContext.builder()
            .create()
            .fontFamily(COURIER_FONT)
            .fontSize(DEFAULT_FONT_SIZE)
            .build();

    private static PdfBuilder builder;
    private PDDocument document;
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
        contentStream = mock(PDPageContentStream.class);
        builder.line = 0;
        builder.lastY = 0;
        document = new PDDocument();
        defaultJoiner = new StringJoiner("\n");
    }

    @Test
    public void testWriteStringWithoutText() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        final String expectedWrittenString = "";
        doWriteText(expectedWrittenString);

        verifyTextWriteMethods(1);
        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 1);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    @Test
    public void testWriteStringWithSingleLineText() throws Throwable {
        builder.context = builder.context = DEFAULT_CONTEXT;;
        builder.contentStream = contentStream;

        defaultJoiner.add("single line");
        final String expectedWrittenString = "single line";
        doWriteText(expectedWrittenString);

        verifyTextWriteMethods(1);
        final float expectedPositionY = builder.getPositionY(PDPage.PAGE_SIZE_A4, 1);
        validateWriteTextResult(expectedWrittenString, PDType1Font.COURIER, Float.valueOf(0), Float.valueOf(0), expectedPositionY);
    }

    @Test
    public void testWriteStringWithMultiLineText() throws Throwable {
        builder.context = builder.context = DEFAULT_CONTEXT;;
        builder.contentStream = contentStream;

        defaultJoiner.add(DefaultString.LOREM_IPSUM);
        final List<String> expectedWrittenString = Arrays.asList(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor ",
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ",
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ",
                "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt ",
                "in culpa qui officia deserunt mollit anim id est laborum."
                );
        final String toBeWritten = defaultJoiner.toString();
        builder.writeText(toBeWritten, document, builder.getPageSize(PageSize.A4));

        final List<String> wrappedLines = textCaptor.getAllValues();
        verifyTextWriteMethods(6);
        assertEquals(Arrays.asList(DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT), fontFamilyCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE), fontSizeCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0)), positionXCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_Y_POSITION_1, DEFAULT_Y_POSITION_2, DEFAULT_Y_POSITION_3, DEFAULT_Y_POSITION_4, DEFAULT_Y_POSITION_5, DEFAULT_Y_POSITION_6), positionYCaptor.getAllValues());
        assertEquals(6, wrappedLines.size());
        assertEquals(expectedWrittenString.get(0), wrappedLines.get(0));
        assertEquals(expectedWrittenString.get(1), wrappedLines.get(1));
        assertEquals(expectedWrittenString.get(2), wrappedLines.get(2));
        assertEquals(expectedWrittenString.get(3), wrappedLines.get(3));
        assertEquals(expectedWrittenString.get(4), wrappedLines.get(4));
        assertEquals(expectedWrittenString.get(5), wrappedLines.get(5));
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

    private void verifyTextWriteMethods(final int textWriteCount)
            throws IOException {
        verify(contentStream, times(textWriteCount)).beginText();
        verify(contentStream, times(textWriteCount)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(textWriteCount)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(textWriteCount)).drawString(textCaptor.capture());
        verify(contentStream, times(textWriteCount)).endText();
    }

}
