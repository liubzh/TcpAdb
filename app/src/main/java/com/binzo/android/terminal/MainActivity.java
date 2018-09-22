package com.binzo.android.terminal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.binzo.android.terminal.util.FileUtil;

/**
 * Created by binzo on 2018/03/18.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final String WHITE_LIST_ASSET_FILE = "white_list.txt";
    private static final String KILL_ASSET_FILE = "kill.sh";
    private String killScriptFile;
    private String whiteListFile;

    private TextView text;
    private Button btnCheckTcpAdb;
    private Button btnTcpAdb;
    private Button btnViewProcess;
    private Button btnKillAllProcess;
    private Button btnKillProcess;
    private Button btnOpenPuppyAI;
    private Button btnOpenPuppyAISettings;
    private Terminal terminal;

    private String tcp_port = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        whiteListFile = getFilesDir() + "/" + WHITE_LIST_ASSET_FILE;
        killScriptFile = getFilesDir() + "/" + KILL_ASSET_FILE;
        FileUtil.copyAssetToFile(this, WHITE_LIST_ASSET_FILE, whiteListFile); // 将 Asset 中的脚本拷贝到 data 中
        FileUtil.copyAssetToFile(this, KILL_ASSET_FILE, killScriptFile); // 将 Asset 中的脚本拷贝到 data 中
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        text = findViewById(R.id.Text);
        btnCheckTcpAdb = findViewById(R.id.btnCheckTcpAdb);
        btnCheckTcpAdb.setOnClickListener(this);
        btnViewProcess = findViewById(R.id.btnViewProcess);
        btnViewProcess.setOnClickListener(this);
        btnTcpAdb = findViewById(R.id.btnTcpAdb);
        btnTcpAdb.setOnClickListener(this);
        btnKillAllProcess = findViewById(R.id.btnKillAllProcess);
        btnKillAllProcess.setOnClickListener(this);
        btnKillProcess = findViewById(R.id.btnKillProcess);
        btnKillProcess.setOnClickListener(this);
        btnOpenPuppyAI = findViewById(R.id.btnOpenPuppyAI);
        btnOpenPuppyAI.setOnClickListener(this);
        btnOpenPuppyAISettings = findViewById(R.id.btnOpenPuppyAISettings);
        btnOpenPuppyAISettings.setOnClickListener(this);
        terminal = new Terminal(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateTcpAdbState() {
        String command = "getprop service.adb.tcp.port";
        ShellCommander.CommandResult result = terminal.exe(command, true, true, true);
        tcp_port = result.successMsg;
        Log.d(TAG, "port: " + tcp_port);
        if (!TextUtils.isEmpty(tcp_port) && !"-1".equals(tcp_port)) {
            Log.d(TAG, "on");
            Drawable state_on = getDrawable(R.drawable.state_on);
            state_on.setBounds(0, 0, state_on.getMinimumWidth(), state_on.getMinimumHeight());
            Drawable transparent = getDrawable(R.color.transparent);
            transparent.setBounds(0, 0, state_on.getMinimumWidth(), state_on.getMinimumHeight());
            btnTcpAdb.setCompoundDrawables(state_on, null, transparent, null);
        } else {
            Log.d(TAG, "off");
            Drawable state_off = getDrawable(R.drawable.state_off);
            state_off.setBounds(0, 0, state_off.getMinimumWidth(), state_off.getMinimumHeight());
            Drawable transparent = getDrawable(R.color.transparent);
            transparent.setBounds(0, 0, state_off.getMinimumWidth(), state_off.getMinimumHeight());
            btnTcpAdb.setCompoundDrawables(state_off, null, transparent, null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String[] commands = null;
        if (id == R.id.btnCheckTcpAdb) {
            updateTcpAdbState();
            btnTcpAdb.setEnabled(true);
        } else if (id == R.id.btnTcpAdb) {
            int port = -1;
            if (TextUtils.isEmpty(tcp_port) || "-1".equals(tcp_port)) {
                port = 5555;
            }
            commands = new String[]{
                    "setprop service.adb.tcp.port " + port,
                    "stop adbd",
                    "start adbd"
            };
            terminal.exe(commands, true, true, true);
            updateTcpAdbState();
        } else if (id == R.id.btnViewProcess) {
            commands = new String[]{
                    "ps -A | grep ^u0_"
            };
        } else if (id == R.id.btnKillAllProcess) {
            commands = new String[]{
                    "am kill-all"
            };
        } else if (id == R.id.btnKillProcess) {
            commands = new String[]{
                    "export WHITE_LIST_FILE=" + whiteListFile,
                    "export KILL_DIR=" + getFilesDir(),
                    "sh " + killScriptFile
            };
        } else if (id == R.id.btnOpenPuppyAI) {
            commands = new String[]{
                    "am start com.puppy.ai/.MainActivity"
            };
        } else if (id == R.id.btnOpenPuppyAISettings) {
            commands = new String[]{
                    "am start com.puppy.ai/.settings.PuppyAiSettings"
            };
        }
        terminal.exe(commands, true, true, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_screen) {
            terminal.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
