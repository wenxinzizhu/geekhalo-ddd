package com.geekhalo.ddd.lite.demo.application.impl;

import com.geekhalo.ddd.lite.demo.application.NewsCategoryApplication;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategory;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryDto;
import org.springframework.stereotype.Service;

@Service
public class NewsCategoryApplicationImpl extends BaseNewsCategoryApplicationSupport
    implements NewsCategoryApplication {

    @Override
    protected NewsCategoryDto convertNewsCategory(NewsCategory src) {
        return new NewsCategoryDto(src);
    }
}
