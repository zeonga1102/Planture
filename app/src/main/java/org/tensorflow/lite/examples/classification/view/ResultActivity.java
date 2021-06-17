package org.tensorflow.lite.examples.classification.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.lite.examples.classification.CrawlVo;
import org.tensorflow.lite.examples.classification.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ResultActivity extends AppCompatActivity {

    List<String> imgUrls  = new ArrayList<String>();

    //HashMap<String,String> map = new HashMap<>();

    private MeasuredViewPager viewPager;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        String flowerName = (String)intent.getExtras().get("result");

        TextView textExplain = findViewById(R.id.text_explain);
        TextView textName = findViewById(R.id.text_name);

        textName.setText(flowerName);

        BackgroundTask async=new BackgroundTask(flowerName);
        try {
            CrawlVo vo = async.execute().get();
            imgUrls = vo.getImgSrcList();
            textExplain.setText(vo.getContents());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);
        viewPager.setAdapter(new ViewpagerAdapter(this, imgUrls));

        button = findViewById(R.id.btn_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

class BackgroundTask extends AsyncTask<Integer , Integer , CrawlVo> {

    private String flowerName;

    public BackgroundTask(String flowerName){
        this.flowerName = flowerName;
    }

    @Override
    protected CrawlVo doInBackground(Integer... integers) {

        String url = "https://ko.wikipedia.org/wiki/"+flowerName;
        Log.e("ksy", flowerName);
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements pElements = doc.getElementsByTag("P");
        String desc = pElements.get(0).text();


        String imgSrc = "https://www.google.co.kr/search?q=" + flowerName + "&sxsrf=ALeKk01jh0eiM_TSzZd57Tq41MUha2oStA:1620287330088&source=lnms&tbm=isch&sa=X&ved=2ahUKEwj3r_nVyLTwAhUn7GEKHc2hAFcQ_AUoAXoECAEQAw&biw=1536&bih=719&dpr=1.25";
        try {
            doc = Jsoup.connect(imgSrc).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> imgSrcList = new ArrayList<String>();
        Elements imgElements = doc.getElementsByAttributeValue("class", "rg_i Q4LuWd");

        for (int i = 0; i < imgElements.size(); i++) {
            Element tmpEle = imgElements.get(i);
            String imgUrl = tmpEle.attr("data-src");

            if (imgUrl.length() > 10) {
                imgSrcList.add(imgUrl);
            }

            if (imgSrcList.size() == 5) break;
        }

        System.out.print(desc);

        System.out.println(imgSrcList);


        String imgUrl = null;
        CrawlVo vo = new CrawlVo();

        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < imgSrcList.size(); i++) {

            imgUrl = imgSrcList.get(i);
            stringList.add(imgUrl);


        }

        vo.setImgSrcList(stringList);
        vo.setContents(desc);


        return vo;
    }
}