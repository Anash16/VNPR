package com.project.vnpr.Activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.project.vnpr.R
import com.project.vnpr.Fragment.VehicleInfo

class Details : AppCompatActivity() {
    private  var number:String?=""
    private  var type:String?=""
    private  var owner :String?=""
    private  var phone :String?=""
    private var model :String?=""
    private var fuel :String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)
        setUpToolbar()
       number=intent.getStringExtra(
           VehicleInfo.KEY_VEHICLE_NUMBER
        )
        owner=intent.getStringExtra(
            VehicleInfo.KEY_VEHICLE_OWNER
        )
      type= intent.getStringExtra(
          VehicleInfo.KEY_VEHICLE_TYPE
        )
        phone=intent.getStringExtra(
            VehicleInfo.KEY_PHONE_NUMBER
        )
        model=intent.getStringExtra(
            VehicleInfo.KEY_VEHICLE_MODEL
        )
        fuel=intent.getStringExtra(
            VehicleInfo.KEY_FUEL_TYPE
        )

        findViewById<TextView>(R.id.vehicle_number).text=number
        findViewById<TextView>(R.id.vehicle_type).text=type
        findViewById<TextView>(R.id.owner_name).text=owner
        findViewById<TextView>(R.id.owner_contact).text=phone
        findViewById<TextView>(R.id.vehicle_model).text=model
        findViewById<TextView>(R.id.fuel_type).text=fuel


    }
    fun setUpToolbar() {
        setSupportActionBar(findViewById(R.id.tool))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    @Override
    override fun onOptionsItemSelected(item: MenuItem) : Boolean{
        when (item.getItemId()) {
            // Respond to the action bar's Up/Home button
             android.R.id.home->{
                 finish()
                 var s:SharedPreferences.Editor =getPreferences(Context.MODE_PRIVATE).edit()
                 s.apply()
                 return true;
             }
            //handle the home button onClick event here.
        }

        return super.onOptionsItemSelected(item);
    }

}