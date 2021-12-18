/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.NativeMediaManager;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.ListIterator;

public class MediaUtils {
    public static final int MAX_FILE_SIGNATURE_LENGTH = 22;
    static final String NATIVE_MEDIA_ERROR_FORMAT = "Internal media error: %d";
    static final String NATIVE_MEDIA_WARNING_FORMAT = "Internal media warning: %d";
    public static final String CONTENT_TYPE_AIFF = "audio/x-aiff";
    public static final String CONTENT_TYPE_MP3 = "audio/mp3";
    public static final String CONTENT_TYPE_MPA = "audio/mpeg";
    public static final String CONTENT_TYPE_WAV = "audio/x-wav";
    public static final String CONTENT_TYPE_JFX = "video/x-javafx";
    public static final String CONTENT_TYPE_FLV = "video/x-flv";
    public static final String CONTENT_TYPE_MP4 = "video/mp4";
    public static final String CONTENT_TYPE_M4A = "audio/x-m4a";
    public static final String CONTENT_TYPE_M4V = "video/x-m4v";
    public static final String CONTENT_TYPE_M3U8 = "application/vnd.apple.mpegurl";
    public static final String CONTENT_TYPE_M3U = "audio/mpegurl";
    private static final String FILE_TYPE_AIF = "aif";
    private static final String FILE_TYPE_AIFF = "aiff";
    private static final String FILE_TYPE_FLV = "flv";
    private static final String FILE_TYPE_FXM = "fxm";
    private static final String FILE_TYPE_MPA = "mp3";
    private static final String FILE_TYPE_WAV = "wav";
    private static final String FILE_TYPE_MP4 = "mp4";
    private static final String FILE_TYPE_M4A = "m4a";
    private static final String FILE_TYPE_M4V = "m4v";
    private static final String FILE_TYPE_M3U8 = "m3u8";
    private static final String FILE_TYPE_M3U = "m3u";

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String fileSignatureToContentType(byte[] arrby, int n) throws MediaException {
        String string = "application/octet-stream";
        if (n < 22) {
            throw new MediaException("Empty signature!");
        }
        if (arrby.length < 22) {
            return string;
        }
        if ((arrby[0] & 0xFF) == 70 && (arrby[1] & 0xFF) == 76 && (arrby[2] & 0xFF) == 86) {
            return CONTENT_TYPE_JFX;
        }
        if (((arrby[0] & 0xFF) << 24 | (arrby[1] & 0xFF) << 16 | (arrby[2] & 0xFF) << 8 | arrby[3] & 0xFF) == 1380533830 && ((arrby[8] & 0xFF) << 24 | (arrby[9] & 0xFF) << 16 | (arrby[10] & 0xFF) << 8 | arrby[11] & 0xFF) == 1463899717 && ((arrby[12] & 0xFF) << 24 | (arrby[13] & 0xFF) << 16 | (arrby[14] & 0xFF) << 8 | arrby[15] & 0xFF) == 1718449184) {
            if ((arrby[20] & 0xFF) == 1) {
                if ((arrby[21] & 0xFF) == 0) return CONTENT_TYPE_WAV;
            }
            if ((arrby[20] & 0xFF) != 3) throw new MediaException("Compressed WAVE is not supported!");
            if ((arrby[21] & 0xFF) != 0) throw new MediaException("Compressed WAVE is not supported!");
            return CONTENT_TYPE_WAV;
        }
        if (((arrby[0] & 0xFF) << 24 | (arrby[1] & 0xFF) << 16 | (arrby[2] & 0xFF) << 8 | arrby[3] & 0xFF) == 1380533830 && ((arrby[8] & 0xFF) << 24 | (arrby[9] & 0xFF) << 16 | (arrby[10] & 0xFF) << 8 | arrby[11] & 0xFF) == 1463899717) {
            return CONTENT_TYPE_WAV;
        }
        if (((arrby[0] & 0xFF) << 24 | (arrby[1] & 0xFF) << 16 | (arrby[2] & 0xFF) << 8 | arrby[3] & 0xFF) == 1179603533 && ((arrby[8] & 0xFF) << 24 | (arrby[9] & 0xFF) << 16 | (arrby[10] & 0xFF) << 8 | arrby[11] & 0xFF) == 1095321158 && ((arrby[12] & 0xFF) << 24 | (arrby[13] & 0xFF) << 16 | (arrby[14] & 0xFF) << 8 | arrby[15] & 0xFF) == 1129270605) {
            return CONTENT_TYPE_AIFF;
        }
        if ((arrby[0] & 0xFF) == 73 && (arrby[1] & 0xFF) == 68 && (arrby[2] & 0xFF) == 51) {
            return CONTENT_TYPE_MPA;
        }
        if ((arrby[0] & 0xFF) == 255 && (arrby[1] & 0xE0) == 224 && (arrby[2] & 0x18) != 8 && (arrby[3] & 6) != 0) {
            return CONTENT_TYPE_MPA;
        }
        if (((arrby[4] & 0xFF) << 24 | (arrby[5] & 0xFF) << 16 | (arrby[6] & 0xFF) << 8 | arrby[7] & 0xFF) != 1718909296) throw new MediaException("Unrecognized file signature!");
        if ((arrby[8] & 0xFF) == 77 && (arrby[9] & 0xFF) == 52 && (arrby[10] & 0xFF) == 65 && (arrby[11] & 0xFF) == 32) {
            return CONTENT_TYPE_M4A;
        }
        if ((arrby[8] & 0xFF) == 77 && (arrby[9] & 0xFF) == 52 && (arrby[10] & 0xFF) == 86 && (arrby[11] & 0xFF) == 32) {
            return CONTENT_TYPE_M4V;
        }
        if ((arrby[8] & 0xFF) == 109 && (arrby[9] & 0xFF) == 112 && (arrby[10] & 0xFF) == 52 && (arrby[11] & 0xFF) == 50) {
            return CONTENT_TYPE_MP4;
        }
        if ((arrby[8] & 0xFF) == 105 && (arrby[9] & 0xFF) == 115 && (arrby[10] & 0xFF) == 111 && (arrby[11] & 0xFF) == 109) {
            return CONTENT_TYPE_MP4;
        }
        if ((arrby[8] & 0xFF) != 77) return string;
        if ((arrby[9] & 0xFF) != 80) return string;
        if ((arrby[10] & 0xFF) != 52) return string;
        if ((arrby[11] & 0xFF) != 32) return string;
        return CONTENT_TYPE_MP4;
    }

