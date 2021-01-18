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
import java.util.concurrent.TimeUnit;

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

    private final static String API_KEY = "RGAPI-a2c173c1-16e9-46a5-ba1b-d27da9054550";

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    //TODO CREATE filterRequests

    public JSONObject filterRequests(String requestType) throws JSONException, AdminServiceException{
        List<Request> requests;
        requests = requestRepository.findRequestsByRequestType(requestType);
        JSONObject listOfRequests = new JSONObject(requests.toString());
        if(listOfRequests==null) return JsonUtils.stringToJsonObject("Status", "There are no requests");
        return listOfRequests;
    }

    public JSONObject acceptRequest(int requestId) throws JSONException,AdminServiceException {


        //get the request
        Request request = requestRepository.findRequestByRequest_id(requestId);
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);

        //TODO CHECK FOR REQEUST STATUS = DENIED
        if (requestResults.getRequest_status().equals("ACCEPTED"))
            return JsonUtils.stringToJsonObject("Status", "Failed: This request has been accepted");

        //get the user
        User user = userRepository.findById(request.getUserid());
        String summonerName = user.getSummoner_name();

        //call RITO api - find summoner's encrypted ID'S
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
                    response2 = Requests.get(url2);
                } else {
                    response2="UNRANKED";
                }
                break;
            case topPlayersProfilesRequestType:
                url2=UrlUtils.getAllChallengerPlayersURL(API_KEY);
                break;
            case generalChampionStatsType:
                response2 = calculateStats();
                break;
            default:
                return JsonUtils.stringToJsonObject("Status", "Failed");
        }


        if (!request.getRequest_type().equalsIgnoreCase(generalChampionStatsType) && !request.getRequest_type().equalsIgnoreCase(LeaderboardsRequestType))
        response2 = Requests.get(url2);



        if(response2 != null) {
            requestResultsRepository.updateRequest_Accept("ACCEPTED",response2,requestId);
            return JsonUtils.stringToJsonObject("Status","Successful");
        }else {
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }

    }

    public JSONObject denyRequest(int requestId) throws JSONException,AdminServiceException {

        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);

        if (requestResults.getRequest_status().equals("DENIED"))
            return JsonUtils.stringToJsonObject("Status", "Failed: This request has been denied");

        requestRepository.updateRequest_Deny("DENIED",requestId);
        return JsonUtils.stringToJsonObject("Status", "Successful");
    }

    /**
     *
     *
     * @return a map contain stats of how many times the player played a champ in the past x games.
     * @throws JSONException
     */
    public String calculateStats () {
        String mappedStats = "{67=1, 69=1, 28=1, 134=1, 157=1, 236=2, 876=5, 117=1, 516=1, 517=2, 51=1, 30=1, 53=4, 98=1, 54=1, 10=1, 13=1, 35=2, 38=1, 39=2, 18=9, 163=1, 141=2, 120=5, 164=5, 121=6, 2=1, 266=1, 421=3, 201=1, 202=1, 268=1, 246=1, 104=19, 203=2, 81=2, 9=3, 61=1, 84=1, 62=3, 20=1}";
        return mappedStats;

    }

    public String getAllRequests() throws JSONException,AdminServiceException{
        List<Request> allRequests = requestRepository.getAllRequests();
        if(allRequests!=null) {
            //JSONObject allRequestsJson = new JSONObject(allRequests.toString());
            //return JsonUtils.jsonToMap(allRequestsJson);
            return allRequests.toString();
        }else{
            //JSONObject response = JsonUtils.stringToJsonObject("Status", "There are 0 requests");
            //return JsonUtils.jsonToMap(response);
            return null;
        }
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

        //For Player at position 0 its a unique case and doesnt match the algorithm in the loop
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
