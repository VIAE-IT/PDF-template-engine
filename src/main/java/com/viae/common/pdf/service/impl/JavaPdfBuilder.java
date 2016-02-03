package com.viae.common.pdf.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import com.viae.common.pdf.model.PdfContext;

public class JavaPdfBuilder extends PdfBuilder {

    private PDPage pageState;

    public JavaPdfBuilder(){
        context = PdfContext.builder().create().build();
    }

    public static interface BuilderClient {
        void whenBuilding(JavaPdfBuilder builder);
    }

    public void buildPdf(final BuilderClient client, final PageSize pageSize, final String path){
        try(final PDDocument document = new PDDocument()){
            this.document = document;
            this.pageState = new PDPage(getPageSize(pageSize));
            document.addPage(pageState);
            line = 0;
            lastY = pageState.findMediaBox().getHeight();

            try(final PDPageContentStream cos = new PDPageContentStream(document, pageState)){
                contentStream = cos;

                client.whenBuilding(this);
            }

            document.save(path);
        } catch(final IOException | COSVisitorException e){
            throw new RuntimeException(e);
        }
    }

    public void setContext(final PdfContext context) {
        this.context = context;
    }

    public void writeText(final String text){
        try {
            writeText(text, document, pageState.findMediaBox());
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeTableRow(final String... columns){
        try {
            writeTable(pageState, new String[][]{columns});
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeImage(final File image, final ImageType type, final float maxWidth){
        try {
            writeImage(pageState, image, type, maxWidth);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeImage(final File image, final ImageType type){
        try {
            writeImage(pageState, image, type);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeNewLine(){
        try {
            writeNewLine(document, pageState.findMediaBox());
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
