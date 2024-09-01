package com.TakeItFree.TakeItFree.Service;

import com.TakeItFree.TakeItFree.Security.CustomSecurityUser;
import com.TakeItFree.TakeItFree.domain.User;
import com.TakeItFree.TakeItFree.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Invalid Username and password");

        return new CustomSecurityUser(user);
    }

}