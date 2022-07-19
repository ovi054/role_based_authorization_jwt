package com.security.api;

import com.security.config.JwtTokenUtil;
import com.security.entity.Role;
import com.security.entity.User;
import com.security.repository.RoleRepository;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class AuthApi {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/auth/all")
    @ResponseBody
    public List<User> getALl()
    {
        return userRepository.findAll();
    }

    @GetMapping("/auth/register")
    public String showForm(Model model)
    {
        User user = new User();
        Role roleobj = new Role();
        model.addAttribute("user",user);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles",roles);
        model.addAttribute("roleobj",roleobj);
        //List<String> listRole= Arrays.asList("ROLE_ADMIN","ROLE_EDITOR","ROLE_VIEWER");
        //model.addAttribute("listRole",listRole);
        return "register_form";
    }

    /*
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
     */
    @PostMapping("/auth/register")
    @ResponseBody
    public String submitRegister(@ModelAttribute("user") User user,@ModelAttribute("roleobj") Role roleobj)
    {
        //System.out.println(user.getEmail());
        //System.out.println(user.getPassword());
        //System.out.println(str);
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        //Role role = new Role(str);
        //int val = (Integer) str;
        user.addRole(roleobj);
        userRepository.save(user);
        return "Registration successful";
    }
    @GetMapping("/auth/login")
    public String loginForm(Model model)
    {
        AuthRequest request = new AuthRequest();
        model.addAttribute("request",request);
        return "login_form";
    }
    @PostMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody AuthRequest request) //@ModelAttribute("user")
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
            //return "index";

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
