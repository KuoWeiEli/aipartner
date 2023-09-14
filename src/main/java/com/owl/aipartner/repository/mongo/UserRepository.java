package com.owl.aipartner.repository.mongo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.owl.aipartner.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    List<User> findByAgeBetweenAndNameLikeIgnoreCase(int ageFrom, int ageTo, String name, Sort sort);

    @Query(
        value = "{'$and' : [{'age': {'$gte': ?0, '$lte': ?1}}, {'name': {'$regex': ?2, '$options': 'i'}}]}",
        sort = "{ 'age': 1, 'name': -1 }"
    )
    List<User> findByCustomQuery(int ageFrom, int ageTo, String name);

    @Query(
        value = "{'_id': {'$in': ?0}}",
        exists = true
    )
    boolean existsByIdIn(List<Long> ids);

    @Query(
        value = "{'_id': {'$in': ?0}}",
        count = true
    )
    int countByIdIn(List<Long> ids);
}
