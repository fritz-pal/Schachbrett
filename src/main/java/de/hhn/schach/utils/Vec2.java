package de.hhn.schach.utils;

public class Vec2 {
    private final int X;
    private final int Y;

    public Vec2(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new IllegalArgumentException("Coordinate is out of bounds (" + x + ", " + y + ")");
        }
        this.X = x;
        this.Y = y;
    }

    public Vec2(String name) {
        if (!Vec2.isValidName(name)) {
            throw new IllegalArgumentException("Name is not name of a valid spot on the Field");
        }
        name = name.toLowerCase();
        this.Y = (int) name.charAt(0) - (int) 'a';
        this.X = (int) name.charAt(1) - (int) '1';
    }

    private static boolean isValidName(String name) {
        if (name.length() == 2) {
            name = name.toLowerCase();
            int c1 = (int) name.charAt(0) - (int) 'a';
            int c2 = (int) name.charAt(1) - (int) '1';
            return isInBounds(c1, c2);
        }
        return false;
    }

    public static boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public String getName() {
        String returnValue = "";
        returnValue += (char) ((int) 'a' + this.Y);
        returnValue += (char) ((int) '1' + this.X);
        return returnValue;
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int hashCode() {
        return this.Y * 8 + this.X;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vec2 vec2 && vec2.getX() == this.X && vec2.getY() == this.Y;
    }
}