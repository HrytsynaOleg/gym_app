package com.gym.service;

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

    public UserDetailService(IUserDao dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = dao.getUserByName(username);
        return new User(user.getUserName(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
    }
}
