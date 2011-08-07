package com.thoughtworks.mingle.murmurs;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.dephillipsdesign.http.HttpClientUtil;

public class MurmurContentProvider extends ContentProvider {

  public static final Uri CONTENT_URI = Uri.parse("content://com.thoughtworks.mingle.murmurs");

  private InputStream openRemoteListUri() {
    try {
      String uri = Settings.getProjectPath() + "/murmurs.xml";
      Log.i(MurmurContentProvider.class.getName(), uri);
      return HttpClientUtil.openInputStream(uri);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private InputStream openRemoteGetUri(int id) {
    try {
      String uri = Settings.getProjectPath() + "/murmurs/" + id + ".xml";
      Log.i(MurmurContentProvider.class.getName(), uri);
      return HttpClientUtil.openInputStream(uri);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private Cursor findMurmur(int id) {
    MatrixCursor cursor = new MatrixCursor(Murmur.DETAIL_COLUMN_NAMES);
    Murmur murmur = new MurmursLoader().loadOneFromXml(openRemoteGetUri(id));
    cursor.addRow(new Object[] { murmur.getId(), murmur.getAuthor(), murmur.getCreatedAtFormatted(), murmur.getBody(), murmur.getIconPathUri() });
    return cursor;
  }
  
  private Cursor addRows(MatrixCursor cursor, List<Murmur> murmurs) {
    Log.d(MurmurContentProvider.class.getName(), "Adding " + murmurs.size() + " to cursor");
    for (Murmur m : murmurs) {
      cursor.addRow(new Object[] { m.getId(), m.getTagline(), m.getShortBody(), m.getIconPathUri() });
    }
    return cursor;
  }

  private Cursor queryRecentMurmurs() {
    MatrixCursor cursor = new MatrixCursor(Murmur.SUMMARY_COLUMN_NAMES);
    List<Murmur> murmurs = new MurmursLoader().loadMultipleFromXml(openRemoteListUri());
    return addRows(cursor, murmurs);
  }
  
  public Cursor query(Uri uri, String[] columns, String where_clause, String[] selection, String order_by) {
    if (where_clause == null) {
      return queryRecentMurmurs();
    } else {
      int id = Integer.parseInt(where_clause.split("=")[1]);
      return findMurmur(id);
    }
  }

  public String getType(Uri uri) {
    return "vnd.android.cursor.dir/vnd.com.thoughtworks.mingle.murmur";
  }

  public int delete(Uri arg0, String arg1, String[] arg2) {
    throw new UnsupportedOperationException();
  }

  public Uri insert(Uri uri, ContentValues contentValues) {
    String body = (String) contentValues.get("body");
    Murmur murmur = new Murmur(body);
    murmur.saveAsNew();
    return null;
  }

  public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
    throw new UnsupportedOperationException();
  }

  public boolean onCreate() {
    return true;
  }

}
