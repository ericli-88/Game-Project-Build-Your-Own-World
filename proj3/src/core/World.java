package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.Random;

import static utils.RandomUtils.uniform;

public class World {

    //确定 背景板的 尺寸
    private static final int BACK_HEIGHT = 60;

    private static final int BACK_WIDTH = 70;



    private static final long SEED = 5527594778232254774L;
    private static final Random RANDOM = new Random(SEED);


    //总尺寸，用于计算 占比
    double totalSize;

    //当前尺寸
    double currSize;

    World (){
        currSize = 0;
        totalSize = 1.0* BACK_HEIGHT* BACK_WIDTH;
    }
    /**************************************************************************************************************/
    //第一个nested class： room
    private class Room{

        //宽
        private int w;
        //长
        private int h;
        //中心的x坐标
        private int centerX;
        //中心的y坐标
        private int centerY;


        //Constructor
        Room(int a, int b, int c, int d){
            //宽
            w = a;
            //长
            h = b;
            //中心的x坐标
            centerX = c;
            //中心的y坐标
            centerY = d;
        }

        /**************************************************************************************************************/
        //第一个method： 建立room
        public void add1Room (TETile[][] world){

            //开始画Room
            for (int i = centerX - w/2; i < centerX + w/2; i++){
                for (int j = centerY - h/2; j < centerY + h/2; j++){
                    if (i == centerX - w/2 || i == centerX + w/2 - 1 || j == centerY - h/2 || j == centerY + h/2 - 1){
                        world[i][j] = Tileset.WALL;
                    } else {
                        world[i][j] = Tileset.FLOOR;
                    }
                }
            }


        }
        /**************************************************************************************************************/


        /**************************************************************************************************************/
        //第二个method，确认能不能生成一个room
        public boolean validRoom (TETile[][] world, int w, int h, int cX, int cY){

            //确定这次的起始点
            //一定要确保生成的地方不是 地板或是墙; 也不是边界
            //但凡 有一项符合 直接 false
            if (world[cX][cY].equals(Tileset.FLOOR) || world[cX][cY].equals(Tileset.WALL)
                    || cX == 0 || cX == BACK_WIDTH - 1  || cY == 0 || cY == BACK_HEIGHT - 1){
                return false;
            }

            //check 这个 room会不会和其他房间重叠
            //若重叠 也不行

            //确保 size 不会抄出板
            if(cX - w/2 < 0 || cX + w/2 >= BACK_WIDTH ||
               cY - h/2 < 0 || cY + h/2 >= BACK_HEIGHT){

                return false;
            }

            //最后 check 这个 room会不会和其他房间重叠
            //若重叠 也不行
            for (int i = cX - w/2; i < cX + w/2; i++){
                for (int j = cY - h/2; j < cY + h/2; j++){
                    if (!world[i][j].equals(Tileset.NOTHING)){
                        return false;
                    }
                }
            }

            return true;

        }
        /**************************************************************************************************************/



        /**************************************************************************************************************/
        //Method 3
        //更新 room的参数
        public void updateR (int a, int b, int c, int d){
            //宽
            w = a;
            //长
            h = b;
            //中心的x坐标
            centerX = c;
            //中心的y坐标
            centerY = d;
        }
        /**************************************************************************************************************/







    }
    /**************************************************************************************************************/







    //创自己的世界（总的func）
     public void createWorld (){
         TERenderer ter = new TERenderer();
         ter.initialize(BACK_WIDTH,BACK_HEIGHT);

         //创自己的世界

         TETile[][] world = new TETile[BACK_WIDTH][BACK_HEIGHT];

         //先用Nothing打底
         for (int x = 0; x < world.length; x++) {
             for (int y = 0; y < world[0].length; y++) {
                 //world[x][y] = Tileset.NOTHING;
                 world[x][y] = Tileset.NOTHING;
             }
         }
         //画背景板
         //不断地画room，每多画一个，就和上一个串在一起

         //先确定 room的个数: >= 8个
         int roomNum = uniform(RANDOM,8, 13);

         //计算创造几个rooms了
         int count = 0;


         //记录所有中心的位置
         int precenterX = -1;
         int precenterY = -1;



         //开始loop: 建好几个room
         //若占比小于66%
         while (currSize / totalSize < 0.50 /*|| count < roomNum*/ ){

             //随机生成这个长方形的size (<= 10)
             int w = uniform(RANDOM,13,18);
             int h = uniform(RANDOM,13,18);

             //随机生成这个长方形的中心
             int centerX = RANDOM.nextInt(BACK_WIDTH);
             int centerY = RANDOM.nextInt(BACK_HEIGHT);

             //declare一个Room Variable
             Room room = new Room(w,h,centerX,centerY);

             //找到Valid的size 和 center
             while (! room.validRoom(world, w, h, centerX, centerY)){

                 //Redo

                 //随机生成这个长方形的size (<= 10)
                 w = uniform(RANDOM,13, 19);
                 h = uniform(RANDOM,13, 19);

                 //随机生成这个长方形的中心
                 centerX = RANDOM.nextInt(BACK_WIDTH);
                 centerY = RANDOM.nextInt(BACK_HEIGHT);
             }

             //更新room的参数
             room.updateR(w,h,centerX,centerY);

             //更新 墙和地板的数量
             currSize += 1.0 * w * h;

             //画 Room
             room.add1Room(world);

             //和上一个连接

             if (precenterX != -1){

                 //连接
                 connectR(world,precenterX,precenterY,centerX,centerY);
                 //更新 上一个 center的坐标
                 precenterX = centerX;
                 precenterY = centerY;

             } else {
                 //更新 上一个 center的坐标
                 precenterX = centerX;
                 precenterY = centerY;
             }

             count ++;

         }



         System.out.println(currSize / totalSize*100);
         System.out.println("Count: " + count);
         ter.renderFrame(world);
     }
    /**************************************************************************************************************/



