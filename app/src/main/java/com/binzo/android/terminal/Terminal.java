package com.binzo.android.terminal;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by binzo on 2018/4/14.
 */

public class Terminal {
    private TextView textView;
    private StringBuffer sb = new StringBuffer();

    public Terminal(TextView textView) {
        this.textView = textView;
    }

    private void appendResult(String result) {
        sb.append(result).append("\n");
        textView.setText(sb.toString());
    }

    private void appendCommands(String[] commands) {
        if (commands == null || commands.length == 0) {
            return;
        }
        sb.append("# ").append(commands[0]);
        for (int i = 1; i < commands.length; i++) {
            sb.append("; ").append(commands[i]);
        }
        sb.append("\n");
        textView.setText(sb.toString());
    }

    public void clear() {
        textView.setText("");
        sb = new StringBuffer();
    }

    public ShellCommander.CommandResult exe(String command, boolean isRoot, boolean isNeedResultMsg, boolean showOnTerminal){
        return exe(new String[] {command}, isRoot, isNeedResultMsg, showOnTerminal);
    }

    public ShellCommander.CommandResult exe(String[] commands, boolean isRoot, boolean isNeedResultMsg, boolean showOnTerminal){
        if (showOnTerminal) {
            appendCommands(commands);
        }
        ShellCommander.CommandResult result = ShellCommander.execCommand(commands, isRoot, isNeedResultMsg);
        if (showOnTerminal) {
            if (!TextUtils.isEmpty(result.successMsg)) {
                appendResult(result.successMsg);
            }
            if (!TextUtils.isEmpty(result.errorMsg)) {
                appendResult(result.errorMsg);
            }
        }
        return result;
    }
}
