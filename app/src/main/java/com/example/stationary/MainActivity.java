package com.example.stationary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stationary.data.PetContract;
import com.example.stationary.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //private Cursor cursor;
    private ListView listView;
    private PetCursorAdapter madapter;
    private View emptyView;
    private FloatingActionButton fab;
    private final static int PET_LOADER=0;



    @Override
    protected void onStart() {
        //displayDatabaseInfo();
        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup FAB to open EditorActivity
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        listView= (ListView) findViewById(R.id.listView);
        emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        //displayDatabaseInfo();
        /**
         * null cursor passed to cursorAdapter as onLoadFinished will populate it
         */
        madapter=new PetCursorAdapter(this,null);
        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPetUri=ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,id);
                intent.setData(currentPetUri);
                startActivity(intent);
                /**
                 * id is obtained from cursor PetEntry._ID
                 */
                Toast.makeText(getApplicationContext(),"listView.onItemClickListener"+ id,Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * kick of the loaderManager it will call onCreateLoader ONLY USING FOR DATA QUERY ON BACKGROUND THREAD
         */
        getLoaderManager().initLoader(PET_LOADER,null,this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                dummyData();
                //displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                int id=getContentResolver().delete(PetContract.PetEntry.CONTENT_URI,null,null);
                //displayDatabaseInfo();
                Toast.makeText(this,"DELETED "+id,Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void dummyData() {
        ContentValues values=new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME,"TOTO");
        values.put(PetContract.PetEntry.COLUMN_PET_BREED,"kuta");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,20);
        Uri newUri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this,"insert_pet_failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"insert_pet_successful at "+ ContentUris.parseId(newUri),Toast.LENGTH_SHORT).show();
        }
    }



    /**
     *query on background thread for editing pet
     * A CursorLoader is a subclass of AsyncTaskLoader that queries a ContentProvider, via a ContentResolver and specific URI,
     * and returns a Cursor of desired data. This loader runs its query on a background thread so that it doesn’t block the UI.
     * When a CursorLoader is active, it is tied to a URI, and you can choose to have it monitor this URI for any changes in data;
     * this means that the CursorLoader can deliver new results whenever the contents of our weather database change,
     * and we can automatically update the UI to reflect any weather change!
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection={
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED
        };
        /**
         * cursor loader automatically calls contentResolver which calls contentProvider of specified URI
         * as we have specified petProvider in manifest file which will be called from here
         */
        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI,projection,null,null,null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        madapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        madapter.swapCursor(null);
    }



    /**
     *querying data on main thread
     *
     private void displayDatabaseInfo() {
     String[] projection={
     PetContract.PetEntry._ID,
     PetContract.PetEntry.COLUMN_PET_NAME,
     PetContract.PetEntry.COLUMN_PET_BREED,
     PetContract.PetEntry.COLUMN_PET_GENDER,
     PetContract.PetEntry.COLUMN_PET_WEIGHT
     };
     cursor=getContentResolver().query(PetContract.PetEntry.CONTENT_URI,projection,null,null,null);
     madapter=new PetCursorAdapter(this,cursor);
     listView.setAdapter(madapter);
     }*/
}
