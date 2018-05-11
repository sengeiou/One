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
    public String hts_name = "";
    public String show_name = "";
    public String image_name = "";

    public RemoteItem doCopy() {
        RemoteItem result = new RemoteItem();
        result.hts_name = this.hts_name;
        result.show_name = this.show_name;
        result.image_name = this.image_name;
        return result;
    }

    @Override
    public String toString() {
        return "RemoteItem{" +
                "hts_name='" + hts_name + '\'' +
                ", show_name='" + show_name + '\'' +
                ", image_name='" + image_name + '\'' +
                '}';
    }
}
