package com.journal.repo;

import com.journal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface UserRepo extends MongoRepository<User, ObjectId> {

    public User findByUsername(String username);

    public void deleteByUsername(String username);
}
