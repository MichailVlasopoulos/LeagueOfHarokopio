package gr.hua.DistSysApp.ritoAPI.Services;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
