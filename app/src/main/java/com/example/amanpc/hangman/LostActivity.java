package com.example.amanpc.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LostActivity extends AppCompatActivity {
    String a;

   /* public LostActivity(String a) {
        this.a = a;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        TextView textView=findViewById(R.id.correctWordText);

        TextView textView1=findViewById(R.id.lostText);
        ImageButton imageButton=findViewById(R.id.playGameButton);


        if (getIntent().getStringExtra("Word") != null) {
            textView1.setText("You Lost\nThe Word was");
            textView.setText(getIntent().getStringExtra("Word"));
        }
        else if(getIntent().getStringExtra("Won") != null){
            textView1.setText("You Won\nThe Word was");
            textView.setText(getIntent().getStringExtra("Won"));
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LostActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}