    /**************************************************************************************************************/
    //World's Method 2
    //连接Room
    public void connectR (TETile[][] world, int cx1, int cy1, int cx2, int cy2){
        //选择先横着走，还是竖着走
        int xORy = RANDOM.nextInt(2);

        //若为0， 先横 后竖
        if (xORy == 0){
            //先横
            //永远都是 x1 到 x2
            if (cx1 > cx2){

                char how = '-';
                moveX(world,cx1, cx2, cy1, how);

                //开始调整y
                if (cy1 > cy2){
                    how = '-';
                    moveY(world, cy1, cy2, cx2, how);
                } else if (cy1 < cy2){
                    how = '+';
                    moveY(world, cy1, cy2, cx2, how);
                } //若y已经相等，那就没必要动了

            } else if (cx1 < cx2){

                char how = '+';
                moveX(world,cx1, cx2, cy1, how);

                //开始调整y
                if (cy1 > cy2){
                    how = '-';
                    moveY(world, cy1, cy2, cx2, how);
                } else if (cy1 < cy2){
                    how = '+';
                    moveY(world, cy1, cy2, cx2, how);
                } //若y已经相等，那就没必要动了
            } else {
                //x本来就相等
                //直接做y
                char how;
                //开始调整y
                if (cy1 > cy2){
                    how = '-';
                    moveY(world, cy1, cy2, cx2, how);
                } else if (cy1 < cy2){
                    how = '+';
                    moveY(world, cy1, cy2, cx2, how);
                } //若y已经相等，那就没必要动了
            }




        } else {
            /*************************************/
            //先竖
            //永远都是y1 到 y2
            if (cy1 > cy2){
                char how = '-';
                moveY(world, cy1, cy2, cx1, how);

                //开始调整x
                if (cx1 > cx2){
                    how = '-';
                    moveX(world,cx1, cx2, cy2, how);
                } else if (cx1 < cx2){
                    how = '+';
                    moveX(world,cx1, cx2, cy2, how);
                } //若y已经相等，那就没必要动了


            } else if (cy1 < cy2){

                char how = '+';
                moveY(world, cy1, cy2, cx1, how);

                //开始调整x
                if (cx1 > cx2){
                    how = '-';
                    moveX(world,cx1, cx2, cy2, how);
                } else if (cx1 < cx2){
                    how = '+';
                    moveX(world,cx1, cx2, cy2, how);
                } //若x已经相等，那就没必要动了




            } else {

                char how;
                //开始调整x
                if (cx1 > cx2){
                    how = '-';
                    moveX(world,cx1, cx2, cy2, how);
                } else if (cy1 < cy2){
                    how = '+';
                    moveX(world,cx1, cx2, cy2, how);
                } //若x已经相等，那就没必要动了
            }




        }
    }


    //横着走
    public void moveX (TETile[][] world, int startX, int endX, int startY, char a){
        if (a == '+'){
            for (int i = startX; i <= endX; i++){
                if (world[i][startY] ==  Tileset.NOTHING){
                    currSize ++;
                }
                world[i][startY] = Tileset.FLOOR;

                if (world[i][startY - 1] == Tileset.NOTHING){
                    world[i][startY - 1] = Tileset.WALL;
                    currSize ++;
                }

                if (world[i][startY + 1] == Tileset.NOTHING){
                    world[i][startY + 1] = Tileset.WALL;
                    currSize ++;
                }

            }


        } else {
            for (int i = startX; i >= endX; i--){
                if (world[i][startY] ==  Tileset.NOTHING){
                    currSize ++;
                }
                world[i][startY] = Tileset.FLOOR;

                if (world[i][startY - 1] == Tileset.NOTHING){
                    world[i][startY - 1] = Tileset.WALL;
                    currSize ++;
                }

                if (world[i][startY + 1] == Tileset.NOTHING){
                    world[i][startY + 1] = Tileset.WALL;
                    currSize ++;
                }

            }
        }



    }


    //竖着走
    public void moveY (TETile[][] world, int startY, int endY, int endX, char a){

        if (a == '+'){
            for (int i = startY; i <= endY; i++){

                if (world[endX][i] ==  Tileset.NOTHING){
                    currSize ++;
                }


                world[endX][i] = Tileset.FLOOR;

                if (world[endX - 1][i] == Tileset.NOTHING){
                    world[endX - 1][i] = Tileset.WALL;
                    currSize ++;
                }

                if (world[endX+1][i] == Tileset.NOTHING){
                    world[endX+1][i ] = Tileset.WALL;
                    currSize ++;
                }

            }
        } else {
            for (int i = startY; i >= endY; i--){
                if (world[endX][i] ==  Tileset.NOTHING){
                    currSize ++;
                }

                world[endX][i] = Tileset.FLOOR;

                if (world[endX - 1][i] == Tileset.NOTHING){
                    world[endX-1][i] = Tileset.WALL;
                    currSize ++;
                }

                if (world[endX+1][i] == Tileset.NOTHING){
                    world[endX+1][i] = Tileset.WALL;
                    currSize ++;
                }

            }
        }


    }
    /**************************************************************************************************************/



}
