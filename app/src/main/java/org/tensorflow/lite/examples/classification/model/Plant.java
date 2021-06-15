package org.tensorflow.lite.examples.classification.model;

public class Plant {
    private String key;
    private String name;
    private String desc;
    private String imgUrl;
    private String regDate;
    private int waterPeriod;
    private int waterTime;

    public Plant(){}
    public Plant(String name, String desc, String imgUrl, String regDate) {
        this.name = name;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.regDate = regDate;
    }
    public Plant(String key, String name, String desc, String imgUrl, String regDate, int waterPeriod, int waterTime) {
        this.key = key;
        this.name = name;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.regDate = regDate;
        this.waterPeriod = waterPeriod;
        this.waterTime = waterTime;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public int getWaterPeriod() { return waterPeriod; }

    public void setWaterPeriod(int waterPeriod) { this.waterPeriod = waterPeriod; }

    public int getWaterTime() { return waterTime; }
    public void setWaterTime(int waterTime) { this.waterTime = waterTime; }
}
