package gr.hua.DistSysApp.ritoAPI.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.*;
import gr.hua.DistSysApp.ritoAPI.Repositories.*;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.Requests;
import gr.hua.DistSysApp.ritoAPI.Utilities.ResultUtils;
import gr.hua.DistSysApp.ritoAPI.Utilities.UrlUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PremiumUserAdminService {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private SubscriptionRequestRepository subscriptionRequestRepository;

    @Autowired
    private SubscriptionRequestResultsRepository subscriptionRequestResultsRepository;

    private final static String goPremiumRequestType = "Go Premium";

    //TODO filterSubscriptionRequest

    public JSONObject updateSubscriptionRequest(int requestId) throws JSONException,AdminServiceException {

        //get the request
        SubscriptionRequest subscriptionRequest = subscriptionRequestRepository.findSubscriptionRequestByRequest_id(requestId);
        SubscriptionRequestsResults subscriptionRequestsResults = subscriptionRequestResultsRepository.findSubscriptionRequestResultsByRequest_id(requestId);

        if (subscriptionRequestsResults.getRequest_status().equals("ACCEPTED"))
            return JsonUtils.stringToJsonObject("Status","Failed: This request has been accepted");

        //get the user
        User user = subscriptionRequest.getUser();

        subscriptionRequestResultsRepository.acceptSubscriptionRequest("ACCEPTED",subscriptionRequest.getSubscription_request_id());

        if(subscriptionRequest.getRequest_type().equals(goPremiumRequestType)) {
            authoritiesRepository.goPremium(user.getId());
        } else {
            authoritiesRepository.cancelPremium(user.getId());
        }

        return JsonUtils.stringToJsonObject("Status","Successful");
    }

    public String filterRequests(String requestStatus) throws JSONException, AdminServiceException, JsonProcessingException {
        List<SubscriptionRequestsResults> requests = subscriptionRequestResultsRepository.findRequestsResultsByRequestStatus(requestStatus);
        String json = "\"Requests\": {\n";
        for (int i=0; i<requests.size(); i++){
            if(i==requests.size()-1){
                json.concat("\"Request\": { \n");
                json.concat("\"subscription_request_id\":"+requests.get(i).getSubscription_request_id()+",\n");
                json.concat("\"subscription_request_status\":"+"\""+requests.get(i).getRequest_status()+"\",\n");
                json.concat("\"subscription_request_type\":"+"\""+requests.get(i).getSubscriptionRequest().getRequest_type()+"\",\n }");

                }
            json.concat("\"Request\": { \n");
            json.concat("\"subscription_request_id\":"+requests.get(i).getSubscription_request_id()+",\n");
            json.concat("\"subscription_request_status\":"+"\""+requests.get(i).getRequest_status()+"\",\n");
            json.concat("\"subscription_request_type\":"+"\""+requests.get(i).getSubscriptionRequest().getRequest_type()+"\",\n },");


        }
        return json;
    }

}
