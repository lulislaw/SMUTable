package com.example.smutable;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

public class activity_news extends AppCompatActivity {
    TextView[] newstext = new TextView[10];
    ImageView[] newsimage = new ImageView[10];
    private Document doc;
    private Document docnews;
    private Thread secThread;
    private Runnable runnable;
    String[] maintext = new String[10];
    String[] shortmaintext = new String[10];
    String[] headertext = new String[10];
    String[] urlnews = new String[10];
    boolean ready = false;
    int specint = 0;
    int avgsymbols = 230;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
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

        init();

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

    public void setnewstext(){
        for(int i = 0; i < 10; i++){

        }
    }

    private void getWeb()
    {
        try {
            doc = Jsoup.connect("https://guu.ru/category/news_ru/").get();
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
                Elements classp = contentnew.getElementsByTag("p");
                Element imageElement = contentnew.select("img").first();
                String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src
                String srcValue = imageElement.attr("src");  //
                String text = classp.text();
                maintext[i] = text;
                Log.d("MyLog", i + " - " + srcValue);
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
                        newstext[specint].setText(shortmaintext[specint]);
                       new DownloadImageTask(newsimage[specint]).execute(srcValue);
                    }
                });

            }



        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}