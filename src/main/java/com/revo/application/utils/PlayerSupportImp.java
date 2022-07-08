package com.revo.application.utils;

import com.revo.domain.Point;
import com.revo.domain.User;
import com.revo.domain.port.PlayerSupport;
import com.revo.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;

import static com.revo.application.utils.PluginUtils.mapLocation;
import static com.revo.application.utils.PluginUtils.mapPlayer;

@RequiredArgsConstructor
public class PlayerSupportImp implements PlayerSupport {
    private final UserRepository userRepositoryPort;

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
