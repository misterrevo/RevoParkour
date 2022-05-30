package com.revo.application;

import com.revo.application.database.file.AreaFileRepository;
import com.revo.application.database.file.UserFileRepository;
import com.revo.application.utils.PlayerSupport;
import com.revo.domain.AreaService;
import com.revo.domain.port.AreaRepositoryPort;
import com.revo.domain.port.PlayerSupportPort;
import com.revo.domain.port.UserRepositoryPort;

import java.util.Objects;

public abstract class InstanceManager {

    private static AreaRepositoryPort areaRepository;
    private static UserRepositoryPort userRepository;
    private static PlayerSupportPort playerSupport;
    private static AreaService areaService;

    public static AreaRepositoryPort areaRepository() {
        if (Objects.nonNull(areaRepository)) {
            return areaRepository;
        }
        return new AreaFileRepository();
    }

    public static PlayerSupportPort playerSupport() {
        if (Objects.nonNull(playerSupport)) {
            return playerSupport;
        }
        return new PlayerSupport(userRepository());
    }

    public static UserRepositoryPort userRepository() {
        if (Objects.nonNull(userRepository)) {
            return userRepository;
        }
        return new UserFileRepository();
    }

    public static AreaService areaService() {
        if (Objects.nonNull(areaService)) {
            return areaService;
        }
        areaService = new AreaService(areaRepository(), playerSupport(), userRepository());
        return areaService;
    }
}
