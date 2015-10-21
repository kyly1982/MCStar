package com.mckuai.mcstar.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by kyly on 2015/10/12.
 */
public class Page implements Serializable {
    int page = 0;
    int allCount = 0;
    int pageSize = 20;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int count) {
        this.allCount = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean EOF() {
        if (0 == allCount) {
            return true;
        }

        int pagecount = (0 == (allCount % pageSize) ? (allCount / pageSize) : ((allCount / pageSize) + 1));
        return page == pagecount;
    }

    public int getNextPage() {
        if (0 == page ||0 == allCount || allCount < pageSize) {
            return 1;
        }
        int pagecount = (0 == (allCount % pageSize) ? (allCount / pageSize) : ((allCount / pageSize) + 1));
        if (page < pagecount) {
            return page + 1;
        } else return page;
    }

    public Page clone(@NonNull Page page) {
        this.page = page.getPage();
        this.pageSize = page.getPageSize();
        this.allCount = page.getAllCount();
        return this;
    }
}
