package com.example.d.ebookreader;

/**
 * Created by d on 2018/5/15.
 */

public class bookGrid {
    private String name;
    private int imageId;
    private String uri;
    public  bookGrid(String name,int imageId,String uri){
        this.name=name;
        this.imageId=imageId;
        this.uri=uri;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public String getUri(){
        return uri;
    }
}
