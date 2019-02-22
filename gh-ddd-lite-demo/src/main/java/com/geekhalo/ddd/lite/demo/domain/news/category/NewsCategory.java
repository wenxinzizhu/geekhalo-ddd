package com.geekhalo.ddd.lite.demo.domain.news.category;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * EnableGenForAggregate 自动创建聚合相关的 Base 类
 */
@EnableGenForAggregate

@Data
@Entity
@Table(name = "tb_news_category")
public class NewsCategory extends JpaAggregate {

    private String name;

    @Setter(AccessLevel.PRIVATE)
    @Convert(converter = CodeBasedNewsCategoryStatusConverter.class)
    private NewsCategoryStatus status;

    private NewsCategory(){

    }

    /**
     * 静态工程，完成 NewsCategory 的创建
     * @param creator
     * @return
     */
    public static NewsCategory create(NewsCategoryCreator creator){
        NewsCategory category = new NewsCategory();
        creator.accept(category);
        category.init();
        return category;
    }

    /**
     * 更新
     * @param updater
     */
    public void update(NewsCategoryUpdater updater){
        updater.accept(this);
    }

    /**
     * 启用
     */
    public void enable(){
        setStatus(NewsCategoryStatus.ENABLE);
    }

    /**
     * 禁用
     */
    public void disable(){
        setStatus(NewsCategoryStatus.DISABLE);
    }

    /**
     * 初始化，默认状态位 ENABLE
     */
    private void init() {
        setStatus(NewsCategoryStatus.ENABLE);
    }
}
