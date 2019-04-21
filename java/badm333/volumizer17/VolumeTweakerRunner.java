package badmexican333.volumizer17;

//copyright 2016 Phillip E. Lo Vetere

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;
import android.media.MediaRouter;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by badme_000 on 8/2/2016.
 */
public class VolumeTweakerRunner extends IntentService {

    public static final String USERNAME = "username";

    public static final String DEVICE_NAME = "device_name";

    public int changesRunning = 0;

    public String username;

    public String deviceName;

    public static final String TAG = "VolumeTweakerRunner";

    public FileObserver observer;

    protected SQLiteDatabase db;

    protected SQLiteOpenHelper databaseHelper;

    protected Handler handler;

    protected Handler mainThreadHandler;

    AudioManager player;

    public boolean isGood = true;

    protected String songName = "";

    protected int PreviousVolumeValue = 0;

    int observerInstances = 0;

    CountDownTimer timer;

    public int rolloverVolumeChange = -100;

    public int savedVolumeChange = 0;

    public boolean isFirstSong = true;

    public boolean isCursorGood;

    public int oldVolume;

    public int savedVolumeLevel;

    public int lastChange;

    public int changeNumber;

    public int currentVolumeLevel;

    public int ChangesRequired;

    final int[] secondsWait = new int[1];

    public String LastSongName;

    public boolean isFirstSong2 = true;

    public int newVolume = 0;

    public VolumeTweakerRunner()
    {
        super("VolumeTweakerRunner");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mainThreadHandler = new Handler();

        Toast toast = Toast.makeText(getApplicationContext(), "Volume Tweaker Started", Toast.LENGTH_SHORT);

        toast.show();

        Log.v(TAG, "the volume tweaker started successfully");

        databaseHelper = new VolumizerDatabaseHelper(this);

        db = databaseHelper.getReadableDatabase();

        username = intent.getStringExtra(USERNAME);

        deviceName = intent.getStringExtra(DEVICE_NAME);

        Cursor fileCursor = db.query("DEVICES", new String[]{"DEVICE_NAME"},
                "USERNAME = ?",
                new String[]{"FILEPATH"},
                null,null,null);

        secondsWait[0] = 20000;

        //final String observedPath = android.os.Environment.getExternalStorageDirectory().toString() + "/legacy/Music/";

        player = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        final String[] observedPath = new String[1];

        if (fileCursor.moveToFirst())
        {
            observedPath[0] = fileCursor.getString(0);
        }
        else
        {
            observedPath[0] = " ";
        }

        fileCursor.close();

        observer = new FileObserver(observedPath[0]) {

            @Override
            public void onEvent(int event, final String file) {

                event &= FileObserver.ALL_EVENTS;
                switch (event) {
                    //case FileObserver.OPEN:
                    case FileObserver.ACCESS:

                        if (player.isMusicActive() && ((!(file.equals(songName))) || (songName.equals("")))) {
                            ++observerInstances;
                            songName = file + "";

                            if(observerInstances <= 1) {
                                {
                                    mainThreadHandler.post(new Runnable() {
                                        public void run() {
                                            new CountDownTimer(200, 200) {
                                                public void onTick(long millisUntilFinished) {
                                                }

                                                public void onFinish() {
                                                    --observerInstances;
                                                    ++changesRunning;

                                                    if(changesRunning > 1)
                                                    {
                                                        isGood = false;
                                                    }
                                                    onConditionsMet();
                                                }
                                            }.start();
                                        }
                                    });
                                }
                            }
                            else
                            {
                                --observerInstances;
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        };

        Cursor cursor = db.query("USERS",
                new String[] {"CHANGES_REQUIRED"},
                "USERNAME = ?",
                new String[] {username},
                null,null,null);

        if(cursor.moveToFirst() && cursor.getCount() != 0)
        {
            ChangesRequired = cursor.getInt(0);
        }
        else
        {
            ChangesRequired = 1;
        }

        cursor.close();

        Log.v(TAG, "Service trying to watch " + observedPath[0]);

        observer.startWatching();

        return START_REDELIVER_INTENT;

        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        handler = new Handler();
    }




    public void onConditionsMet()
    {
                        mainThreadHandler.post(new Runnable() {
                            public void run()
                            {
                                if (!isFirstSong) {
                                    isGood = false;
                                    newVolume = player.getStreamVolume(AudioManager.STREAM_MUSIC);

                                    if(rolloverVolumeChange == -100)
                                    {
                                        rolloverVolumeChange = 0;
                                    }

                                    rolloverVolumeChange += newVolume - oldVolume;

                                    //timer.cancel();
                                    //if(!isFirstSong) {
                                        GatherChangeVolumeOnFinish(username, deviceName, LastSongName, isCursorGood, oldVolume, savedVolumeLevel, lastChange, changeNumber);

                                    LastSongName = songName;
                                    //}
                                    //Log.v(TAG,"Volumizer cancelled");
                                    Log.v(TAG,"Volumizer finishing");

                                }
                            }
                        });

        mainThreadHandler.post(new Runnable() {
            public void run()
            {
                new CountDownTimer(100,100)
                {
                    public void onTick(long millisUntilFinished)
                    {

                    }

                    public void onFinish()
                    {
                        ++changesRunning;
                        GatherChangeVolume(username, deviceName, songName);
                        changesRunning = 0;
                    }

                }.start();
            }
        });



    }

    protected void GatherChangeVolume(final String username,final String deviceName,final String SongName)
    {
        if(!isFirstSong)//if this isn't the first song played
        {


            //call class timer usually called using global variables with saved information, so the information for the last song is saved whenever the next one is played.

            /*mainThreadHandler.post(new Runnable() {
                public void run() {
                    Log.v(TAG,"The volume tweaker instance cancelled");
                }
            });*/

            --changesRunning;
        }
        else
        {
            isFirstSong = false;
        }

        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "The volume tweaker is working");

                if(SongName != null)
                {
                    Log.v(TAG,"The song name is " + SongName);
                }
            }

        });

        Cursor cursor = db.query("VOLUME_LEVELS",
                new String[]{"VOLUME_LEVELS","LAST_CHANGE","CHANGE_NUMBER"},
                "USERNAME = ? AND DEVICE_NAME = ? AND SONG_NAMES = ?",
                new String[] {username, deviceName, SongName},
                null,null,null);

        if(cursor.moveToFirst() && cursor.getCount() != 0) {

            currentVolumeLevel = cursor.getInt(0);

            lastChange = cursor.getInt(1);

            changeNumber = cursor.getInt(2);

            //use below comment code with some additional stuff if android doesn't automatically handle requests to set volume greater than the maximum

            if(rolloverVolumeChange != -100) {

                if (((currentVolumeLevel + rolloverVolumeChange) > 0)) { //&& (currentVolumeLevel < player.getStreamMaxVolume(AudioManager.STREAM_MUSIC))) {

                    if(currentVolumeLevel + rolloverVolumeChange <= player.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                        player.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumeLevel + rolloverVolumeChange, AudioManager.FLAG_SHOW_UI);
                    }
                    else
                    {
                        player.setStreamVolume(AudioManager.STREAM_MUSIC, player.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
                    }
                } else {
                    mainThreadHandler.post(new Runnable() {
                        public void run()
                        {
                            Log.v(TAG,"The volume would have been muted");
                        }
                    });
                    player.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_SHOW_UI);
                }
            }
            else
            {
                    player.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumeLevel, AudioManager.FLAG_SHOW_UI);
            }
            isCursorGood = true;

