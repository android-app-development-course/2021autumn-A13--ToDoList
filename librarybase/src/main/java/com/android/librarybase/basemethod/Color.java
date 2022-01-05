package com.android.librarybase.basemethod;

import com.android.librarybase.R;

public class Color {
    public static int getTagColor(int mTagColor){
        if(mTagColor== R.color.main_blue)return R.color.main_blue;
        if(mTagColor== R.color.main_pink)return R.color.main_pink;
        if(mTagColor== R.color.main_yellow)return R.color.main_yellow;
        if(mTagColor== R.color.main_green)return R.color.main_green;
        if(mTagColor== R.color.main_purple)return R.color.main_purple;
        return -1;
    }
    public static int getTagLightColor(int mTagColor){
        if(mTagColor== R.color.main_blue)return R.color.main_light_blue;
        if(mTagColor== R.color.main_pink)return R.color.main_light_pink;
        if(mTagColor== R.color.main_yellow)return R.color.main_light_yellow;
        if(mTagColor== R.color.main_green)return R.color.main_light_green;
        if(mTagColor== R.color.main_purple)return R.color.main_light_purple;
        return -1;
    }
    public static int getTagDrawableNormalColor(int mTagColor){
        if(mTagColor== R.color.main_blue)return R.drawable.tag_blue;
        if(mTagColor== R.color.main_pink)return R.drawable.tag_pink;
        if(mTagColor== R.color.main_yellow)return R.drawable.tag_yellow;
        if(mTagColor== R.color.main_green)return R.drawable.tag_green;
        if(mTagColor== R.color.main_purple)return R.drawable.tag_purple;
        return -1;
    }
    public static int getTagDrawableSelectedColor(int mTagColor){
        if(mTagColor== R.color.main_blue)return R.drawable.tag_blue_text;
        if(mTagColor== R.color.main_pink)return R.drawable.tag_pink_text;
        if(mTagColor== R.color.main_yellow)return R.drawable.tag_yellow_text;
        if(mTagColor== R.color.main_green)return R.drawable.tag_green_text;
        if(mTagColor== R.color.main_purple)return R.drawable.tag_purple_text;
        return -1;
    }
}
