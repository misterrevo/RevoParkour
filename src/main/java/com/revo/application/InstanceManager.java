package com.revo.application;

import com.revo.application.database.file.AreaFileRepository;
import com.revo.application.database.file.UserFileRepository;
import com.revo.application.utils.PlayerSupportImp;
import com.revo.domain.AreaService;
import com.revo.domain.port.AreaRepository;
import com.revo.domain.port.PlayerSupport;
import com.revo.domain.port.UserRepository;

import java.util.Objects;

public abstract class InstanceManager {
    private static AreaRepository areaRepository;
    private static UserRepository userRepository;
    private static PlayerSupport playerSupport;
    private static AreaService areaService;

    public static AreaRepository areaRepository() {
        if (Objects.nonNull(areaRepository)) {
            return areaRepository;
        }
        return new AreaFileRepository();
    }

    public static PlayerSupport playerSupport() {
        if (Objects.nonNull(playerSupport)) {
            return playerSupport;
        }
        return new PlayerSupportImp(userRepository());
    }

    public static UserRepository userRepository() {
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
