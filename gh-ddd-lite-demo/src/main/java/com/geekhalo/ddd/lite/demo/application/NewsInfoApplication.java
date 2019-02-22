package com.geekhalo.ddd.lite.demo.application;

import com.geekhalo.ddd.lite.codegen.controller.GenController;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfo;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoCreator;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoDto;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoUpdater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@GenController("com.geekhalo.ddd.lite.demo.controller.BaseNewsInfoController")
public interface NewsInfoApplication extends BaseNewsInfoApplication{

    // 手工维护方法
    NewsInfo create(Long categoryId, NewsInfoCreator creator);

    @Override
    void disable(Long id);

    @Override
    void update(Long id, NewsInfoUpdater updater);

    @Override
    void enable(Long id);

    @Override
    Page<NewsInfoDto> findValidByCategoryId(Long categoryId, Pageable pageable);
}
