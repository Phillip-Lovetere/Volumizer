package badmexican333.volumizer17;

//Created by Phillip E. Lo Vetere summer of 2016(Copyright 2016 Phillip E. Lo Vetere)

//Some assets(namely the app logo) were created with the assistance of Android Asset Studio

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public String currentUser;

    public String currentDevice;

    public String filePath;

    public boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdapterView.OnItemClickListener mainMenu = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> main_menu, View itemView, int position, long id) {

                if (position == 0) {
                    SettingsMenu(itemView);
                }

                if (position == 1) {
                    helpMenu(itemView);
                }
            }
        };

        ListView listView = (ListView) findViewById(R.id.main_menu);
        listView.setOnItemClickListener(mainMenu);

        Switch tweakerSwitch = (Switch) findViewById(R.id.VolumizerSwitch);

        if(savedInstanceState == null)
        {
            Names_Retriever_Runner();
            if(isVolumeTweakerRunning(VolumeTweakerRunner.class))
            {
                tweakerSwitch.toggle();
            }
        }
        else
        {
            currentUser = savedInstanceState.getString("user");
            currentDevice = savedInstanceState.getString("device");
            checked = savedInstanceState.getBoolean("isChecked");
            tweakerSwitch.setChecked(checked);
        }




        //listener for volume tweaker switch
        tweakerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            public void onCheckedChanged(CompoundButton tweakSwitch, boolean isChecked)
            {
                checked = isChecked;
                if(isChecked && !(isVolumeTweakerRunning(VolumeTweakerRunner.class)))
                {
                    start_volume_tweaker();
                }
                else if(!isChecked)
                {
                    Log.v("MainActivity","The switch was turned off");
                    stop_volume_tweaker();
                }
            }
        }

        );





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
			
			//stuff that has been cut out of final publication

            /*case R.id.new_user:
                newUser();
                return super.onOptionsItemSelected(item);


            case R.id.new_device:
                newDevice();
                return super.onOptionsItemSelected(item);

            case R.id.switch_user:
                switchUser();
                return super.onOptionsItemSelected(item);

            case R.id.switch_device:
                switchDevice();
                return super.onOptionsItemSelected(item);

            case R.id.names_display:

                return super.onOptionsItemSelected(item);*/

            case R.id.about:
                aboutScreen();
                return super.onOptionsItemSelected(item);

            case R.id.legal:
                legalScreen();
                return super.onOptionsItemSelected(item);


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void legalScreen() {
        Intent showLegal = new Intent(this, LegalInformation.class);

        startActivity(showLegal);
    }

    public void aboutScreen() {
        Intent showAbout = new Intent(this, About_Screen.class);

        startActivity(showAbout);
    }

    public void SettingsMenu(View view) {
        Intent callSettings = new Intent(MainActivity.this, Settings.class);

        startActivity(callSettings);
    }


    public void helpMenu(View view) {
        Intent callHelpMenu = new Intent(this, helpMenu.class);

        startActivity(callHelpMenu);

    }

    public void newUser() {
        Intent callNewUser = new Intent(this, newUser.class);

        startActivity(callNewUser);
    }

    public void newDevice() {
        Intent callNewDevice = new Intent(this, newDevice.class);

        startActivity(callNewDevice);
    }

    public void switchUser() {
        Intent callSwitchUser = new Intent(this, switchUser.class);

        startActivity(callSwitchUser);
    }

    public void switchDevice() {
        Intent callSwitchDevice = new Intent(this, switchDevice.class);

        startActivity(callSwitchDevice);
    }

    public void start_volume_tweaker()
    {
        Log.v("MainActivity","The method to start volume tweaker was called");

        Intent intent = new Intent(this, VolumeTweakerRunner.class);

        intent.putExtra("username", currentUser);

        intent.putExtra("device_name", currentDevice);

        Log.v("MainActivity","The intent was created successfully");

        startService(intent);

        Log.v("MainActivity","The service was launched");
    }

    public void stop_volume_tweaker()
    {
        Intent intent = new Intent(this, VolumeTweakerRunner.class);

        stopService(intent);
    }

    public void Names_Retriever_Runner() {

        String [] names = new String[2];

        DatabaseReader datagetter = new DatabaseReader();

        names = datagetter.logIn(MainActivity.this);

        currentUser = names[0];

        currentDevice = names[1];

        String successMessage = "User " + names[0] + " logged in with Device " + names[1];

        Toast toast = Toast.makeText(this, successMessage, Toast.LENGTH_LONG);

        toast.show();

    }


    private boolean isVolumeTweakerRunning(Class<?> VolumeTweakerRunner)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(VolumeTweakerRunner.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("user", currentUser);
        savedInstanceState.putString("device", currentDevice);
        savedInstanceState.putBoolean("isChecked", checked);

    }


}