package com.jdrake.apps.captest;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by jeremyd on 1/23/2016.
 */
public class ZonesDatabaseHelper extends SQLiteOpenHelper
{
    private final Context fContext;
    public ZonesDatabaseHelper(Context context)
    {
        super(context, "zones.sqlite", null, 1);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        SQLiteStatementSplitter splitter = new SQLiteStatementSplitter(fContext.getResources().openRawResource(R.raw.zones_create));
        try
        {
            Pattern p = Pattern.compile("^(?:BEGIN|COMMIT);$", Pattern.MULTILINE);
            db.beginTransaction();
            for (String stmt : splitter)
            {
                if (!p.matcher(stmt).find())
                    db.execSQL(stmt);
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
            try
            {
                splitter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // this space intentionally left blank
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    private enum StreamState
    {
        START, TABLE, ADD, CHANGE, REMOVE, ROW_ARRAY, ROW
    }

    @Override
    public void onOpen(final SQLiteDatabase db)
    {
        super.onOpen(db);
        new AsyncTask<Void, Void, Void>() {
            private SQLiteStatement getInsertStatementForTable(String table)
            {
                if ("state".equals(table))
                    return db.compileStatement("INSERT INTO state (state_id, state_abbrev, state_name) VALUES (?, ?, ?)");
                else if ("county".equals(table))
                    return db.compileStatement("INSERT INTO county (state_id, county_id, county_name) VALUES (?, ?, ?)");
                else if ("zone".equals(table))
                    return db.compileStatement("INSERT INTO zone (state_id, zone_id, zone_name) VALUES (?, ?, ?)");
                else if ("county_zone".equals(table))
                    return db.compileStatement("INSERT INTO county_zone (county_state_id, county_id, zone_state_id, zone_id) VALUES (?, ?, ?, ?)");
                else if ("database_version".equals(table))
                    return db.compileStatement("INSERT INTO database_version (database_version_id, schema_version, last_update_time) VALUES (?, ?, ?)");
                else
                    return null;
            }

            private SQLiteStatement getUpdateStatementForTable(String table)
            {
                if ("state".equals(table))
                    return db.compileStatement("UPDATE state SET state_abbrev = ?2, state_name = ?3 WHERE state_id = ?1");
                else if ("county".equals(table))
                    return db.compileStatement("UPDATE county SET county_name = ?3 WHERE state_id = ?1 AND county_id = ?2");
                else if ("zone".equals(table))
                    return db.compileStatement("UPDATE zone SET zone_name = ?3 WHERE state_id = ?1 AND zone_id = ?2");
                //else if ("county_zone".equals(table))
                //    return db.compileStatement("UPDATE county_zone SET (county_state_id, county_id, zone_state_id, zone_id) VALUES (?, ?, ?, ?)");
                else if ("database_version".equals(table))
                    return db.compileStatement("UPDATE database_version SET schema_version = ?2, last_update_time = ?3 WHERE database_version_id = ?1");
                else
                    return null;
            }

            private SQLiteStatement getDeleteStatementForTable(String table)
            {
                if ("state".equals(table))
                    return db.compileStatement("DELETE FROM state WHERE state_id = ?");
                else if ("county".equals(table))
                    return db.compileStatement("DELETE FROM county WHERE state_id = ? and county_id = ?");
                else if ("zone".equals(table))
                    return db.compileStatement("DELETE FROM zone WHERE state_id = ? AND zone_id = ?");
                else if ("county_zone".equals(table))
                    return db.compileStatement("DELETE FROM county_zone WHERE county_state_id = ? AND county_id = ? AND zone_state_id = ? AND zone_id = ?");
                else if ("database_version".equals(table))
                    return db.compileStatement("DELETE FROM database_version WHERE database_version_id = ?");
                else
                    return null;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                HttpURLConnection conn = null;
                try
                {
                    String versionArg="";
                    Cursor cursor = db.rawQuery("SELECT last_update_time FROM database_version", null);
                    try
                    {
                        if (cursor.moveToFirst())
                            versionArg = String.format(Locale.US, "&since=%s", Uri.encode(cursor.getString(0)));
                    }
                    finally
                    {
                        cursor.close();
                        cursor = null;
                    }
                    String urlprefix = "https://captest-1180.appspot.com";
                    if (BuildConfig.FLAVOR.equals("local"))
                        urlprefix = "http://10.0.2.2:8080";
                    conn = (HttpURLConnection)new URL(String.format(Locale.US, "%s/data.cgi?schema=%d%s", urlprefix, db.getVersion(), versionArg)).openConnection();
                    int status = conn.getResponseCode();
                    if (status != 200)
                    {
                        if (status == 301 || status == 302)
                        {
                            conn.getInputStream().close();
                            conn.disconnect();
                            conn = (HttpURLConnection)new URL(conn.getHeaderField("Location")).openConnection();
                            status = conn.getResponseCode();
                        }
                        if (status != 200)
                        {
                            conn.getErrorStream().close();
                            throw new IOException("Get failed with status code " + status);
                        }
                    }

                    JsonParser parser = new JacksonFactory().createJsonParser(conn.getInputStream(), Charset.forName("UTF-8"));
                    try {
                        Deque<StreamState> stateDeque = new ArrayDeque<StreamState>();
                        String table = null;
                        int index = 0;
                        SQLiteStatement stmt = null;
                        db.beginTransaction();
                        try {
                            do {
                                JsonToken token = parser.nextToken();
                                switch (token) {
                                    case START_OBJECT:
                                        if (stateDeque.isEmpty())
                                            stateDeque.push(StreamState.START);
                                        break;
                                    case END_OBJECT:
                                        switch (stateDeque.peek()) {
                                            case START:
                                                stateDeque.pop();
                                                break;
                                            case TABLE:
                                                stateDeque.pop();
                                                table = null;
                                                break;
                                            case ADD:
                                            case CHANGE:
                                            case REMOVE:
                                                if (stmt != null)
                                                    stmt.close();
                                                stmt = null;
                                                stateDeque.pop();
                                                break;
                                        }
                                        break;
                                    case FIELD_NAME:
                                        switch (stateDeque.peek()) {
                                            case START:
                                                table = parser.getCurrentName();
                                                stateDeque.push(StreamState.TABLE);
                                                break;
                                            case TABLE:
                                                if ("added".equals(parser.getCurrentName())) {
                                                    stmt = getInsertStatementForTable(table);
                                                    stateDeque.push(StreamState.ADD);
                                                } else if ("changed".equals(parser.getCurrentName())) {
                                                    stmt = getUpdateStatementForTable(table);
                                                    stateDeque.push(StreamState.CHANGE);
                                                } else if ("removed".equals(parser.getCurrentName())) {
                                                    stmt = getDeleteStatementForTable(table);
                                                    stateDeque.push(StreamState.REMOVE);
                                                }
                                                break;
                                        }
                                        break;
                                    case START_ARRAY:
                                        switch (stateDeque.peek()) {
                                            case ADD:
                                            case CHANGE:
                                            case REMOVE:
                                                stateDeque.push(StreamState.ROW_ARRAY);
                                                break;
                                            case ROW_ARRAY:
                                                stateDeque.push(StreamState.ROW);
                                                index = 0;
                                                break;
                                        }
                                        break;
                                    case END_ARRAY:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.execute();
                                            case ROW_ARRAY:
                                                stateDeque.pop();
                                                break;
                                        }
                                        if (!stateDeque.isEmpty()) {
                                            switch (stateDeque.peek()) {
                                                case ADD:
                                                case CHANGE:
                                                case REMOVE:
                                                    if (stmt != null)
                                                        stmt.close();
                                                    stmt = null;
                                                    stateDeque.pop();
                                                    break;
                                            }
                                        }
                                        break;
                                    case VALUE_STRING:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.bindString(++index, parser.getText());
                                                break;
                                        }
                                        break;
                                    case VALUE_NUMBER_INT:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.bindLong(++index, parser.getLongValue());
                                                break;
                                        }
                                        break;
                                    case VALUE_NUMBER_FLOAT:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.bindDouble(++index, parser.getDoubleValue());
                                                break;
                                        }
                                        break;
                                    case VALUE_NULL:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.bindNull(++index);
                                                break;
                                        }
                                        break;
                                    case VALUE_TRUE:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.bindLong(++index, 1);
                                                break;
                                        }
                                        break;
                                    case VALUE_FALSE:
                                        switch (stateDeque.peek()) {
                                            case ROW:
                                                if (stmt != null)
                                                    stmt.bindLong(++index, 0);
                                                break;
                                        }
                                        break;
                                }
                            } while (!stateDeque.isEmpty());
                            db.setTransactionSuccessful();
                        }
                        finally
                        {
                            parser.close();
                        }
                    }
                    finally
                    {
                        db.endTransaction();
                    }
                }
                catch (MalformedURLException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                finally
                {
                    if (conn != null)
                        conn.disconnect();
                    db.releaseReference();
                }
                return null;
            }
        }.execute();
        db.acquireReference();
    }
}
