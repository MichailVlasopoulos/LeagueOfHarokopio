package gr.hua.DistSysApp.ritoAPI.Controllers.AdminControllers;

import gr.hua.DistSysApp.ritoAPI.Services.AdminService;
import gr.hua.DistSysApp.ritoAPI.Services.MainAdminService;
import gr.hua.DistSysApp.ritoAPI.exceptionHandling.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class MainAdminController {

    @Autowired
    private MainAdminService mainAdminService;

    @GetMapping("/mainAdmin/manageAllUsers")
    public String manageAllUsers() throws ResourceNotFoundException {
        String allUsers = mainAdminService.manageAllUsers();
        if(allUsers==null) throw new ResourceNotFoundException("There are no users registered at this moment");
        return allUsers;
    }

    @GetMapping("/mainAdmin/manageAllAdmins")
    public String manageAllAdmins() throws ResourceNotFoundException {
        String allAdmins = mainAdminService.manageAllAdmins();
        if(allAdmins==null) throw new ResourceNotFoundException("There are no admins registered at this moment");
        return allAdmins;
    }
}
