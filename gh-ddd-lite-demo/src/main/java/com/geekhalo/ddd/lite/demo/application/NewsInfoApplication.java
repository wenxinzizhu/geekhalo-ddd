package com.geekhalo.ddd.lite.demo.application;

import com.geekhalo.ddd.lite.codegen.controller.GenController;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryId;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfo;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoCreator;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoDto;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoUpdater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;

@GenController("com.geekhalo.ddd.lite.demo.controller.BaseNewsInfoController")
public interface NewsInfoApplication extends BaseNewsInfoApplication {

    // 手工维护方法
    NewsInfo create(NewsCategoryId categoryId, NewsInfoCreator creator);

}
