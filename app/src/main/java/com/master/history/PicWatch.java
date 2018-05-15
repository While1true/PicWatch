package com.master.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/3/28.
 */

public class PicWatch extends AppCompatActivity {
    String http = "http://47.106.176.142/masterWeiBo/getMycloud100?";
    private View progress;
    private WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchxxx);
        webview = findViewById(R.id.webview);
        progress = findViewById(R.id.progressx);
        TextView tv = findViewById(R.id.title);
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv.setText("我的云图");
        final String user = getIntent().getStringExtra("user");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = NetUtils.get(http + "user=" + "1ec5b436727ddc245b8e2a984eab14b3");
                if(str==null){
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<Base<List<Data2>>>() {
                }.getType();
                final Base<List<Data2>> bean = gson.fromJson(str, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progress.setVisibility(View.GONE);
                            String style="<style type=\"text/css\">#ff{border:1px solid #666666;margin:3px;}#xx{display: grid;grid-template-columns: 33% 33% 33%;grid-template-rows: auto auto auto;}img{width:100%;height:auto;}</style>";
                            String url="";
                            for (Data2 data2 : bean.getData()) {
                                url+=String.format(" <div id=\"ff\"><p>日期：%s</p><img src=\"%s\"></img></div>",data2.getDate(),data2.getUrl());
                            }
                            url="<div id=\"xx\">"+url+"</div>";
                            webview.loadData(style+url,"text/html","utf-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }


}
