package com.viae.common.pdf.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.viae.common.pdf.model.FontFamily;
import com.viae.common.pdf.util.TextWrapUtil.WrapResult;

public class TextWrapUtilTest {
    final String DUMMY_TEXT_SHORT = "Maarten Vandeperre";
    final String DUMMY_TEXT_LONG = "Zolder vormt zondag het decor voor de strijd om de regenboogtrui in het veld. Volgt Mathieu van der Poel zichzelf op? Troeft Wout van Aert de Nederlandse titelverdediger af? Of is er een derde hond? Breng hier uw stem uit.";

    @Test
    public void coverageTest(){
        assertNotNull(new TextWrapUtil());
    }

    @Test
    public void testWrapTextForNullString(){
        final String text = null;
        final WrapResult<List<String>> result = TextWrapUtil.wrapText(text, FontFamily.COURIER, 1, 1);
        assertNotNull(result);
        assertEquals(0, result.getMaxNumberOfLines());
        assertEquals(Collections.emptyList(), result.getWrapResult());
    }

    @Test
    public void testWrapTextForEmptyString(){
        final String text = "";
        final WrapResult<List<String>> result = TextWrapUtil.wrapText(text, FontFamily.COURIER, 1, 1);
        assertNotNull(result);
        assertEquals(0, result.getMaxNumberOfLines());
        assertEquals(Collections.emptyList(), result.getWrapResult());
    }

    @Test
    public void testWrapTextForSpacelessString(){
        final String text = "test";
        final WrapResult<List<String>> result = TextWrapUtil.wrapText(text, FontFamily.COURIER, 1, 1);
        assertNotNull(result);
        assertEquals(1, result.getMaxNumberOfLines());
        assertEquals("test", result.getWrapResult().get(0));
    }

    @Test
    public void testWrapTextForNullArray(){
        final String[] textArray = null;
        final WrapResult<List<List<String>>> result = TextWrapUtil.wrapText(textArray, FontFamily.COURIER, 1, 1);
        assertNotNull(result);
        assertEquals(0, result.getMaxNumberOfLines());
        assertEquals(Collections.emptyList(), result.getWrapResult());
    }

    @Test
    public void testWrapTextForSingleString() {
        //GIVEN
        final FontFamily fontFamily = FontFamily.COURIER;
        final float fontSize = 5;
        final float maxLineWidth = 207.43782f;
        //WHEN
        final TextWrapUtil.WrapResult<List<String>> wrapResult = TextWrapUtil.wrapText(DUMMY_TEXT_LONG, fontFamily, fontSize, maxLineWidth);
        final List<String> result = wrapResult.getWrapResult();
        //THEN
        assertEquals(5, wrapResult.getMaxNumberOfLines());
        assertEquals(5, result.size());
        final Iterator<String> it = result.iterator();
        assertEquals("Zolder vormt zondag het decor voor de strijd om de ", it.next());
        assertEquals("regenboogtrui in het veld. Volgt Mathieu van der Poel ", it.next());
        assertEquals("zichzelf op? Troeft Wout van Aert de Nederlandse ", it.next());
        assertEquals("titelverdediger af? Of is er een derde hond? Breng ", it.next());
        assertEquals("hier uw stem uit.", it.next());
    }
    @Test
    public void testWrapTextForStringArray() {
        //GIVEN
        final FontFamily fontFamily = FontFamily.COURIER;
        final float fontSize = 5;
        final float maxLineWidth = 207.43782f;
        final String[] input = new String[]{DUMMY_TEXT_SHORT, DUMMY_TEXT_SHORT, DUMMY_TEXT_LONG, DUMMY_TEXT_SHORT, DUMMY_TEXT_SHORT};
        //WHEN
        final TextWrapUtil.WrapResult<List<List<String>>> wrapResult = TextWrapUtil.wrapText(input, fontFamily, fontSize, maxLineWidth);
        final List<List<String>> result = wrapResult.getWrapResult();
        //THEN
        assertEquals(5, wrapResult.getMaxNumberOfLines());
        assertEquals(5, result.size());
        final Iterator<List<String>> listIt = result.iterator();

        final Collection<String> firstResult = listIt.next();
        assertEquals(1, firstResult.size());
        assertEquals("Maarten Vandeperre", firstResult.iterator().next());

        final Collection<String> secondResult = listIt.next();
        assertEquals(1, secondResult.size());
        assertEquals("Maarten Vandeperre", secondResult.iterator().next());

        final Collection<String> thirdResult = listIt.next();
        assertEquals(5, thirdResult.size());
        final Iterator<String> thirdResultIt = thirdResult.iterator();
        assertEquals("Zolder vormt zondag het decor voor de strijd om de ", thirdResultIt.next());
        assertEquals("regenboogtrui in het veld. Volgt Mathieu van der Poel ", thirdResultIt.next());
        assertEquals("zichzelf op? Troeft Wout van Aert de Nederlandse ", thirdResultIt.next());
        assertEquals("titelverdediger af? Of is er een derde hond? Breng ", thirdResultIt.next());
        assertEquals("hier uw stem uit.", thirdResultIt.next());;

        final Collection<String> fourthResult = listIt.next();
        assertEquals(1, fourthResult.size());
        assertEquals("Maarten Vandeperre", fourthResult.iterator().next());

        final Collection<String> fifthResult = listIt.next();
        assertEquals(1, fifthResult.size());
        assertEquals("Maarten Vandeperre", fifthResult.iterator().next());

    }

}
