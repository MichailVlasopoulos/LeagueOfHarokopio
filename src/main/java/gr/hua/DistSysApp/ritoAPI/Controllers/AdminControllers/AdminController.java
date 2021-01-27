package gr.hua.DistSysApp.ritoAPI.Controllers.AdminControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.hua.DistSysApp.ritoAPI.Services.AdminService;
import gr.hua.DistSysApp.ritoAPI.Services.AdminServiceException;
import gr.hua.DistSysApp.ritoAPI.Services.PremiumUserServiceException;
import gr.hua.DistSysApp.ritoAPI.Utilities.JsonUtils;
import gr.hua.DistSysApp.ritoAPI.exceptionHandling.ResourceNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String admin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello, you have: " + authentication.getAuthorities() + " authorities";
    }

    @GetMapping(path="/users")
    public String getAlLUsers () {
        return adminService.getAllUsers().toString();
    }

    @GetMapping(path="/admin/updateRequest/accept") // TODO DO POST
    @ResponseBody
    public String updateRequest_Accept (@RequestParam int requestId) throws JSONException, AdminServiceException, ResourceNotFoundException {
        try{
            JSONObject response = adminService.acceptRequest(requestId);
            if (response==null) throw new ResourceNotFoundException("Error while Accepting the request");
            return response.toString();
        }catch (AdminServiceException e){
            throw new AdminServiceException("Internal Server Exception while getting exception");
        }

    }

    @GetMapping(path="/admin/updateRequest/deny") // TODO DO POST
    @ResponseBody
    public String updateRequest_Deny (@RequestParam int requestId) throws JSONException, AdminServiceException, ResourceNotFoundException {
        try{
            JSONObject response = adminService.denyRequest(requestId);
            if (response==null) throw new ResourceNotFoundException("Error while Denying the request");
            return response.toString();
        }catch (AdminServiceException e){
            throw new AdminServiceException("Internal Server Exception while getting exception");
        }
    }

    @GetMapping(path="/admin/getAllRequests")
    public String getAllRequests () throws JSONException, AdminServiceException, ResourceNotFoundException {
        //All requests : requestId, username , timestamp , request_type,  summoner_name gia to aist8etik to 8anou
        try{
            String response = "{\"Requests\":" + adminService.getAllRequests() + "}";
            if (response==null) throw new ResourceNotFoundException("Error retrieving all requests");
            return response;
        }catch (AdminServiceException | JsonProcessingException e){
            throw new AdminServiceException("Internal Server Exception");
        }
    }

    @GetMapping(path="/admin/getRequestsByType")
    @ResponseBody
    public String getRequestsByType(@RequestParam String requestType) throws JSONException,  AdminServiceException, ResourceNotFoundException{
        try{
            String response = adminService.filterRequests(requestType);
            if (response==null) throw new ResourceNotFoundException("Error retrieving all requests");
            return response;
        }catch (JsonProcessingException e){
            throw new AdminServiceException("Internal Server Exception");
        }
    }

}
