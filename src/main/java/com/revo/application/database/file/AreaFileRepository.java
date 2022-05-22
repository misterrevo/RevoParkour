package com.revo.application.database.file;

import com.revo.application.utils.BukkitUtils;
import com.revo.domain.Area;
import com.revo.domain.port.AreaRepositoryPort;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.units.qual.A;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        File folder = getFolder(AREA_FOLDER_NAME);
        List<Area> areas = new ArrayList<>();
        Stream.of(folder.listFiles()).forEach(file -> areas.add(mapAreaFromFile(file)));
        return areas;
    }

    private Area mapAreaFromFile(File file) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return buildArea(yamlConfiguration);
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
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public void save(Area area) {

    }

    @Override
    public void deleteByName(String name) {

    }
}
