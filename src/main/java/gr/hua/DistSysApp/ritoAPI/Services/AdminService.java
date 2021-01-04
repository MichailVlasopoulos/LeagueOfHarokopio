package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Requests;
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
        String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+summonerName+"?api_key="+API_KEY;

        String response = Requests.get(url);
        //TODO handle null exception

        // JSONParser parser = new JSONParser(response);
        JSONObject jsonObj = new JSONObject(response);

        //String accountId = jsonObj.getString("accountId");
        String accountId = JsonUtils.recurseKeys(jsonObj, "accountId");


        String url2 = "https://eun1.api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountId+"?api_key="+API_KEY;
        String response2 = Requests.get(url2);

        if(response2 != null) {
            requestRepository.updateRequest_Accept("ACCEPTED",response2,requestId);
            return "Results: "+response2;
        }else {
            return "Error";
        }

    }

}
