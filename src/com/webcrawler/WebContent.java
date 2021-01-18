package com.webcrawler;

import java.util.Map;

public class WebContent {

    private final Map<String,String> contentMap;

    public WebContent(Map<String, String> contentMap) {
        this.contentMap = contentMap;
    }

    public Map<String, String> getContentMap() {
        return contentMap;
    }

}
