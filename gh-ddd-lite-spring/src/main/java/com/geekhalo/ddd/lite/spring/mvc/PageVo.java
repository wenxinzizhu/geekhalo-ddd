package com.geekhalo.ddd.lite.spring.mvc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api
@Data
public class PageVo<T> {
    @ApiModelProperty("页码")
    private int pageNo;

    @ApiModelProperty("每页大小")
    private int pageSize;

    @ApiModelProperty("总页数")
    private int totalPages;

    @ApiModelProperty("总记录数")
    private long totalElements;

    @ApiModelProperty("是否有数据")
    private boolean hasContent;

    @ApiModelProperty("是否第一页")
    private boolean first;

    @ApiModelProperty("是否最后一页")
    private boolean last;

    @ApiModelProperty("是否存在下一页")
    private boolean next;

    @ApiModelProperty("是否存在上一页")
    private boolean hasPrevious;

    @ApiModelProperty("数据")
    private List<T> content;

    public static <T, V> PageVo<V> fromPage(Page<T> page, Function<T, V> converter){
        PageVo<V> pageVo = new PageVo<>();
        pageVo.setPageNo(page.getPageable().getPageNumber());
        pageVo.setPageSize(page.getPageable().getPageSize());
        pageVo.setTotalPages(page.getTotalPages());
        pageVo.setTotalElements(page.getTotalElements());
        pageVo.setHasContent(page.hasContent());
        pageVo.setFirst(page.isFirst());
        pageVo.setLast(page.isLast());
        pageVo.setNext(page.hasNext());
        pageVo.setHasPrevious(page.hasPrevious());
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            pageVo.setContent(page.getContent().stream().map(converter).collect(Collectors.toList()));
        }
        return pageVo;
    }

    public static <T> PageVo<T> fromPage(Page<T> page){
        return fromPage(page, t->t);
    }

    public static <T> PageVo<T> apply(Page<T> page){
        return fromPage(page);
    }

}
