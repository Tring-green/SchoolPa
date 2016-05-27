package com.example.schoolpa.Bean;

/**
 * Created by admin on 2016/2/20.
 */
public class WebBean {
    String id;
    String title;
    String unit;
    String content;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUnit() {
        return unit;
    }

    public String getContent() {
        return content;
    }

    public WebBean(String id, String title, String unit, String content) {
        this.id = id;
        this.title = title;
        this.unit = unit;
        this.content = content;
    }

    @Override
    public String toString() {
        return "WebBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", unit='" + unit + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
