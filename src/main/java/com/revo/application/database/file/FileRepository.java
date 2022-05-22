package com.revo.application.database.file;

import com.revo.domain.Point;

import java.io.File;

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

    File getFileInFolder(String id, String folderName) {
        try {
            File folder = getFolder(folderName);
            File file = new File(folder.getPath() + SLASH + id + YAML_TYPE);
            if(!file.exists()){
                file.createNewFile();
            }
            return file;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
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
