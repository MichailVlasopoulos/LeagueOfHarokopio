package gr.hua.DistSysApp.ritoAPI.Controllers.AdminControllers;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Authorities;
import gr.hua.DistSysApp.ritoAPI.Repositories.AuthoritiesRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import gr.hua.DistSysApp.ritoAPI.Services.AdminService;
import gr.hua.DistSysApp.ritoAPI.Services.MainAdminService;
import gr.hua.DistSysApp.ritoAPI.exceptionHandling.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainAdminController {

    @Autowired
    private MainAdminService mainAdminService;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/mainAdmin/controlPanel")
    public String greeting(Model model) {
        List<Authorities> authoritiesList = authoritiesRepository.getAdmins();
        model.addAttribute("adminList", authoritiesList);
        return "controlPanel";
    }

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
