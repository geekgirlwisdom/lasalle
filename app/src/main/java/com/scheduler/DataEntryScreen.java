package com.scheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.*;
import android.database.*;
import com.scheduler.db.*;
import com.scheduler.util.HelperUtil;
import android.content.Intent;

import java.io.*;

public class DataEntryScreen extends  Activity implements View.OnClickListener {
    Button btnSave;
    Button btnDelete;
    Button btn_rewards;
    TaskDBAdapter db;
    long parentid=0;
    TextView txtView  ;
    Cursor parentCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dataentryscreen);

            db = new TaskDBAdapter(this);
            btnSave = (Button)findViewById(R.id.btnSave);
            btnSave.setOnClickListener(  this);
            btnDelete = (Button)findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(  this);
            btn_rewards = (Button)findViewById(R.id.btn_rewards );
            btn_rewards .setOnClickListener(  this);

            txtView = (TextView)findViewById(R.id.txtView);
            String destPath = "/data/data/" + getPackageName() +   "/databases";
            File f = new File(destPath);
            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();
               // HelperUtil.CopyDB(getBaseContext().getAssets().open("mydb"),  new FileOutputStream(destPath + "/MyDB"));
                HelperUtil.CopyDB(getBaseContext().getAssets().open("TaskDB.db"),  new FileOutputStream(destPath + "/TaskDB.db"));
            }
             db.open();

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
        catch (Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
     }

    public void DisplayParent(Cursor c ) {
        try
        {
            EditText txt_task;
            TextView id_task;
            CheckBox cb_task;
            int i=0;
            long id=0;
            String taskname = "";

            txt_task = (EditText)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
            id_task = (TextView)findViewById( getResources().getIdentifier(  "id_task"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"))  ; //c.getString(2)

            txt_task.setText(taskname);
            id_task.setText(Integer.toString(c.getInt(0)));


            parentid=c.getInt(0);
        }
        catch(Exception e) {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }

    }
    public void DisplayTask(Cursor c, int i) {
        try
        {
            EditText txt_task;
            TextView id_task;
            CheckBox cb_task;

            long id=0;
            String taskname = "";

            txt_task = (EditText)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
            id_task = (TextView)findViewById( getResources().getIdentifier(  "id_task"+i, "id", getPackageName())  );
            cb_task = (CheckBox)findViewById( getResources().getIdentifier(  "cb_task"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"))  ; //c.getString(2)

            if (  taskname != null &&  taskname != "" )
            {

                txt_task.setText(taskname);
                id_task.setText(Integer.toString(c.getInt(0)));
                cb_task.setChecked( HelperUtil.getBoolValue(c.getInt(c.getColumnIndex("completed_bool"))) );

            }
        }
        catch(Exception e) {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }

    }
    public void onClick(View view)
    {
        try
        {
            switch (view.getId())
            {
                case R.id.btnSave:
                    db.open();
                    saveParent();
                    for (int i=1;  i <=5; i++) {
                        save(  i);
                    }
                    if (db.getFinishedSubtaskCount(parentid) == db.getTotalSubtaskCount(parentid))
                    {
                        alert("Congratulations on finishing all tasks!");
                        db.updateParentCompleted(parentid,1);
                    }
                    db.close();
                    break;
                case R.id.btnDelete:
                    db.open();
                    db.deleteAllTasks();
                    db.close();
                    break;
                case R.id.btn_data_entry:
                     showDataEntry(view);
                    break;
                case R.id.btn_rewards:
                 showRewards(view);
                     break;
            }
        }
        catch(Exception e)
        {
            Log.d("ActivityInterface",e.getMessage());
        }
    }

    public void saveParent()
    {
        EditText txt_task;
        TextView id_task;
        CheckBox cb_task;
        int i=0;
        long id=0;

        try
        {
            txt_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));
            id_task = (TextView) findViewById(getResources().getIdentifier("id_task" + i, "id", getPackageName()));
            cb_task = (CheckBox) findViewById(getResources().getIdentifier("cb_task" + i, "id", getPackageName()));

            if (HelperUtil.isEmpty(txt_task))
                alert("Please complete the parent goal");
            else
            {
                if (!HelperUtil.isEmpty(id_task))
                    id = Long.valueOf(id_task.getText().toString());



                    if (id == 0)
                {
                        id = db.insertTask( 999, txt_task.getText().toString(), "", "",  HelperUtil.getIntValue(cb_task) );
                        id_task.setText( Long.toString( id));
                        parentid = id;
                    } else
                        db.update(parentid, 999,txt_task.getText().toString(), "", "",   HelperUtil.getIntValue(cb_task));



                alert("saved");
            }
        }
        catch(Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
    }
    public void save(int i)
    {
         EditText txt_task;
        TextView id_task;
        CheckBox cb_task;
        long id=0;

        try
        {
            txt_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));
            id_task = (TextView) findViewById(getResources().getIdentifier("id_task" + i, "id", getPackageName()));
            cb_task = (CheckBox) findViewById(getResources().getIdentifier("cb_task" + i, "id", getPackageName()));


                    if (!HelperUtil.isEmpty(id_task))
                        id = Long.valueOf(id_task.getText().toString());


                    if (!HelperUtil.isEmpty(txt_task) && id == 0) {
                        id = db.insertTask(parentid, txt_task.getText().toString(), "", "",  HelperUtil.getIntValue(cb_task));
                         id_task.setText(  Long.toString( id  )  );
                    }
                    if (id > 0)
                    {
                        if (HelperUtil.isEmpty(txt_task))
                            db.deleteTask(id);
                        else
                        db.update(id, parentid, txt_task.getText().toString(), "", "",   HelperUtil.getIntValue(cb_task));
                    }


                     alert("saved");
                if ( HelperUtil.getIntValue(cb_task) == 0 )
                    alert("Congratulations on completing subtask: " + txt_task.getText());
        }
        catch(Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
     }
    public  void alert(String msg)
    {
        Toast.makeText( this, msg,Toast.LENGTH_SHORT).show();
    }

    public void showDataEntry(View view)
    {
        startActivity(new Intent("com.scheduler.DataEntryScreen"));
    }

    public void showRewards(View view)
    {
        startActivity(new Intent("com.scheduler.RewardsScreen"));
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
