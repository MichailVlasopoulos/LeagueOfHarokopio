package gr.hua.DistSysApp.ritoAPI.Services;

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

@Service
public class PremiumUserAdminService {

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private SubscriptionRequestRepository subscriptionRequestRepository;

    @Autowired
    private SubscriptionRequestResultsRepository subscriptionRequestResultsRepository;

    private final static String goPremiumRequestType = "Go Premium";

    public JSONObject updateSubscriptionRequest(int requestId) throws JSONException,AdminServiceException {

        //get the request
        System.out.println("lol");
        SubscriptionRequest subscriptionRequest = subscriptionRequestRepository.findSubscriptionRequestByRequest_id(requestId);
        SubscriptionRequestsResults subscriptionRequestsResults = subscriptionRequestResultsRepository.findSubscriptionRequestResultsByRequest_id(requestId);

        //get the user
        User user = subscriptionRequest.getUser();

        subscriptionRequestResultsRepository.acceptSubscriptionRequest("ACCEPTED",subscriptionRequest.getSubscription_request_id());
        System.out.println("1");

        if(subscriptionRequest.getRequest_type().equals(goPremiumRequestType)) {
            authoritiesRepository.goPremium(user.getId());
            System.out.println("1st if");
        } else {
            authoritiesRepository.cancelPremium(user.getId());
            System.out.println("2nd if");

        }

        return JsonUtils.stringToJsonObject("Status","Successful");
    }

}
