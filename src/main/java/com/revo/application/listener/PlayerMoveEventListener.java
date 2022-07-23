package com.revo.application.listener;

import com.revo.application.InstanceManager;
import com.revo.application.utils.PluginUtils;
import com.revo.domain.Area;
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

public class PlayerMoveEventListener implements Listener {
    private static final String REACH_CHECKPOINT_MESSAGE = "&aYou reach checkpoint!";
    private static final String WIN_MESSAGE = "&aYou win an area!";

    private final UserRepository userRepository = InstanceManager.userRepository();
    private final AreaService areaService = InstanceManager.areaService();

    @EventHandler
    public void handleMoveEvent(PlayerMoveEvent moveEvent){
        Player player = moveEvent.getPlayer();
        UUID uuid = player.getUniqueId();
        User user = userRepository.getUserByUUIDOrCreate(uuid.toString());
        if(Objects.nonNull(user.getArea())){
            Area area = areaService.getArea(user.getArea());
            if(playerReachEndPoint(player, area)){
                winArea(player, user);
                return;
            }
            try{
                playerReachCheckPoint(player, user);
                player.sendMessage(REACH_CHECKPOINT_MESSAGE);
            } catch (IsNotCheckPointException exception) {}
        }
    }

    private void playerReachCheckPoint(Player player, User user) {
        areaService.reachCheckPoint(user.getUUID(), PluginUtils.mapPointFromLocation(player.getLocation()));
    }

    private void winArea(Player player, User user) {
        player.sendMessage(WIN_MESSAGE);
        areaService.win(user.getUUID());
    }

    private boolean playerReachEndPoint(Player player, Area area) {
        return PluginUtils.mapPointFromLocation(player.getLocation()).equals(area.getEnd());
    }
}
