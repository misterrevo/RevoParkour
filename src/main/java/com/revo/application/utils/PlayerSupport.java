package com.revo.application.utils;

import com.revo.domain.Point;
import com.revo.domain.User;
import com.revo.domain.port.PlayerSupportPort;
import com.revo.domain.port.UserRepositoryPort;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.revo.application.utils.BukkitUtils.mapLocation;
import static com.revo.application.utils.BukkitUtils.mapPlayer;

public class PlayerSupport implements PlayerSupportPort {

    private final UserRepositoryPort userRepositoryPort;

    public PlayerSupport(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void teleportPlayerToArea(String uuid, Point start) {
        mapPlayer(uuid).teleport(mapLocation(start));
    }

    @Override
    public void teleportPlayerToLastLocation(String uuid) {
        mapPlayer(uuid).teleport(mapLocation(getUser(uuid).getLastLocation()));
    }

    @Override
    public Point getCurrentUserLocationAsPoint() {
        return null;
    }

    private User getUser(String uuid) {
        return userRepositoryPort.getUserByUUIDOrCreate(uuid);
    }
}
