package com.codegym.airline.controllers.user;

import com.codegym.airline.config.JwtTokenUtil;
import com.codegym.airline.models.JwtResponseNew;
import com.codegym.airline.models.User;
import com.codegym.airline.repository.JwtUserRepository;
import com.codegym.airline.services.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/*
Expose a POST API /authenticate using the JwtAuthenticationController. The POST API gets username and password in the
body- Using Spring Authentication Manager we authenticate the username and password.If the credentials are valid,
a JWT token is created using the JWTTokenUtil and provided to the client.
 */
@RestController
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserRepository jwtUserRepository;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * Test Phương thức Get không cần xác thực
     *
     * @return
     */
    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello");
    }

    /**
     * Test phương thức cho user có role Admin mới được gọi
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/hello2")
    public ResponseEntity<?> hello2() {
        return ResponseEntity.ok("Hello");
    }


    /**
     * Phương thức login
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassWord());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUserName());

        final String token = jwtTokenUtil.generateToken(userDetails);
        String username = authenticationRequest.getUserName();
        long id = jwtUserRepository.findByUserName(username).getId();

        return ResponseEntity.ok(new JwtResponseNew(id, username, token));
    }

    /**
     * Phương thức dùng để xác thực
     * @param username
     * @param password
     * @throws Exception
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
