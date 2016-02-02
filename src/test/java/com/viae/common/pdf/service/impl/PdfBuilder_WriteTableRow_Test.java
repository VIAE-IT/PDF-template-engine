package com.viae.common.pdf.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

public class PdfBuilder_WriteTableRow_Test {

    private static final FontFamily COURIER_FONT = FontFamily.COURIER;
    private static final PDFont DEFAULT_FONT = PDType1Font.COURIER;
    private static float DEFAULT_FONT_SIZE = 10f;
    private static float DEFAULT_Y_POSITION_1 = Float.valueOf(-7.9124994f);
    private static float DEFAULT_Y_POSITION_2 = Float.valueOf(-18.462498f);
    private static float DEFAULT_Y_POSITION_3 = Float.valueOf(-29.012497f);
    private static float DEFAULT_Y_POSITION_4 = Float.valueOf(-39.562496f);
    private static float DEFAULT_Y_POSITION_5 = Float.valueOf(-50.112495f);
    private static float DEFAULT_Y_POSITION_6 = Float.valueOf(-60.662495f);
    private static final PdfContext DEFAULT_CONTEXT = PdfContext.builder()
            .create()
            .fontFamily(COURIER_FONT)
            .fontSize(DEFAULT_FONT_SIZE)
            .build();

    private static PdfBuilder builder;
    private PDPage page;
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
        page = new PDPage(PDPage.PAGE_SIZE_A4);
    }

    @Test
    public void testWithSingleRowSingleColumn() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        final List<String> expectedWrittenString = Arrays.asList("column1");
        final String[][] content = new String[][]{{"column1"}};
        builder.writeTable(page, content);

        verifyTextWriteMethods(1);
        assertEquals(Arrays.asList(DEFAULT_FONT), fontFamilyCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_FONT_SIZE), fontSizeCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(0)), positionXCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_Y_POSITION_1), positionYCaptor.getAllValues());
        assertEquals(expectedWrittenString, textCaptor.getAllValues());
    }

    @Test
    public void testWithSingleRowSingleColumnWithWrappedText() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        final List<String> expectedWrittenString = Arrays.asList(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor ",
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ",
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ",
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore ",
                "eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt ",
                "in culpa qui officia deserunt mollit anim id est laborum."
                );
        final String[][] content = new String[][]{{DefaultString.LOREM_IPSUM}};
        builder.writeTable(page, content);

        verifyTextWriteMethods(6);
        assertEquals(Arrays.asList(DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT, DEFAULT_FONT), fontFamilyCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE), fontSizeCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0)), positionXCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_Y_POSITION_1, DEFAULT_Y_POSITION_2, DEFAULT_Y_POSITION_3, DEFAULT_Y_POSITION_4, DEFAULT_Y_POSITION_5, DEFAULT_Y_POSITION_6), positionYCaptor.getAllValues());
        final List<String> wrappedLines = textCaptor.getAllValues();
        assertEquals(6, wrappedLines.size());
        assertEquals(expectedWrittenString.get(0), wrappedLines.get(0));
        assertEquals(expectedWrittenString.get(1), wrappedLines.get(1));
        assertEquals(expectedWrittenString.get(2), wrappedLines.get(2));
        assertEquals(expectedWrittenString.get(3), wrappedLines.get(3));
        assertEquals(expectedWrittenString.get(4), wrappedLines.get(4));
        assertEquals(expectedWrittenString.get(5), wrappedLines.get(5));
    }

    @Test
    public void testWithMultipleRowSingleColumn() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        try{
            final String[][] content = new String[][]{{"row1 - column1"}, {"row2 - column1"}};
            builder.writeTable(page, content);
        } catch(final UnsupportedOperationException e){
            assertTrue(e.getLocalizedMessage(), "Multi row is currently not supported".equals(e.getLocalizedMessage()));
        }
    }

    @Test
    public void testWithSingleRowMultipleColumns() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        final List<String> expectedWrittenString = Arrays.asList("column1", "column2");
        final String[][] content = new String[][]{{"column1", "column2"}};
        builder.writeTable(page, content);

        verifyTextWriteMethods(2);
        assertEquals(Arrays.asList(PDType1Font.COURIER, PDType1Font.COURIER), fontFamilyCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE), fontSizeCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(0), builder.getColumnWidth(page, 2)), positionXCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_Y_POSITION_1, DEFAULT_Y_POSITION_1), positionYCaptor.getAllValues());
        assertEquals(expectedWrittenString, textCaptor.getAllValues());
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
