package com.zshuyin.trips.repository;

import java.util.List;
import java.util.Optional;

import com.zshuyin.trips.bean.Trip;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface TripRepository extends MongoRepository<Trip, String> {
    @Query("{'userId':?0}")
    Optional<List<Trip>> findByUserId(String userId); 
}