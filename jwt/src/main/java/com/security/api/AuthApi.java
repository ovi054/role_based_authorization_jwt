package com.security.api;

import com.security.config.JwtTokenUtil;
import com.security.entity.Role;
import com.security.entity.User;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthApi {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    /*
    @GetMapping("/auth/all")
    public List<User> getALl()
    {
        return userRepository.findAll();
    }
     */

    @PostMapping("/auth/register/{id}")
    public String register(@RequestBody User user, @PathVariable Integer id)
    {
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        Role role=new Role(id);
        user.addRole(role);
        userRepository.save(user);
        return "done";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login( @RequestBody AuthRequest request)
    {
        //return "hello";

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }
}
