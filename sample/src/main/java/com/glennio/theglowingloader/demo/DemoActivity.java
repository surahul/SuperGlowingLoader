package com.glennio.theglowingloader.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.glennio.theglowingloader.R;
import com.glennio.theglowingloader.demo.presenter.Presenter;
import com.glennio.theglowingloader.demo.presenter.PresenterImpl;
import com.glennio.theglowingloader.demo.view.ViewHelper;
import com.glennio.theglowingloader.demo.view.ViewHelperImpl;


public class DemoActivity extends AppCompatActivity {

    private ViewHelper viewHelper;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);

        View contentView = findViewById(android.R.id.content);
        presenter = new PresenterImpl(this);
        viewHelper = new ViewHelperImpl(contentView, presenter);
        presenter.setViewHelper(viewHelper);

    }

}
