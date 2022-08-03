package com.security.repository;

import com.security.entity.Role;
import com.security.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@Rollback(false)
//@Configuration
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //@Test

    @Bean
    public void testCreateRoles()
    {
        Role admin = new Role("ROLE_ADMIN");
        Role editor = new Role("ROLE_EDITOR");
        Role viewer = new Role("ROLE_VIEWER");
        roleRepository.save(admin);
        roleRepository.save(editor);
        roleRepository.save(viewer);
    }
    @Bean
    public void testCreateUser()
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("12345");

        User newUser = new User("avi.pal257@gmail.com", password);
        newUser.addRole(new Role(1));
        User savedUser = userRepository.save(newUser);

        //assertThat(savedUser).isNotNull();
        //assertThat(savedUser.getId()).isGreaterThan(0);
    }

}
