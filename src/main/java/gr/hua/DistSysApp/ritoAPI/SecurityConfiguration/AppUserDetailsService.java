package gr.hua.DistSysApp.ritoAPI.SecurityConfiguration;

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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // Get UserDetails from dao (Search by string s)!!!!
        // sorin sorin
        // Fetch user from db..
        String username = "RNS";
        if(!s.equals(username)){
            throw new UsernameNotFoundException("paketovlaka");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User("RNS","$2y$12$MQk1HuE/eke2lLGFUcDuXeeEnFJcmHBoigCWClyK07HsBdaYWqUDu",authorities);
    }
}
