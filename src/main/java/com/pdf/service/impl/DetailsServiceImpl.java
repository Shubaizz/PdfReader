package com.pdf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdf.domain.Details;
import com.pdf.service.DetailsService;
import com.pdf.mapper.DetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author WT
* @description 针对表【details】的数据库操作Service实现
* @createDate 2024-11-05 13:39:51
*/
@Service
public class DetailsServiceImpl extends ServiceImpl<DetailsMapper, Details>
    implements DetailsService {

    @Autowired
    private DetailsMapper detailsMapper;

    @Transactional
    @Override
    public void savePdfDetail(Integer informationId, String text) {
        // 可以自己修改的地方
        List<String> list = new ArrayList<>();
        list.add("附加税费");
        list.add("税款计算");
        list.add("税款缴纳");
        list.add("销售额");
        list.add("销售额2");

        // list中元素没在文本中就删除这个元素
        for (int i = 0; i < list.size(); i++) {
            if (!text.contains(list.get(i))) {
                list.remove(i);
                i--;
            }
        }
        // 按照list中内容在文本中的位置进行排序
        list.sort(Comparator.comparingInt(text::indexOf));
        // System.out.println(list);

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
                        line = line.trim();
                        String[] split1 = line.split("\t");
                        Details details = Details.builder()
                                .informationid(informationId)
                                .project(startWith)
                                .projectdetails(split1[0])
                                .columnnumber(split1[1])
                                .numberofmoth(split1[2])
                                .numberofyear(split1[3])
                                .immediatewithdrawalmonth(split1[4])
                                .refundprojectsaccumulated(split1[5])
                                .build();
                        detailsMapper.insert(details);
                    }

                }else {
                    throw new RuntimeException("error");
                }
            }else {
                // int c= 1/0;

                regex = list.get(i);
                // 把i后面内容除去"附加税费"其他作为新的文本
                String result = text.substring(text.indexOf(regex));
                // list的最后一位
                result = result.replace(regex, "");
                String[] split = result.split("\n");
                List<String> fixedTable = fixTable(split);

                // 输出修复后的表格
                for (String line : fixedTable) {
                    line = line.trim();
                    String[] split1 = line.split("\t");
                    Details details = Details.builder()
                            .informationid(informationId)
                            .project(regex)
                            .projectdetails(split1[0])
                            .columnnumber(split1[1])
                            .numberofmoth(split1[2])
                            .numberofyear(split1[3])
                            .immediatewithdrawalmonth(split1[4])
                            .refundprojectsaccumulated(split1[5])
                            .build();
                    detailsMapper.insert(details);
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




