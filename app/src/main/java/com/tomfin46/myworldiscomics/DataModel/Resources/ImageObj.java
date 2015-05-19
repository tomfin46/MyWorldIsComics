package com.tomfin46.myworldiscomics.DataModel.Resources;

import java.io.Serializable;

public class ImageObj implements Serializable {
    public String screen_url;
    public String super_url;

    public String getScreen_url() {
        return screen_url;
    }

    public void setScreen_url(String screen_url) {
        this.screen_url = screen_url;
    }

    public String getSuper_url() {
        return super_url;
    }

    public void setSuper_url(String super_url) {
        this.super_url = super_url;
    }
}
