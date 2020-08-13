package com.ciel.scaapi.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * page对象
 */
@Getter
@Setter
public class Pager  implements Serializable {
    public static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private long current;
    /**
     * 每页数量
     */
    private long size;
    /**
     * 总数量
     */
    private long total;
    /**
     * 页数
     */
    private long pages;

    public Pager(IPage<?> page){
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
    }

    public Pager(){
    }

}
