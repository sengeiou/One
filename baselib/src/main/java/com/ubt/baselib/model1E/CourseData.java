package com.ubt.baselib.model1E;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class CourseData extends DataSupport implements Serializable{


    private String token;
    private String userId;
    private int cid;
    private int currGraphProgramId;
    private int sequence;
    private String name;
    private String videoUrl;
    private String thumbnailUrl;
    private String status;
    private String localVideoPath;
    private String subTitle;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCurrGraphProgramId() {
        return currGraphProgramId;
    }

    public void setCurrGraphProgramId(int currGraphProgramId) {
        this.currGraphProgramId = currGraphProgramId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocalVideoPath() {
        return localVideoPath;
    }

    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return "CourseData{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", cid=" + cid +
                ", currGraphProgramId=" + currGraphProgramId +
                ", sequence=" + sequence +
                ", name='" + name + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", status='" + status + '\'' +
                ", localVideoPath='" + localVideoPath + '\'' +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }
}
