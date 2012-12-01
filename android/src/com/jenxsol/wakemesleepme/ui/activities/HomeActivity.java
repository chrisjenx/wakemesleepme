package com.jenxsol.wakemesleepme.ui.activities;

import android.os.Bundle;
import android.view.Menu;

import com.jenxsol.wakemesleepme.R;

public class HomeActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

}
