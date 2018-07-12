package com.zshuyin.trips.repository;

import com.zshuyin.trips.bean.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserRepository extends ReactiveMongoRepository<User, Long> {

}