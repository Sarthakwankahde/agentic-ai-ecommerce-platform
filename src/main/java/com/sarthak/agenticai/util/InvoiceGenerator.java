package com.sarthak.agenticai.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.sarthak.agenticai.dto.InvoiceDto;
import com.sarthak.agenticai.dto.InvoiceItemDto;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class InvoiceGenerator {

    public byte[] generate(InvoiceDto invoice) {

        try {

            Document document = new Document(PageSize.A4);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    22
            );

            Paragraph title = new Paragraph(
                    "Agentic AI Ecommerce Platform",
                    titleFont
            );

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Invoice"));
            document.add(new Paragraph("Order ID : " + invoice.getOrderId()));
            document.add(new Paragraph("Customer : " + invoice.getCustomerName()));
            document.add(new Paragraph("Email : " + invoice.getEmail()));
            document.add(new Paragraph("Address : " + invoice.getAddress()));
            document.add(new Paragraph("Order Date : " + invoice.getOrderDate()));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);

            table.setWidthPercentage(100);

            table.addCell("Product");

            table.addCell("Quantity");

            table.addCell("Price");

            for (InvoiceItemDto item : invoice.getItems()) {

                table.addCell(item.getProductName());

                table.addCell(String.valueOf(item.getQuantity()));

                table.addCell("₹ " + item.getPrice());

            }

            document.add(table);

            document.add(new Paragraph(" "));

            Paragraph total = new Paragraph(
                    "Grand Total : ₹ " + invoice.getTotalAmount()
            );

            total.setAlignment(Element.ALIGN_RIGHT);

            document.add(total);

            document.add(new Paragraph(" "));

            Paragraph footer = new Paragraph(
                    "Thank you for shopping with us!"
            );

            footer.setAlignment(Element.ALIGN_CENTER);

            document.add(footer);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException(ex);

        }

    }

}