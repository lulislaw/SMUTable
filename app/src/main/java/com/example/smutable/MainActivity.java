package com.example.smutable;


import static com.example.smutable.R.layout.activity_main;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import cz.msebera.android.httpclient.Header;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;



public class MainActivity extends AppCompatActivity {

    private final static String FILE_NAME = "DATA.txt";
    private final static String FILE_NAME_GROUP = "GROUP.txt";
    private final static String FILE_NAME_SELECTION = "selection.txt";
    AsyncHttpClient client;
    Workbook workbook;
    String[] url = new String[5];
    TextView[] DAYS = new TextView[7];
    Button[] Subject = new Button[24];
    String[] Subject_text = new String[48];
    String[] Subject_text_rly = new String[24];                     //Массив в соответсвие с "Subject"
    int SHEET_ID, COLLUMN_ID, ROW_ID, URL_ID;
    int WEEK_EVEN;     // 0 = EVEN , 1 = Non-even;
    int SpecialWeekOfYear, CurrentWeekOfYear;
    Integer mYear,mMonth,mdayOfMonth;
    String text, GroupName;
    TextView INFO_BLOCK, TEXTVIEW_GROUP, TEXTVIEW_WEEK;
    Button CLOSE_INFO_BLOCK;
    Boolean NEED_DOWNLOAD;
    ImageButton BUTTON_TO_SELECTION, BUTTON_REFRESH, BUTTON_SET_WEEK;
    Intent INTENT_TO_SELECTION;
    CalendarView CALENDAR_VIEW;
    Boolean Calendar_enable;
    LocalDate date;
    NestedScrollView Scroll;
    Animation animation_scale;
    String[] daysofweeks_string = {
            "Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Суббота"
    };




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        Calendar_enable = false;
        WEEK_EVEN = 0;
        GroupName = "";
        {
            date = LocalDate.of
                    (2022,
                    2,
                    6);

        }

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
        //days//
        DAYS[0] = findViewById(R.id.TEXTVIEW_MONDAY);
        DAYS[1] = findViewById(R.id.TEXTVIEW_TUESDAY);
        DAYS[2] = findViewById(R.id.TEXTVIEW_WEDNESDAY);
        DAYS[3] = findViewById(R.id.TEXTVIEW_THURSDAY);
        DAYS[4] = findViewById(R.id.TEXTVIEW_FRIDAY);
        DAYS[5] = findViewById(R.id.TEXTVIEW_SATURDAY);
        DAYS[6] = findViewById(R.id.TEXTVIEW_SATURDAY);
        //days//
        INFO_BLOCK = findViewById(R.id.INFO_BLOCK);
        TEXTVIEW_GROUP = findViewById(R.id.TEXTVIEW_GROUP);
        TEXTVIEW_WEEK = findViewById(R.id.TEXTVIEW_WEEK);
        CLOSE_INFO_BLOCK = findViewById(R.id.CLOSE_INFO_BLOCK);
        BUTTON_REFRESH = findViewById(R.id.BUTTON_REFRESH);
        BUTTON_TO_SELECTION = findViewById(R.id.BUTTON_TO_SELECTION);
        BUTTON_SET_WEEK = findViewById(R.id.BUTTON_SET_WEEK);
        CALENDAR_VIEW = findViewById(R.id.calendarView);
        Scroll = findViewById(R.id.nestedScrollView);

        //Конец подключения XML//

        animation_scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
        CurrentWeekOfYear = (LocalDate.now().getDayOfYear() - date.getDayOfYear()) / 7 + 1;
        TEXTVIEW_WEEK.setText("Неделя: " + CurrentWeekOfYear);
        if(CurrentWeekOfYear % 2 == 0)
            WEEK_EVEN = 0;
        else
            WEEK_EVEN = 1;


