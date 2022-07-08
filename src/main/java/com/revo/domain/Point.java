package com.revo.domain;


import java.util.Objects;

public class Point {
    private Long id;
    private String world;
    private int x;
    private int y;
    private int z;

    public Point(Long id, String world, int x, int y, int z) {
        this.id = id;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public static final class Builder {
        private Long id;
        private String world;
        private int x;
        private int y;
        private int z;

        private Builder() {
        }

        public static Builder aPoint() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder world(String world) {
            this.world = world;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder z(int z) {
            this.z = z;
            return this;
        }

        public Point build() {
            return new Point(id, world, x, y, z);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z && world.equals(point.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }
}
