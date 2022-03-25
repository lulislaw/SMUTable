package com.example.smutable;


import static com.example.smutable.R.layout.activity_main;

import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


import cz.msebera.android.httpclient.Header;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;


public class MainActivity extends AppCompatActivity {

    private final static String FILE_NAME = "DATA.txt";
    private final static String FILE_NAME_SELECTION = "selection.txt";
    AsyncHttpClient client;
    Workbook workbook;
    String[] url = new String[5];
    Button[] Subject = new Button[24];
    String[] Subject_text = new String[48];
    int SHEET_ID, COLLUMN_ID, ROW_ID, URL_ID;
    String text;
    TextView INFO_BLOCK;
    Button CLOSE_INFO_BLOCK;
    Boolean NEED_DOWNLOAD;
    ImageButton BUTTON_TO_SELECTION;
    Intent INTENT_TO_SELECTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);




        //Начало подключения XML//

        //Subjects//
        Subject[0] = findViewById(R.id.BUTTON_SUBJECT_INFO1);
        Subject[1] = findViewById(R.id.BUTTON_SUBJECT_INFO2);
        Subject[2] = findViewById(R.id.BUTTON_SUBJECT_INFO3);
        Subject[3] = findViewById(R.id.BUTTON_SUBJECT_INFO4);
        Subject[4] = findViewById(R.id.BUTTON_SUBJECT_INFO5);
        Subject[5] = findViewById(R.id.BUTTON_SUBJECT_INFO6);
        Subject[6] = findViewById(R.id.BUTTON_SUBJECT_INFO7);
        Subject[7] = findViewById(R.id.BUTTON_SUBJECT_INFO8);
        Subject[8] = findViewById(R.id.BUTTON_SUBJECT_INFO9);
        Subject[9] = findViewById(R.id.BUTTON_SUBJECT_INFO10);
        Subject[10] = findViewById(R.id.BUTTON_SUBJECT_INFO11);
        Subject[11] = findViewById(R.id.BUTTON_SUBJECT_INFO12);
        Subject[12] = findViewById(R.id.BUTTON_SUBJECT_INFO13);
        Subject[13] = findViewById(R.id.BUTTON_SUBJECT_INFO14);
        Subject[14] = findViewById(R.id.BUTTON_SUBJECT_INFO15);
        Subject[15] = findViewById(R.id.BUTTON_SUBJECT_INFO16);
        Subject[16] = findViewById(R.id.BUTTON_SUBJECT_INFO17);
        Subject[17] = findViewById(R.id.BUTTON_SUBJECT_INFO18);
        Subject[18] = findViewById(R.id.BUTTON_SUBJECT_INFO19);
        Subject[19] = findViewById(R.id.BUTTON_SUBJECT_INFO20);
        Subject[20] = findViewById(R.id.BUTTON_SUBJECT_INFO21);
        Subject[21] = findViewById(R.id.BUTTON_SUBJECT_INFO22);
        Subject[22] = findViewById(R.id.BUTTON_SUBJECT_INFO23);
        Subject[23] = findViewById(R.id.BUTTON_SUBJECT_INFO24);
        //Subjects//
        INFO_BLOCK = findViewById(R.id.INFO_BLOCK);
        CLOSE_INFO_BLOCK = findViewById(R.id.CLOSE_INFO_BLOCK);

        BUTTON_TO_SELECTION = findViewById(R.id.BUTTON_TO_SELECTION);
        INTENT_TO_SELECTION = new Intent(MainActivity.this, SelectionActivity.class);
        //Конец подключения XML//


        url[0] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true";
        url[1] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/SECONDCOURSE.xls?raw=true";
        url[2] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/THIRDCOURSE.xls?raw=true";
        url[3] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FOURCOURSE.xls?raw=true";
        url[4] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIVECOURSE.xls?raw=true";
        ROW_ID = 8;
        NEED_DOWNLOAD = false;
        BUTTON_TO_SELECTION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_SELECTION);
                overridePendingTransition(0,0);
            }
        });


        FileInputStream fins = null;
                String text_selection;
                try {
                    fins = openFileInput(FILE_NAME_SELECTION);
                    byte[] bytes = new byte[fins.available()];
                    fins.read(bytes);
                    text_selection = new String(bytes);
                    URL_ID = text_selection.charAt(0)-'0';
                    SHEET_ID = text_selection.charAt(1)-'0';
                    if(text_selection.length()>3)
                    {
                        COLLUMN_ID = ((text_selection.charAt(2)-'0')*10)+text_selection.charAt(3)-'0';
                    }
                    else
                        COLLUMN_ID = text_selection.charAt(2)-'0';
                        COLLUMN_ID = COLLUMN_ID + 4;
                        System.out.println("---------------------"+COLLUMN_ID);
                    } catch (FileNotFoundException e) {
                    BUTTON_TO_SELECTION.callOnClick();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


        LOAD_DATA();

        {
        Subject[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(0);
            }
        });
        Subject[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(1);
            }
        });
        Subject[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(2);
            }
        });
        Subject[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(3);
            }
        });
        Subject[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(4);
            }
        });
        Subject[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(5);
            }
        });
        Subject[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(6);
            }
        });
        Subject[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(7);
            }
        });
        Subject[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(8);
            }
        });
        Subject[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(9);
            }
        });
        Subject[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(10);
            }
        });
        Subject[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(11);
            }
        });
        Subject[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(12);
            }
        });
        Subject[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(13);
            }
        });
        Subject[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(14);
            }
        });
        Subject[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(15);
            }
        });
        Subject[16].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(16);
            }
        });
        Subject[17].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(17);
            }
        });
        Subject[18].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(18);
            }
        });
        Subject[19].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(19);
            }
        });
        Subject[20].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(20);
            }
        });
        Subject[21].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(21);
            }
        });
        Subject[22].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(22);
            }
        });
        Subject[23].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOW_INFO(23);
            }
        });
    }






        CLOSE_INFO_BLOCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INFO_BLOCK.setVisibility(View.INVISIBLE);
                CLOSE_INFO_BLOCK.setVisibility(View.INVISIBLE);
                CLOSE_INFO_BLOCK.setClickable(false);
            }
        });

            DOWNLOAD_DATA();

        //end main code
    }


        public void DOWNLOAD_DATA()
        {
            client = new AsyncHttpClient();
            client.get(url[URL_ID], new FileAsyncHttpResponseHandler(this) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                    Toast.makeText(MainActivity.this, "Загружена последняя информация", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {

                    WorkbookSettings WORKBOOK_SETTING = new WorkbookSettings();
                    WORKBOOK_SETTING.setGCDisabled(true);
                    Toast.makeText(MainActivity.this, "...", Toast.LENGTH_SHORT).show();
                    if (file != null) {
                        try {
                            workbook = workbook.getWorkbook(file);
                            Sheet sheet = workbook.getSheet(SHEET_ID);
                            for (int i = 0; i < 48; i++) {
                                Subject_text[i] = (sheet.getCell(2, ROW_ID + i).getContents() + "--" + sheet.getCell(COLLUMN_ID, ROW_ID + i).getContents());
                            }
                            SAVE_DATA();
                            LOAD_DATA();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            e.printStackTrace();
                        }


                    }


                }
            });
        }

