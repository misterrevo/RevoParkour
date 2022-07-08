package com.revo.application;

import com.revo.application.command.ParkourExecutor;
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
    private static ParkourExecutor parkourExecutor;
    private static Plugin instance;

    public static AreaRepository areaRepository() {
        if (Objects.isNull(areaRepository)) {
            areaRepository = new AreaFileRepository();
        }
        return areaRepository;
    }

    public static PlayerSupport playerSupport() {
        if (Objects.isNull(playerSupport)) {
            return playerSupport = new PlayerSupportImp(userRepository());
        }
        return playerSupport;
    }

    public static UserRepository userRepository() {
        if (Objects.isNull(userRepository)) {
            userRepository = new UserFileRepository();
        }
        return userRepository;
    }

    public static AreaService areaService() {
        if (Objects.isNull(areaService)) {
            areaService = new AreaService(areaRepository(), playerSupport(), userRepository());
        }
        return areaService;
    }

    public static ParkourExecutor parkourExecutor() {
        if(Objects.isNull(parkourExecutor)){
            parkourExecutor = new ParkourExecutor(areaService());
        }
        return parkourExecutor;
    }

    public static Plugin plugin(){
        if(Objects.isNull(instance)){
            instance = new Plugin();
        }
        return instance;
    }

    public static void loadPluginInstance(Plugin plugin) {
        instance = plugin;
    }
}
