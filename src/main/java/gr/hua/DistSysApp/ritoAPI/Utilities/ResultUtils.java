package gr.hua.DistSysApp.ritoAPI.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultUtils {
    public static JSONObject getActiveGameResults(String summonerName, String APIKey) throws JSONException {

        String url = UrlUtils.getSummonersURL(summonerName,APIKey);

        String response;
        try {
            response = Requests.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error Accessing Url");
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }
        //TODO handle null exception
        if(response==null){
            System.err.println("Expired API KEY or Wrong Summoner Name");
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }

        // JSONParser parser = new JSONParser(response);
        JSONObject jsonObj = new JSONObject(response);
        String summonerId=null;

        try{
            summonerId = JsonUtils.getSummonerId(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Error JSON Parser");
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }

        if (summonerId==null) return JsonUtils.stringToJsonObject("Status", "Failed");

        String url2 = UrlUtils.getActiveGameURL(summonerId,APIKey);
        String activeGameResults = Requests.get(url2);

        if(activeGameResults != null) {
            JSONObject jsonResults = new JSONObject(activeGameResults);
            return jsonResults;
        }else {
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }
    }

    public static JSONObject getSummonerUrlResponse(String url) throws JSONException {

        String response;
        try {
            response = Requests.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error Accessing Url");
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }
        if(response==null){
            System.err.println("Expired API KEY or wrong summoner name");
            return null;
        }

        return new JSONObject(response);
    }

    public static String getSummonersLeagueID(String url) throws JSONException {

        String response;
        try {
            response = Requests.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error Accessing Url");
            return "Failed";
        }
        if(response==null){
            System.err.println("Expired API KEY or Wrong Summoner Name");
            return "Failed";
        }

        //JSONObject jsonObj = new JSONObject(response);

        //TODO CHECK FOR SOLOQ OR FLEX WITH ITERATOR

        if (response.equals("[]"))
            response = "UNRANKED";


        JSONArray jsonArray = new JSONArray(response);
        String temp = JsonUtils.toList(jsonArray).get(0).toString();
        temp.replace("{","");
        temp.replace("}","");
        String[] attr =  temp.split(",");
        String leagueIDString = attr[8];
        String[] leagueIDArray = leagueIDString.split("=");
        String leagueID = leagueIDArray[1];
        //return jsonObj.getString("leagueId");
        return leagueID;
    }
}