            savedVolumeLevel = cursor.getInt(0);
        }
        else
        {
            isCursorGood = false;
            savedVolumeLevel = 0;
            lastChange = 0;
            changeNumber = 0;
        }

        oldVolume = player.getStreamVolume(AudioManager.STREAM_MUSIC);

        cursor.close();
    }



    protected void GatherChangeVolumeOnFinish(final String username,final String deviceName,final String SongName, boolean isCursorGood,final int oldVolume,final int savedVolumeLevel, int lastChange, int changeNumber)
    {

        boolean songAdjustmentCancellation;//used to tell if user turned volume up on one song, then turned it down on another(or vise versa, or if they were just exact opposites)
        //after both volume records were initialized; if they were, the second change isn't recorded.


        ContentValues volumeValues = new ContentValues();

        newVolume = player.getStreamVolume(AudioManager.STREAM_MUSIC);

        if(savedVolumeChange == (newVolume - PreviousVolumeValue)) //if the user makes a change for one song, then the exact opposite for the other, the changeNumber stays the same for the second song(allows you to correct for improper volume adjustment).
        {
            songAdjustmentCancellation = true;
        }
        else
        {
            songAdjustmentCancellation = false;

            if((newVolume - PreviousVolumeValue) == lastChange)
            {
                ++changeNumber;
            }
            else
            {
                changeNumber = 0;
                lastChange = (newVolume - PreviousVolumeValue);
            }
        }


        if(!isCursorGood) {

            volumeValues.put("VOLUME_LEVELS", newVolume);

            volumeValues.put("SONG_NAMES", SongName);

            volumeValues.put("USERNAME", username);

            volumeValues.put("DEVICE_NAME", deviceName);

            volumeValues.put("LAST_CHANGE", 0);

            volumeValues.put("CHANGE_NUMBER",0);

            db.insert("VOLUME_LEVELS", null, volumeValues);
        }
        else if(!songAdjustmentCancellation)
        {

            if(changeNumber >= ChangesRequired)
            {
                volumeValues.put("VOLUME_LEVELS", (savedVolumeLevel + lastChange));

                volumeValues.put("CHANGE_NUMBER", 0);
            }
            else
            {
                volumeValues.put("LAST_CHANGE", lastChange);

                volumeValues.put("CHANGE_NUMBER", changeNumber);
            }

            db.update("VOLUME_LEVELS",
                    volumeValues,
                    "USERNAME = ? AND DEVICE_NAME = ? AND SONG_NAMES = ?",
                    new String[] {username, deviceName, SongName});
        }

        savedVolumeChange = (newVolume - PreviousVolumeValue);

        PreviousVolumeValue = newVolume;

        Log.v(TAG,"The volume tweaker ran");
    }

    public String getName()
    {
        return "VolumeTweakerRunner";
    }

    @Override
    public void onDestroy()
    {
        observer.stopWatching();
        //db.close(); do NOT put this back in; the garbage collector gets this automatically, and for some reason, this breaks the app.

        Log.v(TAG,"The service stopped.");

    }





}
