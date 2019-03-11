package com.geekhalo.ddd.lite.demo.application.impl;

import com.geekhalo.ddd.lite.demo.application.NewsInfoApplication;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryId;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryRepository;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfo;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoCreator;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoDto;
import com.geekhalo.ddd.lite.demo.domain.news.info.NewsInfoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsInfoApplicationImpl extends BaseNewsInfoApplicationSupport
    implements NewsInfoApplication {
    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Override
    public NewsInfo create(NewsCategoryId categoryId, NewsInfoCreator creator) {
        return creatorFor(getNewsInfoRepository())
                .publishBy(getDomainEventBus())
                .instance(()-> NewsInfo.create(this.newsCategoryRepository.getById(categoryId), creator))
                .call();
    }

    @Override
    protected NewsInfoDto convertNewsInfo(NewsInfo src) {
        return new NewsInfoDto(src);
    }
}
