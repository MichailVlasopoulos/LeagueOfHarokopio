package gr.hua.DistSysApp.ritoAPI.Services;

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


}
