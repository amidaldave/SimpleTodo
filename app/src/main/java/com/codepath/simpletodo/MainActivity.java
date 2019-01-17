package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        // mock data
        //items.add("First item");
       // items.add("second item");
        setupListViewListerner();
    }
public void onAddNewItem(View v){
    EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
    String itemText = etNewItem.getText().toString();
    itemsAdapter.add(itemText);
    etNewItem.setText("");
    writeItems();
    Toast.makeText(getApplicationContext(),"item added to list", Toast.LENGTH_SHORT).show();
}
private void setupListViewListerner(){
    Log.i("MainActivity", "Setting up listener on list View");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
            Log.i("MainActovity", "Item removed from list "+i);
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
}
private File getFileData(){
        return new File(getFilesDir(), "todo.txt");
}

private void readItems(){
    try {
        items = new ArrayList<>(FileUtils.readLines(getFileData(), Charset.defaultCharset()));
    } catch (IOException e) {
        Log.e("MainActivity", "Error reading file", e);
        items = new ArrayList<>();
    }
}
private  void writeItems(){
    try {
        FileUtils.writeLines(getFileData(),items);
    } catch (IOException e) {
        Log.e("MainActivity", "Error writing file", e);
    }
}
}
