package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.*;
import gr.hua.DistSysApp.ritoAPI.Repositories.*;
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

    @Autowired
    private SubscriptionRequestRepository subscriptionRequestRepository;

    @Autowired
    private SubscriptionRequestResultsRepository subscriptionRequestResultsRepository;

    private Authentication authentication;
    private String username;
    private final String API_KEY = "RGAPI-4037b92b-0756-43e6-9e7b-33edaac190aa";

    private final static String topPlayersProfilesRequestType = "Top Players Profiles";
    private final static String cancelPremiumRequestType = "Cancel Premium";
    private final static String goPremiumRequestType = "Go Premium";
    private final static String generalChampionStatsType = "General Champion Stats";

    /**
     * @Description This method is designed to show live stats of an active League of legends game
     * @Contains gameId, gameType, gameStartTime, mapId, gameLength, platformId, gameMode, bannedChampions, gameQueueConfigId,
     * observers, participants
     * @return JSON containing live match stats of the user
     * @throws JSONException
     */
    public JSONObject showLiveMatchStats() throws PremiumUserServiceException, JSONException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        String summonerName = user.getSummoner_name();

        int user_id = user.getId();
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return ResultUtils.getActiveGameResults(summonerName,API_KEY);

    }

    @Transactional
    public JSONObject requestTopPlayersProfiles() throws PremiumUserServiceException,JSONException {
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();


        //return topPlayersDBrepository.bringPlayers();

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user_id,topPlayersProfilesRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return createRequest(user_id,topPlayersProfilesRequestType);


    }

    public JSONObject requestGeneralChampionStats() throws PremiumUserServiceException,JSONException {
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user_id,generalChampionStatsType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return createRequest(user_id,generalChampionStatsType);
    }

    public String showRequestResults (String requestType) throws PremiumUserServiceException,JSONException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Request request = requestRepository.findRequestByUseridAndRequestTypeOrdered(user.getId(),requestType);
        int requestId = request.getRequest_id();
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);

        if(requestResults.getRequest_status().equalsIgnoreCase("Pending") || requestResults.getRequest_status().equalsIgnoreCase("Denied") ) {
            return JsonUtils.stringToJsonObject("Status", requestResults.getRequest_status()).toString();
        } else {
            return JsonUtils.stringToJsonObject("Results", requestResults.getResults()).toString();
        }
    }


    public JSONObject requestPremiumCancel() throws PremiumUserServiceException,JSONException {
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        //Check if there is a pending cancel premium request
        if(Utils.isExistingSubscriptionPendingRequest(user.getId(),cancelPremiumRequestType,subscriptionRequestRepository,subscriptionRequestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return createSubscriptionRequest(user,cancelPremiumRequestType);
    }


    @Transactional
    public JSONObject createRequest(int userId, String request_type) throws PremiumUserServiceException,JSONException {

        Request request = new Request();
        request.setUserid(userId);
        request.setCreated_at(new Timestamp(System.currentTimeMillis()));
        request.setRequest_type(request_type);
        requestRepository.saveAndFlush(request);

        RequestResults requestResults = new RequestResults();
        requestResults.setRequest(request);
        requestResults.setRequest_id(request.getRequest_id()); //TODO CHECK IF THIS WORKS
        requestResults.setRequest_status("PENDING");
        requestResultsRepository.saveAndFlush(requestResults);

        return JsonUtils.stringToJsonObject("Status", "Successful");
    }

    @Transactional
    public JSONObject createSubscriptionRequest(User user, String request_type) throws PremiumUserServiceException,JSONException {

            SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
            subscriptionRequest.setCreated_at(new Timestamp(System.currentTimeMillis()));
            subscriptionRequest.setRequest_type(request_type);
            subscriptionRequest.setPaysafe_pin("-1");
            subscriptionRequest.setUser(user);
            subscriptionRequestRepository.saveAndFlush(subscriptionRequest);

            SubscriptionRequestsResults subscriptionRequestsResults = new SubscriptionRequestsResults();
            subscriptionRequestsResults.setSubscriptionRequest(subscriptionRequest);
            subscriptionRequestsResults.setRequest_status("PENDING");
            subscriptionRequestsResults.setSubscription_request_id(subscriptionRequest.getSubscription_request_id());
            subscriptionRequestResultsRepository.saveAndFlush(subscriptionRequestsResults);

        return JsonUtils.stringToJsonObject("Status", "Successful");

    }
}
