package com.zshuyin.trips.repository;

import java.util.Optional;

import com.zshuyin.trips.bean.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{'userName':?0}")
    Optional<User> findByName(String userName); 
}