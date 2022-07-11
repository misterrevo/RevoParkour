package com.revo.application.utils;

import com.revo.domain.Point;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.UUID;

public interface PluginUtils {
    static final String APPLICATION_YAML_NAME = "application.yml";

    static Player mapPlayer(String uuid) {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    static String mapUUID(Player player) {
        return player.getUniqueId().toString();
    }

    static Location mapLocation(Point point) {
        return new Location(mapWorld(point.getWorld()), point.getX(), point.getY(), point.getZ());
    }

    static World mapWorld(String world) {
        return Bukkit.getWorld(world);
    }

    static Point mapPoint(Location location) {
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
