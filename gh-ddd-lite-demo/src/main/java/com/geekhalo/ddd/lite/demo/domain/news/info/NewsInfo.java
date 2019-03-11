package com.geekhalo.ddd.lite.demo.domain.news.info;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.application.GenApplicationIgnore;
import com.geekhalo.ddd.lite.codegen.repository.Index;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategory;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryId;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryStatus;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import com.geekhalo.ddd.lite.domain.support.mongo.IdentitiedMongoAggregate;
import com.geekhalo.ddd.lite.domain.support.mongo.MongoAggregate;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@EnableGenForAggregate

@Index("categoryId")
@QueryEntity
@Data
@Document
public class NewsInfo extends IdentitiedMongoAggregate<NewsInfoId> {
    @Column(name = "category_id", updatable = false)
    @Setter(AccessLevel.PRIVATE)
    private NewsCategoryId categoryId;

    @Setter(AccessLevel.PRIVATE)
    @Convert(converter = CodeBasedNewsInfoStatusConverter.class)
    private NewsInfoStatus status;

    private String title;
    private String content;

    private NewsInfo(){

    }

    /**
     * GenApplicationIgnore 创建 BaseNewsInfoApplication 时，忽略该方法，因为 Optional<NewsCategory> category 需要通过 逻辑进行获取
     * @param category
     * @param creator
     * @return
     */
    @GenApplicationIgnore
    public static NewsInfo create(Optional<NewsCategory> category,
                                  NewsInfoCreator creator){
        // 对 NewsCategory 的存在性和状态进行验证
        if (!category.isPresent() || category.get().getStatus() != NewsCategoryStatus.ENABLE){
            throw new IllegalArgumentException();
        }
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setId(NewsInfoId.create());
        newsInfo.setCategoryId(category.get().getId());
        creator.accept(newsInfo);
        newsInfo.init();
        return newsInfo;
    }

    private void init() {
        setStatus(NewsInfoStatus.ENABLE);
    }

    @Description("更新新闻")
    public void update(NewsInfoUpdater updater){
        updater.accept(this);
    }

    @Description("启用新闻")
    public void enable(){
        setStatus(NewsInfoStatus.ENABLE);
    }

    @Description("禁用新闻")
    public void disable(){
        setStatus(NewsInfoStatus.DISABLE);
    }


}
