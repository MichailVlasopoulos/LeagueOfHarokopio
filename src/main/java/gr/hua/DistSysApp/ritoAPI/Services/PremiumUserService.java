package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.SubscriptionRequest;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestResultsRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Utilities.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class PremiumUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestResultsRepository requestResultsRepository;

    private Authentication authentication;
    private String username;
    private final String API_KEY = "RGAPI-703d8926-609b-4cf6-a061-dec7dbd15648";

    private final static String topPlayersProfilesRequestType = "TopPlayersProfiles";
    private final static String cancelPremiumRequestType = "Cancel Premium";

    /**
     * @Description This method is designed to show live stats of an active League of legends game
     * @Contains gameId, gameType, gameStartTime, mapId, gameLength, platformId, gameMode, bannedChampions, gameQueueConfigId,
     * observers, participants
     * @return JSON containing live match stats of the user
     * @throws JSONException
     */
    public String showLiveMatchStats() throws JSONException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        String summonerName = user.getSummoner_name();

        int user_id = user.getId();
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return ResultUtils.getActiveGameResults(summonerName,API_KEY);

    }

    @Transactional
    public JSONObject requestTopPlayersProfiles() throws JSONException {
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user_id,topPlayersProfilesRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        //Create Request
        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type(topPlayersProfilesRequestType);

        /*
        saveAndFlush returns the saved entity
        if it is null then the request was not succesfully saved to the db , so the request failed
        if it not null then it means it was saved succesfully to the db and the request was successful
         */
        try {
            Request request1=requestRepository.saveAndFlush(request);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }


        RequestResults requestResults = new RequestResults();
        requestResults.setRequest_id(requestRepository.findRequestIDByUseridAndRequestType(user_id,topPlayersProfilesRequestType));
        requestResults.setRequest_status("Pending");
        try {
            requestRepository.saveAndFlush(request);
            return JsonUtils.stringToJsonObject("Status", "Successful");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.stringToJsonObject("Status", "Failed");
        }

    }

    public String showRequestResults (int requestId) {
        RequestResults requestResults = requestResultsRepository.findRequestByRequest_id(requestId);

        if(requestResults.getRequest_status().equalsIgnoreCase("Pending") || requestResults.getRequest_status().equalsIgnoreCase("Denied") ) {
            return  "Your request status is: "+ requestResults.getRequest_status();
        } else {
            return "Your request results are: "+ requestResults.getResults();
        }
    }


    //TODO Cancel Premium

    public JSONObject requestPremiumCancel(){
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setCreated_at(timestamp);
        subscriptionRequest.setRequest_type(cancelPremiumRequestType);
        subscriptionRequest.setPaysafe_pin("-1"); //TODO discuss paysafe pin column nullable -> true
        //TODO NO FIELD USER ID in subscription request
        return null;
    }

}