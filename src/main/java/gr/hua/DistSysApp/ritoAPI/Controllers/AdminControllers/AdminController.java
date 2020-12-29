package gr.hua.DistSysApp.ritoAPI.Controllers.AdminControllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin")
    public String admin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello, you have: " + authentication.getAuthorities() + " authorities";
    }
}
