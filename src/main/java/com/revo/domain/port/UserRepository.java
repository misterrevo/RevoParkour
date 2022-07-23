package com.revo.domain.port;

import com.revo.domain.User;

import java.util.List;

public interface UserRepository {
    User getUserByUUIDOrCreate(String uuid);
    List<User> findAll();
    void save(User user);
}
