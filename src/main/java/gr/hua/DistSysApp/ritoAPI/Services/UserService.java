package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestResultsRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private Authentication authentication;
    private String username;

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
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return CreateRequest(user.getId(),MatchHistoryRequestType,timestamp);

    }

    public JSONObject requestChampionStats() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),MatchHistoryRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return CreateRequest(user.getId(),ChampionStatisticsRequestType,timestamp);

    }

    public JSONObject requestLeaderboards() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),MatchHistoryRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return CreateRequest(user.getId(),LeaderboardsRequestType,timestamp);

    }

    public JSONObject requestMyProfile() throws PremiumUserServiceException, JSONException {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //Check if there is already a request in this category
        //TODO FIX JSON
        if (Utils.isExistingPendingRequest(user.getId(),MatchHistoryRequestType,requestRepository,requestResultsRepository))
            return JsonUtils.stringToJsonObject("Status", "Failed ,There is already a pending request");

        return CreateRequest(user.getId(),MyProfileRequestType,timestamp);
    }

    public JSONObject showRequestResults(int requestId) throws PremiumUserServiceException, JSONException {
        RequestResults requestResults = requestResultsRepository.findRequestByRequest_id(requestId);

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


}
