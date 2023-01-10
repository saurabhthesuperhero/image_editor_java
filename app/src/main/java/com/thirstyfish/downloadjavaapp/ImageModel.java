package com.thirstyfish.downloadjavaapp;


import java.io.Serializable;


public class ImageModel implements Serializable {

    public String imgUrl;


    public ImageModel() {

    }



    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
