PDF-template-engine
===================================================
PDF-template-engine is started by [VIAE](http://viae-it.com/) as a wrapper framework around the [apache pdf box framework](https://pdfbox.apache.org/). 
This PDF engine should make it more convenient to create PDF files from Java.
Most of the implementation details should be hidden for the users (e.g. width / height calculations, new line positions, ...).
In a first phase, 2 render solutions will be available: a String based engine and a Java code based engine

##Example String based engine
[Preview](https://github.com/VIAE-IT/PDF-template-engine/blob/master/docu/sample/voorbeeld_string_based.pdf)
```java
final StringJoiner joiner = new StringJoiner("\n");
joiner.add("conf|font_family|HELVETICA_BOLD|EOL|");
joiner.add("conf|font_size|15|EOL|");
joiner.add("conf|line_height|-1|EOL|");
joiner.add("conf|margin_left|100|EOL|");
joiner.add("conf|margin_right|100|EOL|");
joiner.add("img|src/test/resources/schoonheidsspecialiste.png|png|25|EOL|");
joiner.add("text|Big header title|EOL|");
joiner.add("conf|margin_left|100|EOL|");

joiner.add("conf|font_family|HELVETICA_BOLD|EOL|");
joiner.add("conf|font_size|10|EOL|");
joiner.add("text|Subtitle1|EOL|");

joiner.add("conf|font_family|COURIER|EOL|");
joiner.add("conf|font_size|5|EOL|");
joiner.add("conf|cell_margin_left|10|EOL|");
joiner.add("conf|cell_margin_top|10|EOL|");
joiner.add("conf|cell_margin_bottom|10|EOL|");
joiner.add("table_row|name|maarten vandeperre|EOL|");
joiner.add("table_row|role|software engineer|EOL|");
joiner.add("table_row|company|VIAE|EOL|");

joiner.add("conf|font_family|HELVETICA_BOLD|EOL|");
joiner.add("conf|font_size|10|EOL|");
joiner.add("text|Subtitle2|EOL|");
joiner.add("");


final StringPdfBuilder builder = new StringPdfBuilder();
builder.buildPdf(joiner.toString(), PageSize.A4, "sample_string_based.pdf");
```

##Example Java code based engine
[Preview](https://github.com/VIAE-IT/PDF-template-engine/blob/master/docu/sample/sample_java_based.pdf)
```java
final JavaPdfBuilder.BuilderClient client = new JavaPdfBuilder.BuilderClient() {
  @Override
  public void whenBuilding(final JavaPdfBuilder builder) {
      PdfContext context = PdfContext.builder().create().build();

      final StringJoiner joiner = new StringJoiner("\n");
      context = PdfContext.builder()
              .deepCopy(context)
              .fontFamily("HELVETICA_BOLD")
              .fontSize(15)
              .lineHeight(-1)
              .marginLeft(100)
              .marginRight(100)
              .textLinePadding(1f)
              .build();
      builder.setContext(context);
      builder.writeImage(image, ImageType.OTHER);
      builder.writeText("Big header title");
      context = PdfContext.builder()
              .deepCopy(context)
              .marginLeft(100)
              .build();
      builder.setContext(context);

      context = PdfContext.builder()
              .deepCopy(context)
              .fontFamily("HELVETICA_BOLD")
              .fontSize(10)
              .build();
      builder.setContext(context);
      builder.writeText("Subtitle 1");

      context = PdfContext.builder()
              .deepCopy(context)
              .fontFamily("COURIER")
              .fontSize(5)
              .cellMarginLeft(10)
              .cellMarginTop(10)
              .cellMarginBottom(10)
              .build();
      builder.setContext(context);
      builder.writeTableRow("Table header");
      builder.writeTableRow("naam1", "maarten", "vandeperre");
      builder.writeTableRow("naam2", "VIAE is a company actively involved in IT development (going from mobile applications and webdevelopmen to providing libraries like the pdf rendering engine. It is founded by Maarten Vandeperre (software engineer, Gent).");
      builder.writeTableRow("naam3", "maarten vandeperre");
      builder.writeTableRow("naam4", "maarten vandeperre");

      context = PdfContext.builder()
              .deepCopy(context)
              .fontFamily("HELVETICA_BOLD")
              .fontSize(10)
              .build();
      builder.setContext(context);
      builder.writeText("Subtitle2");
      joiner.add("");
  }
};

final JavaPdfBuilder builder = new JavaPdfBuilder();
builder.buildPdf(client, PageSize.A4, "voorbeeld.pdf");
```
