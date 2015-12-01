package com.trekcoders.safar.model;

public class NavDrawerItem {
    private boolean showNotify;
    private String title, title_image;

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getImage(){
        return title_image;
    }

    public void setImage(String title_image){
        this.title_image = title_image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
