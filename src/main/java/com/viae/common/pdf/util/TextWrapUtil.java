package com.viae.common.pdf.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * Utility class to handle text wrapping within the pdf.
 *
 * @author Vandeperre Maarten
 */
public class TextWrapUtil {

    /**
     * Wrap the given text (regarding the given context).
     *
     * @param text, the text you want to wrap
     * @param fontFamily, the used font family (influences the width of the text string).
     * @param fontSize, the used font size (influences the width of the text string).
     * @param maxLineWidth, the maximum width a text string should have within the pdf.
     * @return the wrap result: a combination of the max number of lines (i.e. how many lines were split of) and the lines them self.
     */
    public static WrapResult<List<String>> wrapText(final String text, final PDFont fontFamily, final float fontSize, final float maxLineWidth) {
        final List<String> result = new LinkedList<>();
        final float charWidth = getCharWidth(fontFamily, fontSize);
        final int maxCharAmount = (int) (maxLineWidth / charWidth);

        if(text != null){
            String tempText = text;
            while(tempText.length() > 0){
                final int endIndex = maxCharAmount > tempText.length() ? tempText.length() : maxCharAmount;
                final int lastSpaceIndex = tempText.substring(0, endIndex).trim().lastIndexOf(" ");
                String toAdd = tempText;
                if(lastSpaceIndex != -1 && tempText.length() > maxCharAmount){
                    toAdd = tempText.substring(0, lastSpaceIndex + 1);
                    tempText = tempText.substring(lastSpaceIndex + 1);
                    result.add(toAdd);
                } else {
                    result.add(tempText);
                    tempText = "";
                }
            }
        }
        return new WrapResult<>(result.size(), result);
    }

    /**
     * Wrap the given text lines (regarding the given context).
     *
     * @param text, the lines of text you want to wrap
     * @param fontFamily, the used font family (influences the width of the text string).
     * @param fontSize, the used font size (influences the width of the text string).
     * @param maxLineWidth, the maximum width a text string should have within the pdf.
     * @return the wrap result: a combination of the max number of lines and the lines them self.
     *  E.g. you provide two strings, the first one is 10 chars, the second one 5.
     *  you want to wrap on a width corresponding to 6 characters:
     *  than max number of lines will be 2 (from the first result).
     *  than lines will contain 2 lines at index 0, and 1 line at index 1.
     */
    public static WrapResult<List<List<String>>> wrapText(final String[] textArray, final PDFont fontFamily, final float fontSize, final float maxLineWidth) {
        final List<List<String>> result = new LinkedList<>();
        int maxAmountOfLines = 0;
        if(textArray != null){
            for(final String text : textArray){
                final WrapResult<List<String>> wrapResult = wrapText(text, fontFamily, fontSize, maxLineWidth);
                maxAmountOfLines = Math.max(maxAmountOfLines, wrapResult.getMaxNumberOfLines());
                result.add(wrapResult.getWrapResult());
            }
        }
        return new WrapResult<>(maxAmountOfLines, result);
    }

    private static float getCharWidth(final PDFont fontFamily, final float fontSize) {
        return fontFamily.getFontDescriptor().getFontBoundingBox().getWidth() / 1000 * fontSize;
    }

    /**
     * Result of text wrapping.
     *
     * @author Vandeperre Maarten
     *
     * @param <RESULT_TYPE>, the type of the wrap result.
     */
    public static final class WrapResult<RESULT_TYPE> {
        private final int maxNumberOfLines;
        private final RESULT_TYPE wrapResult;

        private WrapResult(final int maxNumberOfLines, final RESULT_TYPE wrapResult) {
            this.maxNumberOfLines = maxNumberOfLines;
            this.wrapResult = wrapResult;
        }

        /**
         * @return the number of lines of the text entry which was split the most.
         *
         * E.g. you provide two strings, the first one is 10 chars, the second one 5.
         *  you want to wrap on a width corresponding to 6 characters:
         *  than max number of lines will be 2 (from the first result).
         */
        public int getMaxNumberOfLines() {
            return maxNumberOfLines;
        }

        /**
         * @return the result of the text wrap
         */
        public RESULT_TYPE getWrapResult() {
            return wrapResult;
        }
    }
}
