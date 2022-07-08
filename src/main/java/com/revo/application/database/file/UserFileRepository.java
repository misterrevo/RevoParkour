package com.revo.application.database.file;

import com.revo.domain.User;
import com.revo.domain.exception.DatabaseException;
import com.revo.domain.port.UserRepositoryPort;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.revo.application.utils.BukkitUtils.mapPlayer;

public class UserFileRepository extends FileRepository implements UserRepositoryPort {

    private static final String USERS_FOLDER_NAME = "USERS_DATABASE";
    private static final String UUID_PATH = "UUID";
    private static final String NAME_PATH = "NAME";
    private static final String AREA_PATH = "AREA";
    private static final String LAST_CHECKPOINT_PATH = "LAST_CHECKPOINT";
    private static final String LAST_LOCATION_PATH = "LAST_LOCATION";

    @Override
    public User getUserByUUIDOrCreate(String uuid) {
        try {
            YamlConfiguration yamlConfiguration = getYamlConfigurationInFolder(uuid, USERS_FOLDER_NAME);
            if(Objects.nonNull(yamlConfiguration.get(UUID_PATH))){
                return buildUser(yamlConfiguration);
            }
            User user = User.Builder.anUser()
                    .UUID(uuid)
                    .name(mapPlayer(uuid).getName())
                    .build();
            save(user, yamlConfiguration);
        } catch (Exception e) {
            throw new DatabaseException();
        }
        return null;
    }

    private void save(User user, YamlConfiguration yamlConfiguration) throws IOException, URISyntaxException {
        yamlConfiguration.set(UUID_PATH, user.getUUID());
        yamlConfiguration.set(NAME_PATH, user.getName());
        yamlConfiguration.set(AREA_PATH, user.getArea());
        yamlConfiguration.set(LAST_CHECKPOINT_PATH, mapPointToString(user.getLastCheckPoint()));
        yamlConfiguration.set(LAST_LOCATION_PATH, mapPointToString(user.getLastLocation()));
        saveYamlConfiguration(yamlConfiguration, user.getUUID(), USERS_FOLDER_NAME);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            getAllYamlConfigurationsInFolder(USERS_FOLDER_NAME).forEach(yaml -> users.add(buildUser(yaml)));
        } catch (Exception e) {
            throw new DatabaseException();
        }
        return users;
    }

    private User buildUser(YamlConfiguration yamlConfiguration) {
        return User.Builder.anUser()
                .UUID(yamlConfiguration.getString(UUID_PATH))
                .name(yamlConfiguration.getString(NAME_PATH))
                .area(yamlConfiguration.getString(AREA_PATH))
                .lastCheckPoint(mapPointFromString(yamlConfiguration.getString(LAST_CHECKPOINT_PATH)))
                .lastLocation(mapPointFromString(yamlConfiguration.getString(LAST_LOCATION_PATH)))
                .build();
    }
}
