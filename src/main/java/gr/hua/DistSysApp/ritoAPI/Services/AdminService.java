package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Requests;
import gr.hua.DistSysApp.ritoAPI.Utilities.ResultUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.UrlUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    private final static String MatchHistoryRequestType = "Match History";
    private final static String MyProfileRequestType = "My Profile";
    private final static String ChampionStatisticsRequestType = "Champion Statistics";
    private final static String LeaderboardsRequestType = "Leaderboards";
    private final static String topPlayersProfilesRequestType = "Top Players Profiles";
    private final static String cancelPremiumRequestType = "Cancel Premium";
    private final static String generalChampionStatsType = "General Champion Stats";

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public JSONObject acceptRequest(int requestId) throws JSONException {

        //get the request
        Request request = requestRepository.findRequestByRequest_id(requestId);

        //get the user
        User user = userRepository.findById(request.getUserid());
        String summonerName = user.getSummoner_name();

        //call RITO api - find summoner's encrypted ID'S
        String API_KEY = "RGAPI-74e85ff6-eecf-4a0f-a64d-82dc194465a9";
        String url = UrlUtils.getSummonersURL(summonerName,API_KEY);

        JSONObject response = ResultUtils.getSummonerUrlResponse(url);
        if (response.getString("Status").equalsIgnoreCase("Failed"))
            return JsonUtils.stringToJsonObject("Status", "Failed");

        String accountId=JsonUtils.getAccountId(response);;
        String summonerId = JsonUtils.getSummonerId(response);

        if (accountId==null) return JsonUtils.stringToJsonObject("Status", "Failed"); //TODO CHECK IF THIS IS NEEDED

        //call RITO api - get specific response

        String url2 = null;
        String response2 = null;

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
                String leagueId = null;
                url2 = UrlUtils.getLeaderBoardsURL(ResultUtils.getSummonersLeagueID(UrlUtils.getRankedStatsURL(summonerId,API_KEY)),API_KEY);
                break;
                //TODO CHANGE LEAGUE ID AND CHECK IF UNRANKED
            case topPlayersProfilesRequestType:
                url2=UrlUtils.getAllChallengerPlayersURL(API_KEY);
                break;
            case generalChampionStatsType:
                response2 = calculateStats(UrlUtils.getMatchListURL(summonerId,20,API_KEY));
                break;
            default:
                return JsonUtils.stringToJsonObject("Status", "Failed");
        }


        if (!request.getRequest_type().equalsIgnoreCase(generalChampionStatsType))
        response2 = Requests.get(url2);


        if(response2 != null) {
            requestRepository.updateRequest_Accept("ACCEPTED",response2,requestId);
            JSONObject jsonResults = new JSONObject(response2);
            return jsonResults; //TODO WHEN CHECKED CHANGE TO "STATUS" "Successful"
        }else {
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }

    }

    public JSONObject denyRequest(int requestId) throws JSONException {
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
}
