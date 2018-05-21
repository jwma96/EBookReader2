package com.example.d.ebookreader;

/**
 * Created by d on 2018/4/16.
 */

public class bookFile {
    private String name;
    private int imageId;
    private String size;//文件大小 必要时应判断<1M 用kb >用M
    private boolean isCho;
    private String uri;
    public bookFile(String name, int imageId, String size, boolean isCho,String uri){
        this.name=name;
        this.imageId=imageId;
        this.size=size;
        this.isCho=isCho;
        this.uri=uri;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public String getSize(){return  size;}
    public boolean getIsCho(){
        return isCho;
    }
    public String getUri(){return uri;}
    public void setIsCho(boolean isCho){
        this.isCho=isCho;
    }
}
