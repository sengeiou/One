package com.ubt.baselib.model1E;

/**
 * @author：liuhai
 * @date：2018/5/22 16:05
 * @modifier：ubt
 * @modify_date：2018/5/22 16:05
 * [A brief description]
 * version
 */

public class LanguageModel {
    private String languageTitle;

    private String languageContent;

    private String languageType;


    public LanguageModel(String languageTitle, String languageContent, String languageType) {
        this.languageTitle = languageTitle;
        this.languageContent = languageContent;
        this.languageType = languageType;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }

    public String getLanguageContent() {
        return languageContent;
    }

    public void setLanguageContent(String languageContent) {
        this.languageContent = languageContent;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    @Override
    public String toString() {
        return "LanguageModel{" +
                "languageTitle='" + languageTitle + '\'' +
                ", languageContent='" + languageContent + '\'' +
                ", languageType='" + languageType + '\'' +
                '}';
    }
}
