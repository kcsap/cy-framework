package com.cy.framework.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "分页属性", description = "分页属性说明")
public class PageInfo {
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    /**
     * 显示多少条
     */
    @ApiModelProperty(value = "显示的条数,默认10", example = "10")
    private transient int pageSize;
    /**
     * 页数大小 默认第一页
     */
    @ApiModelProperty(value = "页数，默认1", example = "1")
    private transient int currentPage;
    @ApiModelProperty(value = "内部参数", example = "1", hidden = true)
    private transient int totalItem;
    @ApiModelProperty(value = "内部参数", example = "1", hidden = true)
    private transient int startRow;
    /**
     * 排序名称
     */
    @ApiModelProperty(value = "排序的字段名称", example = "create_time")
    private transient String sortName;
    /**
     * 排序的类型 ASC/DESC
     */
    @ApiModelProperty(value = "排序，取值ASC/DESC", example = "ASC")
    private transient String orderBy;
    /**
     * 要显示的字段
     */
    @ApiModelProperty(value = "要显示的字段", example = "id,name,age")
    private transient String fileds;
    @ApiModelProperty(value = "总页数", example = "1", hidden = true)
    private transient Long totalPage;

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public String getFileds() {
        return fileds;
    }

    public void setFileds(String fileds) {
        this.fileds = fileds;
    }

    public int getPageSize() {
        if (pageSize <= 0) {
            // 默认10
            return DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        setStartEndRow();
    }

    public long getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    private void setStartEndRow() {
        this.startRow = this.getPageSize() * (this.getCurrentPage() - 1);
        if (this.startRow < 0) {
            this.startRow = 0;
        }
    }

    public int getStartRow() {
        if (this.startRow < 0) {
            return 0;
        }

        return startRow;
    }

    public void setStartRow(int startRow) {
        if (this.startRow < 0) {
            startRow = 0;
        }

        this.startRow = startRow;
    }

    public long getTotalPage() {
        long pgSize = pageSize;
        long total = totalItem;
        if (pgSize == 0) {
            return 0;
        }
        long result = total / pgSize;
        if ((total == 0) || ((total % pgSize) != 0)) {
            result++;
        }
        return result;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
