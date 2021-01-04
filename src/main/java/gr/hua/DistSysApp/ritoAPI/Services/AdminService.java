package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Requests;
import gr.hua.DistSysApp.ritoAPI.Utilities.UrlUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String acceptRequest(int requestId) throws JSONException {

        //get the request
        Request request = requestRepository.findRequestByRequest_id(requestId);

        //get the user
        User user = userRepository.findById(request.getUserid());
        String summonerName = user.getSummoner_name();

        //call RITO api - find summoner's encrypted ID'S
        String API_KEY = "RGAPI-c97a423e-f1f8-49e7-8d4a-20b11faade24";
        String url = UrlUtils.getSummonersURL(summonerName,API_KEY);

        String response;
        try {
            response = Requests.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error Accessing Url";
        }
        //TODO handle null exception
        if(response==null) return "Expired API KEY or Wrong Summoner Name";

        // JSONParser parser = new JSONParser(response);
        JSONObject jsonObj = new JSONObject(response);
        String accountId=null;

        try{
            accountId = JsonUtils.getAccountId(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error JSON Parser";
        }

        if (accountId==null) return "Error";

        String url2 = UrlUtils.getMatchListURL(accountId,15,API_KEY);
        String response2 = Requests.get(url2);

        if(response2 != null) {
            requestRepository.updateRequest_Accept("ACCEPTED",response2,requestId);
            return "Results: "+response2;
        }else {
            return "Error";
        }

    }

}
