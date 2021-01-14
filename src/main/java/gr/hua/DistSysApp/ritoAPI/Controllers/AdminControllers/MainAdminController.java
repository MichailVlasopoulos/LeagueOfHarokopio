package gr.hua.DistSysApp.ritoAPI.Controllers.AdminControllers;

import gr.hua.DistSysApp.ritoAPI.Models.RegisterRequest;
import gr.hua.DistSysApp.ritoAPI.Services.MainAdminService;
import gr.hua.DistSysApp.ritoAPI.Services.PremiumUserServiceException;
import gr.hua.DistSysApp.ritoAPI.exceptionHandling.ResourceNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainAdminController {

    @Autowired
    private MainAdminService mainAdminService;

    @GetMapping("mainAdmin/getAllUsers")
    public String getAllUsers() throws ResourceNotFoundException {
        String allUsers = mainAdminService.getAllUsers();
        if(allUsers==null) throw new ResourceNotFoundException("There are no users registered at this moment");
        return allUsers;
    }

    @GetMapping("mainAdmin/getAllAdmins")
    public String getAllAdmins() throws ResourceNotFoundException {
        String allAdmins = mainAdminService.getAllAdmins();
        if(allAdmins==null) throw new ResourceNotFoundException("There are no admins registered at this moment");
        return allAdmins;
    }

    @PostMapping(path="mainAdmin/registerAdmin")
    public String RegisterAdmin (@RequestBody RegisterRequest registerRequest) throws JSONException, PremiumUserServiceException, ResourceNotFoundException {
        try{
            JSONObject response = mainAdminService.registerAdmin(registerRequest.getUsername(), registerRequest.getPassword(),registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(),registerRequest.getSummonerName(),registerRequest.getRole());
            if (response==null) throw new ResourceNotFoundException("Error while making the request");
            return response.toString();
        }catch (PremiumUserServiceException e){
            throw new PremiumUserServiceException("Internal Server Exception while getting exception");
        }
    }

    @GetMapping("mainAdmin/deleteAdmin")
    public String DeleteAdmin(@RequestParam int user_id) throws ResourceNotFoundException, PremiumUserServiceException, JSONException {
        try{
            JSONObject response = mainAdminService.deleteAdmin(user_id);
            if(response==null) throw new ResourceNotFoundException("There are no admins registered at this moment");
            return response.toString();
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Internal Server Exception while getting exception");
        }
    }
}
