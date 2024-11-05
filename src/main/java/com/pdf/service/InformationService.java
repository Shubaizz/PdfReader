package com.pdf.service;

import com.pdf.domain.Information;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WT
* @description 针对表【information】的数据库操作Service
* @createDate 2024-11-05 13:39:52
*/
public interface InformationService extends IService<Information> {

    void saveInformation(String text2, String fileName,String text);
}
