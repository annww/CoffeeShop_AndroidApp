package ntu.duongthianhhong.blueymovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import ntu.duongthianhhong.blueymovies.R;

public class IntroActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        imageView =  findViewById(R.id.imageView);
        btnstart = findViewById(R.id.btnstart);
        Glide.with(this)
                .asGif()
                .load(R.drawable.intro)
                .into(imageView);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}