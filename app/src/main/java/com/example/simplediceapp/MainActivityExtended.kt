package com.example.simplediceapp

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.simplediceapp.databinding.ActivityMainExtendedBinding
import com.google.android.material.snackbar.Snackbar

class MainActivityExtended : AppCompatActivity() {

    private lateinit var binding: ActivityMainExtendedBinding
    private var numDice: Int = 2 // number of dices that we use in game
    private var isHoldEnabled: Boolean = true //flag: can user hold dice?
    //Array of dice images ids for easy manipulation via loops
    private val diceImgIdsArray = arrayOf(R.id.dice1ImgExtended,R.id.dice2ImgExtended,
    R.id.dice3ImgExtended,R.id.dice4ImgExtended,R.id.dice5ImgExtended)
    //Array to check when the dice is on Hold or Off
    private val diceStateArray = arrayOf(false,false,false,false,false)
    //Array of the values rolled with the dices
    private val diceValuesArray = arrayOf(1,1,1,1,1)
    private var currentPlayer = 0 // Current player id (Two players only)
    private val playerScores = arrayOf(0,0) // Scores of the players
    private var rollCount = 0 // variable that holds the number of rolls for each player
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(localClassName,"onCreate")
        //Inflate the layout and send it to screen.
        //With view binding there we don't need to call findViewById
        binding = ActivityMainExtendedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySettings()
        //Set the click listener of the roll button
        binding.rollButtonExtended.setOnClickListener{
            //Check the state of the button by analysing its text
            if(binding.rollButtonExtended.text != getString(R.string.button_enabled)){
                //The text is not equal to Roll

                binding.rollButtonExtended.text = getString(R.string.button_enabled)
                if(currentPlayer == 1 && rollCount == 0){
                    //if this is the start of the second player. reset turn
                    resetTurn()
                }
            }

        }

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



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //create a menu with menuInflater and R.menu.menu resource
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Handle the selection of menu items
        when(item.itemId){
            R.id.settings_menu -> startSettingActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startSettingActivity(){
        //creates an Intent to start SettingActivity
        val intent :Intent = Intent(this,SettingsActivity::class.java).apply {
            //Add an extra value of type Int and a key stored in string resource with name num_dice_key
            putExtra(getString(R.string.num_dice_key),numDice)
            //Add an extra value of type Boolean and a key stored in string resource with name hold_enable_key
            putExtra(getString(R.string.hold_enable_key), isHoldEnabled)
            //The extra values can be retrieved in the destination activity with the keys
        }

        //Start the SettingsActivity with the launchSettingsActivity variable
        launchSettingActivity.launch(intent)
    }

    private fun changeDiceTInt(img: ImageView, highlight:Boolean){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Change the tiny of the img. The getColor method is available for API >= M(API 23)
            img.imageTintList =
                ColorStateList.valueOf(getColor(if (highlight) R.color.myYellow
                else R.color.white))
        }else{
            //For older API use getColor from resource class
            @Suppress("DEPRECATION")// Annotation to disable the deprecation warning

            img.imageTintList =
                ColorStateList.valueOf(resources.getColor(if (highlight) R.color.myYellow
                else R.color.white))
        }
    }
    private fun resetTurn(){
        // prepare game for the next turn
        for(num in 1 .. 4){
            diceValuesArray[num] = 1 // Reset the values of displayed by each dice
            diceStateArray[num] = false // Reset the "hold state of each dice"
            findViewById<ImageView>(diceImgIdsArray[num]).let {
                changeDiceTInt(it, false) // reset the tint of each dice img view
                it.setImageResource(resolveDrawable(1))
            }
        }

        //Change the game label to show that user has changed
        binding.playerLabel.apply {
            text = getString(R.string.player_id, currentPlayer)
        }
    }


    private fun resetGame(){
        //Reset variables of the game nad Initialize labels and buttons
        currentPlayer = 0
        playerScores[0] = 0
        playerScores[1] = 0
        rollCount = 0
        binding.rollResultTextExtended.text = getString(R.string.click_start)
        binding.playerLabel.visibility = View.INVISIBLE
        binding.rollButtonExtended.text = getString(R.string.button_initial)
        resetTurn()
    }
    private fun applySettings(){
        //Each time new settings are applied the game resets
        binding.rollButtonExtended.isEnabled = true // enable button so the user can click it again
        resetGame()

        val diceToHideBegin = numDice + 1
        //According to the numDice setting -> hide the remaining dice
        for(num in 1 .. 5){
            //Change the visibility of dices -> the view is found by its id
            if(num in diceToHideBegin .. 5){
                findViewById<ImageView>(diceImgIdsArray[num-1]).apply {
                    //Making the visibility to GONE make it disappear and
                    //not takee space in the layout
                    visibility = View.GONE
                    //Just in case disable any clicking on the image by disabling
                    //clickable and focusable attribute
                    isClickable = false
                    isFocusable = false
                }
            }else{
                findViewById<ImageView>(diceImgIdsArray[num - 1]).apply {
                    //Make the image visible
                    visibility = View.VISIBLE
                    //make the image clickable
                    isClickable = isHoldEnabled
                    isFocusable = isHoldEnabled
                }
            }
        }
    }

    //Register a callback for a SettingsActivity result.
    //This code will be called when we return from Settings Activity (after onStart)

    private val launchSettingActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
                Log.i(localClassName,"onActivityResult")
                if(result.resultCode == RESULT_OK){
                    //Preform operations only when the resultCode is RESULT_OK
                    //Retrieve the data from the result.data Intent (only when it's not null)

                    result.data?.let {
                        data ->
                        // This code will be exectued only when result.data is not null,
                        // "data" is the argument of a lambda
                        // Get the numDice and isHoldEnabled settings
                        numDice = data.getIntExtra(getString(R.string.num_dice_key),2)
                        isHoldEnabled = data.getBooleanExtra(getString(R.string.hold_enable_key),true)
                    }
                    //Apply settings
                    applySettings()
                    //Reset the game - each time the user goes to settings activity
                    //and returns back the game will be reset
                    resetGame()
                    //Display a snackbar pop-up to confirm the settings change
                    Snackbar.make(
                        binding.root,
                        "Current settings: numDice: $numDice, isHoldEnabled: $isHoldEnabled",
                        Snackbar.LENGTH_SHORT
                    ).show()
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