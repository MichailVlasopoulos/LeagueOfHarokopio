package gr.hua.DistSysApp.ritoAPI.Controllers.AdminControllers;

import gr.hua.DistSysApp.ritoAPI.Services.PremiumUserAdminService;
import gr.hua.DistSysApp.ritoAPI.Services.AdminServiceException;
import gr.hua.DistSysApp.ritoAPI.exceptionHandling.ResourceNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminPremiumUserController {

    @Autowired
    private PremiumUserAdminService adminPremiumUserService;

    @GetMapping(path="admin/acceptSubscriptionRequest")
    @ResponseBody
    public String acceptSubscriptionRequest (@RequestParam int requestId) throws JSONException, AdminServiceException, ResourceNotFoundException {
        try{
            JSONObject response = adminPremiumUserService.acceptSubscriptionRequest(requestId);
            if (response==null) throw new ResourceNotFoundException("Error while Accepting the request");
            return response.toString();
        }catch (AdminServiceException e){
            throw new AdminServiceException("Internal Server Exception while getting exception");
        }

    }
}
