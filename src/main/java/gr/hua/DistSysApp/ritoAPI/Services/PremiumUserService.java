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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;

public class PremiumUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestResultsRepository requestResultsRepository;

    private Authentication authentication;
    private String username;
    private final String API_KEY = "RGAPI-74e85ff6-eecf-4a0f-a64d-82dc194465a9";

    public String showLiveMatchStats() throws JSONException {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        String summonerName = user.getSummoner_name();

        int user_id = user.getId();
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return ResultUtils.getActiveGameResults(summonerName,API_KEY);

    }

    public String requestTopPlayersProfiles(){
        //Get user data through jwt token
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type("TopPlayersProfiles");

        /*
        saveAndFlush returns the saved entity
        if it is null then the request was not succesfully saved to the db , so the request failed
        if it not null then it means it was saved succesfully to the db and the request was successful
         */
        try {
            Request request1=requestRepository.saveAndFlush(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "Request Failed";
        }

        //TODO CREATE RequestResults


        RequestResults requestResults = new RequestResults();
        requestResults.setRequest_id(requestRepository.findRequestIDByUseridAndRequestType(user_id,"TopPlayersProfiles"));
        requestResults.setRequest_status("Pending");
        try {
            return "Request created successfully!"+requestRepository.saveAndFlush(request).toString(); //TODO remove toString after debugging
        } catch (Exception e) {
            e.printStackTrace();
            return "Request Failed";
        }

    }

    public String showTopPlayersProfiles (int requestId) {
        RequestResults requestResults = requestResultsRepository.findRequestByRequest_id(requestId);

        if(requestResults.getRequest_status().equalsIgnoreCase("Pending") || requestResults.getRequest_status().equalsIgnoreCase("Denied") ) {
            return  "Your request status is: "+ requestResults.getRequest_status();
        } else {
            return "Your request results are: "+ requestResults.getResults();
        }
    }


}
