package com.revo.domain;

import java.util.List;
import java.util.Objects;

public class Area {
    private String name;
    private String author;
    private List<Point> checkPoints;
    private Point start;
    private Point end;
    private int floor;

    public Area(String name, String author, List<Point> checkpoints, Point start, Point end, int floor) {
        this.name = name;
        this.author = author;
        this.checkPoints = checkpoints;
        this.start = start;
        this.end = end;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Point> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<Point> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public static final class Builder {
        private String name;
        private String author;
        private List<Point> checkpoints;
        private Point start;
        private Point end;
        private int floor;

        private Builder() {
        }

        public static Builder anArea() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder checkpoints(List<Point> checkpoints) {
            this.checkpoints = checkpoints;
            return this;
        }

        public Builder start(Point start) {
            this.start = start;
            return this;
        }

        public Builder end(Point end) {
            this.end = end;
            return this;
        }

        public Builder floor(int floor) {
            this.floor = floor;
            return this;
        }

        public Area build() {
            return new Area(name, author, checkpoints, start, end, floor);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return floor == area.floor && Objects.equals(name, area.name) && Objects.equals(author, area.author) && Objects.equals(checkPoints, area.checkPoints) && Objects.equals(start, area.start) && Objects.equals(end, area.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, checkPoints, start, end, floor);
    }
}
