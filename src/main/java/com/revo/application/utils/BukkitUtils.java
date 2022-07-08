package com.revo.application.utils;

import com.revo.domain.Point;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface BukkitUtils {
    static Player mapPlayer(String UUID){
        return Bukkit.getPlayer(UUID);
    }

    static String mapUUID(Player player){
        return player.getUniqueId().toString();
    }

    static Location mapLocation(Point point){
        return new Location(mapWorld(point.getWorld()), point.getX(), point.getY(), point.getZ());
    }

    static World mapWorld(String world) {
        return Bukkit.getWorld(world);
    }

    static Point mapPoint(Location location){
        World world = location.getWorld();
        return Point.Builder.aPoint()
                .world(world.getName())
                .x((int) location.getX())
                .y((int) location.getY())
                .z((int) location.getZ())
                .build();
    }
}
