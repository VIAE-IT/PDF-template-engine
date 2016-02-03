package com.viae.common.pdf.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import com.viae.common.pdf.model.FontFamily;
import com.viae.common.pdf.model.PdfContext;
import com.viae.common.utils.validate.Validate;

public class StringPdfBuilder extends PdfBuilder {

    private static final String PREFIX_CONFIG = "conf|";
    private static final String PREFIX_TEXT = "text|";
    private static final String PREFIX_IMAGE = "img|";
    private static final String PREFIX_TABLE_ROW = "table_row|";

    public StringPdfBuilder(){
        context = PdfContext.builder().create().build();
    }

    protected static interface BuilderClient {
        void whenBuilding(StringPdfBuilder builder);
    }

    public void buildPdf(final String content, final PageSize pageSize, final String path){
        try(final PDDocument document = new PDDocument()){
            this.document = document;
            final PDPage page  = new PDPage(getPageSize(pageSize));
            document.addPage(page);
            initPageStart(page.findMediaBox());

            try(final PDPageContentStream cos = new PDPageContentStream(document, page)){
                contentStream = cos;

                final String[] lines = content.split("\\|EOL\\|\r?\n");
                for(final String contentLine : lines){
                    handleContentLine(contentLine, document, page);
                }
            }

            document.save(path);
        } catch(final IOException | COSVisitorException e){
            throw new RuntimeException(e);
        }
    }

    private void handleContentLine(final String contentLine, final PDDocument document, final PDPage page) throws IOException{
        if(contentLine.startsWith(PREFIX_CONFIG)){
            handleConfigContent(contentLine);
        } else if(contentLine.startsWith(PREFIX_TEXT)){
            handleTextContent(contentLine, document, page.getMediaBox());
        } else if(contentLine.startsWith(PREFIX_IMAGE)){
            handleImageContent(contentLine, document, page);
        } else if(contentLine.startsWith(PREFIX_TABLE_ROW)){
            final String[] data = contentLine.replace(PREFIX_TABLE_ROW, "").split("\\|");
            writeTable(page, new String[][]{data});
        }
    }

    private void handleImageContent(final String contentLine, final PDDocument document, final PDPage page) throws IOException {
        final Pattern pattern = Pattern.compile("^img\\|([^|]+)\\|([^|]+)\\|?([^|]*)$");
        final Matcher matcher = pattern.matcher(contentLine);
        if(matcher.matches()){
            final String path = matcher.group(1);
            final String extensionValue = matcher.group(2);
            final String maxWidthValue = matcher.group(3);
            final ImageType extension = "jpg".equals(extensionValue.toLowerCase()) || "jpeg".equals(extensionValue.toLowerCase()) ? ImageType.JPEG : ImageType.OTHER;
            final File image = new File(path);
            Validate.isTrue(image.exists(), "image does not exist");
            if(maxWidthValue != null && ! "".equals(maxWidthValue)){
                writeImage(page, image, extension, Float.parseFloat(maxWidthValue));
            } else {
                writeImage(page, image, extension);
            }
        } else {
            throw new IllegalArgumentException(String.format("%s is not a valid text line!\nShould be of format 'img|path|extension|(|max width)|EOL|\n'", contentLine + ""));
        }
    }

    private void handleTextContent(final String textLine, final PDDocument document, final PDRectangle rect) throws IOException {
        final Pattern pattern = Pattern.compile("^text\\|([^|]+)$");
        final Matcher matcher = pattern.matcher(textLine);
        if(matcher.matches()){
            writeText(textLine, document, rect);
        } else {
            throw new IllegalArgumentException(String.format("%s is not a valid text line!\nShould be of format 'text|text|EOL|\n'", textLine + ""));
        }
    }

    private void handleConfigContent(final String configLine){
        final Pattern pattern = Pattern.compile("^conf\\|([^|]+)\\|([^|]+)$");
        final Matcher matcher = pattern.matcher(configLine);
        if(matcher.matches()){
            final String key = matcher.group(1);
            final String value = matcher.group(2);
            handleConfig(key, value);
        } else {
            throw new IllegalArgumentException(String.format("%s is not a valid config line!\nShould be of format 'conf|key|value|EOL|\n'", configLine + ""));
        }
    }

    private void handleConfig(final String key, final String value){
        switch (key) {
            case "font_family":
                context = PdfContext.builder().deepCopy(context).fontFamily(getFontFamily(value)).build();
                break;
            case "font_size":
                context = PdfContext.builder().deepCopy(context).fontSize(Float.parseFloat(value)).build();
                break;
            case "line_height":
                context = PdfContext.builder().deepCopy(context).lineHeight(Integer.parseInt(value)).build();
                break;
            case "margin_left":
                context = PdfContext.builder().deepCopy(context).marginLeft(Float.parseFloat(value)).build();
                break;
            case "margin_right":
                context = PdfContext.builder().deepCopy(context).marginRight(Float.parseFloat(value)).build();
                break;
            case "border_width":
                context = PdfContext.builder().deepCopy(context).borderWidth(Float.parseFloat(value)).build();
                break;
            case "cell_margin_left":
                context = PdfContext.builder().deepCopy(context).cellMarginLeft(Float.parseFloat(value)).build();
                break;
            case "cell_margin_bottom":
                context = PdfContext.builder().deepCopy(context).cellMarginBottom(Float.parseFloat(value)).build();
                break;
            case "cell_margin_right":
                context = PdfContext.builder().deepCopy(context).cellMarginRight(Float.parseFloat(value)).build();
                break;
            case "cell_margin_top":
                context = PdfContext.builder().deepCopy(context).cellMarginTop(Float.parseFloat(value)).build();
                break;
            default:
                throw new UnsupportedOperationException(String.format("Configuration for %s is not supported yet.", key));
        }
    }

    protected FontFamily getFontFamily(final String fontFamily) {
        switch (fontFamily) {
            case "HELVETICA_BOLD":
                return FontFamily.HELVETICA_BOLD;
            case "HELVETICA":
                return FontFamily.HELVETICA;
            case "COURIER":
                return FontFamily.COURIER;
            default:
                throw new UnsupportedOperationException(String.format("font family %s is currently not supported", fontFamily));
        }
    }
}
