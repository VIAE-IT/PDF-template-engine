package com.viae.common.pdf.model;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public enum FontFamily {
    COURIER(PDType1Font.COURIER, 0.8f, 0.375f),
    HELVETICA_BOLD(PDType1Font.HELVETICA_BOLD, 0.4f, 0.375f),
    HELVETICA(PDType1Font.HELVETICA, 0.375f, 0.375f);

    private final PDFont fontFamily;
    private final float errorMarginX;
    private final float errorMarginY;
    private FontFamily(final PDFont fontFamily, final float errorMarginX, final float errorMarginY){
        this.fontFamily = fontFamily;
        this.errorMarginX = errorMarginX;
        this.errorMarginY = errorMarginY;
    }
    public PDFont getFontFamily() {
        return fontFamily;
    }

    public float getErrorMarginX() {
        return errorMarginX;
    }

    public float getErrorMarginY() {
        return errorMarginY;
    }
}
