package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String TODO_TEXT = "todo_text";
    private static final int REQUEST_TODO_EDIT = 10;

    ArrayList<String> todoItems = new ArrayList<String>();
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etEditText;
    int positionBeingEdited = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvitems);
        populateToDoItems();
        lvItems.setAdapter(itemsAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItemsToFile();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionBeingEdited = position;
                startEditTextActivity(todoItems.get(position));
            }
        });
    }

    void startEditTextActivity(String text) {
        Intent i = new Intent(this, EditItemActivity.class);
        i.putExtra(TODO_TEXT, text);
        startActivityForResult(i, REQUEST_TODO_EDIT);
    }

    void populateToDoItems() {
        readItemsFromFile();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                todoItems);

    }

    public void onAddItem(View view) {
        String itemText = etEditText.getText().toString();
        if (itemText.isEmpty()) return;
        todoItems.add(itemText);
        etEditText.setText("");
        writeItemsToFile();
    }

    private void readItemsFromFile() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
        }
    }

    private void writeItemsToFile() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TODO_EDIT && resultCode == RESULT_OK) {
            String text = (String) data.getExtras().get(TODO_TEXT);
            todoItems.set(positionBeingEdited, text);
            itemsAdapter.notifyDataSetChanged();
            writeItemsToFile();
        }
    }
}
