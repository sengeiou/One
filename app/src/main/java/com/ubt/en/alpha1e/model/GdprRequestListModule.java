package com.ubt.en.alpha1e.model;

import java.util.List;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class GdprRequestListModule {

    private List<GdprRequestModule> gdprUserPactInfoModels;

    public List<GdprRequestModule> getGdprRequestModuleList() {
        return gdprUserPactInfoModels;
    }

    public void setGdprRequestModuleList(List<GdprRequestModule> gdprRequestModuleList) {
        this.gdprUserPactInfoModels = gdprRequestModuleList;
    }

    @Override
    public String toString() {
        return "GdprRequestListModule{" +
                "gdprRequestModuleList=" + gdprUserPactInfoModels +
                '}';
    }
}
