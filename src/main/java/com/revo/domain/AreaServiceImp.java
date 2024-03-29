package com.revo.domain;

import com.revo.domain.exception.AreaConfigurationException;
import com.revo.domain.exception.AreaNameInUseException;
import com.revo.domain.exception.AreaNotFoundException;
import com.revo.domain.exception.DatabaseException;
import com.revo.domain.exception.IsNotCheckPointException;
import com.revo.domain.exception.ReachEndPoint;
import com.revo.domain.exception.UserHasAreaException;
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
        saveArea(area);
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
                saveUser(target);
                return;
            }
        });
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
        saveArea(area);
    }

    @Override
    public void setEnd(String UUID, String name, Point point) {
        updateEndInArea(point, getArea(name));
        return;
    }

    private void updateEndInArea(Point point, Area area) {
        area.setEnd(point);
        saveArea(area);
    }

    @Override
    public void setCheckPoint(String name, Point point) {
        Area area = getArea(name);
        addCheckPointInArea(point, area);
        saveArea(area);
    }

    private void addCheckPointInArea(Point point, Area area) {
        List<Point> points = area.getCheckPoints();
        points.add(point);
    }

    @Override
    public void removeCheckPoint(String name, Point point) {
        Area area = getArea(name);
        removeCheckPointInArea(point, area);
    }

    private void removeCheckPointInArea(Point point, Area area) {
        List<Point> points = area.getCheckPoints();
        if(isNotInAreaCheckPoint(point, points)){
            throw new IsNotCheckPointException();
        }
        points.remove(point);
        saveArea(area);
    }

    private boolean isNotInAreaCheckPoint(Point point, List<Point> points) {
        return !points.contains(point);
    }

    private void saveArea(Area area) {
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
        if(areaIsNotConfigured(area)){
            throw new AreaConfigurationException();
        }
        if(userHaveArea(user)){
            throw new UserHasAreaException();
        }
        user.setArea(area.getName());
        user.setLastCheckPoint(area.getStart());
        user.setLastLocation(playerSupport.getCurrentUserLocationAsPoint(UUID));
        playerSupport.teleportPlayerToArea(UUID, area.getStart());
        saveUser(user);
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    private boolean areaIsNotConfigured(Area area) {
        return Objects.isNull(area.getStart()) || Objects.isNull(area.getEnd()) || Objects.isNull(area.getFloor());
    }
    @Override
    public Area getArea(String areaName) {
        return areaRepository.findByName(areaName)
                .orElseThrow(() -> new AreaNotFoundException());
    }

    private User getUser(String UUID) {
        return userRepository.getUserByUUIDOrCreate(UUID);
    }

    @Override
    public void leaveArea(String UUID) {
        User user = getUser(UUID);
        if (userHaveArea(user)) {
            user.setArea(null);
            saveUser(user);
            playerSupport.teleportPlayerToLastLocation(UUID);
            return;
        }
        throw new UserHasNotAreaException();
    }

    private boolean userHaveArea(User user) {
        return Objects.nonNull(user.getArea());
    }

    @Override
    public void reachCheckPoint(String UUID, Point point) {
        User user = getUser(UUID);
        Area area = getArea(user.getArea());
        area.getCheckPoints().forEach(target -> {
            if (reachNewCheckPoint(point, user, target)) {
                user.setLastCheckPoint(point);
                saveUser(user);
                throw new ReachEndPoint();
            }
        });
    }

    private boolean reachNewCheckPoint(Point point, User user, Point target) {
        return target.equals(point) && !user.getLastCheckPoint().equals(point);
    }

    @Override
    public void win(String UUID) {
        User user = getUser(UUID);
        Area area = getArea(user.getArea());
        if (Objects.nonNull(area)) {
            user.setArea(null);
            saveUser(user);
            playerSupport.teleportPlayerToLastLocation(UUID);
        }
    }

    @Override
    public void setFloor(String name, int floor) {
        Area area = getArea(name);
        area.setFloor(floor);
        saveArea(area);
    }

    @Override
    public boolean touchFloor(String UUID, Point point) {
        User user = getUser(UUID);
        Area area = getArea(user.getArea());
        if(isOnFloorOrUnder(point.getY(), area.getFloor())){
            playerSupport.teleportPlayerToLastCheckPoint(user.getUUID());
            return true;
        }
        return false;
    }

    private boolean isOnFloorOrUnder(int currentY, int floor) {
        return currentY <= floor;
    }
}
