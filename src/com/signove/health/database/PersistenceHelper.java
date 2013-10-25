package com.signove.health.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class of connection with database.
 * @author Tiago Brasileiro Araujo
 *
 */
public class PersistenceHelper extends SQLiteOpenHelper {
    
    public static final String NAME_DATABASE =  "HEALTHDB";
    public static final int VERSION =  1;
     
    private static PersistenceHelper instance;
     
    private PersistenceHelper(Context context) {
        super(context, NAME_DATABASE, null, VERSION);
    }
     
    public static PersistenceHelper getInstance(Context context) {
        if(instance == null)
            instance = new PersistenceHelper(context);
         
        return instance;
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HealthDAO.SCRIPT_CREATE_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(HealthDAO.SCRIPT_DROP_TABLE);
        onCreate(db);
    }
 
}
