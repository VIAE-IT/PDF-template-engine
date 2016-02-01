package com.viae.common.pdf.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

public class PdfBuilder_WriteTableRow_Test {

    private static final String COURIER_FONT = "COURIER";
    private static float DEFAULT_FONT_SIZE = 10f;
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
        page = new PDPage(PDPage.PAGE_SIZE_A4);
    }

    @Test
    public void testWithSingleRowSingleColumn() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        final List<String> expectedWrittenString = Arrays.asList("column1");
        final String[][] content = new String[][]{{"column1"}};
        builder.writeTable(page, content);

        verify(contentStream, times(1)).beginText();
        verify(contentStream, times(1)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(1)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(1)).drawString(textCaptor.capture());
        verify(contentStream, times(1)).endText();

        assertEquals(Arrays.asList(PDType1Font.COURIER), fontFamilyCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_FONT_SIZE), fontSizeCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(0)), positionXCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(-18.462498f)), positionYCaptor.getAllValues());
        assertEquals(expectedWrittenString, textCaptor.getAllValues());
    }

    @Test
    public void testWithSingleRowMultipleColumns() throws Throwable {
        builder.context = DEFAULT_CONTEXT;
        builder.contentStream = contentStream;

        final List<String> expectedWrittenString = Arrays.asList("column1", "column2");
        final String[][] content = new String[][]{{"column1", "column2"}};
        builder.writeTable(page, content);

        verify(contentStream, times(2)).beginText();
        verify(contentStream, times(2)).setFont(fontFamilyCaptor.capture(), fontSizeCaptor.capture());
        verify(contentStream, times(2)).moveTextPositionByAmount(positionXCaptor.capture(), positionYCaptor.capture());
        verify(contentStream, times(2)).drawString(textCaptor.capture());
        verify(contentStream, times(2)).endText();

        assertEquals(Arrays.asList(PDType1Font.COURIER, PDType1Font.COURIER), fontFamilyCaptor.getAllValues());
        assertEquals(Arrays.asList(DEFAULT_FONT_SIZE, DEFAULT_FONT_SIZE), fontSizeCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(0), builder.getColumnWidth(page, 2)), positionXCaptor.getAllValues());
        assertEquals(Arrays.asList(Float.valueOf(-7.9124994f), Float.valueOf(-7.9124994f)), positionYCaptor.getAllValues());
        assertEquals(expectedWrittenString, textCaptor.getAllValues());
    }

}
