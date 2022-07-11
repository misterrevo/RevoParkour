package com.revo.domain;

import com.revo.domain.exception.AreaNameInUseException;
import com.revo.domain.exception.AreaNotFoundException;
import com.revo.domain.exception.DatabaseException;
import com.revo.domain.exception.UserHasNotAreaException;
import com.revo.domain.port.AreaRepository;
import com.revo.domain.port.AreaService;
import com.revo.domain.port.PlayerSupport;
import com.revo.domain.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AreaServiceImp implements AreaService {
    private final AreaRepository areaRepository;
    private final PlayerSupport playerSupport;
    private final UserRepository userRepository;

    public AreaServiceImp(AreaRepository areaRepository, PlayerSupport playerSupport, UserRepository userRepository) {
        this.areaRepository = areaRepository;
        this.playerSupport = playerSupport;
        this.userRepository = userRepository;
    }

    @Override
    public List<Area> getAllAreas() {
        try {
            return areaRepository.findAll();
        } catch (DatabaseException exception) {
            return new ArrayList<>();
        }
    }

    @Override
    public void createArea(String UUID, String name) {
        User user = getUser(UUID);
        if (existsByName(name)) {
            throw new AreaNameInUseException();
        }
        Area area = buildArea(name, user);
        save(area);
    }

    private boolean existsByName(String name) {
        return areaRepository.existsByName(name);
    }

    @Override
    public void deleteArea(String name) {
        if (!existsByName(name)) {
            throw new AreaNotFoundException();
        }
        removeUsersFromArea(name);
        delete(name);
    }

    private void removeUsersFromArea(String name) {
        List<User> users = getAllUsers();
        users.forEach(target -> {
            if (Objects.equals(target.getArea(), name)) {
                target.setArea(null);
                return;
            }
        });
        throw new UserHasNotAreaException();
    }

    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void delete(String name) {
        areaRepository.deleteByName(name);
    }

    @Override
    public void setStart(String UUID, String name, Point point) {
        Area area = getArea(name);
        updateStartInArea(point, area);
    }

    private void updateStartInArea(Point point, Area area) {
        area.setStart(point);
        save(area);
    }

    @Override
    public void setEnd(String UUID, String name, Point point) {
        updateEndInArea(point, getArea(name));
        return;
    }

    private void updateEndInArea(Point point, Area area) {
        area.setEnd(point);
        save(area);
    }

    @Override
    public void setCheckPoint(String UUID, String name, Point point) {
        Area area = getArea(name);
        addCheckPointInArea(point, area);
        save(area);
    }

    private void addCheckPointInArea(Point point, Area area) {
        List<Point> points = area.getCheckPoints();
        points.add(point);
    }

    @Override
    public void removeCheckPoint(String UUID, String name, Point point) {
        Area area = getArea(name);
        removeCheckPointInArea(point, area);
    }

    private void removeCheckPointInArea(Point point, Area area) {
        List<Point> points = area.getCheckPoints();
        points.remove(point);
        save(area);
    }

    private void save(Area area) {
        areaRepository.save(area);
    }

    private Area buildArea(String name, User user) {
        return Area.Builder.anArea()
                .name(name)
                .author(user.getName())
                .build();
    }

    @Override
    public void joinToArea(String UUID, String areaName) {
        User user = getUser(UUID);
        Area area = getArea(areaName);
        playerSupport.teleportPlayerToArea(UUID, area.getStart());
        user.setArea(area.getName());
        user.setLastCheckPoint(area.getStart());
        user.setLastLocation(playerSupport.getCurrentUserLocationAsPoint(UUID));
    }

    private Area getArea(String areaName) {
        return areaRepository.findByName(areaName)
                .orElseThrow(() -> new AreaNotFoundException());
    }

    private User getUser(String UUID) {
        return userRepository.getUserByUUIDOrCreate(UUID);
    }

    @Override
    public void leaveArea(String UUID) {
        User user = getUser(UUID);
        if (Objects.nonNull(user.getArea())) {
            user.setArea(null);
            playerSupport.teleportPlayerToLastLocation(UUID);
            return;
        }
    }

    @Override
    public void reachCheckPoint(String UUID, Point point) {
        User user = getUser(UUID);
        Area area = getArea(user.getArea());
        area.getCheckPoints().forEach(target -> {
            if (Objects.equals(target, point)) {
                user.setLastCheckPoint(point);
            }
        });
    }

    @Override
    public void win(String UUID) {
        User winUser = getUser(UUID);
        Area currentArea = getArea(winUser.getArea());
        getAllUsers().forEach(user -> {
            Area area = getArea(user.getArea());
            if (Objects.nonNull(area) && isCurrentArea(area, currentArea)) {
                user.setArea(null);
                playerSupport.teleportPlayerToLastLocation(UUID);
                return;
            }
        });
    }

    private boolean isCurrentArea(Area area, Area currentArea) {
        return Objects.equals(area.getName(), currentArea.getName());
    }
}
