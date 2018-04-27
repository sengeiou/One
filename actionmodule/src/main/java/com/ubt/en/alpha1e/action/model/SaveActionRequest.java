package com.ubt.en.alpha1e.action.model;

/**
 * @author：liuhai
 * @date：2018/4/27 17:39
 * @modifier：ubt
 * @modify_date：2018/4/27 17:39
 * [A brief description]
 * version
 */

public class SaveActionRequest {
    private String actionoriginalid;
    private String actiondesciber;
    private String apptype;
    private String requestkey;
    private String actionuserid;
    private String serviceversion;
    private String actionname;
    private String actiontype;
    private String systemlanguage;
    private String requesttime;
    private String actiontime;

    public String getActionoriginalid() {
        return actionoriginalid;
    }

    public void setActionoriginalid(String actionoriginalid) {
        this.actionoriginalid = actionoriginalid;
    }

    public String getActiondesciber() {
        return actiondesciber;
    }

    public void setActiondesciber(String actiondesciber) {
        this.actiondesciber = actiondesciber;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getRequestkey() {
        return requestkey;
    }

    public void setRequestkey(String requestkey) {
        this.requestkey = requestkey;
    }

    public String getActionuserid() {
        return actionuserid;
    }

    public void setActionuserid(String actionuserid) {
        this.actionuserid = actionuserid;
    }

    public String getServiceversion() {
        return serviceversion;
    }

    public void setServiceversion(String serviceversion) {
        this.serviceversion = serviceversion;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getSystemlanguage() {
        return systemlanguage;
    }

    public void setSystemlanguage(String systemlanguage) {
        this.systemlanguage = systemlanguage;
    }

    public String getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime;
    }

    public String getActiontime() {
        return actiontime;
    }

    public void setActiontime(String actiontime) {
        this.actiontime = actiontime;
    }

    @Override
    public String toString() {
        return "SaveActionRequest{" +
                "actionoriginalid='" + actionoriginalid + '\'' +
                ", actiondesciber='" + actiondesciber + '\'' +
                ", apptype='" + apptype + '\'' +
                ", requestkey='" + requestkey + '\'' +
                ", actionuserid='" + actionuserid + '\'' +
                ", serviceversion='" + serviceversion + '\'' +
                ", actionname='" + actionname + '\'' +
                ", actiontype='" + actiontype + '\'' +
                ", systemlanguage='" + systemlanguage + '\'' +
                ", requesttime='" + requesttime + '\'' +
                ", actiontime='" + actiontime + '\'' +
                '}';
    }
}
