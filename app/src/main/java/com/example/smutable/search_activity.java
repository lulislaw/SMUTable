package com.example.smutable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageButton searchbutton = findViewById(R.id.search);
        EditText edittext = findViewById(R.id.edittext);
        finishtext = findViewById(R.id.search_textview);
        filter_scroll_view = findViewById(R.id.filter_scrollview);
        filter_scroll_view.setVisibility(View.GONE);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String search_text = edittext.getText().toString();
                finishtext.setText("");
                searchconnectfail("https://github.com/lulislaw/ExcelFilesForAnroidGUU/blob/main/FIRSTCOURSE.xls?raw=true", search_text);

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


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
                                    String mess = sheet.getCell(1,8+s).getContents() +"\n" + sheet.getCell(2,8+s).getContents() + "\n" + sheet.getCell(3,8+s).getContents() +"\n" + sheet.getCell(4+c,8+s).getContents();
                                    if(sheet.getCell(4+c,8+s).getContents().contains(search_message))
                                    {
                                        if(!finishtext.getText().toString().contains(mess))
                                        {
                                            finishtext.setText(finishtext.getText().toString() + mess);

                                        }
                                    }
                                }
                            }

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