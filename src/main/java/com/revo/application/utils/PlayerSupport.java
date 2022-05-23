package com.revo.application.utils;

import com.revo.domain.Point;
import com.revo.domain.port.PlayerSupportPort;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static com.revo.application.utils.BukkitUtils.mapLocation;
import static com.revo.application.utils.BukkitUtils.mapPlayer;

public class PlayerSupport implements PlayerSupportPort {

    private static final String INVALID_AREA_MESSAGE = "Area with name %s does not exists!";
    private static final String JOIN_MESSAGE = "You are joining to area %s!";
    private static final String LEAVE_MESSAGE = "You are leaving area %s!";

    @Override
    public void sendInvalidAreaMessage(String uuid, String areaName) {
        mapPlayer(uuid).sendMessage(INVALID_AREA_MESSAGE.formatted(areaName));
    }

    @Override
    public void sendJoinMessage(String uuid, String areaName) {
        mapPlayer(uuid).sendMessage(JOIN_MESSAGE.formatted(areaName));
    }

    @Override
    public void teleportPlayerToArea(String uuid, Point start) {
        mapPlayer(uuid).teleport(mapLocation(start));
    }

    @Override
    public void sendLeaveMessage(String uuid, String name) {
        mapPlayer(uuid).sendMessage(LEAVE_MESSAGE.formatted(name));
    }

    @Override
    public void teleportPlayerToLastLocation(String uuid) {

    }

    @Override
    public void sendNotInAreaMessage(String uuid) {

    }

    @Override
    public void sendAreaNameInUseMessage(String uuid, String name) {

    }

    @Override
    public void sendAreaCreateMessage(String uuid, String name) {

    }

    @Override
    public void sendSetStartMessage(String uuid, String name, Point point) {

    }

    @Override
    public void sendSetEndMessage(String uuid, String name, Point point) {

    }

    @Override
    public void sendSetCheckPointMessage(String uuid, String name, Point point) {

    }

    @Override
    public void sendRemoveCheckPointMessage(String uuid, String name, Point point) {

    }

    @Override
    public Point getCurrentUserLocationAsPoint() {
        return null;
    }

    @Override
    public void sendReachCheckPointMessage(String uuid) {

    }

    @Override
    public void sendWinMessage(String uuid, String name) {

    }

    @Override
    public void sendDeleteAreaMessage(String uuid, String name) {

    }

    @Override
    public void sendAreaDeleteByAdminMessage(String uuid, String name) {

    }
}
