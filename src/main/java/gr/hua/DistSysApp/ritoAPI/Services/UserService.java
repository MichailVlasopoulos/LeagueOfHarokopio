package gr.hua.DistSysApp.ritoAPI.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;

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
    private final static String API_KEY = "RGAPI-004f6b79-5217-4f61-a61d-d294dba386ba";

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

        return createRequest(user.getId(),MatchHistoryRequestType,timestamp);

    }

    public JSONObject requestChampionsMastery() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),ChampionStatisticsRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed: There is already a pending request");

        return createRequest(user.getId(),ChampionStatisticsRequestType,timestamp);

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

        return createRequest(user.getId(),LeaderboardsRequestType,timestamp);

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

        return createRequest(user.getId(),MyProfileRequestType,timestamp);
    }

    public String showRequestResults(String requestType) throws PremiumUserServiceException, JSONException, JsonProcessingException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Request request = requestRepository.findRequestByUseridAndRequestTypeOrdered(user.getId(),requestType);
        int requestId = request.getRequest_id();
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);


        if(requestResults.getRequest_status().equals("PENDING") || requestResults.getRequest_status().equals("DENIED")) {
            return "Status"+requestResults.getRequest_status();
        } else {
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

        return createSubscriptionRequest(user,goPremiumRequestType,paysafePin);
    }

    @Transactional
    public JSONObject createRequest(int userId, String request_type, Timestamp timestamp) throws PremiumUserServiceException, JSONException {

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
    public JSONObject createSubscriptionRequest(User user, String request_type, String paysafePin) throws PremiumUserServiceException,JSONException {


            SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
            subscriptionRequest.setCreated_at(new Timestamp(System.currentTimeMillis()));
            subscriptionRequest.setRequest_type(request_type);
            subscriptionRequest.setPaysafe_pin(paysafePin);
            subscriptionRequest.setUser(user);
            subscriptionRequestRepository.saveAndFlush(subscriptionRequest);

            SubscriptionRequestsResults subscriptionRequestsResults = new SubscriptionRequestsResults();
            subscriptionRequestsResults.setSubscriptionRequest(subscriptionRequest);
            subscriptionRequestsResults.setRequest_status("Pending");
            subscriptionRequestsResults.setSubscription_request_id(subscriptionRequest.getSubscription_request_id());
            subscriptionRequestResultsRepository.saveAndFlush(subscriptionRequestsResults);


        return JsonUtils.stringToJsonObject("Status", "Successful");

    }

    public String getMyRequests(String requestStatus) throws JSONException, AdminServiceException, JsonProcessingException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        List<Request> requests = user.getRequests();
        List<RequestResults> pendingRequests = new ArrayList<>();

        RequestResults requestResults;
        RequestResults tempResults = new RequestResults();

        //for each request
        for (Request request:requests) {

             //get request results
             requestResults = requestResultsRepository.findRequestResultsByRequest_id(request.getRequest_id());

             String status = requestResults.getRequest_status();
             if (status.equals("PENDING")) {
                 tempResults.setRequest_status(status);
                 tempResults.setRequest_id(requestResults.getRequest_id());
                 tempResults.setResults(requestResults.getResults());
                 pendingRequests.add(tempResults);
                 System.out.println("lol");
             }

             if (requestStatus.equals("RESOLVED") && !(status.equals("PENDING"))){
                 tempResults.setRequest_status(status);
                 tempResults.setRequest_id(requestResults.getRequest_id());
                 tempResults.setResults(requestResults.getResults());
                 pendingRequests.add(tempResults);
             }

        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(pendingRequests);
        return json;
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
