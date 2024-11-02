package com.gym.security;

import com.gym.dao.IUserDao;
import com.gym.model.UserModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {
    private final IUserDao dao;
    private final LoginAttemptService loginAttemptService;

    public UserDetailService(IUserDao dao, LoginAttemptService loginAttemptService) {
        this.dao = dao;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(username)) {
            throw new UsernameNotFoundException("blocked");
        }
        UserModel user = dao.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User name or password incorrect");
        }
        return new User(user.getUserName(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
    }
}
