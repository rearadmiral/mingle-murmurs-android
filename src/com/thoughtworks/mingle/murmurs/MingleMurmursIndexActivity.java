package com.thoughtworks.mingle.murmurs;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

import com.thoughtworks.mingle.R;

public class MingleMurmursIndexActivity extends ListActivity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Uri murmurs_uri = MurmurContentProvider.CONTENT_URI;

    Cursor cursor = managedQuery(murmurs_uri,
        null, // Which columns to return
        null, // Which rows to return (all rows)
        null, // Selection arguments (none)
        null);

    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.murmur_summary,
        cursor, Murmur.SUMMARY_COLUMN_NAMES,
        new int[] { 0, R.id.tagline, R.id.body, R.id.icon });
    setListAdapter(adapter);

    getListView().setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        Intent showContent = new Intent(getApplicationContext(),
            MingleMurmursShowActivity.class);
        showContent.setData(Murmur.constructUri(id));
        startActivity(showContent);
      }
    });
  }
  
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add("Refresh");
    menu.add("Post New Murmur");
    menu.add("Settings");
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getTitle().equals("Post New Murmur")) {
      Intent intent = new Intent(this, MingleMurmursCreateActivity.class);
      this.startActivity(intent);
      return true;
    } else if (item.getTitle().equals("Settings")) {
      Intent intent = new Intent(this, SettingsActivity.class);
      this.startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}