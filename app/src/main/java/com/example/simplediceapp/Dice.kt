package com.example.simplediceapp

import java.util.*;
/*
This class creates our Dice and we create method
that represents number on wall of dice
 */
class Dice(val numSides: Int = 6) {
    fun roll(isRangesRandom: Boolean = true): Int{
        if(isRangesRandom){
            return (1..numSides).random()
        }else{
            return Random().nextInt(numSides)+1
        }
    }
}