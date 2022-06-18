package aiou.muslim.mttech;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Masjid extends AppCompatActivity {
    WebView webview;
    private static final String URL = "https://www.google.com/maps/search/masjid/@31.4826353,74.3343878,11z/data=!3m1!4b1";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid);
        webview = (WebView) findViewById(R.id.masjid1);
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(URL);

        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //your handling...

            webview.setVisibility(View.VISIBLE);
            return super.shouldOverrideUrlLoading(view, url);
        }
        public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
            Toast.makeText(Masjid.this, "Your Internet Connection May not be active Or " + description , Toast.LENGTH_LONG).show();
            webview.setVisibility(View.GONE);
        }
    }

}