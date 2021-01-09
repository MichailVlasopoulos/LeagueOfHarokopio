package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestResultsRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Requests;
import gr.hua.DistSysApp.ritoAPI.Utilities.ResultUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.UrlUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestResultsRepository requestResultsRepository;

    private final static String MatchHistoryRequestType = "Match History";
    private final static String MyProfileRequestType = "My Profile";
    private final static String ChampionStatisticsRequestType = "Champion Statistics";
    private final static String LeaderboardsRequestType = "Leaderboards";
    private final static String topPlayersProfilesRequestType = "Top Players Profiles";
    private final static String cancelPremiumRequestType = "Cancel Premium";
    private final static String generalChampionStatsType = "General Champion Stats";

    private final static String API_KEY = "RGAPI-150dc81e-bdf7-4fb4-b55f-11a729f3caf5";

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public JSONObject acceptRequest(int requestId) throws JSONException,AdminServiceException {

        //get the request
        Request request = requestRepository.findRequestByRequest_id(requestId);
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);

        //get the user
        User user = userRepository.findById(request.getUserid());
        String summonerName = user.getSummoner_name();

        //call RITO api - find summoner's encrypted ID'S
        //String API_KEY = "RGAPI-127d784a-e23e-4757-8a5e-bb4c3a71240a";
        String url = UrlUtils.getSummonersURL(summonerName,API_KEY);

        JSONObject response = ResultUtils.getSummonerUrlResponse(url);
        if (response==null)
            return JsonUtils.stringToJsonObject("Status", "Failed");

        String accountId=JsonUtils.getAccountId(response);
        String summonerId = JsonUtils.getSummonerId(response);

        if (accountId==null) return JsonUtils.stringToJsonObject("Status", "Failed"); //TODO CHECK IF THIS IS NEEDED

        //call RITO api - get specific response

        String url2 = null;
        String response2 = null;
        String leagueId = null;

        //TODO Check URL parameters for rito api call
        switch(request.getRequest_type()) {
            case MatchHistoryRequestType:
                url2 = UrlUtils.getMatchListURL(accountId, 15, API_KEY);
                break;
            case MyProfileRequestType:
                url2 = UrlUtils.getSummonersURL(summonerName,API_KEY);
                break;
            case ChampionStatisticsRequestType:
                url2 = UrlUtils.getAllChampionMasteryEntries(summonerId,API_KEY);
                break;
            case LeaderboardsRequestType:
                String tempResponse = UrlUtils.getRankedStatsURL(summonerId,API_KEY);
                leagueId = ResultUtils.getSummonersLeagueID(tempResponse);
                if ( !leagueId.equals("UNRANKED") ) {
                    url2 = UrlUtils.getLeaderBoardsURL(leagueId, API_KEY);
                } else {
                    response2="UNRANKED";
                }
                break;
                //TODO CHANGE LEAGUE ID AND CHECK IF UNRANKED
            case topPlayersProfilesRequestType:
                url2=UrlUtils.getAllChallengerPlayersURL(API_KEY);
                break;
            case generalChampionStatsType:
                response2 = calculateTopPlayersChampionWLRation();
                break;
            default:
                return JsonUtils.stringToJsonObject("Status", "Failed");
        }


        if (!request.getRequest_type().equalsIgnoreCase(generalChampionStatsType) && !leagueId.equals("UNRANKED"))
        response2 = Requests.get(url2);



        if(response2 != null) {
            requestResultsRepository.updateRequest_Accept("ACCEPTED",response2,requestId);
             //TODO WHEN CHECKED CHANGE TO "STATUS" "Successful"
            return JsonUtils.stringToJsonObject("Status","Successful");
        }else {
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }

    }

    public JSONObject denyRequest(int requestId) throws JSONException,AdminServiceException {
        requestRepository.updateRequest_Deny("Denied",requestId);
        return JsonUtils.stringToJsonObject("Status", "Successful");
    }

    /**
     *
     * @param data
     * @return a map contain stats of how many times the player played a champ in the past x games.
     * @throws JSONException
     */
    public String calculateStats (String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data.trim());
        Iterator<String> keys = jsonObject.keys();
        HashMap<Integer, Integer> championsMap = new HashMap<Integer, Integer>();

        while (keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                championsMap.put(jsonObject.getInt("champion"), championsMap.get(jsonObject.getInt("champion")) + 1);
            }
        }

        return championsMap.toString();

    }

    public String calculateTopPlayersChampionWLRation() throws JSONException {
        //Bring top 10 players profiles
        //Bring last 20 games for all 10 players
        //analyze all 200 games

        String topPlayersUrl = UrlUtils.getAllChallengerPlayersURL(API_KEY);
        List<String> topPlayersSummonerNameList = new ArrayList<String>();
        String topPlayers = Requests.get(topPlayersUrl);
        //System.out.println(topPlayers);
        String[] stringArrayPlayers =  topPlayers.split("}");
        for(int j=0; j<stringArrayPlayers.length; j++) {
            //System.out.println(stringArrayPlayers[j] + "}");
        }

        //For Player at position 0 its a unique case and doesnt match the algorith in the loop
        String[] stringArrayPlayersDataAtZero = (stringArrayPlayers[0]+"}").split(",");
        //System.out.println(stringArrayPlayersDataAtZero[5]);
        String[] summonerNameArrayAtZero = stringArrayPlayersDataAtZero[5].split(":");
        String summonerNameAtZero = summonerNameArrayAtZero[1];
        topPlayersSummonerNameList.add(summonerNameAtZero.replace("\"", ""));
        //System.out.println(summonerNameAtZero);


        for (int i=1; i<stringArrayPlayers.length; i++){
            String[] stringArrayPlayersData = (stringArrayPlayers[i]+"}").split(",");
            if(stringArrayPlayersData.length <=3 ){
                continue;
            }else {
                //System.out.println(stringArrayPlayersData[6]);
                String[] summonerNameArray = stringArrayPlayersData[6].split(":");
                String summonerName = summonerNameArray[1];
                topPlayersSummonerNameList.add(summonerName.replace("\"", ""));
                //System.out.println(summonerName.replace("\"", ""));
            }
        }

        System.out.println(topPlayersSummonerNameList);


        return topPlayersSummonerNameList.toString();
    }
}
