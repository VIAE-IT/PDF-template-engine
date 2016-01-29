package com.viae.common.pdf.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PdfContext {
    private String fontFamily;
    private float fontSize;
    private float marginLeft;
    private float marginRight;
    private float lineHeight;
    private float borderWidth;
    private float cellMarginLeft;
    private float cellMarginRight;
    private float cellMarginTop;
    private float cellMarginBottom;
    private float textLinePadding;

    public PdfContext(){
    }

    public PdfContext(final PdfContext toCopy){
        if(toCopy != null){
            setFontFamily(toCopy.getFontFamily());
            setFontSize(toCopy.getFontSize());
            setMarginLeft(toCopy.getMarginLeft());
            setMarginRight(toCopy.getMarginRight());
            setLineHeight(toCopy.getLineHeight());
            setBorderWidth(toCopy.getBorderWidth());
            setCellMarginBottom(toCopy.getCellMarginBottom());
            setCellMarginLeft(toCopy.getCellMarginLeft());
            setCellMarginRight(toCopy.getCellMarginRight());
            setCellMarginTop(toCopy.getCellMarginTop());
            setTextLinePadding(toCopy.getTextLinePadding());
        }
    }

    public String getFontFamily() {
        return fontFamily;
    }

    private void setFontFamily(final String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public float getFontSize() {
        return fontSize;
    }

    private void setFontSize(final float fontSize) {
        this.fontSize = fontSize;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    private void setMarginLeft(final float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    private void setMarginRight(final float marginRight) {
        this.marginRight = marginRight;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    private void setLineHeight(final float lineHeight) {
        this.lineHeight = lineHeight;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    private void setBorderWidth(final float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getCellMarginLeft() {
        return cellMarginLeft;
    }

    private void setCellMarginLeft(final float cellMarginLeft) {
        this.cellMarginLeft = cellMarginLeft;
    }

    public float getCellMarginRight() {
        return cellMarginRight;
    }

    private void setCellMarginRight(final float cellMarginRight) {
        this.cellMarginRight = cellMarginRight;
    }

    public float getCellMarginTop() {
        return cellMarginTop;
    }

    private void setCellMarginTop(final float cellMarginTop) {
        this.cellMarginTop = cellMarginTop;
    }

    public float getCellMarginBottom() {
        return cellMarginBottom;
    }

    private void setCellMarginBottom(final float cellMarginBottom) {
        this.cellMarginBottom = cellMarginBottom;
    }

    public float getTextLinePadding() {
        return textLinePadding;
    }

    private void setTextLinePadding(final float textLinePadding) {
        this.textLinePadding = textLinePadding;
    }

    public static final Builder builder(){
        return Builder.getInstance().clean();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Builder class to create {@link PdfContext} objects
     */
    public final static class Builder {
        private PdfContext context;

        private Builder() {
        }

        public Builder fontFamily(final String fontFamily){
            context.setFontFamily(fontFamily);
            return this;
        }

        public Builder fontSize(final float fontSize){
            context.setFontSize(fontSize);
            return this;
        }

        public Builder marginLeft(final float marginLeft){
            context.setMarginLeft(marginLeft);
            return this;
        }

        public Builder marginRight(final float marginRight){
            context.setMarginRight(marginRight);
            return this;
        }

        public Builder lineHeight(final float lineHeight){
            context.setLineHeight(lineHeight);
            return this;
        }

        public Builder borderWidth(final float borderWidth){
            context.setBorderWidth(borderWidth);
            return this;
        }

        public Builder cellMarginBottom(final float cellMarginBottom){
            context.setCellMarginBottom(cellMarginBottom);
            return this;
        }

        public Builder cellMarginLeft(final float cellMarginLeft){
            context.setCellMarginLeft(cellMarginLeft);
            return this;
        }

        public Builder cellMarginTop(final float cellMarginTop){
            context.setCellMarginTop(cellMarginTop);
            return this;
        }

        public Builder cellMarginRight(final float cellMarginRight){
            context.setCellMarginRight(cellMarginRight);
            return this;
        }

        public Builder textLinePadding(final float textLinePadding){
            context.setTextLinePadding(textLinePadding);
            return this;
        }

        public Builder clean() {
            context = null;
            return this;
        }

        public Builder create() {
            context = new PdfContext();
            return this;
        }

        public Builder deepCopy(final PdfContext original) {
            context = new PdfContext(original);
            return this;
        }

        public PdfContext build() {
            return context;
        }

        private static final class LazyInit {
            public static final Builder INSTANCE = new Builder();
        }

        private static synchronized Builder getInstance() {
            return LazyInit.INSTANCE;
        }
    }
}
