package com.example.insys.oneibory.model;

/**
 * Created by Aazam on 12/3/2015.
 */
public class Category {

    private String mainCategoryUrl, subCategoryUrl, mainCatgryName, subCategryName;


    public Category(String mainCategoryUrl, String subCategoryUrl, String mainCatgryName, String subCategryName) {
        this.mainCategoryUrl = mainCategoryUrl;
        this.subCategoryUrl = subCategoryUrl;
        this.mainCatgryName = mainCatgryName;
        this.subCategryName = subCategryName;
    }

    public String getMainCategoryUrl() {
        return mainCategoryUrl;
    }

    public void setMainCategoryUrl(String mainCategoryUrl) {
        this.mainCategoryUrl = mainCategoryUrl;
    }

    public String getSubCategoryUrl() {
        return subCategoryUrl;
    }

    public void setSubCategoryUrl(String subCategoryUrl) {
        this.subCategoryUrl = subCategoryUrl;
    }

    public String getMainCatgryName() {
        return mainCatgryName;
    }

    public void setMainCatgryName(String mainCatgryName) {
        this.mainCatgryName = mainCatgryName;
    }

    public String getSubCategryName() {
        return subCategryName;
    }

    public void setSubCategryName(String subCategryName) {
        this.subCategryName = subCategryName;
    }
}
