package com.scheduler.util;

import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HelperUtil  {
    public static void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

}
