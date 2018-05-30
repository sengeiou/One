package com.ubt.playaction.model;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class ActionData {

    private String actionName;
    private String actionTime;
    private int actionIcon;
    private boolean select;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public int getActionIcon() {
        return actionIcon;
    }

    public void setActionIcon(int actionIcon) {
        this.actionIcon = actionIcon;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "ActionData{" +
                "actionName='" + actionName + '\'' +
                ", actionTime='" + actionTime + '\'' +
                ", actionIcon=" + actionIcon +
                ", select=" + select +
                '}';
    }
}
