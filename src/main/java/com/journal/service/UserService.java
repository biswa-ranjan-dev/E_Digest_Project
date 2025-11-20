package com.journal.service;

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.repo.JournalEntryRepo;
import com.journal.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return userRepo.save(user);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void delete(ObjectId id) {
        userRepo.deleteById(id);
    }
    public void delete(String username) {
        userRepo.deleteByUsername(username);
    }
    public User findUserByUserName(String userName) {
        return userRepo.findByUsername(userName);
    }
}
