/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TweetsToHBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.json.*;

/**
 *
 * @author eve
 */
public class JsonParse {
    
    private ArrayList<JsonTweet> tweets;
    
    public JsonParse() {
        this.tweets = new ArrayList<JsonTweet>();
    }
    
    public ArrayList<JsonTweet> getTweets() {
        return this.tweets;
    }
    
    public String getTimestampFromPath(String path) {
        String timestamp = null;
        String[] tmp = path.split("/");
        for (String tm : tmp) {
            if (tm.matches("[0-9]*")) {
                System.out.println(tm);
            }
        }
        return timestamp;
    }
    
    private boolean isFileValid(String filename) {
        String ext = filename.split("\\.")[1];
        if (ext == null) {
            return false;
        } else if ("json".equals(ext)) {
            return true;
        }
        return false;
    }
    
    public void openDir(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                this.openDir(file.listFiles());
            } else {
                if (!this.isFileValid(file.getName())) {
                    continue;
                }
                
                FileInputStream fis = null;
                
                try {
                    fis = new FileInputStream(file);
                    
                    int content;
                    String text = "";
                    while ((content = fis.read()) != -1) {
                        // convert to char and display it
                        String tmp = Character.toString((char) content);
                        text = text + tmp;
                    }
                    this.parseJSON(text);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                
            }
        }
    }
    
    public void openFile(String path) {
        File[] files = new File(path).listFiles();
        this.openDir(files);
    }
    
    public void parseJSON(String singleLine) {
        JSONObject json = new JSONObject(singleLine);
        JSONArray jry = json.getJSONArray("statuses");
        for (int i = 0; i < jry.length(); i++) {
            JSONObject obj, pobj;
            obj = (JSONObject) jry.get(i);
            pobj = (JSONObject) obj.get("user");
            
            JsonTweet jt = new JsonTweet(obj.get("id_str").toString(), pobj.get("id_str").toString());
            jt.setText(obj.get("text").toString());
            jt.setRetweetCount(obj.get("retweet_count").toString());
            jt.setFavoriteCount(obj.get("favorite_count").toString());
            jt.setContributors(obj.get("contributors").toString());
            jt.setLang(obj.get("lang").toString());
            jt.setGeo(obj.get("geo").toString());
            jt.setSource(obj.get("source").toString());
            jt.setCreatedAt(obj.get("created_at").toString());
            jt.setPlace(obj.get("place").toString());
            jt.setRetweeted(obj.get("retweeted").toString());
            jt.setTruncated(obj.get("truncated").toString());
            jt.setFavorited(obj.get("favorited").toString());
            jt.setCoordinates(obj.get("coordinates").toString());
            
            JsonTweetUser jtu = jt.getUser();
            jtu.setLocation(pobj.get("location").toString());
            jtu.setDefaultProfile(pobj.get("default_profile").toString());
            jtu.setBackgroundTile(pobj.get("profile_background_tile").toString());
            jtu.setStatusesCount(pobj.get("statuses_count").toString());
            jtu.setLang(pobj.get("lang").toString());
            jtu.setFollowing(pobj.get("following").toString());
            jtu.setProtected(pobj.get("protected").toString());
            jtu.setFavoriteCount(pobj.get("favourites_count").toString());
            jtu.setDescription(pobj.get("description").toString());
            jtu.setVerified(pobj.get("verified").toString());
            jtu.setContributorsEnabled(pobj.get("contributors_enabled").toString());
            jtu.setname(pobj.get("name").toString());
            jtu.setCreatedAt(pobj.get("created_at").toString());
            jtu.setTranslationEnabled(pobj.get("is_translation_enabled").toString());
            jtu.setDefaultProfileImg(pobj.get("default_profile_image").toString());
            jtu.setFollowerCount(pobj.get("followers_count").toString());
            jtu.setExtendedProfile(pobj.get("has_extended_profile").toString());
            jtu.setProfileImg(pobj.get("profile_image_url_https").toString());
            jtu.setGeoEnabled(pobj.get("geo_enabled").toString());
            jtu.setProfileBackgroundImg(pobj.get("profile_background_image_url_https").toString());
            jtu.setUrl(pobj.get("url").toString());
            jtu.setUtcOffset(pobj.get("utc_offset").toString());
            jtu.setTimeZone(pobj.get("time_zone").toString());
            jtu.setNotifications(pobj.get("notifications").toString());
            jtu.setFiendCount(pobj.get("friends_count").toString());
            jtu.setScreenName(pobj.get("screen_name").toString());
            jtu.setListedCount(pobj.get("listed_count").toString());
            jtu.setIsTranslator(pobj.get("is_translator").toString());
            
            tweets.add(jt);
        }
    }
    
}
