package com.revo.application.utils;

import com.revo.domain.Point;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface PluginUtils {
    static Player getPlayerByUUID(String uuid) {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    static Location mapLocationFromPoint(Point point) {
        return new Location(getWorldByName(point.getWorld()), point.getX(), point.getY(), point.getZ());
    }

    static World getWorldByName(String world) {
        return Bukkit.getWorld(world);
    }

    static Point mapPointFromLocation(Location location) {
        World world = location.getWorld();
        return Point.Builder.aPoint()
                .world(world.getName())
                .x((int) location.getX())
                .y((int) location.getY())
                .z((int) location.getZ())
                .build();
    }

    static String translateSpecialCode(String msg) {
        String coloredMsg = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '&') {
                coloredMsg += '§';
            } else {
                coloredMsg += msg.charAt(i);
            }
        }
        return coloredMsg;
    }
}
