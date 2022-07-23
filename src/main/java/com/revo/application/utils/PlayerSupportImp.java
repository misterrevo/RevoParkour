package com.revo.application.utils;

import com.revo.domain.Point;
import com.revo.domain.User;
import com.revo.domain.port.PlayerSupport;
import com.revo.domain.port.UserRepository;
import org.bukkit.entity.Player;


public class PlayerSupportImp implements PlayerSupport {
    private final UserRepository userRepositoryPort;

    public PlayerSupportImp(UserRepository userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void teleportPlayerToArea(String uuid, Point start) {
        PluginUtils.getPlayerByUUID(uuid).teleport(PluginUtils.mapLocationFromPoint(start));
    }

    @Override
    public void teleportPlayerToLastLocation(String uuid) {
        PluginUtils.getPlayerByUUID(uuid).teleport(PluginUtils.mapLocationFromPoint(getUser(uuid).getLastLocation()));
    }

    @Override
    public Point getCurrentUserLocationAsPoint(String uuid) {
        Player player = PluginUtils.getPlayerByUUID(uuid);
        return PluginUtils.mapPointFromLocation(player.getLocation());
    }

    @Override
    public void teleportPlayerToLastCheckPoint(String uuid) {
        Player player = PluginUtils.getPlayerByUUID(uuid);
        player.teleport(PluginUtils.mapLocationFromPoint(getUser(uuid).getLastCheckPoint()));
    }

    private User getUser(String uuid) {
        return userRepositoryPort.getUserByUUIDOrCreate(uuid);
    }
}
