package com.gungunpriatna.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gungunpriatna.mynotesapp.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.gungunpriatna.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.gungunpriatna.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.gungunpriatna.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;

public class NoteHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    private NoteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    //init database
    public static NoteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
    }

    //open koneksi db
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    //tutup koneksi db
    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    //get Data
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);

        cursor.moveToFirst();
        Note note;
        if (cursor.getCount() > 0) {
            do {

                note = new Note();
                //set id
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                //set title
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                //set description
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                //SET DATE
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                //tambahkan ke arraylist
                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        }

        cursor.close();
        return arrayList;
    }

    //insert data
    public long insertNote(Note note) {
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());

        //insert ke table
        return database.insert(DATABASE_TABLE, null, args);
    }

    //update data
    public int updateNote(Note note) {
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());

        //update data berdasarkan id
        return database.update(DATABASE_TABLE, args, _ID + "= '" + note.getId() + "'", null);
    }

    //hapus data
    public int deleteNote(int id) {
        return database.delete(DATABASE_TABLE, _ID + " = '" + id + "'", null);
    }

}
