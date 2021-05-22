package org.tensorflow.lite.examples.classification.model;

public class Post {
    private String uid;
    private String key;
    private String regDate;
    private String desc;
    private String imgUrl;
    private int like;
    private String reply;

    public Post(String uid, String key, String regDate, String desc, String imgUrl, int like, String reply) {
        this.uid = uid;
        this.key = key;
        this.regDate = regDate;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.like = like;
        this.reply = reply;
    }

}
