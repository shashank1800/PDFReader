package com.shashankbhat.pdfreader;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @BindView(R.id.pdfView)
    PDFView pdfView;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.show_pdf)
    Button showPdfButton;

    @BindView(R.id.url_edit_text)
    EditText url_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        ButterKnife.bind(this);

        requestPermission();

        //Default Pdf
        url_edit_text.setText("http://hortonworks.com/wp-content/uploads/2016/05/Hortonworks.CheatSheet.SQLtoHive.pdf");
        pdfView.fromAsset("paper.pdf").load();

        showPdfButton.setOnClickListener(view -> {
            String url = url_edit_text.getText().toString();
            if (!url.isEmpty())
                new RetrievePdfAsyncTask(context, pdfView).execute(url_edit_text.getText().toString());
            else
                Toast.makeText(context, "Empty URL", Toast.LENGTH_SHORT).show();
        });
    }

    private void requestPermission() {

        MultiplePermissionsListener snackbarMultiplePermissionsListener =
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                        .with(linearLayout, "Write to External Storage is needed to download the PDF and store it")
                        .withOpenSettingsButton("Settings")
                        .build();

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(snackbarMultiplePermissionsListener).check();
    }

}
