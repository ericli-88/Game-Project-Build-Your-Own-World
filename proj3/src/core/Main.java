package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Color.*;
import static utils.RandomUtils.uniform;

public class Main {

    //确定 背景板的 尺寸
    private static final int BACK_HEIGHT = 60;

    private static final int BACK_WIDTH = 60;

    TERenderer ter;


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(BACK_WIDTH,BACK_HEIGHT);
        //向用户展示menu
        menu();
        char c;
        boolean entered = false;
        while (!entered) {
            //目前感觉不需要 while true， menu出现一次就好了

            // hasNextKeyTyped checks if the user has typed a key that we haven't processed.
            // This loop runs until all unprocessed keys are processed.
            // If there are no unprocessed keys, we go back to the outer infinite loop to wait for the next key.
            while (StdDraw.hasNextKeyTyped()) {

                // nextKeyTyped returns the next key to process.
                // Always check hasNextKeyTyped before calling nextKeyTyped.
                // If you call nextKeyTyped and there's no key to process, code will crash!
                c = StdDraw.nextKeyTyped();

                c = Character.toLowerCase(c);

                // Switch statements can be useful to replace long if-else statements!
                switch (c) {

                    //结束
                    case 'q':
                        System.exit(0); // Closes the game window and quits the game.
                        entered = true;
                        break;

                    //开始一个新的世界
                    case 'n':
                        entered = true;
                        //呼叫 输入 seed 的界面
                        long finalSeed = SEEDmenu();
                        //先创建一个背景板
                        //TERenderer ter = new TERenderer();
                        ter.initialize(BACK_WIDTH,BACK_HEIGHT);
                        //创一个世界
                        World x = new World(finalSeed,BACK_HEIGHT,BACK_WIDTH,ter);
                        x.createWorld ();
                        break;



                    case 'l':
                        //从保存文件中加在已保存的游戏
                        entered = true;


                        break;



                }

            }



        }


        // build your own world!
        //World x = new World(SEED,BACK_HEIGHT,BACK_WIDTH,ter);
        //x.createWorld ();

    }


    //Method： 设置菜单
    public static void menu(){


        //设置颜色
        StdDraw.setPenColor(WHITE);

        //设置第一行的字体+大小
        Font CS61B = new Font ("default", 0, 55);
        //使用上述字体
        StdDraw.setFont(CS61B);
        //输出文字
        StdDraw.textLeft(19, 47, "CS61B: BYOW");

        //设置后面几行的字体+大小
        Font option= new Font ("default", 0, 35);
        //使用上述字体
        StdDraw.setFont(option);
        //输出文字
        StdDraw.textLeft(23, 36, "(N) New Game");
        StdDraw.textLeft(23, 30, "(L) Load Game");
        StdDraw.textLeft(23, 24, "(Q) Quit Game");

        //Display
        StdDraw.show();
        //避免闪烁
        StdDraw.pause(2);


    }



    //输入种子的版面
    public static long SEEDmenu(/*TERenderer ter*/){

        //刷新版面
        StdDraw.clear(BLACK);


        //先考虑 怎么吃seed
        //开始吃输入
        boolean getS = false;

        //最后Seed的样子
        String finalSeed = "";
        //用户每输入一个数，就加到finalSeed里
        char c;

        while (true) {

            // hasNextKeyTyped checks if the user has typed a key that we haven't processed.
            // This loop runs until all unprocessed keys are processed.
            // If there are no unprocessed keys, we go back to the outer infinite loop to wait for the next key.
            while (StdDraw.hasNextKeyTyped()) {

                // nextKeyTyped returns the next key to process.
                // Always check hasNextKeyTyped before calling nextKeyTyped.
                // If you call nextKeyTyped and there's no key to process, code will crash!
                c = StdDraw.nextKeyTyped();

                //判断c是不是数字
                //如果是，更新seed
                if (Character.isDigit(c)){
                    finalSeed += c;
                    break;
                }
                //如果不是，那就k按是不是S，如果也不是，就当没发生过，吃下一个
                else{
                    if (Character.toLowerCase(c) == 's'){
                        //把最新的种子输出出去
                        return Long.parseLong(finalSeed);
                    }
                    break;

                }

            }


            //设置颜色
            StdDraw.setPenColor(WHITE);
            //设置第一行的字体+大小
            Font CS61B = new Font ("default", 0, 55);
            //使用上述字体
            StdDraw.setFont(CS61B);
            //输出文字
            StdDraw.textLeft(19, 47, "CS61B: BYOW");

            //设置后面几行的字体+大小
            Font option= new Font ("default", 0, 45);
            //使用上述字体
            StdDraw.setFont(option);
            //输出Prompt文字
            StdDraw.textLeft(10, 35, "Enter     seed     followed     by     S:");

            //输出现有seed
            //设置新颜色
            StdDraw.setPenColor(YELLOW);
            StdDraw.textLeft(10, 25, finalSeed);

            //Display
            StdDraw.show();



        }

    }
}
