package gr.hua.distsys.DistSysSec;

import gr.hua.distsys.DistSysSec.Utilities.Requests;

public class RequestsTest {
    public static void main(String[] args){
        String API_KEY = "YOUR_RIOT_API_KEY_HERE";
        String response = Requests.get("https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Ogami+Yasu?api_key=" + API_KEY);
        System.out.println("Account Details: " + response);

        String ACCOUNT_ID = "q0C8Uq_E9ahLuoaZeGp8jQ3i74iOyyq0iY2DlJLx9B9wt6rAIN2xWqqA"; // Retrieved from above call.
        response = Requests.get("https://eun1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + ACCOUNT_ID + "?api_key=" + API_KEY);
        System.out.println("Raw Matches: " + response);
    }
}
