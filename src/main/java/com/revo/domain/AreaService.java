package com.revo.domain;

import com.revo.domain.exception.AreaNameInUseException;
import com.revo.domain.exception.AreaNotFoundException;
import com.revo.domain.exception.DatabaseException;
import com.revo.domain.exception.UserNotHasArea;
import com.revo.domain.port.AreaRepository;
import com.revo.domain.port.PlayerSupport;
import com.revo.domain.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AreaService {

    private final AreaRepository areaRepository;
    private final PlayerSupport playerSupport;
    private final UserRepository userRepository;

    public AreaService(AreaRepository areaRepository, PlayerSupport playerSupport, UserRepository userRepository) {
        this.areaRepository = areaRepository;
        this.playerSupport = playerSupport;
        this.userRepository = userRepository;
    }

    public List<Area> getAllAreas(){
        try {
            return areaRepository.findAll();
        } catch (DatabaseException e) {
            return new ArrayList<>();
        }
    }

    public void createArea(String UUID, String name){
        User user = getUser(UUID);
        if(existsByName(name)){
            throw new AreaNameInUseException();
        }
        Area area = buildArea(name, user);
        save(area);
    }

    private boolean existsByName(String name){
        return areaRepository.existsByName(name);
    }

    public void deleteArea(String UUID, String name) {
        if(!existsByName(name)){
            throw new AreaNotFoundException();
        }
        removeUsersFromArea(name);
        delete(name);
    }

    private void removeUsersFromArea(String name) {
        List<User> users = getAllUsers();
        users.forEach(target -> {
            if(Objects.equals(target.getArea(), name)){
                target.setArea(null);
                return;
            }
        });
        throw new UserNotHasArea();
    }

    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void delete(String name){
        areaRepository.deleteByName(name);
    }

    public void setStart(String UUID, String name, Point point){
        Area optionalArea = getArea(name)
                .orElseThrow(() -> new AreaNotFoundException());
        updateStartInArea(point, optionalArea);
    }

    private void updateStartInArea(Point point, Area area) {
        area.setStart(point);
        save(area);
    }

    public void setEnd(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            area.setEnd(point);
            try{
                save(area);
            } catch (DatabaseException exception) {
                
            }
            return;
        }
    }

    public void setCheckPoint(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            List<Point> points = area.getCheckPoints();
            points.add(point);
            save(area);
            return;
        }
    }

    public void removeCheckPoint(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            List<Point> points = area.getCheckPoints();
            points.remove(point);
            save(area);
            return;
        }
    }

    private void save(Area area) {
        try {
            areaRepository.save(area);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private Area buildArea(String name, User user) {
        return Area.Builder.anArea()
                .name(name)
                .author(user.getName())
                .build();
    }

    public void joinToArea(String UUID, String areaName){
        User user = getUser(UUID);
        Optional<Area> optionalArea = getArea(areaName);
        if(optionalArea.isEmpty()){
            return;
        }
        Area area = optionalArea.get();
        playerSupport.teleportPlayerToArea(UUID, area.getStart());
        user.setArea(area.getName());
        user.setLastCheckPoint(area.getStart());
        user.setLastLocation(playerSupport.getCurrentUserLocationAsPoint());
    }

    private Optional<Area> getArea(String areaName) {
        return areaRepository.findByName(areaName);
    }

    private User getUser(String UUID) {
        return userRepository.getUserByUUIDOrCreate(UUID);
    }

    public void leaveArea(String UUID){
        User user = getUser(UUID);
        if(Objects.nonNull(user.getArea())){
            user.setArea(null);
            playerSupport.teleportPlayerToLastLocation(UUID);
            return;
        }
    }

    public void reachCheckPoint(String UUID, Point point){
        User user = getUser(UUID);
        Area area = getArea(user.getArea()).get(); //CHECK AREA EXISTS
        area.getCheckPoints().forEach(target -> {
            if(Objects.equals(target, point)){
                user.setLastCheckPoint(point);
            }
        });
    }

    public void win(String UUID){
        User user = getUser(UUID);
        Area area = getArea(user.getArea()).get(); //CHECK AREA EXISTS
        if(Objects.nonNull(area)){
            user.setArea(null);
            playerSupport.teleportPlayerToLastLocation(UUID);
            return;
        }
    }
}
