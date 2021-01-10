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

    private final static String goPremiumRequestType = "Go Premium";

    public JSONObject updateSubscriptionRequest(int requestId) throws JSONException,AdminServiceException {

        //get the request
        SubscriptionRequest subscriptionRequest = subscriptionRequestRepository.findSubscriptionRequestByRequest_id(requestId);

        //get the user
        User user = subscriptionRequest.getUser();

        subscriptionRequestRepository.acceptSubscriptionRequest("ACCEPTED",subscriptionRequest.getSubscription_request_id());

        if(subscriptionRequest.getRequest_type().equals(goPremiumRequestType)) {
            authoritiesRepository.goPremium(user.getId());
        } else {
            authoritiesRepository.cancelPremium(user.getId());
        }

        return JsonUtils.stringToJsonObject("Status","Successful");
    }

}
