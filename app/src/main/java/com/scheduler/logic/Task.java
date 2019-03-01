package com.scheduler.logic;

import com.scheduler.*;
import com.scheduler.db.*;

import android.os.Bundle;
import android.app.Activity;

public class Task extends Activity {

    public long id;
    public long parentid ;
    public int completed_bool ;
    public String taskname ;
    String  description  ;
    public java.util.Date dte_enddate;
    String str_enddate;
    String str_completed_date;
    public java.util.Date dte_completed_date;
    TaskDBAdapter taskdb;

    public Task()
    {
        id=0;
        parentid=0;
        completed_bool=0;
        taskname="";
        description = "";
        str_enddate="";
        str_completed_date="";

        taskdb = new TaskDBAdapter(this );
        taskdb.open();
    }
    public void save()
    {
        id = taskdb.insertTask(parentid, taskname, str_enddate,  str_completed_date,  completed_bool );
    }

    public void close()
    {
        taskdb.close();
    }
}
