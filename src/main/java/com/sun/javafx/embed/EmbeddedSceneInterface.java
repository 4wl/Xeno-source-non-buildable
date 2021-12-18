/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.event.EventType
 *  javafx.scene.input.InputMethodEvent
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.InputMethodTextRun
 */
package com.sun.javafx.embed;

import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.scene.traversal.Direction;
import java.nio.IntBuffer;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.InputMethodTextRun;

public interface EmbeddedSceneInterface {
    public void setSize(int var1, int var2);

    public void setPixelScaleFactor(float var1);

    public boolean getPixels(IntBuffer var1, int var2, int var3);

    public void mouseEvent(int var1, int var2, boolean var3, boolean var4, boolean var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, int var14, boolean var15);

    public void keyEvent(int var1, int var2, char[] var3, int var4);

    public void menuEvent(int var1, int var2, int var3, int var4, boolean var5);

    public boolean traverseOut(Direction var1);

    public void setDragStartListener(HostDragStartListener var1);

    public EmbeddedSceneDTInterface createDropTarget();

    public void inputMethodEvent(EventType<InputMethodEvent> var1, ObservableList<InputMethodTextRun> var2, String var3, int var4);

    public InputMethodRequests getInputMethodRequests();
}

