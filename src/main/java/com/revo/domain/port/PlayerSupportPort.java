package com.revo.domain.port;

import com.revo.domain.Point;

public interface PlayerSupportPort {
    void teleportPlayerToArea(String uuid, Point start);
    void teleportPlayerToLastLocation(String uuid);
    Point getCurrentUserLocationAsPoint();
}
