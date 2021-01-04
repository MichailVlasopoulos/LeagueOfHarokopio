package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestResultsRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    /*
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    */

    //TODO handle saveAndFlush possible exception {DB could be down etc.}
    public String requestMatchHistory() {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type("MatchHistory");
        requestRepository.saveAndFlush(request);
        
        RequestResults requestResults = new RequestResults();
        requestResults.setRequest(request);
        requestResults.setRequest_id(request.getRequest_id());
        requestResults.setRequest_status("PENDING");

        requestResultsRepository.saveAndFlush(requestResults);
        return "Request created successfully!";
    }


    //TODO handle null request exception
    public String showMatchHistory(int requestId) {

        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Request request = requestRepository.findRequestByRequest_idAndUserid(requestId,user.getId());
         */
        Request request = requestRepository.findRequestByRequest_id(requestId);
        RequestResults requestResults = requestResultsRepository.findRequestByRequest_id(request.getRequest_id());

        if(requestResults.getRequest_status().equals("PENDING") || requestResults.getRequest_status().equals("DENIED")) {
            return  "Your request status is: "+ requestResults.getRequest_status();
        } else {
            return "Your request results are: "+ requestResults.getRequest_status();
        }
    }


    public String requestChampionStats(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type("ChampionStats");

        /*
        saveAndFlush returns the saved entity
        if it is null then the request was not succesfully saved to the db , so the request failed
        if it not null then it means it was saved succesfully to the db and the request was successful
         */
        if(requestRepository.saveAndFlush(request)!=null) {
            return "Request created successfully!";
        }else{
            return "Request Failed";
        }
    }

    public String showChampionStats(int requestId) {

        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Request request = requestRepository.findRequestByRequest_idAndUserid(requestId,user.getId());
         */
        Request request = requestRepository.findRequestByRequest_id(requestId);

        if(request.getRequest_type().equals("PENDING") || request.getRequest_type().equals("DENIED")) {
            return  "Your request status is: "+ request.getRequest_type();
        } else {
            return "Your request results are: "+ request.getRequest_body();
        }
    }

    public String requestLeaderboards(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type("Leaderboards");

        /*
        saveAndFlush returns the saved entity
        if it is null then the request was not succesfully saved to the db , so the request failed
        if it not null then it means it was saved succesfully to the db and the request was successful
         */
        //TODO CHECK FOR BETTER METHOD
        Request success = requestRepository.saveAndFlush(request);
        if(!success.getRequest_type().isEmpty()) {
            return "Request created successfully!";
        }else{
            return "Request Failed";
        }
    }

    public String requestMyProfile(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type("MyProfile");

        /*
        saveAndFlush returns the saved entity
        if it is null then the request was not succesfully saved to the db , so the request failed
        if it not null then it means it was saved succesfully to the db and the request was successful
         */
        if(requestRepository.saveAndFlush(request)!=null) {
            return "Request created successfully!";
        }else{
            return "Request Failed";
        }
    }



}
