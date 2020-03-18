package com.shashankbhat.pdfreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

class RetrievePdfAsyncTask extends AsyncTask<String, Void, File> {

    private String root;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private PDFView pdfView;

    RetrievePdfAsyncTask(Context context, PDFView pdfView) {
        this.context = context;
        this.pdfView = pdfView;
        root = Environment.getExternalStorageDirectory().toString();
    }

    @Override
    protected File doInBackground(String... strings) {

        try {

            URL url = new URL(strings[0]);
            URLConnection connection = url.openConnection();

            int lengthOfFile = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(), lengthOfFile);

            OutputStream output = new FileOutputStream(root + "/pdffile.pdf");

            byte[] data = new byte[1024];

            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            return new File(root + "/pdffile.pdf");

        } catch (Exception ignored) { }

        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (file != null) {
            pdfView.fromFile(file).load();
        } else {
            Toast.makeText(context, "Check URL once again", Toast.LENGTH_SHORT).show();
        }
    }
}