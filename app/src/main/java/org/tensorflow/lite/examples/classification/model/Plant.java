package org.tensorflow.lite.examples.classification.model;

public class Plant {
    private String uid;
    private String key;

    public Plant(){}

    public Plant(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    private String name;
    private String regDate;
    private String desc;
    private String imgUrl;
    private String waterTime;

    public Plant(String uid, String key, String name, String regDate, String desc, String imgUrl, String waterTime) {
        this.uid = uid;
        this.key = key;
        this.name = name;
        this.regDate = regDate;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.waterTime = waterTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
