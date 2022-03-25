package com.example.smutable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class SelectionActivity extends AppCompatActivity {



    private final static String FILE_NAME_SELECTION = "selection.txt";
    ImageButton BUTTON_TO_MAIN;
    Intent INTENT_TO_MAIN;
    Spinner SPINNER_SELECT_COURSE, SPINNER_SELECT_GROUP, SPINNER_SELECT_INSTITUTE;
    String[] url = new String[4];
    int URL_ID;
    AsyncHttpClient client;
    Workbook workbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);


        SPINNER_SELECT_COURSE = findViewById(R.id.SPINNER_SELECT_COURSE);
        SPINNER_SELECT_GROUP = findViewById(R.id.SPINNER_SELECT_GROUP);
        SPINNER_SELECT_INSTITUTE = findViewById(R.id.SPINNER_SELECT_INSTITUTE);

        BUTTON_TO_MAIN = findViewById(R.id.BUTTON_TO_MAIN);
        INTENT_TO_MAIN = new Intent(SelectionActivity.this, MainActivity.class);


        URL_ID = 0;
        url[0] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FirstCourse.xls?raw=true";
        url[1] = "";
        url[2] = "";
        url[3] = "";


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

                client = new AsyncHttpClient();
                client.get(url[URL_ID], new FileAsyncHttpResponseHandler(SelectionActivity.this) {
                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                        Toast.makeText(SelectionActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, File file) {
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setGCDisabled(true);
                        if(file != null) {

                            try {
                                workbook = workbook.getWorkbook(file);

                                String[] ARRAYSPINNER_2 = new String[]
                                        {
                                                workbook.getSheet(0).getName(),workbook.getSheet(1).getName(),workbook.getSheet(2).getName(),workbook.getSheet(3).getName(),workbook.getSheet(4).getName()
                                        };
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

                Sheet SHEET = workbook.getSheet(position);
                int CHAR_ = 0;
                for(int i = 0; i < 30; i++)
                {

                    System.out.println(SHEET.getCell(4+i,4).getContents());
                    String sgc = SHEET.getCell(4+i,4).getContents();
                    if(sgc.charAt(0) == '0')
                    {
                        CHAR_ = i;
                        System.out.println("------"+CHAR_);
                        break;
                    }
                }
                System.out.println(CHAR_);
                String[] GROUPS = new String[CHAR_];
                for(int i = 0; i < CHAR_; i++)
                {
                    GROUPS[i] =SHEET.getCell(4+i,6).getContents() + " - " + SHEET.getCell(4+i,5).getContents();
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



    }
}