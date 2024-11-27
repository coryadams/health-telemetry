package com.heartpass.healthtelemetry.repository;

import com.heartpass.healthtelemetry.entity.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepo extends CrudRepository<Activity, Integer> {

}
