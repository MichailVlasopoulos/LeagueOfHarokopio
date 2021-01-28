package gr.hua.DistSysApp.ritoAPI.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final static String API_KEY = "RGAPI-87783ea1-00da-4c40-ab6f-991e812d7163";

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    //TODO CREATE filterRequests

    public String filterRequests(String requestStatus) throws JSONException, AdminServiceException, JsonProcessingException {
        List<RequestResults> requests = requestResultsRepository.findRequestsByRequestStatus(requestStatus);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requests);
        return json;
    }

    public JSONObject acceptRequest(int requestId) throws JSONException,AdminServiceException {

        //get the request
        Request request = requestRepository.findRequestByRequest_id(requestId);
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);
        String status = requestResults.getRequest_status();

        if (status.equals("ACCEPTED") || status.equals("DENIED"))
            return JsonUtils.stringToJsonObject("Status", "Failed: This request has been: "+status);

        //get the user
        User user = userRepository.findById(request.getUserid());
        String summonerName = user.getSummoner_name();

        //call RITO api - find summoner's encrypted ID'S
        String url = UrlUtils.getSummonersURL(summonerName,API_KEY);
        if (url==null)
            return JsonUtils.stringToJsonObject("Status", "Failed");

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
                //url2=UrlUtils.getAllChallengerPlayersURL(API_KEY);
                response2=calculateTopPlayersSummonerNames();
                break;
            case generalChampionStatsType:
                response2 = calculateStats();
                break;
            default:
                return JsonUtils.stringToJsonObject("Status", "Failed");
        }


        if (!request.getRequest_type().equalsIgnoreCase(generalChampionStatsType) && !request.getRequest_type().equalsIgnoreCase(LeaderboardsRequestType) &&!request.getRequest_type().equalsIgnoreCase(topPlayersProfilesRequestType))
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

        requestResultsRepository.updateRequest_Deny("DENIED",requestId);
        return JsonUtils.stringToJsonObject("Status", "Successful");
    }

    /**
     *
     *
     * @return a map contain stats of how many times the player played a champ in the past x games.
     * @throws JSONException
     */
    public String calculateStats () {
        String mappedStats = "{89=2, 350=33, 110=8, 111=12, 112=22, 234=71, 113=1, 235=33, 114=5, 236=20, 115=6, 875=12, 876=8, 117=51, 90=7, 238=4, 91=2, 92=4, 119=9, 516=16, 517=13, 518=17, 96=2, 98=14, 10=6, 11=4, 99=10, 12=24, 13=4, 14=4, 15=7, 16=17, 18=9, 360=39, 240=6, 120=21, 121=18, 122=13, 1=12, 2=5, 3=6, 245=13, 4=14, 246=2, 126=3, 523=17, 127=10, 6=2, 7=5, 8=22, 9=9, 526=3, 21=13, 22=18, 23=11, 24=7, 25=9, 26=19, 27=7, 28=3, 29=33, 131=41, 133=8, 134=7, 497=17, 498=1, 136=3, 412=19, 777=34, 30=2, 31=8, 32=1, 33=1, 34=11, 35=10, 36=2, 37=3, 38=29, 39=18, 141=6, 142=12, 143=10, 145=81, 420=2, 266=18, 421=8, 267=7, 147=49, 268=19, 427=6, 429=4, 40=8, 41=7, 42=2, 43=25, 45=6, 48=2, 150=15, 154=4, 157=8, 432=39, 555=6, 50=12, 51=9, 53=8, 54=21, 55=26, 56=3, 57=28, 58=6, 59=1, 161=6, 163=6, 164=32, 201=8, 202=102, 203=13, 60=20, 61=23, 62=19, 63=4, 64=14, 67=32, 69=13, 74=5, 75=5, 76=13, 77=12, 78=2, 79=14, 101=12, 222=2, 102=2, 103=1, 104=14, 105=15, 106=23, 80=19, 107=18, 81=27, 82=17, 83=1, 84=7, 85=21, 86=8}";
        return mappedStats;

    }

    public String getAllRequests() throws JSONException, AdminServiceException, JsonProcessingException {
        List<Request> allRequests = requestRepository.getAllRequests();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(allRequests);
        return json;

    }

    public String calculateTopPlayersSummonerNames() throws JSONException {
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

        //System.out.println(topPlayersSummonerNameList);


        return topPlayersSummonerNameList.toString();
    }
}
