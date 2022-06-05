package com.example.smutable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
// bot menu enable!
// bot menu enable!
// bot menu enable!
// bot menu enable!
// bot menu enable!

public class activity_news extends AppCompatActivity {
    TextView curpagetext;
    TextView[] newstext = new TextView[10];
    TextView[] newstext_header = new TextView[10];
    TextView[] newstext_date = new TextView[10];
    ImageView[] newsimage = new ImageView[10];
    Button[] newsbuttons = new Button[10];
    View background;
    ProgressBar loadingcircle;
    NestedScrollView svnews;
    LinearLayout pageslin;
    Button prevpage, nextpage;
    String[] maintext = new String[10];
    String[] shortmaintext = new String[10];
    String[] headertext = new String[10];
    String[] datetext = new String[10];
    String[] urlnews = new String[10];
    int specint = 0;
    int avgsymbols = 130;
    int curpage = 1;
    private Document doc;
    private Document docnews;
    private Thread secThread;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        curpagetext = findViewById(R.id.curpagetextview);
        pageslin = findViewById(R.id.pageslin);
        prevpage = findViewById(R.id.pageprev);
        nextpage = findViewById(R.id.pagenext);
        svnews = findViewById(R.id.scrollviewnews);
        background = findViewById(R.id.backview);
        loadingcircle = findViewById(R.id.progcircle);
        newstext[0] = findViewById(R.id.Block1_text);
        newstext[1] = findViewById(R.id.Block2_text);
        newstext[2] = findViewById(R.id.Block3_text);
        newstext[3] = findViewById(R.id.Block4_text);
        newstext[4] = findViewById(R.id.Block5_text);
        newstext[5] = findViewById(R.id.Block6_text);
        newstext[6] = findViewById(R.id.Block7_text);
        newstext[7] = findViewById(R.id.Block8_text);
        newstext[8] = findViewById(R.id.Block9_text);
        newstext[9] = findViewById(R.id.Block10_text);
        newsimage[0] = findViewById(R.id.imageViewNews1);
        newsimage[1] = findViewById(R.id.imageViewNews2);
        newsimage[2] = findViewById(R.id.imageViewNews3);
        newsimage[3] = findViewById(R.id.imageViewNews4);
        newsimage[4] = findViewById(R.id.imageViewNews5);
        newsimage[5] = findViewById(R.id.imageViewNews6);
        newsimage[6] = findViewById(R.id.imageViewNews7);
        newsimage[7] = findViewById(R.id.imageViewNews8);
        newsimage[8] = findViewById(R.id.imageViewNews9);
        newsimage[9] = findViewById(R.id.imageViewNews10);
        newstext_header[0] = findViewById(R.id.Block1_header);
        newstext_header[1] = findViewById(R.id.Block2_header);
        newstext_header[2] = findViewById(R.id.Block3_header);
        newstext_header[3] = findViewById(R.id.Block4_header);
        newstext_header[4] = findViewById(R.id.Block5_header);
        newstext_header[5] = findViewById(R.id.Block6_header);
        newstext_header[6] = findViewById(R.id.Block7_header);
        newstext_header[7] = findViewById(R.id.Block8_header);
        newstext_header[8] = findViewById(R.id.Block9_header);
        newstext_header[9] = findViewById(R.id.Block10_header);
        newstext_date[0] = findViewById(R.id.Block1_date);
        newstext_date[1] = findViewById(R.id.Block2_date);
        newstext_date[2] = findViewById(R.id.Block3_date);
        newstext_date[3] = findViewById(R.id.Block4_date);
        newstext_date[4] = findViewById(R.id.Block5_date);
        newstext_date[5] = findViewById(R.id.Block6_date);
        newstext_date[6] = findViewById(R.id.Block7_date);
        newstext_date[7] = findViewById(R.id.Block8_date);
        newstext_date[8] = findViewById(R.id.Block9_date);
        newstext_date[9] = findViewById(R.id.Block10_date);
        newsbuttons[0] = findViewById(R.id.Block1_button);
        newsbuttons[1] = findViewById(R.id.Block2_button);
        newsbuttons[2] = findViewById(R.id.Block3_button);
        newsbuttons[3] = findViewById(R.id.Block4_button);
        newsbuttons[4] = findViewById(R.id.Block5_button);
        newsbuttons[5] = findViewById(R.id.Block6_button);
        newsbuttons[6] = findViewById(R.id.Block7_button);
        newsbuttons[7] = findViewById(R.id.Block8_button);
        newsbuttons[8] = findViewById(R.id.Block9_button);
        newsbuttons[9] = findViewById(R.id.Block10_button);


