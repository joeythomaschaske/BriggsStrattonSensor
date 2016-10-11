package database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Kyle on 10/11/2016.
 */

public class BriggsDB extends SQLiteOpenHelper {

    // need these attributes for our columns values, they are all supported by SQLite
    private static final String TEXT_TYPE = " TEXT ";
    private static final String COMMA_SEP = " , ";
    private static final String AUTOINCREMENT = " AUTOINCREMENT ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String REAL = " REAL ";
    private static final String UNIQUE = " UNIQUE ";
    private static final String AND = " AND ";
    private static final String BETWEEN = " BETWEEN ";

    private static String dbName = "Briggs.db";
    private static SQLiteDatabase db;
    public static final int DATABASE_VERSION = 3;

    public BriggsDB(Context context){
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Create tables
    public class Engine{
        public static final String TABLE_NAME = "Engine";
        public static final String ID = "_id";
        public static final String ENGINE_HOURS = "EngineHours";
        // MAYBE WANT TYPE OF ENGINE?
        public static final String ENGINE_TYPE = "EngineType";
        public static final String DATE = "Date";
        public static final String TIME = "Time";

        // This will be our table
        private final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Engine.TABLE_NAME + " (" +
                        Engine.ID + " INTEGER PRIMARY KEY" + AUTOINCREMENT + NOT_NULL + UNIQUE + COMMA_SEP +
                        Engine.ENGINE_HOURS + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        Engine.ENGINE_TYPE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        Engine.DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                        Engine.TIME + TEXT_TYPE + NOT_NULL+ ")";

        private final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Engine.TABLE_NAME;
    }
    //Oil table
    public class Oil{

    }
    //Battery table
    public class Battery{

    }
}
