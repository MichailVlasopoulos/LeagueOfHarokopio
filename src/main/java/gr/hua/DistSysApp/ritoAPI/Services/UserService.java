package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.RequestRepository;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String requestShowMatchHistory() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        int user_id = user.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Request request = new Request();
        request.setUserid(user_id);
        request.setCreated_at(timestamp);
        request.setRequest_type("PENDING");
        requestRepository.saveAndFlush(request);

        return "Request created successfully!";
    }

}
