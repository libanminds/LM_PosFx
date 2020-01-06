package com.libanminds.utils;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.datatable.DataTable;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDFGenerator {

    public static void generateDemoPDF(Stage stage) {
        ArrayList<ArrayList<String>> tableRows = new ArrayList<>();
        tableRows.add(new ArrayList<>(Arrays.asList("Apple", "5", "2")));
        tableRows.add(new ArrayList<>(Arrays.asList("Banana", "21", "3")));
        tableRows.add(new ArrayList<>(Arrays.asList("Tomato", "3", "2")));
        tableRows.add(new ArrayList<>(Arrays.asList("Potato", "15", "0")));
        tableRows.add(new ArrayList<>(Arrays.asList("Fridge", "5", "2")));
        tableRows.add(new ArrayList<>(Arrays.asList("Nuclear Heads", "10", "0")));
        tableRows.add(new ArrayList<>(Arrays.asList("Oranges", "50", "2")));

        String[] totals = {"Total Paid: $5000", "Total Returned: $1600", "Total: $3400"};
        PDFGenerator.generatePDF(stage, "Sold Items Report", "The following is the list of the quantities of items sold from X till Y",
                new ArrayList<>(
                        Arrays.asList("Item Name", "Quantity Sold", "Quantity Returned")),

                tableRows,
                totals
        );
    }

    public static void generatePDF(Stage stage, String title, String description, ArrayList<String> headers, ArrayList<ArrayList<String>> tableRows, String[] totals) {
        try {
            try (PDDocument doc = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                PDPageContentStream content = new PDPageContentStream(doc, page);

                content.beginText();
                content.newLineAtOffset(50, 780);
                content.setFont(PDType1Font.TIMES_BOLD, 16);
                content.setLeading(14.5f);
                content.showText(title);
                content.newLine();
                content.endText();

                multiLineText(content, description, 730);
                content.close();

                List<List> data = new ArrayList();
                data.add(headers);
                data.addAll(tableRows);

                float margin = 50;
                float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
                float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
                float yPosition = 680;
                float bottomMargin = 140;

                BaseTable dataTable = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, true);
                DataTable t = new DataTable(dataTable, page);
                t.addListToTable(data, DataTable.HASHEADER);
                float yPos = dataTable.draw();

                PDPage currentPage;
                if (dataTable.getCurrentPage() != page)
                    currentPage = dataTable.getCurrentPage();
                else
                    currentPage = page;

                content = new PDPageContentStream(doc, currentPage, PDPageContentStream.AppendMode.APPEND, false, true);
                content.beginText();
                content.newLineAtOffset(50, yPos - 20);
                content.setFont(PDType1Font.TIMES_ROMAN, 12);
                content.setLeading(14.5f);
                String s;
                for (String total : totals) {
                    s = total;
                    content.showText(s);
                    content.newLine();
                }
                content.endText();
                content.close();



                final DirectoryChooser directoryChooser = new DirectoryChooser();
                final File selectedDirectory = directoryChooser.showDialog(stage);
                if (selectedDirectory != null) {
                    doc.save(selectedDirectory.getAbsolutePath() + "/pdf_doc.pdf");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void multiLineText(PDPageContentStream content, String text, float yPos) {
        try {
            String[] wrT;
            String s;
            wrT = WordUtils.wrap(text, 110).split("\\r?\\n");
            content.beginText();
            content.newLineAtOffset(50, yPos);
            content.setFont(PDType1Font.TIMES_ROMAN, 12);
            content.setLeading(14.5f);
            for (String value : wrT) {
                s = value;
                content.showText(s);
                content.newLine();
            }
            content.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
