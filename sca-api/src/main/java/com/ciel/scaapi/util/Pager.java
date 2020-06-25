package com.ciel.scaapi.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * page对象
 */
public class Pager<T> extends Page<T> {
    /**
     * 当前页
     */
    @NotNull(message = "当前页不能为空")
    private long current;
    /**
     * 每页数量
     */
    @NotNull(message = "每页数量不能为空")
    private long size;
    /**
     * 总数量
     */
    private long total;
    /**
     * 页数
     */
    private long pages;

    public Pager(IPage<Object> page){
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
    }

    public Pager(){
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Page<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getPages() {
        return pages;
    }

    @Override
    public IPage<T> setPages(long pages) {
        this.pages = pages;
        return this;
    }
}
