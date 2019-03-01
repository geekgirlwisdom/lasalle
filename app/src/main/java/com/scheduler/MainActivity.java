package com.scheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.scheduler.*;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    protected Button btn_data_entry;
    protected Button btn_rewards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn_data_entry = new Button(this);
        btn_rewards = new Button(this);
        btn_data_entry.setOnClickListener(this);
        btn_rewards.setOnClickListener(this);
    }


    public void onClick(View view)
    {
        try {
            switch (view.getId())
            {
                case R.id.btn_data_entry:
                    showDataEntry(view);
                    break;
               // case R.id.btn_rewards:
                 //   showRewards(view);
                   // break;


            }
        }
        catch(Exception e)
        {
            Log.d("ActivityInterface",e.getMessage());
        }
    }

    public void showDataEntry(View view)
    {
        startActivity(new Intent("com.scheduler.DataEntryScreen"));
    }

    public void showRewards(View view)
    {
        startActivity(new Intent("com.scheduler.RewardsScreen"));
    }


}
