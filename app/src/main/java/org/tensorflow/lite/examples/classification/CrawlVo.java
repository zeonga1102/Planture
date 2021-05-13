package org.tensorflow.lite.examples.classification;


import java.util.List;

public class CrawlVo {
    private String contents;
    private List<String> imgSrcList;



    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<String> getImgSrcList() {
        return imgSrcList;
    }

    public void setImgSrcList(List<String> imgSrcList) {
        this.imgSrcList = imgSrcList;
    }
}
