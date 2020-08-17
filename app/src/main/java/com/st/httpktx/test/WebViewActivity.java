package com.st.httpktx.test;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author ST 2020/6/19
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    private TextView tvTitle;

    private TextView tvStart;

    private TextView tvProgress;

    private TextView tvEnd;
    private Set<String>links=new HashSet<>();
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //获得控件
        webView = (WebView) findViewById(R.id.wv_webview);

        //获得其他控件
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        String cacheDirPath = this.getFilesDir().getAbsolutePath()+"cache/";
        settings.setAppCachePath(cacheDirPath);
        // 1. 设置缓存路径

        settings.setAppCacheMaxSize(20*1024*1024);
        // 2. 设置缓存大小

        settings.setAppCacheEnabled(true);
        // 3. 开启Application Cache存储机制

       // 特别注意
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        //        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        settings.setAppCacheEnabled(true);

//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //关闭密码保存提醒(密码明文存储漏洞)
        settings.setSavePassword(false);
        // 禁用 file 协议(保安设置)
        settings.setAllowFileAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //访问网页
        webView.loadUrl("http://zuidazy4.com/?m=vod-detail-id-89354.html");
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        //设置WebViewClient
        webView.setWebViewClient(new WebViewClient() {

            // 复写shouldInterceptRequest
            //API21以下用shouldInterceptRequest(WebView view, String url)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.e("21版本以下的资源url拦截",url);
                if (url.endsWith(".m3u8")){
                    Log.e("21版本以下的资源url拦截",url);
                }
                return super.shouldInterceptRequest(view, url);
            }
//
//
//            // API21以上用shouldInterceptRequest(WebView view, WebResourceRequest request)
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                Log.e("21版本以上的资源url拦截",request.getUrl().toString());
//                return super.shouldInterceptRequest(view, request);
//            }

            @SuppressLint("LongLogTag")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                Log.e("ST--->shouldOverrideUrlLoading加载的url",url);
                view.loadUrl(url);
                //返回true
                return true;
            }

            //加载前
            @SuppressLint("LongLogTag")
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("ST--->onPageStarted加载的url",url);
                tvStart.setText("开始加载！！");
            }

            //加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("ST--->加载的url",url);
                tvEnd.setText("加载完成...");
                // 在结束加载网页时会回调

                // 获取页面内容
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('html')[0].innerHTML);");

                // 获取解析<meta name="share-description" content="获取到的值">
                view.loadUrl("javascript:window.java_obj.showDescription("
                        + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                        + ");");
                super.onPageFinished(view, url);
            }

        });
        //设置WebChromeClient类
        webView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                tvTitle.setText(title);
            }

            //进度显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    tvProgress.setText(newProgress + "%");
                } else {
                    tvProgress.setText("100%");
                }
            }

        });
    }
    //点击返回上一页面而不是退出浏览器

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //销毁Webview
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
    public final class InJavaScriptLocalObj
    {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
            findUrlByStr(html);

        }

        @JavascriptInterface
        public void showDescription(String str) {
            System.out.println("====>html=" + str);
            Log.e("ST--->html",str);
        }
    }
    public  void findUrlByStr(String data){
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String link=matcher.group();
            if (link.endsWith(".mp4")||link.endsWith(".m3u8")){
                links.add(matcher.group());
            }
        }
        for (String s:links){
            Log.e("ST--->连接",s);
        }
////		data = "地球亚洲中国https://www.baidu.com/s?wd=java 厉害厉害！";
//        Pattern pattern = Pattern.compile("http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\\(\\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+");
//        Matcher matcher = pattern.matcher(data);
//        while (matcher.find()) {
//            Log.e("ST--->连接",matcher.group());
//        }
//        if (matcher.find()) {
//
//        }
    }
}
