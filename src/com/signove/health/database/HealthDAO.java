package com.signove.health.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.signove.health.structures.HealthData;

public class HealthDAO {
    public static final String TABLE_NAME = "HEALTH";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_HEARTBEAT = "HEARTBEAT";
    public static final String COLUMN_DEVICE = "DEVICE";
    public static final String COLUMN_DATE = "DATE_MEASUREMENT";
 
 
    public static final String SCRIPT_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HEARTBEAT + " REAL," + COLUMN_DATE + " TEXT,"
            + COLUMN_DEVICE + " TEXT" + ")";
 
    public static final String SCRIPT_DROP_TABLE =  "DROP TABLE IF EXISTS " + TABLE_NAME;
 
 
    private SQLiteDatabase dataBase = null;
 
 
    private static HealthDAO instance;
     
    public static HealthDAO getInstance(Context context) {
        if(instance == null)
            instance = new HealthDAO(context);
        return instance;
    }
    
    /**
     * Singleton pattern.
     * @param context
     */
    private HealthDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }
 
    public void save(HealthData healthData) {
        ContentValues values = contentValues(healthData);
        dataBase.insert(TABLE_NAME, null, values);
    }
 
    public List<HealthData> ListAll() {
        String queryReturnAll = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<HealthData> veiculos = createHealthList(cursor);
 
        return veiculos;
    }
 
    public void delete(HealthData health) {
        String[] whereArgs = {
                String.valueOf(health.getId())
        };
 
        dataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", whereArgs);
    }
    
    public void deleteAll() {
        dataBase.delete(TABLE_NAME, null, null);
    }
    
    public void closeConnection() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close(); 
    }
 
    private List<HealthData> createHealthList(Cursor cursor) {
        List<HealthData> healthDatas = new ArrayList<HealthData>();
        if(cursor == null)
            return healthDatas;
         
        try {
 
            if (cursor.moveToFirst()) {
                do {
 
                    int indexID = cursor.getColumnIndex(COLUMN_ID);
                    int indexHeartbeat = cursor.getColumnIndex(COLUMN_HEARTBEAT);
                    int indexDevice = cursor.getColumnIndex(COLUMN_DEVICE);
                    int indexDate = cursor.getColumnIndex(COLUMN_DATE);
 
                    int id = cursor.getInt(indexID);
                    Double heartbeat = cursor.getDouble(indexHeartbeat);
                    String device = cursor.getString(indexDevice);
                    String date = cursor.getString(indexDate);
 
                    HealthData health = new HealthData(device, heartbeat, convertToDate(date), id);
 
                    healthDatas.add(health);
 
                } while (cursor.moveToNext());
            }
             
        } finally {
            cursor.close();
        }
        return healthDatas;
    }
 
    private ContentValues contentValues(HealthData healthData) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEARTBEAT, healthData.getHeartbeat());
        values.put(COLUMN_DEVICE, healthData.getDevice());
        values.put(COLUMN_DATE, convertToString(healthData.getDate()));
 
        return values;
    }

    private String convertToString(Date date) {
        SimpleDateFormat formatBra;     
        formatBra = new SimpleDateFormat("dd/MM/yyyy");  
              
        java.util.Date newDate;
        try {
            newDate = formatBra.parse(date.toString());
            return (formatBra.format(newDate));
        } catch (ParseException e) {
            return "Error when converting the date.";
        }
    }
    
    private Date convertToDate(String date_string) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {  
            date = (java.util.Date)formatter.parse(date_string);
            return date;
        } catch (ParseException e) {
            return null;  
        }  
    }
}
