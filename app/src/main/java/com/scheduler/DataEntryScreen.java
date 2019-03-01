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
import com.scheduler.logic.*;

import java.io.*;

public class DataEntryScreen extends  Activity implements View.OnClickListener {
    Button btnSave;
    EditText txtName;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataentryscreen);
        TaskDBAdapter db = new TaskDBAdapter(this);

        btnSave = (Button)findViewById(R.id.btnSave);
        txtName = (EditText)findViewById(R.id.txtName);

        try
        {
            String destPath = "/data/data/" + getPackageName() +   "/databases";
            txtName.setText(destPath);
            File f = new File(destPath);
            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();
                CopyDB(getBaseContext().getAssets().open("mydb"),  new FileOutputStream(destPath + "/MyDB"));
            }
             db.open();

             long id = db.insertTask(00, "Main Task", "","",0  );
            db.insertTask(id, "Sub Task 1", "","",1  );
            db.insertTask(id, "Sub Task 2", "","",0 );
            db.insertTask(id, "Sub Task 3", "","",0  );

           // Cursor c = db.getAllData();
            Cursor c= db.getSubtasks(id);
            txtName.setText("Total tasks: " + db.getTotalSubtaskCount(id) + " for the parentid of " +id   );
            if (c.moveToFirst())
            {
                 do {
                   DisplayTask(c);
                } while (c.moveToNext());
            }
            db.close();
        }
        catch (Exception e)
        {
            Log.e("dataentry",e.getMessage());
        }



       // btnSave.setOnClickListener(  this);
    }

    public void CopyDB(InputStream inputStream,
                       OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void DisplayTask(Cursor c) {
        try {
             Toast.makeText(this,
                    "id: " + c.getInt(0) + "\n" +
                            "parentid: " + c.getInt(1) + "\n" +
                            "taskname:  " + c.getString(2),
                    Toast.LENGTH_LONG).show();

        }
        catch(Exception e) {
            Log.e("dataentry",e.getMessage());
        }

    }
    public void onClick(View view)
    {
        try {
            switch (view.getId())
            {
                case R.id.btnSave:
                    save();
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
    public void save()
    {
        TaskDBAdapter taskdb = new TaskDBAdapter(this );
        taskdb.open();
         long id = taskdb.insertTask(0, "test", "0000",  "0000",  0);

        txtName.setText("WORKS " +  id);
    }
}
