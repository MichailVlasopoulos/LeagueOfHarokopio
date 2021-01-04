package gr.hua.DistSysApp.ritoAPI.Utilities;

public class UrlUtils {

    public static String getMatchListURL(String accountId , Integer Head, String APIKey){
        return "https://eun1.api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountId+"?endIndex="+Head+"&api_key="+APIKey;
    }

    public static String getSummonersURL(String summonerName ,  String APIKey){
        return "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+summonerName+"?api_key="+APIKey;
    }

    public static String getActiveGameURL(String summonerID, String APIKey){
        return "https://eun1.api.riotgames.com/lol/spectator/v4/active-games/by-summoner/"+summonerID+"?api_key="+APIKey;
    }

    /**
     * Get a player's total champion mastery score, which is the sum of individual champion mastery levels.
     * @param summonerID
     * @param APIKey
     * @return total champion mastery score for given summoner ID
     */
    public static String getTotalMasteryPointsURL (String summonerID, String APIKey){
        return "https://eun1.api.riotgames.com/lol/champion-mastery/v4/scores/by-summoner/"+summonerID+"?api_key="+APIKey;
    }

    /**
     * Get all champion mastery entries sorted by number of champion points descending
     * @param summonerID
     * @param APIKey
     * @return champions mastery points for all champions , sorted DESC , for given summoner id
     */
    public static String getAllChampionMasteryEntries (String summonerID, String APIKey){
        return "https://eun1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/"+summonerID+"?api_key="+APIKey;
    }

    /**
     * Returns champion rotations, including free-to-play and low-level free-to-play rotations (REST)
     * @param APIKey
     * @return all free to play champions for this rotation
     */
    public static String getFreeChampionRotationURL (String APIKey){
        return "https://eun1.api.riotgames.com/lol/platform/v3/champion-rotations?api_key="+APIKey;
    }

    /**
     * Returns all EUNE region Challenger Players
     * @param APIKey
     * @return all challenger players and their soloq stats. Only EUNE region
     */
    public static String getAllChallengerPlayersURL (String APIKey){
        return "https://eun1.api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/CHALLENGER/I?page=1&api_key="+APIKey;
    }

    /**
     * Get match by id
     * @param matchId
     * @param APIKey
     * @return match Stats for a given match id
     */
    public static String getMatchByIdURL(String matchId, String APIKey){
        return "https://americas.api.riotgames.com/lor/match/v1/matches/"+matchId+"?api_key="+APIKey;
    }

    /**
     * Get league entries in all queues for a given summoner ID.
     * @param summonerID
     * @param APIKey
     * @return Ranked Solo and Ranked Flex stats for a given summonerID
     */
    public static String getRankedStatsURL (String summonerID , String APIKey){
        return "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/"+summonerID+"?api_key="+APIKey;
    }
}
