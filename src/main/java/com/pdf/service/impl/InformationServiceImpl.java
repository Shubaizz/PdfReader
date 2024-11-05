package com.pdf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdf.domain.Information;
import com.pdf.service.InformationService;
import com.pdf.mapper.InformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author WT
* @description 针对表【information】的数据库操作Service实现
* @createDate 2024-11-05 13:39:52
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private DetailsServiceImpl detailsService;

    @Transactional
    @Override
    public void saveInformation(String text2, String fileName,String text) {
        String regex = "申报日期：(\\d{4}-\\d{2}-\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text2);
        String dateofdeclaration = null;
        if (matcher.find()) {
            dateofdeclaration = matcher.group(1).trim();
        } else {
            throw new RuntimeException("error");
        }

        regex = "填表日期：(\\d{4}-\\d{2}-\\d{2})";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(text2);
        String dataoffilling = null;
        if (matcher.find()) {
            dataoffilling = matcher.group(1).trim();
        } else {
            throw new RuntimeException("error");
        }

        regex = "纳税人名称：(.*?)(?= )";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(text2);
        String name = null;
        if (matcher.find()) {
            name = matcher.group(1).trim();
        } else {
            throw new RuntimeException("error");
        }

        regex = "纳税人识别号（统一社会信用代码）：(.*?)(?=\\s|$)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(text2);
        String number = null;
        if (matcher.find()) {
            number = matcher.group(1).trim();
        } else {
            throw new RuntimeException("error");
        }

        regex = "税款所属时间：(\\d{4}-\\d{2}-\\d{2}至\\d{4}-\\d{2}-\\d{2})";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(text2);
        String taxationperiod = null;
        if (matcher.find()) {
            taxationperiod = matcher.group(1).trim();
        } else {
            throw new RuntimeException("error");
        }

        regex = "金额单位：(.*)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(text2);
        String amountunit = null;
        if (matcher.find()) {
            amountunit = matcher.group(1).trim();
        } else {
            throw new RuntimeException("error");
        }

        String[] split1 = text2.split("\n");
        String tableName = split1[0]+split1[1];

        Information information = Information.builder()
                .name(name)
                .dataoffilling(dataoffilling)
                .dateofdeclaration(dateofdeclaration)
                .taxationperiod(taxationperiod)
                .number(number)
                .amountunit(amountunit)
                .tablename(tableName)
                .filename(fileName)
                .build();
        int insert = informationMapper.insert(information);

        detailsService.savePdfDetail(insert,text);
    }
}




