package lk.cj.testapp.service.implementation;

import lk.cj.testapp.model.User;
import lk.cj.testapp.repository.UserRepository;
import lk.cj.testapp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(userName);
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        if (user != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
