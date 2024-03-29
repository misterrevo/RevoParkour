package com.revo.application.database.file;

import com.revo.domain.Point;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class FileRepository {
    private static final String SLASH = "/";
    private static final String POINT_STRING_SEPARATOR = ";";
    private static final Object YAML_TYPE = ".yml";
    private static final String PLUGINS_FOLDER_NAME = "plugins";
    private static final String CURRENT_DIRECTORY_PROPERTY = "user.dir";
    private static final String CURRENT_PLUGIN_FOLDER_NAME = "REVO_PARKOUR";

    String mapPointToString(Point point) {
        if(Objects.isNull(point)){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(point.getWorld());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getX());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getY());
        stringBuilder.append(POINT_STRING_SEPARATOR);
        stringBuilder.append(point.getZ());
        return stringBuilder.toString();
    }

    YamlConfiguration getYamlConfigurationInFolder(String id, String folderName) throws IOException, URISyntaxException {
        File folder = getFolder(folderName);
        File file = createFileInFolderAndGet(folder, id);
        return YamlConfiguration.loadConfiguration(file);
    }

    private File createFileInFolderAndGet(File folder, String id) throws IOException {
        File file = buildFile(folder, id);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private File buildFile(File folder, String id) {
        return new File(folder.getPath() + SLASH + id + YAML_TYPE);
    }

    void deleteFile(String id, String folderName) throws IOException, URISyntaxException {
        File folder = getFolder(folderName);
        File file = buildFile(folder, id);
        file.delete();
    }

    boolean fileExists(String id, String folderName) throws IOException, URISyntaxException {
        File folder = getFolder(folderName);
        File file = buildFile(folder, id);
        return file.exists();
    }

    void saveYamlConfiguration(YamlConfiguration yamlConfiguration, String id, String folderName) throws IOException, URISyntaxException {
        yamlConfiguration.save(createFileInFolderAndGet(getFolder(folderName), id));
    }

    File getFolder(String name) throws IOException, URISyntaxException {
        File pluginFolder = new File(System.getProperty(CURRENT_DIRECTORY_PROPERTY) + SLASH + PLUGINS_FOLDER_NAME + SLASH + CURRENT_PLUGIN_FOLDER_NAME);
        if(!pluginFolder.exists()){
            pluginFolder.mkdir();
        }
        File folder = new File(pluginFolder.getPath() + SLASH + name);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    List<YamlConfiguration> getAllYamlConfigurationsInFolder(String name) throws IOException, URISyntaxException {
        File folder = getFolder(name);
        return Stream.of(folder.listFiles()).map(file -> YamlConfiguration.loadConfiguration(file)).collect(Collectors.toList());
    }

    Point mapPointFromString(String string) {
        if(Objects.isNull(string)){
            return null;
        }
        String[] splitString = string.split(POINT_STRING_SEPARATOR);
        return buildPoint(splitString);
    }

    private Point buildPoint(String[] splittedString) {
        return Point.Builder.aPoint()
                .world(splittedString[0])
                .x(Integer.valueOf(splittedString[1]))
                .y(Integer.valueOf(splittedString[2]))
                .z(Integer.valueOf(splittedString[3]))
                .build();
    }
}
