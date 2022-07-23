package com.revo.application.event;

import com.revo.application.InstanceManager;
import com.revo.application.utils.PluginUtils;
import com.revo.domain.User;
import com.revo.domain.exception.IsNotCheckPointException;
import com.revo.domain.port.AreaService;
import com.revo.domain.port.UserRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;
import java.util.UUID;

public class ReachCheckPointEvent implements Listener {
    private static final String REACH_CHECKPOINT_MESSAGE = "&aYou reach checkpoint!";

    private final UserRepository userRepository = InstanceManager.userRepository();
    private final AreaService areaService = InstanceManager.areaService();

    @EventHandler
    public void handleMoveEvent(PlayerMoveEvent moveEvent){
        Player player = moveEvent.getPlayer();
        UUID uuid = player.getUniqueId();
        User user = userRepository.getUserByUUIDOrCreate(uuid.toString());
        if(Objects.nonNull(user.getArea())){
            try{
                areaService.reachCheckPoint(user.getUUID(), PluginUtils.mapPointFromLocation(player.getLocation()));
                player.sendMessage(REACH_CHECKPOINT_MESSAGE);
            } catch (IsNotCheckPointException exception) {}
        }
    }
}
