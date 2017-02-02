package com.cryptonites.homey.cfd;

    // this project will control the home appliances over the voice command of user

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Appliance fan, tv, light;
    private Button fanBtn, tvBtn, lightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fan = new Appliance("fan");
        tv = new Appliance("tv");
        light = new Appliance("light");

        fanBtn = (Button) findViewById(R.id.fan_button);
        tvBtn = (Button) findViewById(R.id.tv_button);
        lightBtn = (Button) findViewById(R.id.light_button);

        fanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplianceControl(fan);
            }
        });

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplianceControl(light);
            }
        });

        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplianceControl(tv);
            }
        });

    }

    public void ApplianceControl(Appliance appliance) {
        appliance.setState(!appliance.getState());

        /*TODO: write a function to call the pi and giver proper instruction to control the device
         * and return the proper response about the execution of that command
        */

        String message = "The " + appliance.getName() + " has " + ((appliance.getState())? "started" : "been switched off" );
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
