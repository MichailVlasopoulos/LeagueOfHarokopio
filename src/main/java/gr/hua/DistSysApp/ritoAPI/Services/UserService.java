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


    //TODO handle saveAndFlush possible exception {DB could be down etc.}
    public String requestMatchHistory() {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return CreateRequest(user.getId(),"Match History",timestamp);

    }


    //TODO handle null request exception
    public String showRequestResults(int requestId) {
        RequestResults requestResults = requestResultsRepository.findRequestByRequest_id(requestId);

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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        return CreateRequest(user.getId(),"Champion Statistics",timestamp);

    }

    public String showChampionStats(int requestId) {

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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return CreateRequest(user.getId(),"Leaderboards",timestamp);

    }

    public String requestMyProfile(){

        authentication = SecurityContextHolder.getContext().getAuthentication();
        username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return CreateRequest(user.getId(),"My profile",timestamp);
    }

    @Transactional
    public String CreateRequest(int userId, String request_type,Timestamp timestamp) {

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

        return "Request added successfully";
    }


}
