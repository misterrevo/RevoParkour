package com.revo.application.database.file;

import com.revo.domain.Point;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FileRepository {

    private static final String SLASH = "/";
    private static final String POINT_STRING_SEPARATOR = ";";
    private static final Object YAML_TYPE = ".yml";

    String mapPointToString(Point point) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(point.getId());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getWorld());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getX());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getY());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getZ());
        return stringBuilder.toString();
    }

    YamlConfiguration getYamlConfigurationInFolder(String id, String folderName) {
        try {
            File folder = getFolder(folderName);
            File file = getFileInFolder(folder, id);
            return YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private File getFileInFolder(File folder, String id){
        try {
            File file = buildFile(folder, id);
            if(!file.exists()){
                file.createNewFile();
            }
            return file;
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    private File buildFile(File folder, String id) {
        return new File(folder.getPath() + SLASH + id + YAML_TYPE);
    }

    void deleteFile(String id, String folderName){
        File folder = getFolder(folderName);
        File file = buildFile(folder, id);
        file.delete();
    }

    boolean fileExists(String id, String folderName){
        File folder = getFolder(folderName);
        File file = buildFile(folder, id);
        return file.exists();
    }

    void saveYamlConfiguration(YamlConfiguration yamlConfiguration, String id, String folderName){
        try {
            yamlConfiguration.save(getFileInFolder(getFolder(folderName), id));
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    File getFolder(String name){
        try {
            File folder = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + SLASH + name);
            if(!folder.exists()){
                folder.createNewFile();
            }
            return folder;
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    List<YamlConfiguration> getAllYamlConfigurationsInFolder(String name){
        File folder = getFolder(name);
        return Stream.of(folder.listFiles()).map(file -> YamlConfiguration.loadConfiguration(file)).collect(Collectors.toList());
    }

    Point mapPointFromString(String string) {
        String[] splittedString = string.split(POINT_STRING_SEPARATOR);
        return Point.Builder.aPoint()
                .id(Long.valueOf(splittedString[0]))
                .world(splittedString[1])
                .x(Integer.valueOf(splittedString[2]))
                .y(Integer.valueOf(splittedString[3]))
                .z(Integer.valueOf(splittedString[4]))
                .build();
    }
}
