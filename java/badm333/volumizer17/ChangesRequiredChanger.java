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

public class ChangesRequiredChanger extends AppCompatActivity {

    SQLiteDatabase db;

    String user;

    int changesRequired;

    EditText changesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes_required_changer);

        SQLiteOpenHelper databaseHelper = new VolumizerDatabaseHelper(this);

        changesView = (EditText)findViewById(R.id.changesRequiredField);

        db = databaseHelper.getReadableDatabase();

        user = "user1";

        Cursor cursor = db.query("USERS",
                new String[]{"CHANGES_REQUIRED"},
                "USERNAME = ?",
                new String[]{user},
        null,null,null);

        if(savedInstanceState == null) {

            if (cursor.moveToFirst()) {
                changesRequired = cursor.getInt(0);
            } else {
                changesRequired = 2;
            }
        }
        else
        {
            changesRequired = savedInstanceState.getInt("changeNumber");
        }

        changesView.setText(String.valueOf(changesRequired));

        cursor.close();


    }

    public void changeChangesRequired(View view)
    {
        String newChanges = changesView.getText().toString();
        if(!newChanges.equals("")) {

            ContentValues values = new ContentValues();

            values.put("CHANGES_REQUIRED", newChanges);

            db.update("USERS",
                    values,
                    "USERNAME = ?",
                    new String[]{user});


            db.close();

            Toast toast = Toast.makeText(this,"@string/newValue",Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    @Override

    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("changeNumber",(Integer.parseInt(changesView.getText().toString())));
    }
}
