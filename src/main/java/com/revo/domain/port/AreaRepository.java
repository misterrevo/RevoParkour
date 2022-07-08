package com.revo.domain.port;

import com.revo.domain.Area;
import com.revo.domain.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface AreaRepository {
    List<Area> findAll() throws DatabaseException;
    Optional<Area> findByName(String name) throws DatabaseException;
    boolean existsByName(String name) throws DatabaseException;
    void save(Area area) throws DatabaseException;
    void deleteByName(String name) throws DatabaseException;
}
