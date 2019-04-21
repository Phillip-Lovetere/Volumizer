package badmexican333.volumizer17;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class newDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);
    }

    public void deviceCreator(View view) {
        SQLiteOpenHelper DatabaseHelper = new VolumizerDatabaseHelper(this);

        SQLiteDatabase db = DatabaseHelper.getWritableDatabase();

        EditText deviceName = (EditText) findViewById(R.id.username_entrance);

        String name = deviceName.getText().toString();

        if (!name.equals("")) {
            ContentValues username_input = new ContentValues();
            username_input.put("username", name);

            db.insert("USERS", null, username_input);

            Toast toast = Toast.makeText(this, "@string/deviceName_success", Toast.LENGTH_SHORT);

            db.close();

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "@string/deviceName_error", Toast.LENGTH_SHORT);

            toast.show();
        }
    }
}
