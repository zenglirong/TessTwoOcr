package com.iflytek.tess;

import static com.iflytek.tess.TessOcrAPI.tessOcr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.temp);
        imageView.setImageBitmap(bitmap);
        String text = tessOcr(this, bitmap, R.raw.chi);
        textView.setText(text);

        Toast.makeText(this, "执行完成", Toast.LENGTH_SHORT).show();
    }
}