package gr.hua.DistSysApp.ritoAPI.Utilities;

public class UrlUtils {

    public static String getMatchListURL(String accountId , Integer Head, String APIKey){
        return "https://eun1.api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountId+"?endIndex="+Head+"&api_key="+APIKey;
    }

    public static String getSummonersURL(String summonerName ,  String APIKey){
        return "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+summonerName+"?api_key="+APIKey;
    }
}
