package com.rapidzz.yourmusicmap.view.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rapidzz.yourmusicmap.R;

import androidx.appcompat.app.AppCompatActivity;

public class PaypalWebView extends AppCompatActivity {


    public static final String ORG_DONATION_URL = "orgdonationurl";
    private String url= "";
    private WebView webView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_web_view);

        webView = (WebView) findViewById(R.id.givewebview);
       // ((TextView) findViewById(R.id.txtOrgTitle)).setText("Give To " + Organization.getInstance().getOrgdes());
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        url = "https://www.sandbox.paypal.com/cgi-bin/webscr?&cmd=_xclick&business=goldentiger2attack@gmail.com&currency_code=USD&no_shipping=1&amount=1&item_name=Change song";
        /*if(this.getIntent().hasExtra(ORG_DONATION_URL))
        {
            url = "";//this.getIntent().getStringExtra(ORG_DONATION_URL);
        }
*/
        if(url.isEmpty())
        {
            finish();
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);

    }
}
