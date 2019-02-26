package com.geekhalo.ddd.lite.demo.domain.news.category;

import com.geekhalo.ddd.lite.codegen.application.GenApplication;

import java.util.Optional;

/**
 * GenApplication 自动将该接口中的方法添加到 BaseNewsCategoryRepository 中
 */
@GenApplication
public interface NewsCategoryRepository extends BaseNewsCategoryRepository{
    @Override
    Optional<NewsCategory> getById(NewsCategoryId aLong);
}
