package gr.hua.DistSysApp.ritoAPI.SecurityConfiguration;

import gr.hua.DistSysApp.ritoAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get UserDetails from dao (Search by string s)!!!!
        // sorin sorin
        // Fetch user from db..

        gr.hua.DistSysApp.ritoAPI.Models.Entities.User user = userRepository.findByUsername(username);

        // String username = "RNS";
        if(username==null){
            throw new UsernameNotFoundException("paketovlaka");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthorities().getRole()));
        return new User(user.getUsername(),user.getUserPassword().getPassword_hash(),authorities);
    }
}
