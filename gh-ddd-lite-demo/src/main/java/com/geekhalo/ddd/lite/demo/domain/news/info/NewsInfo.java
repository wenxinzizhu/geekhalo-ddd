package com.geekhalo.ddd.lite.demo.domain.news.info;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.application.GenApplicationIgnore;
import com.geekhalo.ddd.lite.codegen.repository.Index;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategory;
import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryStatus;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@EnableGenForAggregate

@Index("categoryId")

@Data
@Entity
@Table(name = "tb_news_info")
public class NewsInfo extends JpaAggregate {
    @Column(name = "category_id", updatable = false)
    private Long categoryId;

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
    public static NewsInfo create(Optional<NewsCategory> category, NewsInfoCreator creator){
        // 对 NewsCategory 的存在性和状态进行验证
        if (!category.isPresent() || category.get().getStatus() != NewsCategoryStatus.ENABLE){
            throw new IllegalArgumentException();
        }
        NewsInfo newsInfo = new NewsInfo();
        creator.accept(newsInfo);
        newsInfo.init();
        return newsInfo;
    }

    public void update(NewsInfoUpdater updater){
        updater.accept(this);
    }

    public void enable(){
        setStatus(NewsInfoStatus.ENABLE);
    }

    public void disable(){
        setStatus(NewsInfoStatus.DISABLE);
    }

    private void init() {
        setStatus(NewsInfoStatus.ENABLE);
    }
}
