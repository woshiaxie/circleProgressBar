package com.huang.customedview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huang.customedview.UI.CircleProgressView;

public class MainActivity extends AppCompatActivity {
    private CircleProgressView mCircleProgressView;
    private EditText mEditText;
    private Button mSetProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {

        mCircleProgressView = (CircleProgressView) this.findViewById(R.id.activity_main_circleProgressView);
        mCircleProgressView.setSweepAngle(270);

        mEditText = (EditText)this.findViewById(R.id.activity_main_et);

        mSetProgressButton = (Button)this.findViewById(R.id.activity_main_setProgress);
        mSetProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String progressStr = mEditText.getText().toString();
                if (!TextUtils.isEmpty(progressStr)) {
                    float angle =  0;
                    try {
                        angle = Float.parseFloat(progressStr);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    mCircleProgressView.setSweepAngle(angle);
                }
            }
        });
    }
}
