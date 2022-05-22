package com.revo.domain;

public class User {

    private String UUID;
    private String name;
    private String area;
    private Point lastCheckPoint;
    private Point lastLocation;

    public User(String UUID, String name, String area, Point lastCheckPoint, Point lastLocation) {
        this.UUID = UUID;
        this.name = name;
        this.area = area;
        this.lastCheckPoint = lastCheckPoint;
        this.lastLocation = lastLocation;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Point getLastCheckPoint() {
        return lastCheckPoint;
    }

    public void setLastCheckPoint(Point lastCheckPoint) {
        this.lastCheckPoint = lastCheckPoint;
    }

    public Point getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
    }

    public static final class Builder {
        private String UUID;
        private String name;
        private String area;
        private Point lastCheckPoint;
        private Point lastLocation;

        private Builder() {
        }

        public static Builder anUser() {
            return new Builder();
        }

        public Builder UUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder area(String area) {
            this.area = area;
            return this;
        }

        public Builder lastCheckPoint(Point lastCheckPoint) {
            this.lastCheckPoint = lastCheckPoint;
            return this;
        }

        public Builder lastLocation(Point lastLocation){
            this.lastLocation = lastLocation;
            return this;
        }

        public User build() {
            return new User(UUID, name, area, lastCheckPoint, lastLocation);
        }
    }
}
