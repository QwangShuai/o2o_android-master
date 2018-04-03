package com.gjzg.bean;


import java.io.Serializable;
import java.util.List;

public class IdCacheBean implements Serializable{

    private List<String> cacheIdList;

    public List<String> getCacheIdList() {
        return cacheIdList;
    }

    public void setCacheIdList(List<String> cacheIdList) {
        this.cacheIdList = cacheIdList;
    }
}
