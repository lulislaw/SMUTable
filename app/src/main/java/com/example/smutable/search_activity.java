package com.example.smutable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class search_activity extends AppCompatActivity {

    TextView finishtext;
    AsyncHttpClient client;
    Workbook workbook;
    Integer[] maxgroup = new Integer[5];
    NestedScrollView filter_scroll_view;
    String[] urls = new String[4];
    Button BUTTON_TO_MAIN, BUTTON_TO_NEWS, BUTTON_TO_SELECTION, BUTTON_TO_NOTES;
    Intent INTENT_TO_SELECTION,INTENT_TO_NEWS, INTENT_TO_MAIN, INTENT_TO_NOTES;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        BUTTON_TO_MAIN= findViewById(R.id.home_button_inside);
        BUTTON_TO_NEWS = findViewById(R.id.news_button_inside);
        BUTTON_TO_SELECTION = findViewById(R.id.settings_button_inside);
        BUTTON_TO_NOTES = findViewById(R.id.note_button_inside);
        INTENT_TO_MAIN = new Intent(search_activity.this, MainActivity.class);
        INTENT_TO_SELECTION = new Intent(search_activity.this, SelectionActivity.class);
        INTENT_TO_NEWS = new Intent(search_activity.this, activity_news.class);
        ImageView searchbutton = findViewById(R.id.search);
        EditText edittext = findViewById(R.id.edittext);
        finishtext = findViewById(R.id.search_textview);
        filter_scroll_view = findViewById(R.id.filter_scrollview);
        filter_scroll_view.setVisibility(View.GONE);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edittext.getText().toString().length() >= 3) {
                    String search_text = edittext.getText().toString();
                    finishtext.setText("");
                    searchconnectfail("https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true", search_text);
                }
                else
                {
                    finishtext.setText("");
                }


            }
        });
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchbutton.callOnClick();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        INTENT_TO_NOTES = new Intent(search_activity.this, NotesActivity.class);


        BUTTON_TO_NOTES = findViewById(R.id.note_button_inside);
        BUTTON_TO_NOTES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(INTENT_TO_NOTES);
                overridePendingTransition(0,0);
            }
        });
        BUTTON_TO_SELECTION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_SELECTION);
                overridePendingTransition(0,0);
            }
        });

        BUTTON_TO_NEWS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_NEWS);
                overridePendingTransition(0,0);
            }
        });

        BUTTON_TO_MAIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_MAIN);
                overridePendingTransition(0,0);
            }
        });







    }

    public void searchconnectfail(String url, String search_message)
    {
        client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                try{

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("MyLog", "fail");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {



                WorkbookSettings WORKBOOK_SETTING = new WorkbookSettings();
                WORKBOOK_SETTING.setGCDisabled(true);
                Log.d("MyLog", "succes");
                if (file != null) {
                    try {
                        Log.d("MyLog", "start");
                        workbook = workbook.getWorkbook(file);
                        for(int i = 0; i < 5; i++) {
                            Sheet sheet = workbook.getSheet(i);
                            int CHAR_ = 0;
                            for(int c = 0; c < 30; c++)
                            {
                                String sgc = sheet.getCell(4+c,3).getContents();
                                if(sgc.charAt(0) == '0')
                                {
                                    maxgroup[i] = c;
                                    break;
                                }
                            }

                            for(int s = 0; s < 48; s++){
                                for(int c = 0; c < maxgroup[i]; c++)
                                {
                                    String mess = sheet.getCell(1,8+s).getContents() +"\n" + sheet.getCell(2,8+s).getContents() + "\n" + sheet.getCell(3,8+s).getContents() +"\n" + sheet.getCell(4+c,8+s).getContents() + "\n";

                                    if(sheet.getCell(4+c,8+s).getContents().toLowerCase().contains(search_message.toLowerCase()))
                                    {
                                        if(!finishtext.getText().toString().contains(mess))
                                        {
                                            finishtext.setText(finishtext.getText().toString() + mess + "\n\n");

                                        }
                                    }
                                }
                            }
                            finishtext.setText(finishtext.getText().toString().substring(0));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}