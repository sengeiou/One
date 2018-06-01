package com.ubt.en.alpha1e.ble.model;

/**
 * @author：liuhai
 * @date：2018/4/20 17:28
 * @modifier：ubt
 * @modify_date：2018/4/20 17:28
 * [A brief description]
 * version
 */

public class RobotLanguage {

    private String languageId;
    private String languageName;
    private String languageSingleName;
    private boolean isSelect;

    private int result = -1;
    private int progess = 0;

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageSingleName() {
        return languageSingleName;
    }

    public void setLanguageSingleName(String languageSingleName) {
        this.languageSingleName = languageSingleName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getProgess() {
        return progess;
    }

    public void setProgess(int progess) {
        this.progess = progess;
    }
}
