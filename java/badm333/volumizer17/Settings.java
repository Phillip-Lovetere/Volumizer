package badmexican333.volumizer17;

//Copyright 2016 Phillip LoVetere

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        AdapterView.OnItemClickListener settingsMenu = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> settings_menu, View itemView, int position, long id) {
                if(position == 0)
                {
                    backup_restore_launcher(itemView);
                }

               /* if(position == 1)
                {

                }

                if(position == 2)
                {

                }

                if(position == 3)
                {

                }*/

                if(position == 1)
                {
                    musicLocationLauncher(itemView);
                }

                if(position == 2)
                {
                    changeChangesLauncher(itemView);
                }

            }

        };
        ListView listView = (ListView) findViewById(R.id.settings_menu);
        listView.setOnItemClickListener(settingsMenu);
    }

    public void changeChangesLauncher(View view)
    {
        Intent changeLauncher = new Intent(this, ChangesRequiredChanger.class);

        startActivity(changeLauncher);
    }

    public void musicLocationLauncher(View view)
    {
        Intent musicLocLauncher = new Intent(this, MusicLocationSetter.class);
        startActivity(musicLocLauncher);
    }

    public void backup_restore_launcher(View view)
    {
        Intent backupRestoreLauncher = new Intent(this, BackupRestore.class);
        startActivity(backupRestoreLauncher);
    }

    public void password_launcher()
    {
        Intent PasswordLauncher = new Intent(this, SetChangeDeletePassword.class);
        startActivity(PasswordLauncher);
    }

    public void advanced_settings_launcher(View view)
    {
        Intent AdvancedSettingsLauncher = new Intent(this, AdvancedSettings.class);
        startActivity(AdvancedSettingsLauncher);
    }

}

