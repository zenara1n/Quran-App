package aiou.muslim.mttech.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import aiou.muslim.mttech.Models.AzkarModel;

public class DBManagerAzkar {

    public String DATABASE_NAME = "azkar.sqlite";
    static final int DATABASE_VERSION = 2;
    final Context context;
    SQLiteDatabase db;
    DatabaseHelper DBHelper;

    public DBManagerAzkar(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.disableWriteAheadLogging();
        }

    }

    // ---opens the database---
    public DBManagerAzkar open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void createDataBase() throws IOException {

        boolean mDataBaseExist = checkDataBase();

        if (mDataBaseExist) {
            // do nothing - database already exist

        } else {

            DBHelper.getReadableDatabase();
            DBHelper.close();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * This method checks whether database is exists or not
     **/
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = context.getDatabasePath(DATABASE_NAME).getPath()
                    .toString();

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public long copyDataBase() throws IOException {
        String DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath()
                .toString();

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        return length;
    }

    public List<String> getCategories(String lang) {
        List<String> dataar = new ArrayList<>();
        List<String> dataen = new ArrayList<>();
        List<String> outputdata = new ArrayList<>();
        String query = "SELECT DISTINCT cateng,subcat_name from azkarnew";
        Cursor cursor = db.rawQuery(query, null);
        if (dataar.size() > 0) {
            dataar.clear();
        }
        if (dataen.size() > 0) {
            dataen.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                dataar.add(cursor.getString(1));
                dataen.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (lang.equals("ar")){
            outputdata=dataar;
        }else {
            outputdata=dataen;
        }
        return outputdata;
    }

    public List<AzkarModel> getAzkar(String cateng) {
        String[] selection = {cateng};
        List<AzkarModel> data = new ArrayList<>();
        String query = "select * from azkarnew where cateng = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                AzkarModel azkarModel = new AzkarModel();
                azkarModel.setId(cursor.getInt(0));
                azkarModel.setSubcat_name(cursor.getString(1));
                azkarModel.setCategory_name(cursor.getString(2));
                azkarModel.setAzkarfield(cursor.getString(3));
                azkarModel.setFav(cursor.getString(4));
                azkarModel.setCateng(cursor.getString(5));
                azkarModel.setTranslation(cursor.getString(6));
                data.add(azkarModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

}


