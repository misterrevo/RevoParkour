package com.revo.domain.port;

import com.revo.domain.Point;
import com.revo.domain.User;

public interface PlayerSupport {
    void teleportPlayerToArea(String uuid, Point start);
    void teleportPlayerToLastLocation(String uuid);
    Point getCurrentUserLocationAsPoint(String uuid);
    void teleportPlayerToLastCheckPoint(String uuid);
}