        init();
        prevpage.setEnabled(false);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curpage++;
                if (curpage > 1) {
                    prevpage.setEnabled(true);
                }
                String s = "" + curpage;
                curpagetext.setText(s);
                svnews.scrollTo(svnews.getScrollX(), 0);
                loadingcircle.setVisibility(View.VISIBLE);
                background.setVisibility(View.VISIBLE);
                init();
            }
        });

        prevpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curpage--;
                if (curpage < 2) {
                    prevpage.setEnabled(false);
                }
                String s = "" + curpage;
                curpagetext.setText(s);
                svnews.scrollTo(svnews.getScrollX(), 0);
                loadingcircle.setVisibility(View.VISIBLE);
                background.setVisibility(View.VISIBLE);
                init();
            }
        });

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            newsbuttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 clickbuttonnews(finalI);
                }
            });
        }
    }


    public void clickbuttonnews(int a){
        if(newstext[a].getText() == maintext[a]){
            newstext[a].setText(shortmaintext[a]);
            newsbuttons[a].setText("Читать полностью");
            svnews.scrollBy(0,newsimage[a].getTop());
        }
        else
        {
            newstext[a].setText(maintext[a]);
            newsbuttons[a].setText("Скрыть");

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    private void init()
    {
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };

        secThread = new Thread(runnable);
        secThread.start();


    }



    private void getWeb()
    {

        String str = "https://guu.ru/category/news_ru/page/" + curpage;
        try {
            doc = Jsoup.connect(str).get();
            Element content = doc.getElementById("content");
            Elements links  = content.getElementsByClass("img-holder thumbnail pull-left");
                int a = 0;
            for(Element link : links){
                String linkHref = link.attr("href");
                String linkText = link.text();
                urlnews[a] = linkHref;
                a++;

            }
            for(int i = 0; i < 10; i++){
                docnews = Jsoup.connect(urlnews[i]).get();
                Element contentnew = docnews.getElementById("content");
              //  Elements classp = contentnew.getElementsByTag("p");
                Elements classp = contentnew.getElementsByClass("cf");
                Elements cfp = classp.select("p");
                Elements cfh2 = classp.select("h2");
                Element imageElement = contentnew.select("img").first();
                String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src
                String srcValue = imageElement.attr("src");  //
                String eventdate = contentnew.getElementsByClass("event-date").text();
                String text = cfp.text();
                maintext[i] = text;
                headertext[i] = cfh2.text();
                datetext[i] = eventdate;
                if(maintext[i].length() > avgsymbols+40){
                    for(int w = avgsymbols-10; w < avgsymbols+40; w++){
                        if(maintext[i].charAt(w) == ' ')
                        {
                            shortmaintext[i] = maintext[i].substring(0,w) + "...";
                            break;
                        }
                        else
                        {
                            shortmaintext[i] = maintext[i].substring(0,avgsymbols+40) + "...";
                        }
                    }
                }
                else
                {
                    shortmaintext[i] = maintext[i];
                }
                specint = i;

                newstext[i].post(new Runnable() {
                    @Override
                    public void run() {
                        newstext_header[specint].setText(headertext[specint]);
                        newstext[specint].setText(shortmaintext[specint]);
                        newstext_date[specint].setText(eventdate);
                        new DownloadImageTask(newsimage[specint]).execute(srcValue);
                        if(specint == 9){
                            loadingcircle.setVisibility(View.INVISIBLE);
                            background.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}