    public static String filenameToContentType(String string) {
        String string2 = "application/octet-stream";
        int n = string.lastIndexOf(".");
        if (n != -1) {
            String string3 = string.toLowerCase().substring(n + 1);
            if (string3.equals(FILE_TYPE_AIF) || string3.equals(FILE_TYPE_AIFF)) {
                string2 = CONTENT_TYPE_AIFF;
            } else if (string3.equals(FILE_TYPE_FLV) || string3.equals(FILE_TYPE_FXM)) {
                string2 = CONTENT_TYPE_JFX;
            } else if (string3.equals(FILE_TYPE_MPA)) {
                string2 = CONTENT_TYPE_MPA;
            } else if (string3.equals(FILE_TYPE_WAV)) {
                string2 = CONTENT_TYPE_WAV;
            } else if (string3.equals(FILE_TYPE_MP4)) {
                string2 = CONTENT_TYPE_MP4;
            } else if (string3.equals(FILE_TYPE_M4A)) {
                string2 = CONTENT_TYPE_M4A;
            } else if (string3.equals(FILE_TYPE_M4V)) {
                string2 = CONTENT_TYPE_M4V;
            } else if (string3.equals(FILE_TYPE_M3U8)) {
                string2 = CONTENT_TYPE_M3U8;
            } else if (string3.equals(FILE_TYPE_M3U)) {
                string2 = CONTENT_TYPE_M3U;
            }
        }
        return string2;
    }

    public static void warning(Object object, String string) {
        if (object != null & string != null) {
            Logger.logMsg(3, object.getClass().getName() + ": " + string);
        }
    }

    public static void error(Object object, int n, String string, Throwable throwable) {
        Object object2;
        Object object3;
        if (throwable != null && (object3 = throwable.getStackTrace()) != null && ((Object)object3).length > 0) {
            object2 = object3[0];
            Logger.logMsg(4, ((StackTraceElement)object2).getClassName(), ((StackTraceElement)object2).getMethodName(), "( " + ((StackTraceElement)object2).getLineNumber() + ") " + string);
        }
        if (!(object3 = NativeMediaManager.getDefaultInstance().getMediaErrorListeners()).isEmpty()) {
            object2 = object3.listIterator();
            while (object2.hasNext()) {
                MediaErrorListener mediaErrorListener = (MediaErrorListener)((WeakReference)object2.next()).get();
                if (mediaErrorListener != null) {
                    mediaErrorListener.onError(object, n, string);
                    continue;
                }
                object2.remove();
            }
        } else {
            object2 = throwable instanceof MediaException ? (MediaException)throwable : new MediaException(string, throwable);
            throw object2;
        }
    }

    public static void nativeWarning(Object object, int n, String string) {
        String string2 = String.format(NATIVE_MEDIA_WARNING_FORMAT, n);
        if (string != null) {
            string2 = string2 + ": " + string;
        }
        Logger.logMsg(3, string2);
    }

    public static void nativeError(Object object, MediaError mediaError) {
        Logger.logMsg(4, mediaError.description());
        List<WeakReference<MediaErrorListener>> list = NativeMediaManager.getDefaultInstance().getMediaErrorListeners();
        if (!list.isEmpty()) {
            ListIterator<WeakReference<MediaErrorListener>> listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                MediaErrorListener mediaErrorListener = (MediaErrorListener)listIterator.next().get();
                if (mediaErrorListener != null) {
                    mediaErrorListener.onError(object, mediaError.code(), mediaError.description());
                    continue;
                }
                listIterator.remove();
            }
        } else {
            throw new MediaException(mediaError.description(), null, mediaError);
        }
    }
}

