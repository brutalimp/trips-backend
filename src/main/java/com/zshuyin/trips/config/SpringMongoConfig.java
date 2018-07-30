// package com.zshuyin.trips.config;

// import com.mongodb.MongoClient;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

// @Configuration
// @EnableMongoRepositories
// public class SpringMongoConfig extends AbstractMongoConfiguration {

// 	@Override
// 	protected String getDatabaseName() {
// 		return "trips";
// 	}

// 	@Override
// 	public MongoClient mongoClient() {
// 		return new MongoClient("127.0.0.1", 27017);
// 	}

// 	@Override
// 	protected String getMappingBasePackage() {
// 		return "foo.bar.domain";
// 	}
// }