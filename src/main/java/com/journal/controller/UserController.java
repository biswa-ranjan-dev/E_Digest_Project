package com.journal.controller;

import com.journal.entity.User;
import com.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User oldUser = userService.findUserByUserName(userName);
        if (oldUser != null) {
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
            return ResponseEntity.ok(userService.saveNewUser(oldUser));
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        userService.delete(userName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
