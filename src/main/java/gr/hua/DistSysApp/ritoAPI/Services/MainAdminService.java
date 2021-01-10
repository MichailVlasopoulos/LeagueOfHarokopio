package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Authorities;
import gr.hua.DistSysApp.ritoAPI.Repositories.AuthoritiesRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    public String manageAllUsers(){
        List<Authorities> allUsers = authoritiesRepository.getAllUsers();

        //TODO CONVERT TO JSON IF NEEDED
        return allUsers.toString();
    }

    public String manageAllAdmins(){
        List<Authorities> allAdmins = authoritiesRepository.getAllAdmins();

        //TODO CONVERT TO JSON IF NEEDED
        return allAdmins.toString();
    }
}
