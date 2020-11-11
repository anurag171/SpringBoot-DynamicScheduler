package com.anurag.spring.dynamic.schedule.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anurag.spring.dynamic.schedule.db.CountryProductCutOff;

@Repository
public interface SchedulerRepo extends MongoRepository<CountryProductCutOff, String> {

}
