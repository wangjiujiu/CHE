package com.qc.language.framework.sound;

/**
 * 语音Item
 */
public class VoiceItem {

    private static final String TAG = VoiceItem.class.getSimpleName();

    private String url;

    private String time;

    private String path;

    private boolean isUpdate;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
