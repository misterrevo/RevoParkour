package com.revo.application.database.file;

import com.revo.domain.Area;
import com.revo.domain.exception.AreaNotFoundException;
import com.revo.domain.exception.DatabaseException;
import com.revo.domain.port.AreaRepository;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AreaFileRepository extends FileRepository implements AreaRepository {
    private static final String AREA_FOLDER_NAME = "AREA_DATABASE";
    private static final String AUTHOR_PATH = "AUTHOR";
    private static final String CHECKPOINTS_PATH = "CHECKPOINTS";
    private static final String START_PATH = "START";
    private static final String END_PATH = "END";
    private static final String FLOOR_PATH = "FLOOR";
    private static final String NAME_PATH = "NAME";

    @Override
    public List<Area> findAll(){
        List<Area> areas = new ArrayList<>();
        try {
            getAllYamlConfigurationsInFolder(AREA_FOLDER_NAME).forEach(yaml -> areas.add(buildArea(yaml)));
        } catch (Exception exception) {
            throw new DatabaseException();
        }
        return areas;
    }

    private Area buildArea(YamlConfiguration yamlConfiguration) {
        return Area.Builder.anArea()
                .name(yamlConfiguration.getString(NAME_PATH))
                .author(yamlConfiguration.getString(AUTHOR_PATH))
                .checkpoints(yamlConfiguration.getStringList(CHECKPOINTS_PATH).stream().map(super::mapPointFromString).collect(Collectors.toList()))
                .start(mapPointFromString(yamlConfiguration.getString(START_PATH)))
                .end(mapPointFromString(yamlConfiguration.getString(END_PATH)))
                .floor(yamlConfiguration.getInt(FLOOR_PATH))
                .build();
    }

    @Override
    public Optional<Area> findByName(String name){
        try {
            if(!existsByName(name)){
                return Optional.ofNullable(null);
            }
            return Optional.of(buildArea(getYamlConfigurationInFolder(name, AREA_FOLDER_NAME)));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "|" );
            e.printStackTrace();
            throw new DatabaseException();
        }
    }

    @Override
    public boolean existsByName(String name){
        try {
            return fileExists(name, AREA_FOLDER_NAME);
        } catch (Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void save(Area area){
        try{
            saveAreaToYaml(area);
        } catch (Exception exception){
            throw new DatabaseException();
        }
    }

    private void saveAreaToYaml(Area area) throws IOException, URISyntaxException {
        YamlConfiguration yamlConfiguration = getYamlConfigurationInFolder(area.getName(), AREA_FOLDER_NAME);
        yamlConfiguration.set(NAME_PATH, area.getName());
        yamlConfiguration.set(AUTHOR_PATH, area.getAuthor());
        yamlConfiguration.set(CHECKPOINTS_PATH, area.getCheckPoints().stream().map(super::mapPointToString).collect(Collectors.toList()));
        yamlConfiguration.set(START_PATH, mapPointToString(area.getStart()));
        yamlConfiguration.set(END_PATH, mapPointToString(area.getEnd()));
        yamlConfiguration.set(FLOOR_PATH, area.getFloor());
        saveYamlConfiguration(yamlConfiguration, area.getName(), AREA_FOLDER_NAME);
    }

    @Override
    public void deleteByName(String name){
        try {
            deleteFile(name, AREA_FOLDER_NAME);
        } catch (Exception e) {
            throw new DatabaseException();
        }
    }
}
