package org.tensorflow.lite.examples.classification.view.community;

import java.util.HashMap;
import java.util.Map;

public class CommunityDTO {
    public String explain;
    public String imgFileName;
    public String imageUrl;
    public String uid;
    public String userId;
    public String timestamp;
    public int favoriteCount = 0;
    public Map<String, Boolean> favorites = new HashMap<>();

    @Override
    public String toString() {
        return "uid = " + uid + " , userid = " + userId;
    }
}
