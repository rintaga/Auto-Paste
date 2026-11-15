package com.autopaste.app;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class PasteAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getEventType() != AccessibilityEvent.TYPE_VIEW_FOCUSED) return;
        SharedPreferences prefs = getSharedPreferences("autopaste", MODE_PRIVATE);
        if (!prefs.getBoolean("armed", false)) return;

        AccessibilityNodeInfo node = getRootInActiveWindow();
        AccessibilityNodeInfo target = findFocusedEditable(node);
        if (target == null) return;

        String value = prefs.getString("text", "");
        if (value.isEmpty()) return;

        Bundle args = new Bundle();
        args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, value);
        boolean inserted = target.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);
        if (inserted) {
            prefs.edit().putBoolean("armed", false).apply();
            Toast.makeText(this, "Teks dimasukkan satu kali. Periksa sebelum mengirim.", Toast.LENGTH_SHORT).show();
        }
        if (node != null) node.recycle();
    }

    private AccessibilityNodeInfo findFocusedEditable(AccessibilityNodeInfo node) {
        if (node == null) return null;
        if (node.isFocused() && node.isEditable()) return node;
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo found = findFocusedEditable(node.getChild(i));
            if (found != null) return found;
        }
        return null;
    }

    @Override public void onInterrupt() {}
}
