package stima.modules;

public class Tile {
    public char sign;
    public int x, y;

    public Tile(char c, int x, int y) {
        this.sign = c;
        this.x = x;
        this.y = y;
    }

    public static boolean isSameRow(Tile t1, Tile t2) {
        return t1.y == t2.y;
    }

    public static boolean isSameCol(Tile t1, Tile t2) {
        return t1.x == t2.x;
    }

    public static boolean isSameCoords(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    public static boolean isSameSign(Tile t1, Tile t2) {
        return t1.sign == t2.sign;
    }

    public static boolean isNextTo(Tile t1, Tile t2) {
        return (t1.x <= t2.x + 1) && (t1.x >= t2.x - 1) && (t1.y <= t2.y + 1) && (t1.y >= t2.y - 1);
    }


    public boolean isAt(int x1, int y1) {
        return (this.x == x1) && (this.y == y1);
    }
}
