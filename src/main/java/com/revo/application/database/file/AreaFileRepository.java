package com.revo.application.database.file;

import com.revo.domain.Area;
import com.revo.domain.port.AreaRepositoryPort;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AreaFileRepository extends FileRepository implements AreaRepositoryPort {

    private static final String AREA_FOLDER_NAME = "AREA_DATABASE";
    private static final String NAME_PATH = "NAME";
    private static final String AUTHOR_PATH = "AUTHOR";
    private static final String CHECKPOINTS_PATH = "CHECKPOINTS";
    private static final String START_PATH = "START";
    private static final String END_PATH = "END";
    private static final String FLOOR_PATH = "FLOOR";

    @Override
    public List<Area> findAll() {
        List<Area> areas = new ArrayList<>();
        getAllYamlConfigurationsInFolder(AREA_FOLDER_NAME).forEach(yaml -> areas.add(buildArea(yaml)));
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
    public Optional<Area> findByName(String name) {
        if(!existsByName(name)){
            return Optional.ofNullable(null);
        }
        return Optional.of(buildArea(getYamlConfigurationInFolder(name, AREA_FOLDER_NAME)));
    }

    @Override
    public boolean existsByName(String name) {
        return fileExists(name, AREA_FOLDER_NAME);
    }

    @Override
    public void save(Area area) {
        YamlConfiguration yamlConfiguration = getYamlConfigurationInFolder(area.getName(), AREA_FOLDER_NAME);
        yamlConfiguration.set(NAME_PATH, area.getName());
        yamlConfiguration.set(AUTHOR_PATH, area.getAuthor());
        yamlConfiguration.set(CHECKPOINTS_PATH, area.getCheckPoints().stream().map(super::mapPointToString));
        yamlConfiguration.set(START_PATH, mapPointToString(area.getStart()));
        yamlConfiguration.set(END_PATH, mapPointToString(area.getEnd()));
        yamlConfiguration.set(FLOOR_PATH, area.getFloor());
        saveYamlConfiguration(yamlConfiguration, area.getName(), AREA_FOLDER_NAME);
    }

    @Override
    public void deleteByName(String name) {
        deleteFile(name, AREA_FOLDER_NAME);
    }
}
