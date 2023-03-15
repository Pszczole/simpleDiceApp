package com.example.simplediceapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creating connection between our button in layout
        //with their id (rollButton) and type Button
        val button = findViewById<Button>(R.id.rollButton)

        //Function that using method form our Dice class
        //And rolls our dice (Generates random number between 1 - 6
        fun rollDice() {
            val dice = Dice()
            val roll = dice.roll()
            val roll2 = dice.roll(false)
            val crash = findViewById<ConstraintLayout>(R.id.crash_test)
            val screenH = crash?.height
            updateText(roll,roll2)
            updateImg(roll,roll2)
        }
        //Our Listener that is activited on click
        button.setOnClickListener{
            rollDice()
        }


    }


    //Text update on roll (Clicking our button)
    fun updateText(roll:Int, roll2:Int){
        val rollResultTxt = findViewById<TextView>(R.id.rollResultText)
        rollResultTxt.text = "Rolled: ${roll} & ${roll2} "
        val r : Int = (0..255).random()
        val g: Int = (((roll + roll2)/12.0)*255).toInt()
        val b: Int = (((roll * roll2)/36.0)*255).toInt()
        rollResultTxt.setTextColor(Color.rgb(r,g,b))
        Log.i(localClassName,"Text color: R:$r, G:$g, B:$b")
    }

    //Image update on roll (Clicking our button)
    private fun updateImg(roll: Int, roll2: Int){
        val diceImg: ImageView = findViewById(R.id.dice1Img)
        val diceImg2: ImageView = findViewById(R.id.dice2Img)
        diceImg.setImageResource((resolveDrawable(roll)))
        diceImg2.setImageResource((resolveDrawable(roll2)))
    }
    //Function that converts our Int value to Image Resource
    fun resolveDrawable(value: Int): Int{
        return when (value){
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }
    }

    override fun onStart(){
        super.onStart()
        Log.i(localClassName,"onStart")
    }

    override fun onResume(){
        super.onResume()
        Log.i(localClassName,"onResume")
    }

    override fun onPause(){
        super.onPause()
        Log.i(localClassName,"onPause")
    }

    override fun onStop(){
        super.onStop()
        Log.i(localClassName,"onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.i(localClassName,"onDestroy")
    }



}


