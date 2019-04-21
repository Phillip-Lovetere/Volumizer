package badmexican333.volumizer17;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by badme_000 on 8/2/2016.
 */
public class DatabaseReader {

    public String[] currentUserAndDevice = new String[2];


    public DatabaseReader()
    {
    }

    public String[] logIn(Context context)
    {

        SQLiteOpenHelper databaseHelper = new VolumizerDatabaseHelper(context);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query("DEVICES", new String[]{"USERNAME", "DEVICE_NAME"},
                "LOGGEDIN = ?",
                new String[]{Integer.toString(1)},
                null, null, null);

        if (cursor.moveToFirst()) {
            currentUserAndDevice[0] = cursor.getString(0);
            currentUserAndDevice[1] = cursor.getString(1);

            cursor.close();

            db.close();



            return currentUserAndDevice;


        }
        else
        {
            return new String[] {" "," "};
        }

    }

    public String[] changeLoginUserChoices(String currentUsername)
    {
        return new String[] {""};
    }


    public String[] changeLoginDeviceChoices(String newUsername, Context context)
    {
        SQLiteOpenHelper databaseHelper = new VolumizerDatabaseHelper(context);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query("DEVICES", new String[]{"USERNAME", "DEVICE_NAME"}, null, null, null, null, null);

        return new String[] {"",""};
    }

    public String insertUser(String newUserName)
    {

        return "";
    }

    public String[] insertDevice(String username, String newDeviceName)
    {

        return new String[] {""};
    }






}
