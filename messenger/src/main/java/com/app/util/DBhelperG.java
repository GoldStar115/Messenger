package com.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.model.Chat_Single;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gagandeep on 26 Nov 2015.
 */

public class DBhelperG
{

//    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public static String TableName = ""; // name of table


    private final String userId = "userId";
    private final String message = "message";
    private final String date = "date";
    private final String username = "username";
    private final String imgURL = "imgURL";
    private final String youtube = "youtube";
    private final String fbID = "fbID";
    private final String phNO_ = "phNO_";

    public DBhelperG(Context context, String tableName)
    {
        TableName = tableName;

//        dbHelper = new MyDatabaseHelper(context, tableName);

        database = context.openOrCreateDatabase("DatabaseGroupy", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        createTable(tableName);
    }


    public void createTable(String tableName)
    {
        String DATABASE_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s(userId text,username text,message text,date text,imgURL text,youtube text,fbID text,phNO_ text);", tableName);

        database.execSQL(DATABASE_CREATE);
    }


    public long insertRecords(Chat_Single data)
    {
        ContentValues values = new ContentValues();
        values.put(userId, data.getUserId());
        values.put(message, data.getMessage());

        values.put(date, data.getDate());
        values.put(username, data.getUsername());

        values.put(imgURL, data.getImgURL());
        values.put(youtube, data.getYoutube());

        values.put(fbID, data.getFbID());
        values.put(phNO_, data.getPhNO_());


        return database.insert(TableName, null, values);
    }


    public void dropTable(String TableName)
    {
        try
        {
            database.execSQL("DROP TABLE IF EXISTS " + TableName);
        }
        catch(Exception ex){
            Log.e("Exception ","No Table to Drop");
        }
    }

    public int getSizeOfChat()
    {
        try
        {
            String selectQuery = "SELECT  * FROM " + TableName;

            SQLiteDatabase db = database;
            Cursor cursor = db.rawQuery(selectQuery, null);

            int count = cursor.getCount();

            cursor.close();

            return count;
        }
        catch (Exception ex){
            Log.e("DB exception 1",ex.toString());
            return 0;
        }
    }


    public List<Chat_Single> getTopChatList(int listSize)
    {

        List<Chat_Single> getsetList = new ArrayList<Chat_Single>();

        try
        {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TableName;

            SQLiteDatabase db = database;
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list

            int starting_point = 0;

            if (cursor.getCount() > listSize)
            {
                starting_point = cursor.getCount() - listSize;
            }


            if (cursor.moveToPosition(starting_point))
            {
                do
                {
                    String userIdG = cursor.getString(0);
                    String usernameG = cursor.getString(1);
                    String messageG = cursor.getString(2);
                    String dateG = cursor.getString(3);
                    String imgURLG = cursor.getString(4);
                    String youtubeG = cursor.getString(5);
                    String fbIDG = cursor.getString(6);
                    String phNO_G = cursor.getString(7);


                    Chat_Single dataChat = new Chat_Single(userIdG, messageG, dateG, usernameG, imgURLG, youtubeG, fbIDG, phNO_G);


                    getsetList.add(dataChat);

                } while (cursor.moveToNext());
            }

            cursor.close();
            // return getset list
        }
        catch (Exception ex){
            Log.e("DB exception 2",ex.toString());
        }
        return getsetList;
    }


    public List<Chat_Single> getAfterChatList(int afterIndex)
    {

        List<Chat_Single> getsetList = new ArrayList<Chat_Single>();
        // Select All Query

    try
    {
    String selectQuery = "SELECT  * FROM " + TableName + " LIMIT " + (getSizeOfChat() - afterIndex);

    SQLiteDatabase db = database;
    Cursor cursor = db.rawQuery(selectQuery, null);


    if (cursor.moveToFirst())
    {
        do
        {
            String userIdG = cursor.getString(0);
            String usernameG = cursor.getString(1);
            String messageG = cursor.getString(2);
            String dateG = cursor.getString(3);
            String imgURLG = cursor.getString(4);
            String youtubeG = cursor.getString(5);
            String fbIDG = cursor.getString(6);
            String phNO_G = cursor.getString(7);


            Chat_Single dataChat = new Chat_Single(userIdG, messageG, dateG, usernameG, imgURLG, youtubeG, fbIDG, phNO_G);


            getsetList.add(dataChat);

        } while (cursor.moveToNext());
    }


    cursor.close();
    // return getset list
    }
    catch(Exception ex){
    Log.e("DB exception 3",ex.toString());
    }
        return getsetList;
    }


    public List<Chat_Single> getLastRecords(int afterIndex, int loadMoreTimes)
    {
        List<Chat_Single> getsetList = new ArrayList<>();
        // Select All Query

        try
        {
//        String selectQuery = "SELECT  * FROM " + TableName + " ORDER BY ROWID DESC LIMIT " +  afterIndex;/* offset "+loadMoreTimes*afterIndex*/
//        String selectQuery = "SELECT  * FROM " + TableName + " LIMIT " + afterIndex * loadMoreTimes;
            String selectQuery = "SELECT * FROM " + TableName + " ORDER BY ROWID DESC LIMIT " + afterIndex + "," + loadMoreTimes * afterIndex;


            SQLiteDatabase db = database;
            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst())
            {
                do
                {

                    String userIdG = cursor.getString(0);
                    String usernameG = cursor.getString(1);
                    String messageG = cursor.getString(2);
                    String dateG = cursor.getString(3);
                    String imgURLG = cursor.getString(4);
                    String youtubeG = cursor.getString(5);
                    String fbIDG = cursor.getString(6);
                    String phNO_G = cursor.getString(7);


                    Chat_Single dataChat = new Chat_Single(userIdG, messageG, dateG, usernameG, imgURLG, youtubeG, fbIDG, phNO_G);


                    getsetList.add(dataChat);


                } while (cursor.moveToNext());
            }

            // return getset list


            cursor.close();
        }
        catch(Exception ex){
            Log.e("DB exception 4",ex.toString());
        }
        return getsetList;
    }


}