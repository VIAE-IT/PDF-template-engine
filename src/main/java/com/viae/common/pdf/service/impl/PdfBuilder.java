package com.viae.common.pdf.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.viae.common.pdf.model.PdfContext;
import com.viae.common.pdf.util.TextWrapUtil;
import com.viae.common.pdf.util.TextWrapUtil.WrapResult;

public class PdfBuilder {

    private static final int FONT_CORRECTING_FACTOR = 4;

    protected PdfContext context; //TODO make private and add protected setter
    protected PDDocument document; //TODO make private and add protected setter
    protected PDPageContentStream contentStream; //TODO make private and add protected setter
    protected int line; //TODO make private and add protected setter
    protected float lastY; //TODO make private and add protected setter
    protected float heightCorrection; //TODO make private and add protected setter

    public PdfBuilder(){
        context = PdfContext.builder().create().build();
    }

    public PdfContext getContext() {
        return PdfContext.builder().deepCopy(context).build();
    }

    protected PDRectangle getPageSize(final PageSize pageSize){
        switch (pageSize) {
            default:
                return PDPage.PAGE_SIZE_A4;
        }
    }

    protected PDFont getFontFamily(final String fontFamily) {
        switch (fontFamily) {
            case "HELVETICA_BOLD":
                return PDType1Font.HELVETICA_BOLD;
            case "HELVETICA_OBLIQUE":
                return PDType1Font.HELVETICA_OBLIQUE;
            case "COURIER":
                return PDType1Font.COURIER;
            default:
                return PDType1Font.HELVETICA;
        }
    }

    protected void writeImage(final PDPage page, final File file, final ImageType type) throws IOException {
        final float maxWidth = page.findMediaBox().getWidth() - context.getMarginLeft() - context.getMarginRight();
        writeImage(page, file, type, maxWidth);
    }

    protected void writeImage(final PDPage page, final File file, final ImageType type, final float maxWidth) throws IOException {

        PDXObjectImage ximage = null;
        try(InputStream stream = new FileInputStream( file )){
            switch (type) {
                case JPEG:
                    ximage = new PDJpeg(document, stream);
                    break;
                default:
                    final BufferedImage awtImage = ImageIO.read(stream);
                    ximage = new PDPixelMap(document, awtImage);
                    break;
            }
        }

        final float imageWidth = ximage.getWidth() * 1f;
        final float imageHeight = ximage.getHeight() * 1f;
        final float scale = (imageWidth - maxWidth < 0) ? 1f : (maxWidth/imageWidth);
        final float width = imageWidth * scale;
        final float height = imageHeight * scale;
        final float x = context.getMarginLeft();
        final float y = getPositionY(page.findMediaBox(), ++line) - height;

        contentStream.drawXObject(ximage, x, y, width, height);
        heightCorrection += height;
    }

    protected void writeText(final String text, final PDDocument document, final PDRectangle pageSize) throws IOException{
        final float positionY = getPositionY(pageSize, ++line);
        if(positionY < 0){ //NEW PAGE
            contentStream.close();
            final PDPage page = new PDPage(pageSize);
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            line = 0;
            heightCorrection = 0;

            final float newPositionY = getPositionY(pageSize, ++line);
            writeString(contentStream, text, getFontFamily(context.getFontFamily()), context.getFontSize(), context.getMarginLeft(), newPositionY);
        } else {
            writeString(contentStream, text, getFontFamily(context.getFontFamily()), context.getFontSize(), context.getMarginLeft(), positionY);
        }
    }

    private void writeString(final PDPageContentStream cos, final String string, final PDFont font, final float size, final float marginLeft,
            final float positionY)
                    throws IOException {
        cos.beginText();
        cos.setFont(font, size);
        cos.moveTextPositionByAmount(marginLeft, positionY);
        cos.drawString(string);
        cos.endText();
        heightCorrection += getLineHeight();
        lastY = positionY - getLineHeight();
    }

    protected float getPositionY(final PDRectangle rect, final int line){
        final float result = rect.getHeight() - getLineHeight() * (line) - heightCorrection;
        return result;
    }

    private float getLineHeight() {
        final float tempLineHeight = getFontFamily(context.getFontFamily()).getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * context.getFontSize() + context.getTextLinePadding();
        final float currentLineHeight = context.getLineHeight() > 0 ? context.getLineHeight() : tempLineHeight;
        return currentLineHeight;
    }

    /**
     * @param page
     * @param y the y-coordinate of the first row
     * @param content a 2d array containing the table data
     * @throws IOException
     **/
    protected void writeTable(final PDPage page, final String[][] content) throws IOException {
        final int rows = content.length;
        if(rows > 1){
            throw new UnsupportedOperationException("Multi row is currently not supported");
        }
        final int cols = content[0].length;

        final float colWidth = getColumnWidth(page, cols);
        final float tableWidth = colWidth * cols;
        final float colContentWidth = (colWidth - context.getCellMarginLeft() - context.getCellMarginRight());
        final WrapResult<List<List<String>>> wrapResult = TextWrapUtil.wrapText(content[0], getFontFamily(context.getFontFamily()), context.getFontSize(), colContentWidth);

        final float rowHeight = (getLineHeight()* wrapResult.getMaxNumberOfLines()) + context.getCellMarginTop() + context.getCellMarginBottom() + 2 * context.getBorderWidth();
        final float tableHeight = rowHeight;

        final float y = lastY;
        //draw the rows
        float nexty = y;
        for (int i = 0; i <= rows; i++) {
            contentStream.setLineWidth(context.getBorderWidth());
            contentStream.drawLine(context.getMarginLeft(),nexty,context.getMarginLeft()+tableWidth,nexty);
            nexty-= rowHeight;
        }

        //draw the columns
        float nextx = context.getMarginLeft();
        for (int i = 0; i <= cols; i++) {
            contentStream.drawLine(nextx,y,nextx,y-tableHeight);
            nextx += colWidth;
        }

        //now add the text
        float textx = context.getMarginLeft();
        final float texty = y;
        for(int i = 0; i < content.length; i++){
            for(int j = 0 ; j < content[i].length; j++){
                writeWrappedString(wrapResult.getWrapResult().get(j), textx, texty, rowHeight);
                textx += colWidth;
            }
            textx = context.getMarginLeft();
        }

        heightCorrection += rowHeight;
        lastY -= rowHeight;
    }

    protected float getColumnWidth(final PDPage page, final int cols) {
        final float tableWidth = page.findMediaBox().getWidth()-(context.getMarginLeft() + context.getMarginRight());
        final float colWidth = tableWidth/cols;
        return colWidth;
    }

    private void writeWrappedString(final List<String> textLines, final float textx, final float texty, final float rowHeight) throws IOException{
        int lineIndex = 0;
        for(final String text: textLines){
            contentStream.beginText();
            contentStream.setFont(getFontFamily(context.getFontFamily()), context.getFontSize());
            final float textYPosition = ((textLines.size() -1) * getLineHeight()) + texty - rowHeight + context.getCellMarginBottom() + (getLineHeight() / FONT_CORRECTING_FACTOR) - (lineIndex * getLineHeight());
            contentStream.moveTextPositionByAmount(textx + context.getCellMarginLeft() , textYPosition);
            contentStream.drawString(text);
            contentStream.endText();
            lineIndex++;
        }
    }

    public static enum ImageType {
        JPEG, OTHER
    }

    public static enum PageSize {
        A4;
    }
}
