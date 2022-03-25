package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.entity.User;
import recipes.service.UserDetailsServiceImpl;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@AuthenticationPrincipal @Valid @RequestBody User user) {
        if (userDetailsService.emailExists(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userDetailsService.newUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
