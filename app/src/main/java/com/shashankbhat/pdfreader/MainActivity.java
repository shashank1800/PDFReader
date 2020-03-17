package com.shashankbhat.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private PDFView pdfView;
    private EditText url;
    private String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        pdfView = findViewById(R.id.pdfView);

        Button button = findViewById(R.id.submit);
        url = findViewById(R.id.url);

        //Default Pdf
        url.setText("http://hortonworks.com/wp-content/uploads/2016/05/Hortonworks.CheatSheet.SQLtoHive.pdf");

        root = Environment.getExternalStorageDirectory().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!url.getText().toString().isEmpty())
                    new RetrievePdfStream().execute(url.getText().toString());
            }
        });
    }

    class RetrievePdfStream extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {

            try {

                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();

                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), lengthOfFile);

                OutputStream output = new FileOutputStream(root+"/pdffile.pdf");

                byte data[] = new byte[1024];

                int count = 0;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                return new File(root+"/pdffile.pdf");

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                pdfView.fromFile(file).load();

            }else{
                pdfView.fromAsset("paper.pdf").load();
                System.out.println("Offline");
            }
        }
    }
}
