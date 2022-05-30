package com.revo.domain;

import com.revo.domain.port.AreaRepositoryPort;
import com.revo.domain.port.PlayerSupportPort;
import com.revo.domain.port.UserRepositoryPort;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AreaService {

    private final AreaRepositoryPort areaRepository;
    private final PlayerSupportPort playerSupport;
    private final UserRepositoryPort userRepository;

    public AreaService(AreaRepositoryPort areaRepository, PlayerSupportPort playerSupport, UserRepositoryPort userRepository) {
        this.areaRepository = areaRepository;
        this.playerSupport = playerSupport;
        this.userRepository = userRepository;
    }

    public List<Area> getAllAreas(){
        return areaRepository.findAll();
    }

    public void createArea(String UUID, String name){
        User user = getUser(UUID);
        if(existsByName(name)){
            playerSupport.sendAreaNameInUseMessage(UUID, name);
            return;
        }
        Area area = buildArea(name, user);
        save(area);
        playerSupport.sendAreaCreateMessage(UUID, name);
    }

    private boolean existsByName(String name) {
        return areaRepository.existsByName(name);
    }

    public void deleteArea(String UUID, String name){
        if(!existsByName(name)){
            playerSupport.sendInvalidAreaMessage(UUID, name);
            return;
        }
        removeUsersFromArea(name);
        delete(name);
        playerSupport.sendDeleteAreaMessage(UUID, name);
    }

    private void removeUsersFromArea(String name) {
        List<User> users = getAllUsers();
        users.forEach(target -> {
            if(Objects.equals(target.getArea(), name)){
                target.setArea(null);
                playerSupport.sendAreaDeleteByAdminMessage(target.getUUID(), name);
                playerSupport.teleportPlayerToLastLocation(target.getUUID());
            }
        });
    }

    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void delete(String name) {
        areaRepository.deleteByName(name);
    }

    public void setStart(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            area.setStart(point);
            save(area);
            playerSupport.sendSetStartMessage(UUID, name, point);
            return;
        }
        playerSupport.sendInvalidAreaMessage(UUID, name);
    }

    public void setEnd(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            area.setEnd(point);
            save(area);
            playerSupport.sendSetEndMessage(UUID, name, point);
            return;
        }
        playerSupport.sendInvalidAreaMessage(UUID, name);
    }

    public void setCheckPoint(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            List<Point> points = area.getCheckPoints();
            points.add(point);
            save(area);
            playerSupport.sendSetCheckPointMessage(UUID, name, point);
            return;
        }
        playerSupport.sendInvalidAreaMessage(UUID, name);
    }

    public void removeCheckPoint(String UUID, String name, Point point){
        Optional<Area> optionalArea = getArea(name);
        if(optionalArea.isPresent()){
            Area area = optionalArea.get();
            List<Point> points = area.getCheckPoints();
            points.remove(point);
            save(area);
            playerSupport.sendRemoveCheckPointMessage(UUID, name, point);
            return;
        }
        playerSupport.sendInvalidAreaMessage(UUID, name);
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

    public void joinToArea(String UUID, String areaName){
        User user = getUser(UUID);
        Optional<Area> optionalArea = getArea(areaName);
        if(optionalArea.isEmpty()){
            playerSupport.sendInvalidAreaMessage(UUID, areaName);
            return;
        }
        Area area = optionalArea.get();
        playerSupport.sendJoinMessage(UUID, areaName);
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
            playerSupport.sendLeaveMessage(UUID, user.getArea());
            playerSupport.teleportPlayerToLastLocation(UUID);
            return;
        }
        playerSupport.sendNotInAreaMessage(UUID);
    }

    public void reachCheckPoint(String UUID, Point point){
        User user = getUser(UUID);
        Area area = getArea(user.getArea()).get(); //CHECK AREA EXISTS
        area.getCheckPoints().forEach(target -> {
            if(Objects.equals(target, point)){
                user.setLastCheckPoint(point);
                playerSupport.sendReachCheckPointMessage(UUID);
            }
        });
    }

    public void win(String UUID){
        User user = getUser(UUID);
        Area area = getArea(user.getArea()).get(); //CHECK AREA EXISTS
        if(Objects.nonNull(area)){
            user.setArea(null);
            playerSupport.sendWinMessage(UUID, area.getName());
            playerSupport.teleportPlayerToLastLocation(UUID);
            return;
        }
        playerSupport.sendNotInAreaMessage(UUID);
    }

    public void test(){
        System.out.println("In test in service");
    }
}
