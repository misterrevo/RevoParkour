package com.revo.application;
import com.revo.application.database.file.AreaFileRepository;
import com.revo.application.database.file.UserFileRepository;
import com.revo.application.utils.PlayerSupport;
import com.revo.domain.AreaService;
import com.revo.domain.port.AreaRepositoryPort;
import com.revo.domain.port.PlayerSupportPort;
import com.revo.domain.port.UserRepositoryPort;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Plugin extends JavaPlugin {

    private static final InstanceManager instanceManager = new InstanceManager();

    public static InstanceManager manager(){
        return instanceManager;
    }

    public static class InstanceManager{

        private AreaRepositoryPort areaRepository;
        private UserRepositoryPort userRepository;
        private PlayerSupportPort playerSupport;
        private AreaService areaService;

        public AreaRepositoryPort areaRepository(){
            if(Objects.nonNull(areaRepository)){
                return areaRepository;
            }
            return new AreaFileRepository();
        }

        public PlayerSupportPort playerSupport(){
            if(Objects.nonNull(playerSupport)){
                return playerSupport;
            }
            return new PlayerSupport();
        }

        public UserRepositoryPort userRepository(){
            if(Objects.nonNull(userRepository)){
                return userRepository;
            }
            return new UserFileRepository();
        }

        public AreaService areaService(){
            if(Objects.nonNull(areaService)){
                return areaService;
            }
            this.areaService = new AreaService(this.areaRepository(), this.playerSupport(), this.userRepository());
            return areaService;
        }
    }
}
