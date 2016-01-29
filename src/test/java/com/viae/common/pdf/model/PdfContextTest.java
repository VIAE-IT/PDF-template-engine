package com.viae.common.pdf.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import test.config.PojoTester;

public class PdfContextTest {

    @Test
    public void coverage() {
        assertNotNull(new PdfContext.Builder.LazyInit());
    }

    @Test
    public void test() {
        PojoTester.test(PdfContext.class);
    }

}
