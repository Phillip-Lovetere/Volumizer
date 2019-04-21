package badmexican333.volumizer17;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MusicLocationSetter extends AppCompatActivity {

    EditText fileView;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_location_setter);

        SQLiteOpenHelper databaseHelper = new VolumizerDatabaseHelper(this);

        db = databaseHelper.getReadableDatabase();

        fileView = (EditText)findViewById(R.id.musicPlaceField);

        if(savedInstanceState != null)
        {
            fileView.setText(savedInstanceState.getString("location"));
        }
        else
        {
            Cursor cursor = db.query("DEVICES",
                    new String[]{"DEVICE_NAME"},
                    "USERNAME = ?",
                    new String[]{"FILEPATH"},
                    null,null,null);

            if(cursor.moveToFirst())
            {
                fileView.setText(cursor.getString(0));
            }
            else
            {
                fileView.setText("");
            }
            cursor.close();
        }

    }

    public void setNewLocation(View view)
    {
        String newLocation = fileView.getText().toString();

        ContentValues LocationSet = new ContentValues();

        LocationSet.put("DEVICE_NAME",newLocation);

        db.update("DEVICES",
                LocationSet,
                "USERNAME = ?",
                new String[]{"FILEPATH"});

        db.close();

        Toast toast = Toast.makeText(this,"@string/newValue",Toast.LENGTH_SHORT);

        toast.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString("location",fileView.getText().toString());
    }


}
