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

import aiou.muslim.mttech.Models.CitiesModel;

public class DBManager {

    public String DATABASE_NAME = "cities.sqlite";
    static final int DATABASE_VERSION = 1;
    final Context context;
    SQLiteDatabase db;
    DatabaseHelper DBHelper;

    public DBManager(Context ctx) {
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
    public DBManager open() throws SQLException {
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

    public List<CitiesModel> getCities(String country) {
        List<CitiesModel> data = new ArrayList<>();
        String[] selection = {country};
        String query = "select * from citiestbl where country = ?";
        Cursor cursor = db.rawQuery(query, selection);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                CitiesModel conversationModel = new CitiesModel();
                conversationModel.setId(cursor.getInt(0));
                conversationModel.setCountry(cursor.getString(1));
                conversationModel.setCity(cursor.getString(2));
                conversationModel.setLatitude(cursor.getString(3));
                conversationModel.setLongitude(cursor.getString(4));
                data.add(conversationModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public List<String> getCountries() {
        List<String> data = new ArrayList<>();
        data.add("Country:");
        String query = "select distinct country from citiestbl";
        Cursor cursor = db.rawQuery(query, null);
        if (data.size() > 0) {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

}


