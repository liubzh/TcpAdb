package com.binzo.adb.tcp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by binzo on 2018/03/18.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text;
    private Button buttonUsb;
    private Button buttonTcp;
    private StringBuffer sb = new StringBuffer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.Text);
        buttonUsb = findViewById(R.id.ButtonUsb);
        buttonUsb.setOnClickListener(this);
        buttonTcp = findViewById(R.id.ButtonTcp);
        buttonTcp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int port = -1;
        int id = view.getId();
        if (id == R.id.ButtonTcp) {
            port = 5555;
        }
        String[] commands = new String[]{
                "setprop service.adb.tcp.port " + port,
                "stop adbd",
                "start adbd"
        };
        ShellCommander.CommandResult result = ShellCommander.execCommand(commands, true, true);
        if (result.result == 0) {
            sb.append("success\n");
        } else {
            sb.append("fail:" + result.result).append("\n");
        }
        if (!TextUtils.isEmpty(result.successMsg)) {
            sb.append(result.successMsg).append("\n");
        }
        if (!TextUtils.isEmpty(result.errorMsg)) {
            sb.append(result.errorMsg).append("\n");
        }
        text.setText(sb.toString());
    }
}
