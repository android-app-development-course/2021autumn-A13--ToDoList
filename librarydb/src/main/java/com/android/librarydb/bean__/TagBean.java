package com.android.librarydb.bean__;

import com.android.librarybase.basemethod.Color;

public class TagBean {
    private String tag_name;//tag_name



    private int tag_color;//tag_color
    private int tag_light_color;
    private int tag_drawable_normal;
    private int tag_drawable_selected;

    public String getSql_num() {
        return sql_num;
    }

    public void setSql_num(String sql_num) {
        this.sql_num = sql_num;
    }

    private String sql_num;//sql_num

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public void setTag_color(int tag_color) {
        this.tag_color = tag_color;
    }
    public TagBean() {
    }

    public TagBean(String tag_name, int tag_color) {
        this.tag_name = tag_name;
        this.tag_color = tag_color;
        tag_light_color = Color.getTagLightColor(tag_color);
        tag_drawable_normal = Color.getTagDrawableNormalColor(tag_color);
        tag_drawable_selected = Color.getTagDrawableSelectedColor(tag_color);
        sql_num="";
    }

    public String getTag_name() {
        return tag_name;
    }

    public int getTag_color() {
        return tag_color;
    }

    public int getTag_light_color() {
        return tag_light_color;
    }

    public int getTag_drawable_normal() {
        return tag_drawable_normal;
    }

    public int getTag_drawable_selected() {
        return tag_drawable_selected;
    }

    @Override
    public String toString() {
        return "TagBean{" +
                "tag_name='" + tag_name + '\'' +
                ", tag_color=" + tag_color +
                ", tag_light_color=" + tag_light_color +
                ", tag_drawable_normal=" + tag_drawable_normal +
                ", tag_drawable_selected=" + tag_drawable_selected +
                '}';
    }
}
