package com.example.d.ebookreader;

/**
 * Created by d on 2018/5/15.
 */

public class bookGrid {
    private String name;
    private int imageId;
    private String uri;
    private int currentPage;
   // private boolean isShowDelete;
    public  bookGrid(String name,int imageId,String uri,int currentPage){
        this.name=name;
        this.imageId=imageId;
        this.uri=uri;
        this.currentPage=currentPage;
      //  this.isShowDelete=isShowDelete;
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
    public int getCurrentPage(){return currentPage;}

}
