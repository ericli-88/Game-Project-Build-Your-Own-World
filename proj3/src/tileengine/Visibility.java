package tileengine;

/**
 * Computes the tiles that can be seen from an avatar position.
 *
 * <p>The renderer keeps the actual world untouched and receives a masked copy instead. This
 * makes the fog-of-war presentation a pure rendering concern: movement, saving, loading, and
 * coin collection continue to operate on the full world.</p>
 */
public final class Visibility {
    /** Default Manhattan-style viewing radius used by the game. */
    public static final int DEFAULT_RADIUS = 8;

    private Visibility() {
        // Utility class.
    }

    /**
     * Returns a copy of {@code world} where every tile outside the avatar's line of sight is
     * replaced with {@link Tileset#NOTHING}. Walls are visible, but they block tiles behind them.
     */
    public static TETile[][] visibleRegion(TETile[][] world, int originX, int originY, int radius) {
        validateWorld(world);
        if (!inBounds(world, originX, originY)) {
            throw new IllegalArgumentException("Avatar position is outside the world.");
        }
        if (radius < 0) {
            throw new IllegalArgumentException("Viewing radius must be non-negative.");
        }

        TETile[][] visible = new TETile[world.length][world[0].length];
        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                visible[x][y] = isVisible(world, originX, originY, x, y, radius)
                        ? world[x][y] : Tileset.NOTHING;
            }
        }
        return visible;
    }

    /** Returns whether a target tile is visible from an origin tile. */
    public static boolean isVisible(TETile[][] world, int originX, int originY,
                                    int targetX, int targetY, int radius) {
        validateWorld(world);
        if (!inBounds(world, originX, originY) || !inBounds(world, targetX, targetY)) {
            return false;
        }
        int dx = targetX - originX;
        int dy = targetY - originY;
        if (dx * dx + dy * dy > radius * radius) {
            return false;
        }

        // Bresenham's line visits the grid cells between the avatar and the target. A wall is
        // included in the visible edge, but anything after that wall is hidden.
        int x = originX;
        int y = originY;
        int stepX = Integer.signum(dx);
        int stepY = Integer.signum(dy);
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);
        int error = absDx - absDy;

        while (x != targetX || y != targetY) {
            int doubledError = 2 * error;
            if (doubledError > -absDy) {
                error -= absDy;
                x += stepX;
            }
            if (doubledError < absDx) {
                error += absDx;
                y += stepY;
            }
            if (x == targetX && y == targetY) {
                break;
            }
            if (Tileset.WALL.equals(world[x][y])) {
                return false;
            }
        }
        return true;
    }

    private static boolean inBounds(TETile[][] world, int x, int y) {
        return x >= 0 && x < world.length && y >= 0 && y < world[0].length;
    }

    private static void validateWorld(TETile[][] world) {
        if (world == null || world.length == 0 || world[0] == null || world[0].length == 0) {
            throw new IllegalArgumentException("World must be a non-empty rectangular grid.");
        }
        int height = world[0].length;
        for (TETile[] column : world) {
            if (column == null || column.length != height) {
                throw new IllegalArgumentException("World must be rectangular.");
            }
        }
    }
}
