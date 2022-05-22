package com.revo.domain.port;

import com.revo.domain.User;

import java.util.List;

public interface UserRepositoryPort {
    User getUserByUUIDOrCreate(String uuid);
    List<User> findAll();
}
