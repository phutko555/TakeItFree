package com.TakeItFree.TakeItFree.Service;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDetailsServiceTest {
    @Test
    public void generate_encrypted_password() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "asdfasdf";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
        assertThat(rawPassword, not(encodedPassword));
    }

}

