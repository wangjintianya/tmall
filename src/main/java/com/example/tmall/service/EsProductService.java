package com.example.tmall.service;

import com.example.tmall.nosql.elasticsearch.document.EsProduct;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EsProductService {
    /**
     * 从数据库导入所有商品到 ES
     *
     * @return
     */
    int importAll();

    /**
     * 根据 id 删除商品
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除商品
     *
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据 id 创建商品
     *
     * @param id
     * @return
     */
    EsProduct create(Long id);

    /**
     * 根据关键字搜索名称或副标题
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);
}
