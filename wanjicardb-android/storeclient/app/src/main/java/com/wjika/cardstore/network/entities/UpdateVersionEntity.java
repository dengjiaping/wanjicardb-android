package com.wjika.cardstore.network.entities;

/**
 * Created by jacktian on 15/9/24.
 * 版本更新
 */
public class UpdateVersionEntity extends Entity{

    private String version;
    private int updateType; //0 无更新  1 提醒更新  2 强制更新
    private String describe;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
