package com.albert.gestureanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.albert.gestureanimation.vateview.VoteView;

public class MainActivity extends AppCompatActivity {
    VoteView voteView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voteView = (VoteView) findViewById(R.id.voteview);
        findViewById(R.id.tv_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voteView.like();
            }
        });
        findViewById(R.id.tv_unlike).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voteView.unLike();
            }
        });
    }
}
