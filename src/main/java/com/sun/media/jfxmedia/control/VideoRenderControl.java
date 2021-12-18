/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.control;

import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;

public interface VideoRenderControl {
    public void addVideoRendererListener(VideoRendererListener var1);

    public void removeVideoRendererListener(VideoRendererListener var1);

    public void addVideoFrameRateListener(VideoFrameRateListener var1);

    public void removeVideoFrameRateListener(VideoFrameRateListener var1);

    public int getFrameWidth();

    public int getFrameHeight();
}

