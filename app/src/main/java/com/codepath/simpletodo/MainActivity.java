package com.codepath.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  // a numeric code to identify Edit item
    public final static int EDIT_REQUEST_CODE =20;
   // keys used to passing data between activity
    public final static String ITEM_TEXT="itemText";
    public final static String ITEM_POSITION="itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#842d1b"));
                tv.setTextSize(20);
                Typeface typeface = ResourcesCompat.getFont(getContext(),R.font.crete_round_italic);
                tv.setTypeface(typeface);
                return v;
            }
        };

        lvItems.setAdapter(itemsAdapter);
        setupListViewListerner();
    }
public void onAddNewItem(View v){
    EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
    String itemText = etNewItem.getText().toString();
    itemsAdapter.add(itemText);
    etNewItem.setText("");
    writeItems();
    Toast.makeText(getApplicationContext(),R.string.item_add, Toast.LENGTH_SHORT).show();
}
private void setupListViewListerner(){
    Log.i("MainActivity", "Setting up listener on list View");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
            Log.i("MainActovity", R.string.item_remove+" "+i);
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
     lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //create the new activity
             Intent i= new Intent(MainActivity.this, EditItemActivity.class);
             //pass date being edited
             i.putExtra(ITEM_TEXT, items.get(position));
             i.putExtra(ITEM_POSITION,position);
             //display data
             startActivityForResult(i,EDIT_REQUEST_CODE);
         }
     });
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==EDIT_REQUEST_CODE){
            String updateItem = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);
            items.set(position,updateItem);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this,R.string.item_update, Toast.LENGTH_SHORT).show();
        }
    }

    private File getFileData(){
        return new File(getFilesDir(), "todo.txt");
}

private void readItems(){
    try {
        items = new ArrayList<>(FileUtils.readLines(getFileData(), Charset.defaultCharset()));
    } catch (IOException e) {
        Log.e("MainActivity", String.valueOf(R.string.error_read), e);
        items = new ArrayList<>();
    }
}
private  void writeItems(){
    try {
        FileUtils.writeLines(getFileData(),items);
    } catch (IOException e) {
        Log.e("MainActivity", String.valueOf(R.string.error_write), e);
    }
}
}
