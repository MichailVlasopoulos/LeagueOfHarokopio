package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.*;
import gr.hua.DistSysApp.ritoAPI.Repositories.*;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.ResultUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.UrlUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestResultsRepository requestResultsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private SubscriptionRequestRepository subscriptionRequestRepository;

    @Autowired
    private SubscriptionRequestResultsRepository subscriptionRequestResultsRepository;

    private Authentication authentication;
    private String username;
    private final static String API_KEY = "RGAPI-92e160b8-3fb6-4fd1-b809-2383c1013437";

    private final static String MatchHistoryRequestType = "Match History";
    private final static String MyProfileRequestType = "My Profile";
    private final static String ChampionStatisticsRequestType = "Champion Statistics";
    private final static String LeaderboardsRequestType = "Leaderboards";
    private final static String goPremiumRequestType = "Go Premium";
    private final static String cancelPremiumRequestType = "Cancel Premium";


    public JSONObject requestMatchHistory() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),MatchHistoryRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed: There is already a pending request");

        return CreateRequest(user.getId(),MatchHistoryRequestType,timestamp);

    }

    public JSONObject requestChampionStats() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),ChampionStatisticsRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed: There is already a pending request");

        return CreateRequest(user.getId(),ChampionStatisticsRequestType,timestamp);

    }

    public JSONObject requestLeaderboards() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),LeaderboardsRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed: There is already a pending request");

        return CreateRequest(user.getId(),LeaderboardsRequestType,timestamp);

    }

    public JSONObject requestMyProfile() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),MyProfileRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed: There is already a pending request");

        return CreateRequest(user.getId(),MyProfileRequestType,timestamp);
    }

    public String showRequestResults(String requestType) throws PremiumUserServiceException, JSONException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Request request = requestRepository.findRequestByUseridAndRequestTypeOrdered(user.getId(),requestType);
        int requestId = request.getRequest_id();
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);

        if(requestResults.getRequest_status().equals("PENDING") || requestResults.getRequest_status().equals("DENIED")) {
            //return  JsonUtils.stringToJsonObject("Status", requestResults.getRequest_status());
            return requestResults.getRequest_status();
        } else {
            //return JsonUtils.stringToJsonObject("Results", requestResults.getResults());
            return requestResults.getResults();
        }
    }

    public JSONObject requestGoPremium(String paysafePin) throws PremiumUserServiceException,JSONException {
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        //Check if there is a pending cancel premium request
        if(Utils.isExistingSubscriptionPendingRequest(user.getId(),goPremiumRequestType,subscriptionRequestRepository,subscriptionRequestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return CreateSubscriptionRequest(user,goPremiumRequestType,paysafePin);
    }

    @Transactional
    public JSONObject CreateRequest(int userId, String request_type,Timestamp timestamp) throws PremiumUserServiceException, JSONException {

        Request request = new Request();
        request.setUserid(userId);
        request.setCreated_at(timestamp);
        request.setRequest_type(request_type);
        requestRepository.saveAndFlush(request);

        RequestResults requestResults = new RequestResults();
        requestResults.setRequest(request);
        requestResults.setRequest_id(request.getRequest_id());
        requestResults.setRequest_status("PENDING");
        requestResultsRepository.saveAndFlush(requestResults);

        return JsonUtils.stringToJsonObject("Status", "Successful");
    }

    @Transactional
    public JSONObject CreateSubscriptionRequest(User user, String request_type,String paysafePin) throws PremiumUserServiceException,JSONException {


            SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
            subscriptionRequest.setCreated_at(new Timestamp(System.currentTimeMillis()));
            subscriptionRequest.setRequest_type(request_type);
            subscriptionRequest.setPaysafe_pin(paysafePin);
            subscriptionRequest.setUser(user);
            subscriptionRequestRepository.saveAndFlush(subscriptionRequest);

            SubscriptionRequestsResults subscriptionRequestsResults = new SubscriptionRequestsResults();
            subscriptionRequestsResults.setSubscriptionRequest(subscriptionRequest);
            subscriptionRequestsResults.setRequest_status("PENDING");
            subscriptionRequestsResults.setSubscription_request_id(subscriptionRequest.getSubscription_request_id());
            subscriptionRequestResultsRepository.saveAndFlush(subscriptionRequestsResults);


        return JsonUtils.stringToJsonObject("Status", "Successful");

    }

    @Transactional
    public JSONObject Register(String username,String password, String firstName,String lastName, String email, String summonerName) throws PremiumUserServiceException, JSONException {

        try {
            User tempUser = userRepository.findByUsername(username);
            if (tempUser != null ) {
                return JsonUtils.stringToJsonObject("Status", "Failed: Username already exists!");
            }
            tempUser = userRepository.findByEmail(email);
            if (tempUser != null) {
                return JsonUtils.stringToJsonObject("Status", "Failed: Email already exists!");
            }

            String url = UrlUtils.getSummonersURL(summonerName, API_KEY);
            JSONObject response = ResultUtils.getSummonerUrlResponse(url);
            if (response == null)
                return JsonUtils.stringToJsonObject("Status", "Failed: Summoner not found");
            String summonerId = JsonUtils.getSummonerId(response);

            User user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setSummoner_name(summonerName);
            user.setSummoner_id(summonerId);
            userRepository.saveAndFlush(user);

            String passwordHash = passwordEncoder.encode(password);
            UserPassword userPassword = new UserPassword();
            userPassword.setUser(user);
            userPassword.setUser_id(user.getId());
            userPassword.setPassword_hash(passwordHash);
            userPasswordRepository.saveAndFlush(userPassword);


            Authorities authorities = new Authorities();
            authorities.setUser(user);
            authorities.setUser_id(user.getId());
            authorities.setRole("ROLE_USER");
            authoritiesRepository.saveAndFlush(authorities);

            return JsonUtils.stringToJsonObject("Status", "Successful");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.stringToJsonObject("Status", "exception");

        }
    }
    }