//next public voids
        public void SHOW_INFO(int a)
        {
            INFO_BLOCK.setVisibility(View.VISIBLE);
            INFO_BLOCK.setText(Subject_text[a]);
            CLOSE_INFO_BLOCK.setVisibility(View.VISIBLE);
            CLOSE_INFO_BLOCK.setClickable(true);
        }


        //Вывод в консоль//
    public void TEST()
    {
        for (int i = 0; i < 48; i++) {
            System.out.println(Subject_text[i]);
        }
    }
        //Вывод в консоль//


        //Сохранение информации о текущей группе//
    public void SAVE_DATA()
    {

        FileOutputStream fos = null;
        try {
                text = "";
           for(int i = 0; i < 48; i++)
           {
               text = text + Subject_text[i] + 'X';
           }
           StringBuffer sb = new StringBuffer(text);
           sb.delete(text.length()-1,text.length());
            text = sb.toString();
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());


        } catch (IOException ex) {

        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {

            }
        }

    }
        //Сохранение информации о текущей группе//


        //Загрузка информации о текущей группе//
    public void LOAD_DATA()
    {

        FileInputStream fin = null;
        String stringfortext = "";
        int c = 0;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String(bytes);

            StringBuffer sb = new StringBuffer(text);
            for(int i = 0; i < text.length(); i++)
            {
                if(text.charAt(i) == 'X')
                {
                    Subject_text[c] = stringfortext;
                    stringfortext = "";
                    c+=1;
                }
                else
                stringfortext = stringfortext + text.charAt(i);




            }





        } catch (IOException ex) {

        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {

            }
        }



    }
        //Загрузка информации о текущей группе//




}


