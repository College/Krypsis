/*
 * Krypsis - Write web applications based on proved technologies
 * like HTML, JavaScript, CSS... and access functionality of your
 * smartphone in a platform independent way.
 *
 * Copyright (C) 2008 - 2009 krypsis.org (have.a.go@krypsis.org)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.krypsis.android.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;

public class DatabaseManager {

  private static final String DATABASE_PATH = "/data/data/org.krypsis.android/krypsis";
  private static final String APP_INFO_TABLE_NAME = "APP_INFO";

  private static final DatabaseManager INSTANCE = new DatabaseManager();

  private DatabaseManager() {
    createApplicationInfoTable();
  }

  public static DatabaseManager getInstance() {
    return INSTANCE;
  }

  public String readApplicationInfo(String key) {
    SQLiteDatabase database = null;
    Cursor cursor = null;
    String value = null;

    try {
      database = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH, null);
      cursor = database.query(APP_INFO_TABLE_NAME, null, null, null,
              null, null, null);

      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        value = cursor.getString(1);
      }
    } finally {
      if (database != null) {
        database.close();
      }
      if (cursor != null) {
        cursor.close();
      }
    }

    return value;
  }

  public boolean writeApplicationInfo(String key, String value) {
    SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH, null);
    long rowId;
    try {
      database.beginTransaction();

      ContentValues values = new ContentValues(2);
      values.put("KEY", key);
      values.put("VALUE", value);

      rowId = database.insert(APP_INFO_TABLE_NAME, "VALUE", values);
      database.setTransactionSuccessful();
      
      database.endTransaction();
    } finally {
      database.close();
    }
    return rowId != -1;
  }

  private void createApplicationInfoTable() {
    final SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH, null);
    database.execSQL("CREATE TABLE IF NOT EXISTS " + APP_INFO_TABLE_NAME +
            " (key varchar(100) PRIMARY KEY, value varchar(100))");
    database.close();
  }

}
