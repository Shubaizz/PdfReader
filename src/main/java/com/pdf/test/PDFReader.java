package com.pdf.test;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFReader {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("附加税费");
        list.add("税款计算");
        list.add("税款缴纳");
        list.add("销售额");

        // 读取pdf
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile("C:\\Users\\WT\\Desktop\\增值税一般纳税人申报-增值税及附加税费申报表（一般纳税人适用）7月.pdf");

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
        // 按照list中内容在文本中的位置进行排序
        list.sort(Comparator.comparingInt(text::indexOf));

        String regex = null;
        Pattern pattern = null;
        Matcher matcher = null;
        for(int i = 0; i < list.size(); i++){
            if(i < list.size() - 1){
                String startWith = list.get(i);
                String endWith = list.get(i + 1);
                regex = String.format("(?s)%s(.*?)%s", startWith, endWith);
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(text.trim());

                if (matcher.find()) {
                    String result = matcher.group(1).trim();
                    String[] split = result.split("\n");
                    List<String> fixedTable = fixTable(split);

                    // 输出修复后的表格
                    for (String line : fixedTable) {
                        System.out.println(line.trim());
                    }
                }
                System.out.println();
            }else {
                regex = list.get(i);
                // 把i后面内容除去"附加税费"其他作为新的文本
                String result = text.substring(text.indexOf(regex));
                result = result.replace("附加税费", "");
                String[] split = result.split("\n");
                List<String> fixedTable = fixTable(split);

                // 输出修复后的表格
                for (String line : fixedTable) {
                    System.out.println(line.trim());
                }
            }
        }
    }

    private static List<String> fixTable(String[] lines) {
        List<String> fixedLines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        // 遍历每一行文本
        for (String line : lines) {
            // 判断是否是拆分的行，通常拆分的行内容会缺少一部分
            // 假设每行都应该有6个元素（数字和文本），否则可能是被拆分的
            String[] parts = line.split("\\s+"); // 按空白字符拆分

            // 如果该行元素个数小于6（认为是拆分的行），则将其与前一行合并
            if (parts.length < 6) {
                currentLine.append(line.trim());  // 合并到上一行
            } else {
                // 如果前一行有未处理的内容，将其添加到结果中
                if (currentLine.length() > 0) {
                    fixedLines.add(currentLine.toString());
                    currentLine.setLength(0); // 重置 StringBuilder
                }
                // 直接将当前行加入结果
                fixedLines.add(line);
            }
        }

        // 如果最后还有未处理的合并内容，添加到结果中
        if (currentLine.length() > 0) {
            fixedLines.add(currentLine.toString());
        }

        return fixedLines;
    }

}
