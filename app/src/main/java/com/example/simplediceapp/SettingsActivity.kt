package com.example.simplediceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.simplediceapp.databinding.ActivityMainExtendedBinding
import com.example.simplediceapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize binding
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Retrive the current settings of the game that were passed with
        //Intent to this Activity
        val numDice = intent.getIntExtra(getString(R.string.num_dice_key),2)
        val isHoldEnabled = intent.getBooleanExtra(getString(R.string.hold_enable_key),true)
        //Apply the current isHoldEnabled setting
        binding.enableHoldSwitch.isChecked = isHoldEnabled
        //Select the current num dices value form the spinner.
        //We assume there are 5 dices max, and 2 min
        if(numDice in 2 .. 5){
            binding.numDiceSpinner.setSelection(numDice - 2)
        }
        //Set the click listener for confirm button
        binding.confirmButton.setOnClickListener{
            //get current selection from the spinner and convert it to integer number
            //that will indicate the number of dices
            val spinnerSelection = binding.numDiceSpinner.selectedItem.toString().toInt()
            // get the current state of the hold enable switch
            val holdEnable = binding.enableHoldSwitch.isChecked
            //Create an empty intent that will be used to pass data to the calling Activity
            val result = Intent().apply {
                //Add the settings to the result intent
                putExtra(getString(R.string.num_dice_key), spinnerSelection)
                putExtra(getString(R.string.hold_enable_key), holdEnable)
            }

            //Set the resultCode to RESULT_OK and attach the data by the result Intent
            setResult(RESULT_OK, result)
            //Close this activity and go back to the previous one
            finish()
        }



    }
}