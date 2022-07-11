package com.revo.domain.port;

import com.revo.domain.Area;
import com.revo.domain.Point;

import java.util.List;

public interface AreaService {
    List<Area> getAllAreas();

    void createArea(String UUID, String name);

    void deleteArea(String UUID, String name);

    void setStart(String UUID, String name, Point point);

    void setEnd(String UUID, String name, Point point);

    void setCheckPoint(String UUID, String name, Point point);

    void removeCheckPoint(String UUID, String name, Point point);

    void joinToArea(String UUID, String areaName);

    void leaveArea(String UUID);

    void reachCheckPoint(String UUID, Point point);

    void win(String UUID);
}