        CALENDAR_VIEW.setVisibility(View.INVISIBLE);
        CALENDAR_VIEW.setClickable(false);
        INTENT_TO_SELECTION = new Intent(MainActivity.this, SelectionActivity.class);
        url[0] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true";
        url[1] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/SECONDCOURSE.xls?raw=true";
        url[2] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/THIRDCOURSE.xls?raw=true";
        url[3] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FOURCOURSE.xls?raw=true";
        url[4] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIVECOURSE.xls?raw=true";
        ROW_ID = 8;
        NEED_DOWNLOAD = false;


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

                    } catch (FileNotFoundException e) {
                    BUTTON_TO_SELECTION.callOnClick();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



        for(int i = 0; i< 24; i++) {
            int finalI = i;
            Subject[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SHOW_INFO(finalI);
                }
            });
        }

        BUTTON_TO_SELECTION.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(INTENT_TO_SELECTION);
                overridePendingTransition(0,0);
            }
        });
        CLOSE_INFO_BLOCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INFO_BLOCK.setVisibility(View.INVISIBLE);
                INFO_BLOCK.setClickable(false);
                CLOSE_INFO_BLOCK.setVisibility(View.INVISIBLE);
                CLOSE_INFO_BLOCK.setClickable(false);
                CALENDAR_VIEW.setVisibility(View.INVISIBLE);
                CALENDAR_VIEW.setClickable(false);

            }
        });

            BUTTON_REFRESH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TEXTVIEW_WEEK.setText("Неделя: " + CurrentWeekOfYear);
                    if(CurrentWeekOfYear % 2 == 0)
                        WEEK_EVEN = 0;
                    else
                        WEEK_EVEN = 1;
                    SET_TEXT_DAYS(LocalDate.now().getDayOfWeek().getValue(),LocalDate.now());
                    DOWNLOAD_DATA();
                    SAVE_DATA();
                    LOAD_DATA(CurrentWeekOfYear);
                }
            });

            BUTTON_SET_WEEK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CLOSE_INFO_BLOCK.setVisibility(View.VISIBLE);
                    CLOSE_INFO_BLOCK.setClickable(true);
                    CALENDAR_VIEW.setVisibility(View.VISIBLE);
                    CALENDAR_VIEW.setClickable(true);
                    Calendar_enable = true;
                    CALENDAR_VIEW.startAnimation(animation_scale);

                }
            });




        SpecialWeekOfYear = CurrentWeekOfYear;
        CALENDAR_VIEW.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month + 1;          //Блядская джава считает что отсчитывать с нуля нужно только месяц   :|
                mdayOfMonth = dayOfMonth;
                SpecialWeekOfYear = (LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfYear() - date.getDayOfYear()) / 7 + 1;
                TEXTVIEW_WEEK.setText("Неделя: " +SpecialWeekOfYear);
                if(SpecialWeekOfYear % 2 == 0)
                        WEEK_EVEN = 0;
                    else
                        WEEK_EVEN = 1;
                LOAD_DATA(SpecialWeekOfYear);
                SET_TEXT_DAYS(LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue(),LocalDate.of(mYear,mMonth,mdayOfMonth));
                SMOOTH_SCROLL(LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue());
                CALENDAR_VIEW.setVisibility(View.INVISIBLE);
                CALENDAR_VIEW.setClickable(false);
                Calendar_enable = false;
                CLOSE_INFO_BLOCK.setVisibility(View.INVISIBLE);
                CLOSE_INFO_BLOCK.setClickable(false);

            }
        });


        LOAD_DATA(CurrentWeekOfYear);
        DOWNLOAD_DATA();


        SET_TEXT_DAYS(LocalDate.now().getDayOfWeek().getValue(),LocalDate.now());




        //end main code
    }


        public void DOWNLOAD_DATA()
        {
            client = new AsyncHttpClient();
            client.get(url[URL_ID], new FileAsyncHttpResponseHandler(this) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    SMOOTH_SCROLL(LocalDate.now().getDayOfWeek().getValue());
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
                           GroupName = sheet.getCell(COLLUMN_ID, 5).getContents() + " - " + sheet.getCell(COLLUMN_ID, 7).getContents();
                            for (int i = 0; i < 48; i++) {
                                Subject_text[i] = (sheet.getCell(2, ROW_ID + i).getContents() + "\n\n" + sheet.getCell(COLLUMN_ID, ROW_ID + i).getContents());
                            }

                            SAVE_DATA();
                            LOAD_DATA(CurrentWeekOfYear);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            e.printStackTrace();
                        }


                    }
                    SMOOTH_SCROLL(LocalDate.now().getDayOfWeek().getValue());

                }
            });



        }

