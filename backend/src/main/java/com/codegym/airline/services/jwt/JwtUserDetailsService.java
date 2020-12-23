package com.codegym.airline.services.jwt;

import com.codegym.airline.models.Role;
import com.codegym.airline.models.User;
import com.codegym.airline.repository.JwtUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * JWTUserDetailsService triển khai lại interface UserDetailsService cửa Spring Security.
 *
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private JwtUserRepository jwtUserRepository;

    /**
     * Override phương thức loadUserByUsername để tìm được thông tin user trong csdl từ username
     * Spring Security sẽ gọi phương thức này để lấy thông tin user
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = jwtUserRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Role> roles = user.getRoles();
        for(Role role: roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(),
                grantedAuthorities);
    }
}
