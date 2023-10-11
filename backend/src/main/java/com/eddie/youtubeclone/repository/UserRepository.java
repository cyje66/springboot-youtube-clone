package com.eddie.youtubeclone.repository;

import com.eddie.youtubeclone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
