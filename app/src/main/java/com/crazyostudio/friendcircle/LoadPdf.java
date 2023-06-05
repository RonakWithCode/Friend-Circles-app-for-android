package com.crazyostudio.friendcircle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.crazyostudio.friendcircle.databinding.ActivityLoadPdfBinding;

public class LoadPdf extends AppCompatActivity {
    ActivityLoadPdfBinding binding;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String pdf = getIntent().getStringExtra("pdf");
        if (pdf.isEmpty()) {
            Toast.makeText(this, "Fall", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            loadPdfFromUri(pdf);
        }

    }
    private void loadPdfFromUri(String pdf) {
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        binding.web.loadUrl("file:///android_asset/pdf_viewer.html?file=" + pdf);
    }
}