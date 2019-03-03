package com.scheduler;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scheduler.db.TaskDBAdapter;
import com.scheduler.util.HelperUtil;

import java.io.File;
import java.io.FileOutputStream;
import android.content.Intent;
import android.widget.Toast;

public class RewardsScreen extends Activity implements View.OnClickListener  {
    TaskDBAdapter db;
    long parentid=0;
    TextView txtView  ;
    Cursor parentCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewardsscreen);

        String destPath = "/data/data/" + getPackageName() +   "/databases";
        File f = new File(destPath);
        try {

            db = new TaskDBAdapter(this);
        if (!f.exists())
        {
            f.mkdirs();
            f.createNewFile();
            HelperUtil.CopyDB(getBaseContext().getAssets().open("TaskDB.db"),  new FileOutputStream(destPath + "/TaskDB.db"));
        }
         db.openRead();
        parentCursor = db.getParentTask();
        if (parentCursor.moveToFirst())
            if (parentCursor != null)
                DisplayParent(parentCursor);

        Cursor c = db.getSubtasks( parentid );//db.getAllData();

        int ii=1;
        if (c.moveToFirst())
        {
            do {
                DisplayTask(c,ii);
                ii++;
            } while (c.moveToNext());
        }
        db.close();
            }
catch(Exception e)
            {
              Log.d("rewards",e.getMessage());
               e.printStackTrace();
            }
    }

    public void DisplayParent(Cursor c ) {
        try
        {
            TextView txt_task;
            ImageView iv_task;

            int i=0;
            long id=0;

            txt_task = (TextView)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
              iv_task = (ImageView)findViewById( getResources().getIdentifier(  "badge_"+i, "id", getPackageName())  );

            txt_task.setText(  c.getString(c.getColumnIndex("taskname")) );
            parentid=c.getInt(0);
            if ( HelperUtil.getBoolValue(c.getInt(c.getColumnIndex("completed_bool")) ))
                iv_task.setVisibility(View.VISIBLE);
            else {
                iv_task.setImageResource(R.drawable.bluecircle);
                iv_task.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception e) {
            Log.d("rewards",e.getMessage());
            e.printStackTrace();
        }

    }
    public void DisplayTask(Cursor c, int i) {
        try
        {
            TextView txt_task;
            ImageView iv_task;
            String taskname = "";
            long id=0;


            txt_task = (TextView)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
            iv_task = (ImageView)findViewById( getResources().getIdentifier(  "badge_"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"));

            if ( taskname != "" )
            {

                txt_task.setText( taskname );
                if ( HelperUtil.getBoolValue(c.getInt(c.getColumnIndex("completed_bool")) ))
                    iv_task.setVisibility(View.VISIBLE);
                else {
                    iv_task.setImageResource(R.drawable.bluecircle);
                    iv_task.setVisibility(View.VISIBLE);
                }
                //iv_task.setVisibility(View.INVISIBLE);

            }
        }
        catch(Exception e) {
            Log.d("rewards",e.getMessage());
            e.printStackTrace();
        }

    }
    public void onClick(View view)
    {
        try
        {
            switch (view.getId())
            {

                case R.id.btn_data_entry:
                    showDataEntry(view);
                    break;

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

    public  void alert(String msg)
    {
        Toast.makeText( this, msg,Toast.LENGTH_SHORT).show();
    }

    public void onDestroy(){
        super.onDestroy();
        db.close();
    }
    public void OnResume() {
        db.open();
    }
    public void onPause() {
        super.onPause();
        db.close();
    }
}
