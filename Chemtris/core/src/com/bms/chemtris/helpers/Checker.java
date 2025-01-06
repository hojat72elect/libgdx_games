package com.bms.chemtris.helpers;

/**
 * Class to check game pieces.
 */
public class Checker {

    private Storage storage;
    private int score, multiplier;
    private int rows;

    //constructor
    public Checker(Storage s, int size){
        rows = size * size;
        storage = s;
        score = 0;
        multiplier = 1;
    }

    public void checkLevel1(){
        int level1 = storage.level_1.size;

        while (level1 == rows){
            storage.swap(storage.level_1,storage.level_2);
            storage.swap_f(storage.level_1_w,storage.level_2_w);

            storage.swap(storage.level_2,storage.level_3);
            storage.swap_f(storage.level_2_w,storage.level_3_w);

            storage.swap(storage.level_3,storage.level_4);
            storage.swap_f(storage.level_3_w,storage.level_4_w);

            storage.swap(storage.level_4,storage.level_5);
            storage.swap_f(storage.level_4_w,storage.level_5_w);

            level1 = storage.level_1.size;

            score += 1;
            multiplier += 1;
        }
    }

    public void checkLevel2() {
        int level2 = storage.level_2.size;

        while (level2 == rows){

            storage.swap(storage.level_2,storage.level_3);
            storage.swap_f(storage.level_2_w,storage.level_3_w);

            storage.swap(storage.level_3,storage.level_4);
            storage.swap_f(storage.level_3_w,storage.level_4_w);

            storage.swap(storage.level_4,storage.level_5);
            storage.swap_f(storage.level_4_w,storage.level_5_w);

            level2 = storage.level_2.size;

            score += 1;
            multiplier += 1;
        }
    }

    public void checkLevel3(){

        int level3 = storage.level_3.size;

        while (level3 == rows){
            storage.swap(storage.level_3,storage.level_4);
            storage.swap_f(storage.level_3_w,storage.level_4_w);

            storage.swap(storage.level_4,storage.level_5);
            storage.swap_f(storage.level_4_w,storage.level_5_w);

            level3 = storage.level_1.size;

            score += 1;
            multiplier += 1;
        }
    }

    public void checkLevel4(){
        int level4 = storage.level_4.size;

        while (level4 == rows){
            storage.swap(storage.level_4,storage.level_5);
            storage.swap_f(storage.level_4_w,storage.level_5_w);

            level4 = storage.level_4.size;

            score += 1;
            multiplier += 1;
        }
    }

    public void checkLevel5(){
        if(storage.level_5.size == rows){
            storage.clear_row(5);
            ++score;
            ++multiplier;
        }
    }



    public int run(){
        for(int i = 0; i < 3; ++i){
            checkLevel1();
            checkLevel2();
            checkLevel3();
            checkLevel4();
            checkLevel5();
        }

        score *= multiplier;
        multiplier = 1;

        return score;
    }


}
