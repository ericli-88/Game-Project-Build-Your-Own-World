package tileengine;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        resetFont();
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        StdDraw.clear(new Color(0, 0, 0));
        drawTiles(world);
        StdDraw.show();
    }

    /**
     * Draws all world tiles without clearing the canvas or showing the tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void drawTiles(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
    }


    /**
     * Resets the font to default settings. You should call this method before drawing any tiles
     * if you changed the pen settings.
     */
    public void resetFont() {
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
    }

//
//    //ter.partFrame(world);
//    public void partFrame(TETile[][] world,int X, int Y){
//
//        TETile [][] sub = new TETile[world.length][world[0].length];
//
//        //先用Nothing打底
//        for (int x = 0; x < sub.length; x++) {
//            for (int y = 0; y < sub[0].length; y++) {
//                //world[x][y] = Tileset.NOTHING;
//                sub[x][y] = Tileset.NOTHING;
//            }
//        }
//
//        int maximum = 5;
//
//        //开始围绕X，Y写
//
//        for (int x = 0; x < sub.length; x++) {
//            for (int y = 0; y < sub[0].length; y++) {
//                //@source This code was generated by ChatGPT. Because I don't know the rhombus formula
//                if (Math.abs(x - X)+Math.abs(y-Y)<=maximum){
//                    sub[x][y] = world[x][y];
//                }
//            }
//        }
//
//        renderFrame(sub);
//    }


    //先来一个距离公式
    public boolean withinRange(int x, int y, int centerX, int centerY){
        //设置maximum为5

        //如果 当前点 到 中心的距离小于5，就true
        return Math.abs(x - centerX)+Math.abs(y-centerY) <= 5;
    }




    //创一个菱形的新世界
    public void partFrame(TETile[][] world,int X, int Y){
        // Keep the source world intact and render a masked copy. Visibility handles bounds,
        // radius, and wall occlusion in one place, so the toggle cannot corrupt game state.
        renderFrame(Visibility.visibleRegion(world, X, Y, Visibility.DEFAULT_RADIUS));
    }

    /*
     * Legacy implementation retained below for reference while old save files are supported.
     * The renderer now uses Visibility.visibleRegion above; these helpers are no longer on the
     * active rendering path.
     */

    private void legacyPartFrame(TETile[][] world,int X, int Y){
        //设 菱形最长值
        int maximum = 5;

        //第一种：中心点在拐角处/房间里 直接菱形
        //先计算有几个墙连接它
        int num = 0;
        if (world [X+1][Y] == Tileset.WALL){
            num ++;
        }
        if (world [X-1][Y] == Tileset.WALL){
            num ++;
        }
        if (world [X][Y+1] == Tileset.WALL){
            num ++;
        }
        if (world [X][Y-1] == Tileset.WALL){
            num ++;
        }


        //创一个sub
        TETile [][] sub = new TETile[world.length][world[0].length];

        //再创一个作为marked
        //@source This code was generated by ChatGPT.
        //I tried adding a "marked" instance variable to class TETile, but it didn't work.
        Boolean [][] marked = new Boolean[world.length][world[0].length];

        //先用false打底
        for (int x = 0; x < sub.length; x++) {
            for (int y = 0; y < sub[0].length; y++) {
                //world[x][y] = Tileset.NOTHING;
                marked[x][y] = false;
            }
        }

        //

        //先用Nothing打底
        for (int x = 0; x < sub.length; x++) {
            for (int y = 0; y < sub[0].length; y++) {
                //world[x][y] = Tileset.NOTHING;
                sub[x][y] = Tileset.NOTHING;
            }
        }


//        //再把每一块的marked确认为false
//        for (int x = 0; x < sub.length; x++) {
//            for (int y = 0; y < sub[0].length; y++) {
//                //world[x][y] = Tileset.NOTHING;
//                sub[x][y].marked = false;
//            }
//        }

        /***********************************************************************************************************************/
        /***********************************************************************************************************************/
        /***********************************************************************************************************************/
        /***********************************************************************************************************************/

        //开始修改sub，从而满足视野要求

        //若数值小于2 或是 相邻的两个是wall且等于2，直接全看见的菱形
        if (num < 0 /*|| num == 2 &&
                (
                (world [X+1][Y] == Tileset.WALL&& world [X][Y+1] == Tileset.WALL)
                || (world [X+1][Y] == Tileset.WALL&& world [X][Y-1] == Tileset.WALL)
                || (world [X-1][Y] == Tileset.WALL&& world [X][Y-1] == Tileset.WALL)
                || (world [X-1][Y] == Tileset.WALL&& world [X][Y+1] == Tileset.WALL)
                )*/
        ){
            for (int x = 0; x < sub.length; x++) {
                for (int y = 0; y < sub[0].length; y++) {
                    //@source This code was generated by ChatGPT. Because I don't know the rhombus formula
                    if (Math.abs(x - X)+Math.abs(y-Y)<=maximum){
                        sub[x][y] = world[x][y];
                    }
                }
            }
        }

        else{
            //若不行，即 用户处于通道内, 更改sub

            sub[X][Y] = world[X][Y];

            //直接走中心点的四个方位

            //上
            subGenerate (sub, world, X, Y, X, Y+1,marked);
            //下
            subGenerate (sub, world, X, Y, X, Y-1,marked);
            //左
            subGenerate (sub, world, X, Y, X-1, Y,marked);
            //右
            subGenerate (sub, world, X, Y, X+1, Y,marked);
        }


        renderFrame(sub);


    }

    public void subGenerate (TETile[][] sub,TETile[][] world, int centerX, int centerY ,int currentX, int currentY, Boolean [][] marked){
        //若不超过和中心点的距离
        //是wall，就变wall
        //是floor， 就 当前点变floor，再用四个subGenerate

        //public boolean withinRange(int x, int y, int centerX, int centerY)
        //确认在菱形范围内，且没有被标记
        if (withinRange(currentX,currentY, centerX, centerY) && marked[currentX][currentY] == false){
            //确认是什么：
            // 是wall，就assign 结束，

            if (world[currentX][currentY] == Tileset.WALL){
                //标记经过
                marked[currentX][currentY] = true;

                sub[currentX][currentY] = Tileset.WALL;
                return;
            }

            // 反之，如果是floor，就延伸
            else if (world[currentX][currentY] == Tileset.FLOOR || world[currentX][currentY] == Tileset.Coins){

                //标记经过
                marked[currentX][currentY] = true;

                //先吃floor, 再延伸
                sub[currentX][currentY] = world[currentX][currentY];

                //上
                subGenerate (sub, world, centerX, centerY, currentX, currentY+1,marked);
                //下
                subGenerate (sub, world, centerX, centerY, currentX, currentY-1,marked);
                //左
                subGenerate (sub, world, centerX, centerY, currentX-1, currentY,marked);
                //右
                subGenerate (sub, world, centerX, centerY, currentX+1, currentY,marked);
            }


        }



    }





}
