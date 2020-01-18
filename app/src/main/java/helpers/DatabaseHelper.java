package helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "IRDatabase";
    //public static final String TABLE_NAME = "IRTable";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        Log.w("DATABASE", "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE RawData(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR, data VARCHAR)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS RawData");
        onCreate(db);
    }

    public boolean insertData(String name, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("data", data);
        db.insert("RawData",null, contentValues);
        return true;
    }

    public Cursor getAllRawData() {
        ArrayList<String>[] array_list = new ArrayList[2];

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM RawData", null );

        return res;
    }

    public ArrayList cursorToArrayList(Cursor cr, String column){
        ArrayList<String> arrayList = new ArrayList<String>();

        cr.moveToFirst();
        while(cr.isAfterLast() == false){
            arrayList.add(cr.getString(cr.getColumnIndex(column)));
            cr.moveToNext();
        }

        return arrayList;
    }

    public Integer deleteData (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("RawData",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
}
