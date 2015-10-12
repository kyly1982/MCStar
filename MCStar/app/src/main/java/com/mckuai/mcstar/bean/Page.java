package com.mckuai.mcstar.bean;

import java.io.Serializable;

/**
 * Created by kyly on 2015/10/12.
 */
public class Page implements Serializable{
    int page = 0;
    int count = 0;
    int size =20;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean EOF(){
        if (0 == count){
            return true;
        }

        int pagecount = (0 == (count % size) ? (count / size):((count / size) + 1));
        return page == pagecount;
    }

    public int getNextPage(){
        int pagecount = (0 == (count % size) ? (count / size):((count / size) + 1));
        if (page < pagecount){
            return page+1;
        }
        else return page;
    }
}
