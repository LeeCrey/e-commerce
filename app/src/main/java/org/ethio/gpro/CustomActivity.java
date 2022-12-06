package org.ethio.gpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.ethio.gpro.helpers.CustomUrlHelper;
import org.ethio.gpro.ui.MainActivity;

import java.util.Objects;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        Button button = findViewById(R.id.done);
        Button bnt = findViewById(R.id.pass);

        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(CustomActivity.this, MainActivity.class);
                startActivity(t);
                finish();
            }
        });
        TextInputEditText url = findViewById(R.id.url);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = Objects.requireNonNull(url.getText()).toString().trim();
                if (!x.isEmpty()) {
                    CustomUrlHelper.setUrl(CustomActivity.this, x);
                    Intent t = new Intent(CustomActivity.this, MainActivity.class);
                    startActivity(t);
                    finish();
                } else {
                    Toast.makeText(CustomActivity.this, "required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}