package com.example.smutable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.smutable.feedback.FeedBackActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class SelectionActivity extends AppCompatActivity {



    private final static String FILE_NAME_SELECTION = "selection.txt";

    Button BUTTON_SAVE_SELECTION;
    Button BUTTON_TO_MAIN, BUTTON_TO_NEWS, BUTTON_TO_SEARCH, BUTTON_TO_NOTES;
    Intent INTENT_TO_MAIN,INTENT_TO_NEWS, INTENT_TO_SEARCH, INTENT_TO_NOTES, INTENT_TO_FEEDBACK, INTENT_TO_WHO;
    Spinner SPINNER_SELECT_COURSE, SPINNER_SELECT_GROUP, SPINNER_SELECT_INSTITUTE;
    String[] url = new String[5];
    int URL_ID, WORKBOOK_COURSE_ID, SHEET_INSTITUTE_ID, COLLUMN_GROUP_ID;
    AsyncHttpClient client;
    Workbook workbook;
    ImageView image_feedback, ic_more;

    //Переключатель темная/светлая тема
    Switch switch_ThemeColor;
    boolean nightMODE;
    boolean NOnightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);



        //Переключатель темная/светлая тема

        /*switch_ThemeColor = findViewById(R.id.switch_ThemeColor);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false); //светлая тема дефолт

        if (nightMODE) {
            switch_ThemeColor.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switch_ThemeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });*/



        BUTTON_TO_MAIN= findViewById(R.id.home_button_inside);
        BUTTON_TO_NEWS = findViewById(R.id.news_button_inside);
        BUTTON_TO_SEARCH = findViewById(R.id.search_button_inside);
        BUTTON_TO_NOTES = findViewById(R.id.note_button_inside);
        SPINNER_SELECT_COURSE = findViewById(R.id.SPINNER_SELECT_COURSE);
        SPINNER_SELECT_GROUP = findViewById(R.id.SPINNER_SELECT_GROUP);
        SPINNER_SELECT_INSTITUTE = findViewById(R.id.SPINNER_SELECT_INSTITUTE);
        BUTTON_SAVE_SELECTION = findViewById(R.id.BUTTON_SAVE_SELECTION);
        image_feedback = findViewById(R.id.image_feedback);
        ic_more = findViewById(R.id.ic_more);
        //registerForContextMenu(ic_more);

        INTENT_TO_MAIN = new Intent(SelectionActivity.this, MainActivity.class);
        INTENT_TO_NEWS = new Intent(SelectionActivity.this, activity_news.class);
        INTENT_TO_SEARCH = new Intent(SelectionActivity.this, search_activity.class);
        INTENT_TO_FEEDBACK = new Intent(SelectionActivity.this, FeedBackActivity.class);
        INTENT_TO_WHO = new Intent(SelectionActivity.this, HoWeAre.class);

        url[0] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true";
        url[1] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/SECONDCOURSE.xls?raw=true";
        url[2] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/THIRDCOURSE.xls?raw=true";
        url[3] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FOURCOURSE.xls?raw=true";

        INTENT_TO_NOTES = new Intent(SelectionActivity.this, NotesActivity.class);


        BUTTON_TO_NOTES = findViewById(R.id.note_button_inside);
        BUTTON_TO_NOTES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(INTENT_TO_NOTES);
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
        BUTTON_TO_SEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_SEARCH);
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
        image_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(INTENT_TO_FEEDBACK);
            }
        });


        String[] ARRAYSPINNER_1 = new String[]
                {
                        "1 Курс","2 Курс","3 Курс", "4 Курс"
                };
        ArrayAdapter<String> ADAPTER_1 = new ArrayAdapter<String>(SelectionActivity.this, android.R.layout.simple_spinner_item, ARRAYSPINNER_1);
        ADAPTER_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SPINNER_SELECT_COURSE.setAdapter(ADAPTER_1);

        SPINNER_SELECT_COURSE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WORKBOOK_COURSE_ID = position;
                client = new AsyncHttpClient();
                client.get(url[position], new FileAsyncHttpResponseHandler(SelectionActivity.this) {
                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                        Toast.makeText(SelectionActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, File file) {
                        SPINNER_SELECT_INSTITUTE.setClickable(true);
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setGCDisabled(true);
                        if(file != null) {

                            try {
                                workbook = workbook.getWorkbook(file);

                                String[] ARRAYSPINNER_2;
                                if(position >=3) {
                                    ARRAYSPINNER_2 = new String[]
                                            {
                                                    workbook.getSheet(0).getName(), workbook.getSheet(1).getName(), workbook.getSheet(2).getName(), workbook.getSheet(3).getName()
                                            };
                                }
                                else
                                {
                                    ARRAYSPINNER_2 = new String[]
                                            {
                                                    workbook.getSheet(0).getName(), workbook.getSheet(1).getName(), workbook.getSheet(2).getName(), workbook.getSheet(3).getName(), workbook.getSheet(4).getName()
                                            };
                                }
                                ArrayAdapter<String> ADAPTER_2 = new ArrayAdapter<String>(SelectionActivity.this, android.R.layout.simple_spinner_item, ARRAYSPINNER_2);
                                ADAPTER_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                SPINNER_SELECT_INSTITUTE.setAdapter(ADAPTER_2);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BiffException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SPINNER_SELECT_INSTITUTE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPINNER_SELECT_GROUP.setClickable(true);
                SHEET_INSTITUTE_ID = position;
                Sheet SHEET = workbook.getSheet(position);
                int CHAR_ = 0;
                for(int i = 0; i < 30; i++)
                {

                    System.out.println(SHEET.getCell(4+i,3).getContents());
                    String sgc = SHEET.getCell(4+i,3).getContents();
                    if(sgc.charAt(0) == '0')
                    {
                        CHAR_ = i;
                        break;
                    }
                }
                System.out.println(CHAR_);
                String[] GROUPS = new String[CHAR_];
                for(int i = 0; i < CHAR_; i++)
                {
                    if(SHEET.getCell(4+i,5).getContents().length() > 30)
                    GROUPS[i] = SHEET.getCell(4+i,6).getContents() + " - " + SHEET.getCell(4+i,5).getContents().substring(0,30) + "...";
                    else
                    GROUPS[i] = SHEET.getCell(4+i,6).getContents() + " - " + SHEET.getCell(4+i,5).getContents();
                }



                String[] ARRAYSPINNER_3 = GROUPS;
                ArrayAdapter<String> ADAPTER_3 = new ArrayAdapter<String>(SelectionActivity.this, android.R.layout.simple_spinner_item, ARRAYSPINNER_3);
                ADAPTER_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SPINNER_SELECT_GROUP.setAdapter(ADAPTER_3);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SPINNER_SELECT_GROUP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            COLLUMN_GROUP_ID = position;
            BUTTON_SAVE_SELECTION.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        BUTTON_SAVE_SELECTION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileOutputStream fos = null;
                try {

                    String text =""+ WORKBOOK_COURSE_ID + SHEET_INSTITUTE_ID + COLLUMN_GROUP_ID;
                    fos = openFileOutput(FILE_NAME_SELECTION, MODE_PRIVATE);
                    fos.write(text.getBytes());
                    BUTTON_TO_MAIN.callOnClick();

                } catch (IOException ex) {

                } finally {
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException ex) {

                    }
                }



            }
        });

        ic_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(SelectionActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_selection_activity, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.feedBack) {
                            startActivity(INTENT_TO_FEEDBACK);
                        } else if (menuItem.getItemId() == R.id.who) {
                            startActivity(INTENT_TO_WHO);
                        }
                        return false;
                    }
                }); popupMenu.show();
            }
        });
    }
}