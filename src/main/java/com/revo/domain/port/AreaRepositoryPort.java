package com.revo.domain.port;

import com.revo.domain.Area;

import java.util.List;
import java.util.Optional;

public interface AreaRepositoryPort {
    List<Area> findAll();
    Optional<Area> findByName(String name);
    boolean existsByName(String name);
    void save(Area area);
    void deleteByName(String name);
}
