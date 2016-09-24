package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.app.Activity.RESULT_OK;

public class EditItemActivity extends Activity {
    EditText etEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditText = (EditText) findViewById(R.id.editText);
        etEditText.setText((String)getIntent().getExtras().get(MainActivity.TODO_TEXT));
    }

    public void onSaveItem(View view) {
        submitItem(etEditText.getText().toString());
    }

    private void submitItem(String item) {
        Intent data = new Intent();
        data.putExtra(MainActivity.TODO_TEXT, item);
        setResult(RESULT_OK, data);
        finish();
    }
}
