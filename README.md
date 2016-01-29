PDF-template-engine
===================================================
PDF-template-engine is started by [VIAE](http://viae-it.com/) as a wrapper framework around the [apache pdf box framework](https://pdfbox.apache.org/). 
This PDF engine should make it more convenient to create PDF files from Java.
Most of the implementation details should be hidden for the users (e.g. width / height calculations, new line positions, ...).
In a first phase, 2 render solutions will be available: a String based engine and a Java code based engine

##Example String based engine
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
builder.buildPdf(joiner.toString(), PageSize.A4, "voorbeeld.pdf");
```
