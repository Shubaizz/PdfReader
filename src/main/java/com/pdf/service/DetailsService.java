package com.pdf.service;

import com.pdf.domain.Details;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WT
* @description 针对表【details】的数据库操作Service
* @createDate 2024-11-05 13:39:52
*/
public interface DetailsService extends IService<Details> {

    void savePdfDetail(Integer informationId, String text);
}
