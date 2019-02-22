package com.geekhalo.ddd.lite.demo.application;

import com.geekhalo.ddd.lite.codegen.controller.GenController;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategory;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryCreator;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryDto;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryUpdater;

import java.util.Optional;

/**
 * GenController 自动将该类中的方法，添加到 BaseNewsCategoryController 中
 */
@GenController("com.geekhalo.ddd.lite.demo.controller.BaseNewsCategoryController")
public interface NewsCategoryApplication extends BaseNewsCategoryApplication{
    @Override
    NewsCategory create(NewsCategoryCreator creator);

    @Override
    void update(Long id, NewsCategoryUpdater updater);

    @Override
    void enable(Long id);

    @Override
    void disable(Long id);

    @Override
    Optional<NewsCategoryDto> getById(Long aLong);
}
