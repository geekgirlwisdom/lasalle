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

import java.io.*;

public class DataEntryScreen extends  Activity implements View.OnClickListener {
    Button btnSave;
    Button btnDelete;
    TaskDBAdapter db;
    long parentid=0;
    TextView txtView  ;

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

            txtView = (TextView)findViewById(R.id.txtView);
            String destPath = "/data/data/" + getPackageName() +   "/databases";
            File f = new File(destPath);
            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();
                HelperUtil.CopyDB(getBaseContext().getAssets().open("mydb"),  new FileOutputStream(destPath + "/MyDB"));
            }
             db.open();
            alert("starting");
            Toast.makeText(this,"hh",Toast.LENGTH_LONG);

            Cursor c = db.getAllTasks(0);//db.getAllData();
            int ii=0;
            if (c.moveToFirst())
            {
                do {
                    DisplayTask(c,ii);
                    ii++;
                } while (c.moveToNext());
            }


           // db.close();*/
        }
        catch (Exception e)
        {
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
            taskname = c.getString(c.getColumnIndex("taskname"))  ;

                   // alert(taskname + " task , ctr: " + i + ", tot cursor count " + c.getCount());
            if (  taskname != null &&  taskname != "" )
            {
                txt_task.setText(c.getString(2));
                id_task.setText(c.getInt(0));
                alert(Integer.toString(c.getInt(0))  );
                cb_task.setText( c.getInt(c.getColumnIndex("completed_bool")) );
                if (  i==0)
                    parentid=c.getInt(0);

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
                     for (int i=0;  i <=5; i++) {
                         alert("save " + i);
                         save(  i);
                     }
                    break;
                case R.id.btnDelete:
                        db.deleteAllTasks();
                     break;
                case R.id.btn_data_entry:
                    //showDataEntry(view);
                    break;
               // case R.id.btn_rewards:
                   // showRewards(view);
                   // break;
            }
        }
        catch(Exception e)
        {
            Log.d("ActivityInterface",e.getMessage());
        }
    }
    boolean isEmpty(EditText txtValue)
    {
        if (txtValue.getText().toString() == "")
            return true;
        else
            return false;
    }
    boolean isEmpty(CheckBox txtValue)
    {
        if (txtValue.getText().toString() == "")
            return true;
        else
            return false;
    }
    boolean isEmpty(TextView txtValue)
    {
        if (txtValue.getText().toString() == "")
            return true;
        else
            return false;
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

            if (isEmpty(txt_task))
                alert("Please complete the goal at line " + i);
            else
                {
                    if (!isEmpty(id_task))
                        id = Long.valueOf(id_task.getText().toString());

                    alert("id is " + id);
                if (i == 0) {


                    if (isEmpty(id_task)) {
                        id = db.insertTask(00, txt_task.getText().toString(), "", "",  Integer.valueOf(cb_task.getText().toString()));
                        id_task.setText((int) id);
                        parentid = id;
                    } else
                        db.update(parentid, 00, txt_task.getText().toString(), "", "",  Integer.valueOf(cb_task.getText().toString()));


                } else {
                  /*  if (isEmpty(txt_task) && (!isEmpty(id_task))) {
                        db.deleteTask(Long.valueOf(id_task.getText().toString()));
                        id_task.setText("");
                    } */
                    if (!isEmpty(txt_task) && (isEmpty(id_task))) {
                        id = db.insertTask(parentid, txt_task.getText().toString(), "", "", Integer.valueOf(cb_task.getText().toString()));
                        id_task.setText((int) id);
                    }
                    if (!isEmpty(txt_task) && (!isEmpty(id_task))) {
                        db.update(id, parentid, txt_task.getText().toString(), "", "",  Integer.valueOf(cb_task.getText().toString()));
                    }

                }
                    alert("saved");
            }
        }
        catch(Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
     }
     public void alert(String msg)
     {
         txtView.setText(msg);
         Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
     }
}