//next public voids
        public void SHOW_INFO(int a)
        {
            if(Calendar_enable == false) {
                INFO_BLOCK.setVisibility(View.VISIBLE);
                INFO_BLOCK.setClickable(true);
                INFO_BLOCK.setText(Subject_text_rly[a]);
                CLOSE_INFO_BLOCK.setVisibility(View.VISIBLE);
                CLOSE_INFO_BLOCK.setClickable(true);
                INFO_BLOCK.startAnimation(animation_scale);
            }
        }

        public void SET_TEXT_DAYS(int day, LocalDate xDate)
        {
            int dayx1 = day-1;
            int dayx2 = day-1;
            long xDateEpoch = xDate.toEpochDay();
            long xDateEpoch1 = xDate.toEpochDay();
            while(dayx1 >= 0)
            {
                DAYS[dayx1].setText(daysofweeks_string[dayx1] + "  "+  LocalDate.ofEpochDay(xDateEpoch).getDayOfMonth() + "." +LocalDate.ofEpochDay(xDateEpoch).getMonth().getValue() + "." + LocalDate.ofEpochDay(xDateEpoch).getYear());
                xDateEpoch = xDateEpoch -1;
                dayx1= dayx1 -1;
            }
            while(dayx2 <= 5)
            {
                DAYS[dayx2].setText(daysofweeks_string[dayx2] + "  "+  LocalDate.ofEpochDay(xDateEpoch1).getDayOfMonth() + "." + LocalDate.ofEpochDay(xDateEpoch1).getMonth().getValue() + "." + LocalDate.ofEpochDay(xDateEpoch1).getYear());
                xDateEpoch1 = xDateEpoch1 + 1;
                dayx2= dayx2 +1;
            }

        }


        public void SMOOTH_SCROLL(int day)
        {
            day = day-1;
            if(day >= 0 && day <=6) {
                Scroll.smoothScrollTo(Scroll.getScrollX(), DAYS[day].getTop());

            }
        }

        public void SET_DATE()
        {


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


        FileOutputStream fosgroup = null;
        try {

            fosgroup = openFileOutput(FILE_NAME_GROUP, MODE_PRIVATE);

           fosgroup.write(GroupName.getBytes());


        } catch (IOException ex) {

        } finally {
            try {
                if (fosgroup != null)
                    fosgroup.close();
            } catch (IOException ex) {

            }
        }

    }
        //Сохранение информации о текущей группе//


        //Загрузка информации о текущей группе//
    public void LOAD_DATA(int week)
    {

        FileInputStream fin = null;
        String stringfortext = "";
        int c = 0;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String(bytes);


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

            int FIRSTNUMBERROW = 0 + WEEK_EVEN;
            for(int i = 0; i < 24; i++) {
                Subject[i].setText(Subject_text[FIRSTNUMBERROW]);
                Subject_text_rly[i] = Subject_text[FIRSTNUMBERROW];
                String sss = Subject_text[FIRSTNUMBERROW];
                for (Integer w = 8; w < 20; w++) {

                    String s1 = w.toString() + " н";
                    String s2 = w.toString() + " нед.";
                    String s3 = w.toString() + "н";
                    if (sss.contains(s1) | sss.contains(s2) | sss.contains(s3)) {
                        if (w < week) {
                            // Проверка на неделях

                            Subject[i].setText("\n\nЗанятия закончились");
                        }

                    }



                }
                FIRSTNUMBERROW += 2;
            }


        } catch (IOException ex) {

        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {

            }
        }




        FileInputStream fingroup = null;

        try {
            fingroup = openFileInput(FILE_NAME_GROUP);
            byte[] bytes = new byte[fingroup.available()];
            fingroup.read(bytes);
            GroupName = new String(bytes);

            TEXTVIEW_GROUP.setText(GroupName);


        } catch (IOException ex) {

        } finally {
            try {
                if (fingroup != null)
                    fingroup.close();
            } catch (IOException ex) {

            }
        }








    }
        //Загрузка информации о текущей группе//




}


