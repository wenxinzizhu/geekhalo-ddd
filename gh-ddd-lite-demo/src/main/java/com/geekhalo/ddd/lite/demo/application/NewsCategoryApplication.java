package com.geekhalo.ddd.lite.demo.application;

import com.geekhalo.ddd.lite.codegen.controller.GenController;
import com.geekhalo.ddd.lite.demo.domain.news.category.*;

import java.util.Optional;

/**
 * GenController 自动将该类中的方法，添加到 BaseNewsCategoryController 中
 */
@GenController("com.geekhalo.ddd.lite.demo.controller.BaseNewsCategoryController")
public interface NewsCategoryApplication extends BaseNewsCategoryApplication{
    @Override
    NewsCategoryId create(NewsCategoryCreator creator);

    @Override
    void update(NewsCategoryId id, NewsCategoryUpdater updater);

    @Override
    void enable(NewsCategoryId id);

    @Override
    void disable(NewsCategoryId id);

    @Override
    Optional<NewsCategoryDto> getById(NewsCategoryId aLong);
}
