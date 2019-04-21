package badmexican333.volumizer17;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

/**
 * Created by badme_000 on 7/22/2016.
 */
class VolumizerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "volumizer";

    private static final int DB_VERSION = 1;

    VolumizerDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE VOLUME_LEVELS ("
                + "_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"VOLUME_LEVELS INT, "
                +"SONG_NAMES TEXT,"
                +"USERNAME TEXT,"
                +"DEVICE_NAME TEXT,"
                +"LAST_CHANGE INT,"
                +"CHANGE_NUMBER INT);"

        );

        db.execSQL(
                "CREATE TABLE USERS ("
                +"_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"USERNAME TEXT, "
                +"PASSWORD TEXT,"
                +"CHANGES_REQUIRED INT,"
                +"VOLUMECHANGE_ISON_DEFAULT INT"
                +"VOLCHANGE_RECORDING_DURATION INT);"
        );

        db.execSQL(
                "CREATE TABLE DEVICES ("
                +"_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"USERNAME TEXT, "
                +"DEVICE_NAME TEXT,"
                +"LOGGEDIN INT,"
                +"DEFAULT_LOGIN INT);"
        );

        ContentValues defaultUserValues = new ContentValues();

        defaultUserValues.put("USERNAME", "user1");

        defaultUserValues.put("CHANGES_REQUIRED", 2);

        ContentValues defaultDeviceValues = new ContentValues();

        defaultDeviceValues.put("USERNAME", "user1");

        defaultDeviceValues.put("DEVICE_NAME", "device1");

        defaultDeviceValues.put("LOGGEDIN", "1");

        ContentValues defaultFilePathValues = new ContentValues();//file path stored in devices table

        defaultFilePathValues.put("USERNAME","FILEPATH");

        defaultFilePathValues.put("DEVICE_NAME","/storage/sdcard1/Music/");

        db.insert("USERS", null, defaultUserValues);

        db.insert("DEVICES", null, defaultDeviceValues);

        db.insert("DEVICES",null,defaultFilePathValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}
