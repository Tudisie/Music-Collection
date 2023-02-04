package com.mongodb_example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MongoDbExampleApplication{

	public static void main(String[] args) {
		SpringApplication.run(MongoDbExampleApplication.class, args);
	}
}
