package com.example.tmall.service;

import com.example.tmall.mbg.model.PmsBrand;

import java.util.List;

public interface PmsBrandService {
    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrand pmsBrand);

    int updateBrand(Long id, PmsBrand pmsBrand);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
