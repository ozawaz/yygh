package cn.distantstar.mongodb.repository;

import cn.distantstar.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: distantstar
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
