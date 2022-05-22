package com.revo.domain.port;

import com.revo.domain.Point;

public interface PlayerSupportPort {
    void sendInvalidAreaMessage(String uuid, String areaName);
    void sendJoinMessage(String uuid, String areaName);
    void teleportPlayerToArea(Point start);
    void sendLeaveMessage(String uuid, String name);
    void teleportPlayerToLastLocation(String uuid);
    void sendNotInAreaMessage(String uuid);
    void sendAreaNameInUseMessage(String uuid, String name);
    void sendAreaCreateMessage(String uuid, String name);
    void sendSetStartMessage(String uuid, String name, Point point);
    void sendSetEndMessage(String uuid, String name, Point point);
    void sendSetCheckPointMessage(String uuid, String name, Point point);
    void sendRemoveCheckPointMessage(String uuid, String name, Point point);
    Point getCurrentUserLocationAsPoint();
    void sendReachCheckPointMessage(String uuid);
    void sendWinMessage(String uuid, String name);
    void sendDeleteAreaMessage(String uuid, String name);
    void sendAreaDeleteByAdminMessage(String uuid, String name);
}
