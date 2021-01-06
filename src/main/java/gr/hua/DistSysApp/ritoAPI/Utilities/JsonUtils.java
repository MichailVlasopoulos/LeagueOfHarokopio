package gr.hua.DistSysApp.ritoAPI.Utilities;

import com.fasterxml.jackson.core.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {

    public static JSONObject stringToJsonObject(String type , String body) throws JSONException {
        JSONObject obj = new JSONObject("{\""+type+"\": \""+body+"\"}");
        return obj;
    }

    public static String recurseKeys(JSONObject jObj, String findKey) throws JSONException {
        String finalValue = "";
        if (jObj == null) {
            return "";
        }

        Iterator<String> keyItr = jObj.keys();
        Map<String, String> map = new HashMap<>();

        while(keyItr.hasNext()) {
            String key = keyItr.next();
            map.put(key, jObj.getString(key));
        }

        for (Map.Entry<String, String> e : (map).entrySet()) {
            String key = e.getKey();
            if (key.equalsIgnoreCase(findKey)) {
                return jObj.getString(key);
            }

            // read value
            Object value = jObj.get(key);

            if (value instanceof JSONObject) {
                finalValue = recurseKeys((JSONObject)value, findKey);
            }
        }

        // key is not found
        return finalValue;
    }

    public static String getSummonerId (JSONObject jsonObject) throws JSONException{
        String summonerId = null;
        try {
            summonerId=recurseKeys(jsonObject, "id");
        } catch (JSONException e) {
            summonerId=null;
            System.err.println("Error parsing Json");
            e.printStackTrace();
        }
        return summonerId;
    }

    public static String getAccountId (JSONObject jObj ) throws JSONException {
        String accountId = null;
        try {
            accountId=recurseKeys(jObj, "accountId");
        } catch (JSONException e) {
            accountId=null;
            e.printStackTrace();
        }
        return accountId;
    }

    public static String getPuuid (JSONObject jObj ) throws JSONException {
        String puuid = null;
        try {
            puuid=recurseKeys(jObj, "puuid");
        } catch (JSONException e) {
            puuid=null;
            e.printStackTrace();
        }
        return puuid;
    }

    public static String getName (JSONObject jObj ) throws JSONException {
        String name = null;
        try {
            name=recurseKeys(jObj, "name");
        } catch (JSONException e) {
            name=null;
            e.printStackTrace();
        }
        return name;
    }

    public static int getProfileIconId (JSONObject jObj ) throws JSONException {
        int profileIconId = -1;
        try {
            profileIconId=Integer.parseInt(recurseKeys(jObj, "profileIconId"));
        } catch (JSONException e) {
            profileIconId=-1;
            e.printStackTrace();
        }
        return profileIconId;
    }

    public static int getSummonerLevel (JSONObject jObj ) throws JSONException {
        int summonerLevel = -1;
        try {
            summonerLevel=Integer.parseInt(recurseKeys(jObj, "summonerLevel"));
        } catch (JSONException e) {
            summonerLevel=-1;
            e.printStackTrace();
        }
        return summonerLevel;
    }

}
