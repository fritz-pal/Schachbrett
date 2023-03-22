package de.hhn;

public class Vec2 {
    private final int X;
    private final int Y;

    public Vec2(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new IllegalArgumentException("Coordinate is out of bounds");
        }
        this.X = x;
        this.Y = y;
    }

    // Create Vec2 from name, for Example {0, 0} is "A1"
    public Vec2(String name) {
        if (!Vec2.isValidName(name)) {
            throw new IllegalArgumentException("Name is not name of a valid spot on the Field");
        }
        name = name.toLowerCase();
        this.Y = (int) name.charAt(0) - (int) 'a';
        this.X = (int) name.charAt(1) - (int) '1';
    }

    // Returns name of the Vector, x = 3 and y = 4 is "E4"
    public static String getName(int x, int y) {
        return new Vec2(x, y).getName();
    }

    // Return true if the name is valid like "C2" or "a8"
    private static boolean isValidName(String name) {
        if (name.length() == 2) {
            name = name.toLowerCase();
            int c1 = (int) name.charAt(0) - (int) 'a';
            int c2 = (int) name.charAt(1) - (int) '1';
            return c1 < 8 && c1 >= 0 && c2 < 8 && c2 >= 0;
        }
        return false;
    }

    //Returns the Horizontal Offset between 2 points
    public static int xDiff(Vec2 start, Vec2 finish) {
        return finish.X - start.X;
    }

    //Returns the Vertical Offset between 2 points
    public static int yDiff(Vec2 start, Vec2 finish) {
        return finish.Y - start.Y;
    }

    // Returns name of the Vector, x = 3 and y = 4 is "E4"
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
        return this.getName().toUpperCase();
    }

    @Override
    public int hashCode() {
        return this.Y * 8 + this.X;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vec2 vec2 && vec2.X == this.X && vec2.Y == this.Y;
    }
}
