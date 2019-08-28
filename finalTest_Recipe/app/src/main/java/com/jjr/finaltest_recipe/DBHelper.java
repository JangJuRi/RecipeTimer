package com.jjr.finaltest_recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyRecipe.db";
    public static final String RECIPE_TABLE_NAME = "recipe";
    public static final String RECIPE_COLUMN_ID = "id";
    public static final String RECIPE_COLUMN_NAME = "recipe_name";
    public static final String RECIPE_COLUMN_TEXT = "recipe_text";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table recipe " + "(id integer primary key,recipe_name text, recipe_text text)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe");
        onCreate(db);
    }
    public boolean insertRecipe(String recipe_name, String recipe_text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recipe_name", recipe_name);
        contentValues.put("recipe_text", recipe_text);
        db.insert("recipe", null, contentValues);
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recipe where id=" + id + "", null);
        return res;
    }
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, RECIPE_TABLE_NAME);
        return numRows;
    }
    public boolean updateRecipe(Integer id, String recipe_name, String recipe_text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recipe_name",recipe_name );
        contentValues.put("recipe_text", recipe_text);
        db.update("recipe", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public Integer deleteRecipe(Integer id) {  SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("recipe",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }
    public ArrayList getAllRecipe() {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recipe", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(RECIPE_COLUMN_ID))+" "+
                    res.getString(res.getColumnIndex(RECIPE_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}