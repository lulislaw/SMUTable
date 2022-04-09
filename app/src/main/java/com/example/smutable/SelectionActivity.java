package com.example.smutable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class SelectionActivity extends AppCompatActivity {



    private final static String FILE_NAME_SELECTION = "selection.txt";
    ConstraintLayout BUTTON_TO_MAIN;
    Button BUTTON_SAVE_SELECTION;
    Intent INTENT_TO_MAIN;
    Spinner SPINNER_SELECT_COURSE, SPINNER_SELECT_GROUP, SPINNER_SELECT_INSTITUTE;
    String[] url = new String[5];
    int URL_ID, WORKBOOK_COURSE_ID, SHEET_INSTITUTE_ID, COLLUMN_GROUP_ID;
    AsyncHttpClient client;
    Workbook workbook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);


        SPINNER_SELECT_COURSE = findViewById(R.id.SPINNER_SELECT_COURSE);
        SPINNER_SELECT_GROUP = findViewById(R.id.SPINNER_SELECT_GROUP);
        SPINNER_SELECT_INSTITUTE = findViewById(R.id.SPINNER_SELECT_INSTITUTE);
        BUTTON_SAVE_SELECTION = findViewById(R.id.BUTTON_SAVE_SELECTION);
        BUTTON_TO_MAIN = findViewById(R.id.home_group);
        INTENT_TO_MAIN = new Intent(SelectionActivity.this, MainActivity.class);



        url[0] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true";
        url[1] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/SECONDCOURSE.xls?raw=true";
        url[2] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/THIRDCOURSE.xls?raw=true";
        url[3] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FOURCOURSE.xls?raw=true";
        url[4] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIVECOURSE.xls?raw=true";

        BUTTON_TO_MAIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_MAIN);
                overridePendingTransition(0,0);
            }
        });

        String[] ARRAYSPINNER_1 = new String[]
                {
                        "1 Курс","2 Курс","3 Курс", "4 Курс", "1-2 Курс Маг."
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

                    System.out.println(SHEET.getCell(4+i,4).getContents());
                    String sgc = SHEET.getCell(4+i,4).getContents();
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











    }
}