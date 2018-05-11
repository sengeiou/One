package com.ubt.en.alpha1e.action.model;

import android.graphics.Bitmap;

import java.util.Arrays;

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
    private Bitmap mBitmap;

    /**
     * 左侧展示区显示的图片
     */
    private int leftSelectedImage;
    private int[] imageTypeArray;

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

    public int[] getImageTypeArray() {
        return imageTypeArray;
    }

    public void setImageTypeArray(int[] imageTypeArray) {
        this.imageTypeArray = imageTypeArray;
    }


    public int getLeftSelectedImage() {
        return leftSelectedImage;
    }

    public void setLeftSelectedImage(int leftSelectedImage) {
        this.leftSelectedImage = leftSelectedImage;
    }


    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public String toString() {
        return "ActionTypeModel{" +
                "actionName='" + actionName + '\'' +
                ", actionType=" + actionType +
                ", drawableId=" + drawableId +
                ", actionDescrion='" + actionDescrion + '\'' +
                ", isSelected=" + isSelected +
                ", leftSelectedImage=" + leftSelectedImage +
                ", imageTypeArray=" + Arrays.toString(imageTypeArray) +
                '}';
    }
}
