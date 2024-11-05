package com.pdf.controller;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.pdf.service.DetailsService;
import com.pdf.service.InformationService;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class PdfController {
    @Autowired
    private DetailsService detailsService;

    @Autowired
    private InformationService informationService;

    @PostMapping("/getPdf")
    public String getPdf(@RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return "error";
        }
        String fileName = file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();

        // 读取pdf
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromStream(inputStream);

        // 提取表格
        StringBuilder sb = new StringBuilder();
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
        PdfTable[] tableList;
        for (int i = 0; i < pdf.getPages().getCount(); i++) {
            tableList = extractor.extractTable(i);
            if (tableList != null && tableList.length > 0) {
                for (PdfTable table : tableList) {
                    int row = table.getRowCount();
                    int col = table.getColumnCount();
                    for (int j = 0; j < row; j++) {
                        for (int k = 0; k < col; k++) {
                            String text = table.getText(j, k);
                            sb.append(text + "\t");
                        }
                        sb.append("\r\n");
                    }

                }
            }
        }
        // 转化为字符串
        String text = sb.toString();

        sb = new StringBuilder();
        PdfReader reader = new PdfReader(file.getInputStream());
        int numberOfPages = reader.getNumberOfPages();
        for (int i = 1; i <= numberOfPages; i++) {
            String pageContent = PdfTextExtractor.getTextFromPage(reader, i);
            sb.append(pageContent).append("\n");
        }
        reader.close();
        String text2 = sb.toString();
        inputStream.close();

        //System.out.println(text2);
        informationService.saveInformation(text2,fileName,text);

        return "success";
    }

}
