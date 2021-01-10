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

    private Authentication authentication;
    private String username;
    private final static String API_KEY = "RGAPI-150dc81e-bdf7-4fb4-b55f-11a729f3caf5";

    private final static String MatchHistoryRequestType = "Match History";
    private final static String MyProfileRequestType = "My Profile";
    private final static String ChampionStatisticsRequestType = "Champion Statistics";
    private final static String LeaderboardsRequestType = "Leaderboards";


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

    public JSONObject showRequestResults(String requestType) throws PremiumUserServiceException, JSONException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Request request = requestRepository.findRequestByUseridAndRequestTypeOrdered(user.getId(),requestType);
        int requestId = request.getRequest_id();
        RequestResults requestResults = requestResultsRepository.findRequestResultsByRequest_id(requestId);

        if(requestResults.getRequest_status().equals("PENDING") || requestResults.getRequest_status().equals("DENIED")) {
            return  JsonUtils.stringToJsonObject("Status", requestResults.getRequest_status());
        } else {
            return JsonUtils.stringToJsonObject("Results", requestResults.getResults());
        }
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

    public JSONObject Register(String username,String password, String firstName,String lastName, String email, String summonerName) throws PremiumUserServiceException, JSONException {

        try {
            User tempUser = userRepository.findByUsername(username);
            if (tempUser != null) {
                return JsonUtils.stringToJsonObject("Status", "Failed: User already exists!");
            }
            String url = UrlUtils.getSummonersURL(summonerName, API_KEY);

            JSONObject response = ResultUtils.getSummonerUrlResponse(url);
            String summonerId = JsonUtils.getSummonerId(response);

            if (summonerId == null)
                return JsonUtils.stringToJsonObject("Status", "Failed: Summoner not found");

            User user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setSummoner_name(summonerName);
            user.setSummoner_id(summonerId);
            userRepository.saveAndFlush(user);

            int userId = userRepository.findByUsername(username).getId();
            String passwordHash = passwordEncoder.encode(password);
            UserPassword userPassword = new UserPassword(userId, passwordHash);
            userPasswordRepository.saveAndFlush(userPassword);

            Authorities authorities = new Authorities(userId, "ROLE_USER");
            authoritiesRepository.saveAndFlush(authorities);

            return JsonUtils.stringToJsonObject("Status", "Successful");
        } catch (Exception e) {
            return JsonUtils.stringToJsonObject("Status", "exception");


        }
    }


}
