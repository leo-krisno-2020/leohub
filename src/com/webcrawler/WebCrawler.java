package com.webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebCrawler {

    private WebContent webContent;

    public WebContent getWebContent() {
        return webContent;
    }

    public void setWebContent(WebContent webContent) {
        this.webContent = webContent;
    }

    public void readContentFromURLList(List<URL> urlList) {
        final Map<String, String> contentMap = new ConcurrentHashMap<>();
        urlList.forEach(url -> {
            BufferedReader br = null;
            try {
                final URLConnection connection = url.openConnection();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder sbData = new StringBuilder();
                String data;
                while ((data = br.readLine()) != null) {
                    sbData.append(data);
                }
                contentMap.put(url.toString(), sbData.toString().toUpperCase());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.webContent = new WebContent(contentMap);
    }

    public Map<String, Integer> searchContentByText(String searchText) {
        if (webContent == null) {
            return null;
        }

        final Map<String, Integer> resultMap = new HashMap<String, Integer>();
        webContent.getContentMap().forEach((url, content) -> {
            int count = 0;
            int lastIndex = 0;
            while (true) {
                lastIndex = content.indexOf(searchText.toUpperCase(), lastIndex);
                if (lastIndex != -1) {
                    count ++;
                    lastIndex += searchText.length();
                } else {
                    break;
                }
            }
            if (count > 0) {
                resultMap.put(url, count);
            }
        });
        return resultMap;
    }

    public static void main(String[] args) {
        final List<URL> urlList = new ArrayList<URL>();
        try {
            urlList.add(new URL("https://www.dbs.com.sg"));
            urlList.add(new URL("https://www.uobgroup.com"));
            urlList.add(new URL("https://www.ocbc.com"));
            urlList.add(new URL("https://www.sc.com"));
            urlList.add(new URL("https://www.citibank.com.sg"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final String searchText = "bank";

        WebCrawler webCrawler = new WebCrawler();
        webCrawler.readContentFromURLList(urlList);
        final Map<String, Integer> resultMap = webCrawler.searchContentByText(searchText);
        resultMap.forEach((url, content) -> {
            System.out.println("URL = " + url + ", Number of Occurrences: " + content);
        });
    }

}
