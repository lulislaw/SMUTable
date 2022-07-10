package com.example.smutable;



import static com.example.smutable.R.layout.activity_main;
import static com.example.smutable.R.layout.activity_selection;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.color.MaterialColors;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
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
    TextView[] thtime = new TextView[4];
    ConstraintLayout[] Subject = new ConstraintLayout[4];
    ImageView[] onBlockImageNotes = new ImageView[4];
    TextView[] NameSubject = new TextView[4];
    TextView[] TeacherSubject = new TextView[4];
    TextView[] TypeSubject = new TextView[4];
    TextView[] RoomSubject = new TextView[4];
    Button[] DAYS_BUTTON = new Button[3];
    ConstraintLayout[] DAYS_BUTTON_GROUP = new ConstraintLayout[3];
    TextView[] DAYS_BUTTON_TEXT = new TextView[3];
    TextView[] DAYS_BUTTON_TEXT2 = new TextView[3];
    String[] Subject_text = new String[48];
    int SHEET_ID, COLLUMN_ID, ROW_ID, URL_ID, DAY_ID;
    int WEEK_EVEN;
    int SpecialWeekOfYear, CurrentWeekOfYear, UsingWeekOfYear;
    int selectedday;
    int[] thtimecurhours = new int[4];
    int[] thtimecurminutes = new int[4];
    Integer mYear,mMonth,mdayOfMonth;
    String nMonth, nDayOfMonth;
    Integer nYear;
    String[] temp_string = new String[4];
    String text, GroupName;
    TextView INFO_BLOCK, TEXTVIEW_GROUP, TEXTVIEW_WEEK, TEXTVIEW_DATE;
    Button CLOSE_INFO_BLOCK;
    ImageButton BUTTON_SET_WEEK;
    Button BUTTON_TO_SELECTION, BUTTON_REFRESH, BUTTON_TO_NEWS, BUTTON_TO_SEARCH, BUTTON_TO_NOTES;

    Intent INTENT_TO_SELECTION,INTENT_TO_NEWS, INTENT_TO_SEARCH, INTENT_TO_NOTES, INTENT_TO_CREATE_NOTE;
    ImageView onBlockImageNotes_1, onBlockImageNotes_2, onBlockImageNotes_3, onBlockImageNotes_4;

    String checkIf;



    SharedPreferences sharedPreferences;
    boolean nightMODE;


    CalendarView CALENDAR_VIEW;
    LocalDate date, selecteddate;
    ScrollView Scroll;
    Animation animation_scale;
    ImageButton poligon_left, poligon_right;
    LocalDate[] datesbutton = new LocalDate[3];
    int[] thtimeinthours = {
            8, 9, 11, 13, 13, 15, 17, 18, 20
    };
    int[] thtimeintminutes = {
            15, 55, 35, 5, 45, 25, 5, 50, 30
    };
    String[] time = {
      "8:15 - 9:45", "9:55 - 11:25","11:35 - 13:05","13:15 - 14:45", "13:45 - 15:15", "15:25 - 16:55", "17:05 - 18:35","18:50 - 20:20", "20:30 - 22:00"
    };
    String[] timedef = {
            "8.15", "9.55","11.35","13.15", "13.45", "15.25", "17.05","18.50", "20.30"
    };
    String[] monthru = {
      "Января","Февраля","Марта","Апреля","Мая","Июня","Июля","Августа","Сентября","Октября","Ноября","Декабря"
    };
    String[] daysofweeks_string = {
            "Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"
    };
    Character[] alphabet = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false);

        if (nightMODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        WEEK_EVEN = 1;
        GroupName = "";
        {
            date = LocalDate.of
                    (2022,
                    2,
                    6);

        }
        poligon_left = findViewById(R.id.poligon_left);
        poligon_right = findViewById(R.id.poligon_right);
        DAYS_BUTTON_GROUP[0] = findViewById(R.id.DAY_BUTTON_GROUP_LEFT);
        DAYS_BUTTON_GROUP[1] = findViewById(R.id.DAY_BUTTON_GROUP_CENTER);
        DAYS_BUTTON_GROUP[2] = findViewById(R.id.DAY_BUTTON_GROUP_RIGHT);
        DAYS_BUTTON_TEXT[0] = findViewById(R.id.but_day_left);
        DAYS_BUTTON_TEXT[1] = findViewById(R.id.but_day_cen);
        DAYS_BUTTON_TEXT[1] = findViewById(R.id.but_day_cen);
        DAYS_BUTTON_TEXT[2] = findViewById(R.id.but_day_right);
        DAYS_BUTTON_TEXT2[0] = findViewById(R.id.but_month_left);
        DAYS_BUTTON_TEXT2[1] = findViewById(R.id.but_month_cen);
        DAYS_BUTTON_TEXT2[2] = findViewById(R.id.but_month_right);
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
        thtime[0] = findViewById(R.id.thtime1);
        thtime[1] = findViewById(R.id.thtime2);
        thtime[2] = findViewById(R.id.thtime3);
        thtime[3] = findViewById(R.id.thtime4);
        DAYS_BUTTON[0] = findViewById(R.id.DAY_BUTTON_LEFT);
        DAYS_BUTTON[1] = findViewById(R.id.DAY_BUTTON_CENTER);
        DAYS_BUTTON[2] = findViewById(R.id.DAY_BUTTON_RIGHT);
        INFO_BLOCK = findViewById(R.id.INFO_BLOCK);
        TEXTVIEW_GROUP = findViewById(R.id.TEXTVIEW_GROUP);
        TEXTVIEW_WEEK = findViewById(R.id.TEXTVIEW_WEEK);
        TEXTVIEW_DATE = findViewById(R.id.TEXTVIEW_DATE);
        CLOSE_INFO_BLOCK = findViewById(R.id.CLOSE_INFO_BLOCK);
        BUTTON_REFRESH = findViewById(R.id.home_button_inside);
        BUTTON_TO_SELECTION = findViewById(R.id.settings_button_inside);
        BUTTON_TO_NEWS = findViewById(R.id.news_button_inside);
        BUTTON_TO_SEARCH = findViewById(R.id.search_button_inside);
        BUTTON_TO_NOTES = findViewById(R.id.note_button_inside);

        onBlockImageNotes_1 = findViewById(R.id.onBlockImageNotes_1);
        onBlockImageNotes_2 = findViewById(R.id.onBlockImageNotes_2);
        onBlockImageNotes_3 = findViewById(R.id.onBlockImageNotes_3);
        onBlockImageNotes_4 = findViewById(R.id.onBlockImageNotes_4);

        onBlockImageNotes[0] = findViewById(R.id.onBlockImageNotes_1);
        onBlockImageNotes[1] = findViewById(R.id.onBlockImageNotes_2);
        onBlockImageNotes[2] = findViewById(R.id.onBlockImageNotes_3);
        onBlockImageNotes[3] = findViewById(R.id.onBlockImageNotes_4);

        BUTTON_SET_WEEK = findViewById(R.id.calendar);
        CALENDAR_VIEW = findViewById(R.id.calendarView);
        Scroll = findViewById(R.id.nestedScrollView);
        animation_scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
        CurrentWeekOfYear = (LocalDate.now().getDayOfYear() - date.getDayOfYear()) / 7 + 1;
        CALENDAR_VIEW.setVisibility(View.GONE);
        CALENDAR_VIEW.setClickable(false);
        INTENT_TO_NOTES = new Intent(MainActivity.this, NotesActivity.class);
        INTENT_TO_SELECTION = new Intent(MainActivity.this, SelectionActivity.class);
        INTENT_TO_NEWS = new Intent(MainActivity.this, activity_news.class);
        INTENT_TO_SEARCH = new Intent(MainActivity.this, search_activity.class);

        INTENT_TO_CREATE_NOTE = new Intent(MainActivity.this, CreateNoteActivitySecond.class);

        url[0] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true";
        url[1] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/SECONDCOURSE.xls?raw=true";
        url[2] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/THIRDCOURSE.xls?raw=true";
        url[3] = "https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FOURCOURSE.xls?raw=true";
        ROW_ID = 8;


        FileInputStream fins = null;
                String text_selection = "";
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
                    startActivity(INTENT_TO_SELECTION);
                    overridePendingTransition(0,0);
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                poligon_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedday == 0)
                        {
                            selecteddate = LocalDate.ofEpochDay(selecteddate.toEpochDay() - 1);
                        }
                        if (selectedday == 2) {

                            DAYS_BUTTON[1].callOnClick();
                        }

                        else {

                            DAYS_BUTTON[0].callOnClick();

                        }
                    }
                });

        poligon_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedday == 2)
                {
                    selecteddate = LocalDate.ofEpochDay(selecteddate.toEpochDay() + 1);
                }
                if (selectedday == 0)
                    DAYS_BUTTON[1].callOnClick();
                else {
                    DAYS_BUTTON[2].callOnClick();
                }
            }
        });

                for(int i = 0; i <3;i++)
                {
                    int finalI = i;
                    DAYS_BUTTON[i].setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {
                            for(int a = 0; a < 3; a++)
                            {
                                DAYS_BUTTON_GROUP[a].setBackground(getDrawable(R.drawable.strokecorners_grey));
                                DAYS_BUTTON_TEXT[a].setTextColor(MaterialColors.getColor(DAYS_BUTTON_TEXT[finalI], R.attr.colorSecondaryVariant));
                                DAYS_BUTTON_TEXT2[a].setTextColor(MaterialColors.getColor(DAYS_BUTTON_TEXT[finalI], R.attr.colorSecondaryVariant));
                            }
                            DAYS_BUTTON_GROUP[finalI].setBackground(getDrawable(R.drawable.strokecorners));
                            DAYS_BUTTON_TEXT[finalI].setTextColor(MaterialColors.getColor(DAYS_BUTTON_TEXT[finalI], R.attr.colorOnPrimary));
                            DAYS_BUTTON_TEXT2[finalI].setTextColor(MaterialColors.getColor(DAYS_BUTTON_TEXT[finalI], R.attr.colorOnPrimary));
                            selectedday = finalI;
                            DAY_ID = LocalDate.ofEpochDay(selecteddate.toEpochDay()-(Math.abs(2- finalI))).getDayOfWeek().getValue();
                            UsingWeekOfYear = ((LocalDate.ofEpochDay(selecteddate.toEpochDay() - (Math.abs(2 - finalI))).getDayOfYear() - date.getDayOfYear()) / 7 + 1);
                            if(DAY_ID > 6)
                                DAY_ID = 0;
                            if(DAY_ID <0)
                                DAY_ID = 6;
                             if(UsingWeekOfYear % 2 == 0) {
                                TEXTVIEW_WEEK.setText("Четная" + "(" + UsingWeekOfYear + ")");
                                WEEK_EVEN = 1;
                            }
                            else {
                                WEEK_EVEN = 0;
                                TEXTVIEW_WEEK.setText("Нечетная" + "(" + UsingWeekOfYear + ")");
                            }
                            TEXTVIEW_DATE.setText(daysofweeks_string[DAY_ID] + ", "  +
                                    LocalDate.ofEpochDay(selecteddate.toEpochDay()-(Math.abs(2- finalI))+1).getDayOfMonth() + " " +
                                    monthru[LocalDate.ofEpochDay(selecteddate.toEpochDay()+1-(Math.abs(2- finalI))).getMonth().getValue()-1]);

                            Integer dayToNote = LocalDate.ofEpochDay(selecteddate.toEpochDay()-(Math.abs(2- finalI))+1).getDayOfMonth();
                            INTENT_TO_CREATE_NOTE.putExtra("Day", dayToNote.toString());

                            SET_TEXT_DAYS();


                            LOAD_DATA(UsingWeekOfYear);
                        }
                    });
                }

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

                            TEXTVIEW_WEEK.setText("Четная" + "(" + UsingWeekOfYear + ")");
                            WEEK_EVEN = 1;
                        }
                        else {
                            WEEK_EVEN = 0;
                            TEXTVIEW_WEEK.setText("Нечетная" + "(" + UsingWeekOfYear + ")");
                        }
                    TEXTVIEW_DATE.setText(daysofweeks_string[LocalDate.now().getDayOfWeek().getValue()-1] + ", "  + LocalDate.now().getDayOfMonth() + " " + monthru[LocalDate.now().getMonth().getValue()-1]);
                    DAY_ID = LocalDate.now().getDayOfWeek().getValue() - 1;
                    selecteddate = LocalDate.now();
                    UsingWeekOfYear = CurrentWeekOfYear;
                    LOAD_DATA(UsingWeekOfYear);
                    DOWNLOAD_DATA();
                    SAVE_DATA();
                    DAYS_BUTTON[1].callOnClick();


                }
            });

            BUTTON_SET_WEEK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CLOSE_INFO_BLOCK.setVisibility(View.VISIBLE);
                    CLOSE_INFO_BLOCK.setClickable(true);
                    CALENDAR_VIEW.setVisibility(View.VISIBLE);
                    CALENDAR_VIEW.setClickable(true);

                    CALENDAR_VIEW.startAnimation(animation_scale);

                }
            });
                Scroll.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this)
                {
                    public void onSwipeLeft(){
                        poligon_right.callOnClick();
                    }

                    @SuppressLint("ClickableViewAccessibility")
                    public void onSwipeRight()
                    {
                        poligon_left.callOnClick();
                    }

                });
        SpecialWeekOfYear = CurrentWeekOfYear;
        CALENDAR_VIEW.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month + 1;
                mdayOfMonth = dayOfMonth;


                SpecialWeekOfYear = (LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfYear() - date.getDayOfYear()) / 7 + 1;
                UsingWeekOfYear = SpecialWeekOfYear;
                selecteddate = LocalDate.of(mYear,mMonth,mdayOfMonth);
                DAY_ID =(LocalDate.of(mYear,mMonth,mdayOfMonth).getDayOfWeek().getValue()-1);
                LOAD_DATA(UsingWeekOfYear);
                CALENDAR_VIEW.setVisibility(View.GONE);
                CALENDAR_VIEW.setClickable(false);
                CLOSE_INFO_BLOCK.setVisibility(View.GONE);
                CLOSE_INFO_BLOCK.setClickable(false);
                DAYS_BUTTON[1].callOnClick();

            }
        });

        UsingWeekOfYear = CurrentWeekOfYear;
        try {
            BUTTON_REFRESH.callOnClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DOWNLOAD_DATA();


        //////////
        Bundle bundle = getIntent().getExtras();
        try {
            checkIf = bundle.getString("checkIf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ////////// Я ТВОЙ МАМА ТРАХАЛ


        if (Objects.equals(checkIf, "createNote")) {

            SpecialWeekOfYear = CurrentWeekOfYear;

            nMonth = bundle.getString("MonthFromNote");
            nDayOfMonth = bundle.getString("DayFromNote");
            nYear = bundle.getInt("YearFromNote");

            if (Objects.equals(nMonth, "сентября"))
                nMonth = "9";
            else if (Objects.equals(nMonth, "октября"))
                nMonth = "10";
            else if (Objects.equals(nMonth, "ноября"))
                nMonth = "11";
            else if (Objects.equals(nMonth, "декабря"))
                nMonth = "12";
            else if (Objects.equals(nMonth, "января"))
                nMonth = "1";
            else if (Objects.equals(nMonth, "февраля"))
                nMonth = "2";
            else if (Objects.equals(nMonth, "марта"))
                nMonth = "3";
            else if (Objects.equals(nMonth, "апреля"))
                nMonth = "4";
            else if (Objects.equals(nMonth, "мая"))
                nMonth = "5";
            else if (Objects.equals(nMonth, "июня"))
                nMonth = "6";
            else if (Objects.equals(nMonth, "июля"))
                nMonth = "7";
            else if (Objects.equals(nMonth, "августа"))
                nMonth = "8";

            int inMonth = Integer.parseInt(nMonth);
            int inDayOfMonth = Integer.parseInt(nDayOfMonth);

            SpecialWeekOfYear = (LocalDate.of(nYear, inMonth, inDayOfMonth).getDayOfYear() - date.getDayOfYear()) / 7 + 1;
            UsingWeekOfYear = SpecialWeekOfYear;
            selecteddate = LocalDate.of(nYear, inMonth, inDayOfMonth);
            DAY_ID =(LocalDate.of(nYear, inMonth, inDayOfMonth).getDayOfWeek().getValue()-1);
            LOAD_DATA(UsingWeekOfYear);
            DAYS_BUTTON[1].callOnClick();
        }

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            int timertime = 0;
            @Override
            public void run() {
                timertime++;
                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {
                        for(int i =0; i < 4; i++) {

                            Date datetime = new Date();
                            thtime[i].setVisibility(View.VISIBLE);
                            int timeb_h = (thtimecurhours[i] - datetime.getHours());
                            int timeb_m = (thtimecurminutes[i] - datetime.getMinutes());
                            int sumtime_hm = 0;
                            Boolean f = false;
                            if(timeb_h > 0)
                                sumtime_hm = timeb_h * 60 + timeb_m;
                            if(timeb_h == 0) {
                                sumtime_hm = timeb_m;

                            }
                            if(timeb_h<0) {
                                sumtime_hm = Math.abs(timeb_h * 60 + timeb_m);
                                f = true;
                            }
                            if(!f){
                                if(sumtime_hm > 60)
                                thtime[i].setText("Через " + (sumtime_hm / 60) + " ч. " + (sumtime_hm % 60) + " м.");
                                else {
                                    thtime[i].setText("Через " + (sumtime_hm) + " м.");
                                    if (sumtime_hm >= 0 && sumtime_hm < 10)
                                        thtime[i].setTextColor(getColor(R.color.red));
                                }
                                if(sumtime_hm < 0)
                                {
                                    thtime[i].setText("Идет "+ (Math.abs(sumtime_hm)) + " м.");
                                    thtime[i].setTextColor(getColor(R.color.alsoftblue));
                                }
                            }
                            else
                            {
                                thtime[i].setTextColor(getColor(R.color.alsoftblue));
                                if(sumtime_hm > 60)
                                    thtime[i].setText("Идет " + (sumtime_hm / 60) + " ч. " + (sumtime_hm % 60) + " м.");
                                else
                                    thtime[i].setText("Идет "+ (sumtime_hm) + " м.");
                                if(sumtime_hm > 90)
                                    thtime[i].setText("");

                            }
                        }
                    }
                });

            }
        }, 0, 1000);
    }

        public void DOWNLOAD_DATA()
        {
            client = new AsyncHttpClient();
            client.get(url[URL_ID], new FileAsyncHttpResponseHandler(this) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    try{
                        LOAD_DATA(UsingWeekOfYear);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Нет подключения\nЗагружена последняя информация", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {

                    WorkbookSettings WORKBOOK_SETTING = new WorkbookSettings();
                    WORKBOOK_SETTING.setGCDisabled(true);

                    if (file != null) {
                        try {
                            workbook = workbook.getWorkbook(file);
                            Sheet sheet = workbook.getSheet(SHEET_ID);

                            GroupName =  sheet.getName().charAt(0) +"-" + sheet.getCell(COLLUMN_ID, 7).getContents() +" "+ sheet.getCell(COLLUMN_ID, 5).getContents();

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


      public void SET_TEXT_DAYS()
        {

            DAYS_BUTTON_TEXT[0].setText(LocalDate.ofEpochDay(selecteddate.toEpochDay()-1).getDayOfMonth()+"");
            DAYS_BUTTON_TEXT[1].setText(LocalDate.ofEpochDay(selecteddate.toEpochDay()).getDayOfMonth()+"");
            DAYS_BUTTON_TEXT[2].setText(LocalDate.ofEpochDay(selecteddate.toEpochDay()+1).getDayOfMonth()+"");
            DAYS_BUTTON_TEXT2[0].setText(monthru[LocalDate.ofEpochDay(selecteddate.toEpochDay()-1).getMonth().getValue()-1]);
            DAYS_BUTTON_TEXT2[1].setText(monthru[LocalDate.ofEpochDay(selecteddate.toEpochDay()).getMonth().getValue()-1]);
            DAYS_BUTTON_TEXT2[2].setText(monthru[LocalDate.ofEpochDay(selecteddate.toEpochDay()+1).getMonth().getValue()-1]);
            datesbutton[0] = LocalDate.ofEpochDay(selecteddate.toEpochDay()-1);
            datesbutton[1] = LocalDate.ofEpochDay(selecteddate.toEpochDay());
            datesbutton[2] = LocalDate.ofEpochDay(selecteddate.toEpochDay()+1);
        }

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

            List<String> new_temp_string = new ArrayList<>();

           for(int i = 0; i < 4; i++) {
               String[] SPLIT = Subject_text[FIRSTNUMBERROW].split("t");

                    for(int t = 0;t < 9;t++)
                    {
                        if(SPLIT[0].contains(timedef[t])) {
                            TIME[i].setText(time[t]);
                            thtimecurhours[i] = thtimeinthours[t];
                            thtimecurminutes[i] = thtimeintminutes[t];


                        }
                    }

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


                for(int le = 0; le < SPLIT[1].length(); le++)
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
               TeacherSubject[i].setVisibility(View.VISIBLE);
               TypeSubject[i].setVisibility(View.VISIBLE);
                    NameSubject[i].setText(temp_string[0]);
                    new_temp_string.add(temp_string[0]);
                    TypeSubject[i].setText(temp_string[1]);
                    TeacherSubject[i].setText(temp_string[2]);
                    RoomSubject[i].setText(temp_string[3] + "\n");
                   if(temp_string[2] == "")
                       TeacherSubject[i].setVisibility(View.GONE);
                   else
                       TeacherSubject[i].setVisibility(View.VISIBLE);
                   if(temp_string[1] == "")
                       TypeSubject[i].setVisibility(View.GONE);
                   else
                       TypeSubject[i].setVisibility(View.VISIBLE);
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

           for (int i = 0; i < 4; i++) {

               int finalI = i;
               Integer finalI_string = finalI + 1;


               /*onBlockImageNotes[i].setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       INTENT_TO_CREATE_NOTE.putExtra("lesson", new_temp_string.get(finalI));
                       String monthToNote = monthru[LocalDate.ofEpochDay(selecteddate.toEpochDay()+1-(Math.abs(2- finalI))).getMonth().getValue()-1];

                       *//*if (monthToNote == "Сентября") monthToNote = "9";
                       else if (monthToNote == "Октября") monthToNote = "10";
                       else if (monthToNote == "Ноября") monthToNote = "11";
                       else if (monthToNote == "Декабря") monthToNote = "12";
                       else if (monthToNote == "Января") monthToNote = "1";
                       else if (monthToNote == "Февраля") monthToNote = "2";
                       else if (monthToNote == "Марта") monthToNote = "3";
                       else if (monthToNote == "Апреля") monthToNote = "4";
                       else if (monthToNote == "Мая") monthToNote = "5";
                       else if (monthToNote == "Июня") monthToNote = "6";
                       else if (monthToNote == "Июля") monthToNote = "7";
                       else if (monthToNote == "Августа") monthToNote = "8";*//*

                       INTENT_TO_CREATE_NOTE.putExtra("Month", monthToNote);
                       startActivity(INTENT_TO_CREATE_NOTE);

                   }
               });*/

               Subject[i].setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View view) {
                       INTENT_TO_CREATE_NOTE.putExtra("LessonNumber", finalI_string.toString());
                       INTENT_TO_CREATE_NOTE.putExtra("DayOfWeek", daysofweeks_string[DAY_ID]);
                       INTENT_TO_CREATE_NOTE.putExtra("lesson", new_temp_string.get(finalI));
                       String monthToNote = monthru[LocalDate.ofEpochDay(selecteddate.toEpochDay()+1-(Math.abs(2- finalI))).getMonth().getValue()-1];
                       INTENT_TO_CREATE_NOTE.putExtra("Month", monthToNote);

                       startActivity(INTENT_TO_CREATE_NOTE);

                       Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                       vibrator.vibrate(10);

                       return false;
                   }
               });

           }

        } catch (IOException ex) {
        } finally {

            if(DAY_ID == 6)
            {
                for(int i = 0; i<4;i++)
                    Subject[i].setVisibility(View.GONE);
                    TEXTVIEW_WEEK.setText("Выходной");

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


}


