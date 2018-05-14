package com.ubt.en.alpha1e.remote.model;

/**
 * @author：liuhai
 * @date：2018/5/4 16:32
 * @modifier：ubt
 * @modify_date：2018/5/4 16:32
 * [A brief description]
 * version
 */

public class RemoteItem {
    public String hts_name;
    public String show_name;
    public String image_name;

    private int drawableId;

    public String getHts_name() {
        return hts_name;
    }

    public void setHts_name(String hts_name) {
        this.hts_name = hts_name;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    //    public RemoteItem doCopy() {
//        RemoteItem result = new RemoteItem();
//        result.hts_name = this.hts_name;
//        result.show_name = this.show_name;
//        result.image_name = this.image_name;
//        return result;
//    }


    @Override
    public String toString() {
        return "RemoteItem{" +
                "hts_name='" + hts_name + '\'' +
                ", show_name='" + show_name + '\'' +
                ", image_name='" + image_name + '\'' +
                ", drawableId=" + drawableId +
                '}';
    }
}
