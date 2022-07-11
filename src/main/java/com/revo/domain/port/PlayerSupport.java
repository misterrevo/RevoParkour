package com.revo.domain.port;

import com.revo.domain.Point;

public interface PlayerSupport {
    void teleportPlayerToArea(String uuid, Point start);
    void teleportPlayerToLastLocation(String uuid);
    Point getCurrentUserLocationAsPoint(String UUID);
}
