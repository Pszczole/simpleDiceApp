package com.example.simplediceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.example.simplediceapp.databinding.ActivityMainExtendedBinding
import com.google.android.material.snackbar.Snackbar

class MainActivityExtended : AppCompatActivity() {

    private lateinit var binding: ActivityMainExtendedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(localClassName,"onCreate")
        //Inflate the layout and send it to screen.
        //With view binding there we don't need to call findViewById
        binding = ActivityMainExtendedBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private var numDice: Int = 2 // number of dices that we use in game
    private var isHoldEnabled: Boolean = true //flag: can user hold dice?

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