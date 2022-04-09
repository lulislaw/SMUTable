package com.example.smutable;


import static com.example.smutable.R.layout.activity_main;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

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
    TextView[] TIME = new TextView[4];
    ConstraintLayout[] Subject = new ConstraintLayout[4];
    TextView[] NameSubject = new TextView[4];
    TextView[] TeacherSubject = new TextView[4];
    TextView[] TypeSubject = new TextView[4];
    TextView[] RoomSubject = new TextView[4];
    Button[] DAYS_BUTTON = new Button[3];

    String[] Subject_text = new String[48];
    String[] Subject_text_rly = new String[24];                     //Массив в соответсвие с "Subject"
    int SHEET_ID, COLLUMN_ID, ROW_ID, URL_ID, DAY_ID;
    int WEEK_EVEN;     // 1 = EVEN , 0 = Non-even;
    int SpecialWeekOfYear, CurrentWeekOfYear, UsingWeekOfYear;
    int selectedday;
    Integer mYear,mMonth,mdayOfMonth;
    String[] temp_string = new String[4]; //0 - Предмет, 1 - тип, 2 - препод, 3 - аудитория
    String text, GroupName;
    TextView INFO_BLOCK, TEXTVIEW_GROUP, TEXTVIEW_WEEK, TEXTVIEW_DATE;
    Button CLOSE_INFO_BLOCK;
    Boolean NEED_DOWNLOAD;
    ImageButton BUTTON_SET_WEEK;
    ConstraintLayout BUTTON_TO_SELECTION, BUTTON_REFRESH;
    Intent INTENT_TO_SELECTION;
    CalendarView CALENDAR_VIEW;
    Boolean Calendar_enable, firstrun;
    LocalDate date, selecteddate;
    NestedScrollView Scroll;
    Animation animation_scale;

    String[] monthru = {
      "Января","Февраля","Марта","Апреля","Мая","Июня","Июля","Августа","Сентября","Октября","Ноября","Декабря"
    };
    String[] daysofweeks_string = {
            "Понедельник","Вторник","Среда","Четверг","Пяница","Суббота","Воскресенье"
    };
    Character[] alphabet = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        firstrun = true;




        Calendar_enable = false;
        WEEK_EVEN = 1;
        GroupName = "";
        {
            date = LocalDate.of
                    (2022,
                    2,
                    6);

        }

        //Начало подключения XML//


        NameSubject[0] = findViewById(R.id.NameSubject1);
        TeacherSubject[0] = findViewById(R.id.subjectteacher);
        TypeSubject[0] = findViewById(R.id.subjecttype);
        RoomSubject[0] = findViewById(R.id.subjectaud);
        NameSubject[1] = findViewById(R.id.NameSubject2);
        TeacherSubject[1] = findViewById(R.id.subjectteacher2);
        TypeSubject[1] = findViewById(R.id.subjecttype2);
        RoomSubject[1] = findViewById(R.id.subjectaud2);
        NameSubject[2] = findViewById(R.id.NameSubject3);
        TeacherSubject[2] = findViewById(R.id.subjectteacher3);
        TypeSubject[2] = findViewById(R.id.subjecttype3);
        RoomSubject[2] = findViewById(R.id.subjectaud3);
        NameSubject[3] = findViewById(R.id.NameSubject4);
        TeacherSubject[3] = findViewById(R.id.subjectteacher4);
        TypeSubject[3] = findViewById(R.id.subjecttype4);
        RoomSubject[3] = findViewById(R.id.subjectaud4);
        Subject[0] = findViewById(R.id.Subject_layout_1);
        Subject[1] = findViewById(R.id.Subject_layout_2);
        Subject[2] = findViewById(R.id.Subject_layout_3);
        Subject[3] = findViewById(R.id.Subject_layout_4);
        TIME[0] = findViewById(R.id.TIME_1);
        TIME[1] = findViewById(R.id.TIME_2);
        TIME[2] = findViewById(R.id.TIME_3);
        TIME[3] = findViewById(R.id.TIME_4);
        DAYS_BUTTON[0] = findViewById(R.id.DAY_BUTTON_LEFT);
        DAYS_BUTTON[1] = findViewById(R.id.DAY_BUTTON_CENTER);
        DAYS_BUTTON[2] = findViewById(R.id.DAY_BUTTON_RIGHT);
        INFO_BLOCK = findViewById(R.id.INFO_BLOCK);
        TEXTVIEW_GROUP = findViewById(R.id.TEXTVIEW_GROUP);
        TEXTVIEW_WEEK = findViewById(R.id.TEXTVIEW_WEEK);
        TEXTVIEW_DATE = findViewById(R.id.TEXTVIEW_DATE);
        CLOSE_INFO_BLOCK = findViewById(R.id.CLOSE_INFO_BLOCK);
        BUTTON_REFRESH = findViewById(R.id.home_group);
        BUTTON_TO_SELECTION = findViewById(R.id.settings_group);
        BUTTON_SET_WEEK = findViewById(R.id.calendar);
        CALENDAR_VIEW = findViewById(R.id.calendarView);
        Scroll = findViewById(R.id.nestedScrollView);


        //Конец подключения XML//

        animation_scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
        CurrentWeekOfYear = (LocalDate.now().getDayOfYear() - date.getDayOfYear()) / 7 + 1;
        CALENDAR_VIEW.setVisibility(View.GONE);
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



  /*      for(int i = 0; i< 4; i++) {
            int finalI = i;
            Subject[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SHOW_INFO(finalI);
                }
            });

        }

        for(int i = 0; i < 2; i++)
        {
            int finalI = i;
            DAYS_BUTTON[i].setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    for(int c = 0; c<2;c++) {
                        DAYS_BUTTON[c].setBackgroundTintList(getResources().getColorStateList(R.color.purple_200));

                    }
                    selectedday = finalI;
                    DAY_ID = finalI;
                    DAYS_BUTTON[finalI].setBackgroundTintList(getResources().getColorStateList(R.color.purple_500));
                    if(UsingWeekOfYear % 2 == 0) {
                        TEXTVIEW_WEEK.setText(LocalDate.ofEpochDay((selecteddate.toEpochDay())+(finalI+1-selecteddate.getDayOfWeek().getValue())).getDayOfMonth() + " " + monthru[LocalDate.ofEpochDay((selecteddate.toEpochDay())+(finalI+1-selecteddate.getDayOfWeek().getValue())).getMonth().getValue()-1] + ", четная");
                        WEEK_EVEN = 1;
                    }
                    else {
                        WEEK_EVEN = 0;
                        TEXTVIEW_WEEK.setText(LocalDate.ofEpochDay((selecteddate.toEpochDay())+(finalI+1-selecteddate.getDayOfWeek().getValue())).getDayOfMonth() + " " + monthru[LocalDate.ofEpochDay((selecteddate.toEpochDay())+(finalI+1-selecteddate.getDayOfWeek().getValue())).getMonth().getValue()-1] + ", нечетная");
                    }
                    LOAD_DATA(UsingWeekOfYear);
                }
            });
        }
*/

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
                INFO_BLOCK.setVisibility(View.GONE);
                INFO_BLOCK.setClickable(false);
                CLOSE_INFO_BLOCK.setVisibility(View.GONE);
                CLOSE_INFO_BLOCK.setClickable(false);
                CALENDAR_VIEW.setVisibility(View.GONE);
                CALENDAR_VIEW.setClickable(false);

            }
        });

            BUTTON_REFRESH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(CurrentWeekOfYear % 2 == 0) {
                        TEXTVIEW_WEEK.setText("Четная");
                        TEXTVIEW_DATE.setText(daysofweeks_string[LocalDate.now().getDayOfWeek().getValue()-1] + ", "  + LocalDate.now().getDayOfMonth() + " " + monthru[LocalDate.now().getMonth().getValue()-1]);
                        WEEK_EVEN = 1;
                    }
                    else {
                        WEEK_EVEN = 0;
                        TEXTVIEW_WEEK.setText("Нечетная");
                        TEXTVIEW_DATE.setText(daysofweeks_string[LocalDate.now().getDayOfWeek().getValue()-1] + ", "  + LocalDate.now().getDayOfMonth() + " " + monthru[LocalDate.now().getMonth().getValue()-1]);
                    }
                    DAY_ID = LocalDate.now().getDayOfWeek().getValue() - 1;
                    for(int c = 0; c<6;c++)
            //            DAYS_BUTTON[c].setBackgroundTintList(getResources().getColorStateList(R.color.purple_200));
                 //   DAYS_BUTTON[DAY_ID].setBackgroundTintList(getResources().getColorStateList(R.color.purple_500));
                    selecteddate = LocalDate.now();
               //     SET_TEXT_DAYS(LocalDate.now().getDayOfWeek().getValue(),LocalDate.now());
                    DOWNLOAD_DATA();
                    SAVE_DATA();
                    UsingWeekOfYear = CurrentWeekOfYear;
                    LOAD_DATA(UsingWeekOfYear);
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
                Scroll.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this)
                {
                    public void onSwipeLeft(){

                        Toast.makeText(MainActivity.this, "\nLEFT\n", Toast.LENGTH_SHORT).show();

                    }

                    @SuppressLint("ClickableViewAccessibility")
                    public void onSwipeRight()
                    {
                        Toast.makeText(MainActivity.this, "\nRIGHT\n", Toast.LENGTH_SHORT).show();

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

                if(SpecialWeekOfYear % 2 == 0) {
                    TEXTVIEW_WEEK.setText("Четная");
                    TEXTVIEW_DATE.setText(daysofweeks_string[LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue()-1] + ", " + LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfMonth() + " " + monthru[month]);
                    WEEK_EVEN = 1;
                }   else {
                    TEXTVIEW_WEEK.setText("Нечетная");
                    TEXTVIEW_DATE.setText(daysofweeks_string[LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue()-1] + ", "  + LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfMonth() + " " + monthru[month]);
                    WEEK_EVEN = 0;
                }
                    UsingWeekOfYear = SpecialWeekOfYear;
                selecteddate = LocalDate.of(mYear,mMonth,mdayOfMonth);
                DAY_ID =(LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue()-1);
                LOAD_DATA(UsingWeekOfYear);
               // SET_TEXT_DAYS(LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue(),LocalDate.of(mYear,mMonth,mdayOfMonth));
                CALENDAR_VIEW.setVisibility(View.GONE);
                CALENDAR_VIEW.setClickable(false);
                Calendar_enable = false;
                CLOSE_INFO_BLOCK.setVisibility(View.GONE);
                CLOSE_INFO_BLOCK.setClickable(false);
            }
        });

        UsingWeekOfYear = CurrentWeekOfYear;
        try {
            BUTTON_REFRESH.callOnClick();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                            String tempgroupname = sheet.getCell(COLLUMN_ID, 5).getContents().charAt(0) + "";
                            GroupName = sheet.getCell(COLLUMN_ID, 5).getContents();
                            for(int i = 0; i < GroupName.length()-1; i++)
                            {
                                if(GroupName.charAt(i) == ' ')
                                {
                                    tempgroupname = tempgroupname + GroupName.charAt(i+1);
                                }
                            }
                            GroupName = tempgroupname + " " + sheet.getName().charAt(0) +"-" + sheet.getCell(COLLUMN_ID, 7).getContents();
                            for (int i = 0; i < 48; i++) {
                                Subject_text[i] = (sheet.getCell(2, ROW_ID + i).getContents() + "t" + sheet.getCell(COLLUMN_ID, ROW_ID + i).getContents());
                            }

                            SAVE_DATA();
                            UsingWeekOfYear = CurrentWeekOfYear;
                            LOAD_DATA(UsingWeekOfYear);
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
            if(Calendar_enable == false) {
                INFO_BLOCK.setVisibility(View.VISIBLE);
                INFO_BLOCK.setClickable(true);
                INFO_BLOCK.setText(Subject_text_rly[a]);
                CLOSE_INFO_BLOCK.setVisibility(View.VISIBLE);
                CLOSE_INFO_BLOCK.setClickable(true);
                INFO_BLOCK.startAnimation(animation_scale);
            }
        }

    /*    public void SET_TEXT_DAYS(int day, LocalDate xDate)
        {
            int dayx1 = day-1;
            int dayx2 = day-1;
            long xDateEpoch = xDate.toEpochDay();
            long xDateEpoch1 = xDate.toEpochDay();
            while(dayx1 >= 0)
            {

                DAYS_BUTTON[dayx1].setText(Html.fromHtml("<b><big>"+LocalDate.ofEpochDay(xDateEpoch).getDayOfMonth()+"</big></b><br><small>"+daysofweeks_string[dayx1]+"</small>"));
                xDateEpoch = xDateEpoch -1;
                dayx1= dayx1 -1;
            }
            while(dayx2 <= 2)
            {
                DAYS_BUTTON[dayx2].setText(Html.fromHtml("<b><big>"+LocalDate.ofEpochDay(xDateEpoch1).getDayOfMonth()+"</big></b><br><small>"+daysofweeks_string[dayx2]+"</small>"));
                xDateEpoch1 = xDateEpoch1 + 1;
                dayx2= dayx2 +1;
            }

        }
*/
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


            int FIRSTNUMBERROW = 0 + WEEK_EVEN + (DAY_ID*8);
            if (FIRSTNUMBERROW > 40) {
                FIRSTNUMBERROW = 40;

            }
           for(int i = 0; i < 4; i++) {
                String[] SPLIT = Subject_text[FIRSTNUMBERROW].split("t");
                TIME[i].setText(SPLIT[0]);
                Subject_text_rly[i] = Subject_text[FIRSTNUMBERROW];
                String sss = Subject_text[FIRSTNUMBERROW];
                if(SPLIT[1].contains("(ЛЗ"))
                    temp_string[1] = "Лабораторная работа";
                else if(SPLIT[1].contains("(ПЗ"))
                    temp_string[1] = "Практическое занятие";
                else if(SPLIT[1].contains("(Л "))
                    temp_string[1] = "Лекция";
                else
                    temp_string[1] = "";
                temp_string[0] = "";


                for(int le = 0; le < SPLIT[1].length(); le++)                                                 //пилю текст по частям
                {
                    if(SPLIT[1].charAt(le) == '(') {
                        temp_string[0] = SPLIT[1].substring(0,le-1);
                        break;

                    }


                }

                int tempsc = 0;
                for(int le = 0; le < SPLIT[1].length(); le++)
                {
                    if(SPLIT[1].charAt(le) == ')') {
                        tempsc = le+2;
                        break;

                    }
                }
                if(temp_string[0].length() >= 33)
                    temp_string[0] = temp_string[0].substring(0,30) + "...";
                temp_string[3] = "";
                temp_string[2] = "";

                try {

                    if(SPLIT[1].contains("ЛК-")) {
                        temp_string[3] = SPLIT[1].substring(SPLIT[1].length() - 6, SPLIT[1].length());
                        temp_string[2] = SPLIT[1].substring(tempsc, SPLIT[1].length()-7);}
                    else if(SPLIT[1].contains("У-") || SPLIT[1].contains("А-") || SPLIT[1].contains("ПА-")) {
                        temp_string[3] = SPLIT[1].substring(SPLIT[1].length() - 5, SPLIT[1].length());
                        temp_string[2] = SPLIT[1].substring(tempsc, SPLIT[1].length()-6);
                    }
                    else if(SPLIT[1].contains("этаж")) {
                        temp_string[3] = SPLIT[1].substring(SPLIT[1].length() - 9, SPLIT[1].length());

                    }
                    else if(SPLIT[1].contains("Спортивный комплекс")) {
                        temp_string[3] = SPLIT[1].substring(SPLIT[1].length() - 19, SPLIT[1].length());
                    }
                    else
                        temp_string[3] = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                    if(temp_string[2].contains("\n"))
                    {
                        temp_string[2] = temp_string[2].substring(1);
                    }

                                                                                                                //распил окончен

                    NameSubject[i].setText(temp_string[0]);
                    TypeSubject[i].setText(temp_string[1]);
                    TeacherSubject[i].setText(temp_string[2]);
                    RoomSubject[i].setText(temp_string[3]);



                if(SPLIT[1].length() == 1)
                {
                    Subject[i].setVisibility(View.GONE);
                    TIME[i].setVisibility(View.GONE);
                }
                else
                {
                    Subject[i].setVisibility(View.VISIBLE);
                    TIME[i].setVisibility(View.VISIBLE);

                }

                for (Integer w = 8; w < 20; w++) {

                    String s1 = w.toString() + " н";
                    String s2 = w.toString() + " нед.";
                    String s3 = w.toString() + "н";
                    if (sss.contains(s1) | sss.contains(s2) | sss.contains(s3)) {
                        if (w < week) {
                            TIME[i].setVisibility(View.GONE);
                            Subject[i].setVisibility(View.GONE);
                        }
                        else
                        {
                            Subject[i].setVisibility(View.VISIBLE);
                            TIME[i].setVisibility(View.VISIBLE);
                        }
                    }
                }
                FIRSTNUMBERROW += 2;
            }
        } catch (IOException ex) {
        } finally {

            if(DAY_ID == 6)
            {
                for(int i = 0; i<4;i++)
                    Subject[i].setVisibility(View.GONE);

            }


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
            TEXTVIEW_GROUP.setVisibility(View.VISIBLE);
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


