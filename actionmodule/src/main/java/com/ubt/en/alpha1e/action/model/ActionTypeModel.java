package com.ubt.en.alpha1e.action.model;

/**
 * @author：liuhai
 * @date：2018/4/26 20:24
 * @modifier：ubt
 * @modify_date：2018/4/26 20:24
 * [A brief description]
 * version
 */

public class ActionTypeModel {
    private String actionName;
    private int actionType;
    private int drawableId;
    private String actionDescrion;
    private boolean isSelected;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getActionDescrion() {
        return actionDescrion;
    }

    public void setActionDescrion(String actionDescrion) {
        this.actionDescrion = actionDescrion;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
