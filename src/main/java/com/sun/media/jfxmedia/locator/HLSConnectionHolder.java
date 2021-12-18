/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.ConnectionHolder;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

final class HLSConnectionHolder
extends ConnectionHolder {
    private URLConnection urlConnection = null;
    private PlaylistThread playlistThread = new PlaylistThread();
    private VariantPlaylist variantPlaylist = null;
    private Playlist currentPlaylist = null;
    private int mediaFileIndex = -1;
    private CountDownLatch readySignal = new CountDownLatch(1);
    private Semaphore liveSemaphore = new Semaphore(0);
    private boolean isPlaylistClosed = false;
    private boolean isBitrateAdjustable = false;
    private long startTime = -1L;
    private static final long HLS_VALUE_FLOAT_MULTIPLIER = 1000L;
    private static final int HLS_PROP_GET_DURATION = 1;
    private static final int HLS_PROP_GET_HLS_MODE = 2;
    private static final int HLS_PROP_GET_MIMETYPE = 3;
    private static final int HLS_VALUE_MIMETYPE_MP2T = 1;
    private static final int HLS_VALUE_MIMETYPE_MP3 = 2;
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CHARSET_US_ASCII = "US-ASCII";

    HLSConnectionHolder(URI uRI) throws IOException {
        this.playlistThread.setPlaylistURI(uRI);
        this.init();
    }

    private void init() {
        this.playlistThread.putState(0);
        this.playlistThread.start();
    }

    @Override
    public int readNextBlock() throws IOException {
        if (this.isBitrateAdjustable && this.startTime == -1L) {
            this.startTime = System.currentTimeMillis();
        }
        int n = super.readNextBlock();
        if (this.isBitrateAdjustable && n == -1) {
            long l = System.currentTimeMillis() - this.startTime;
            this.startTime = -1L;
            this.adjustBitrate(l);
        }
        return n;
    }

    @Override
    int readBlock(long l, int n) throws IOException {
        throw new IOException();
    }

    @Override
    boolean needBuffer() {
        return true;
    }

    @Override
    boolean isSeekable() {
        return true;
    }

    @Override
    boolean isRandomAccess() {
        return false;
    }

    @Override
    public long seek(long l) {
        try {
            this.readySignal.await();
        }
        catch (Exception exception) {
            return -1L;
        }
        return (long)(this.currentPlaylist.seek(l) * 1000.0);
    }

    @Override
    public void closeConnection() {
        this.currentPlaylist.close();
        super.closeConnection();
        this.resetConnection();
        this.playlistThread.putState(1);
    }

    @Override
    int property(int n, int n2) {
        try {
            this.readySignal.await();
        }
        catch (Exception exception) {
            return -1;
        }
        if (n == 1) {
            return (int)(this.currentPlaylist.getDuration() * 1000.0);
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return this.currentPlaylist.getMimeType();
        }
        return -1;
    }

    @Override
    int getStreamSize() {
        try {
            this.readySignal.await();
        }
        catch (Exception exception) {
            return -1;
        }
        return this.loadNextSegment();
    }

    private void resetConnection() {
        super.closeConnection();
        Locator.closeConnection(this.urlConnection);
        this.urlConnection = null;
    }

    private int loadNextSegment() {
        this.resetConnection();
        String string = this.currentPlaylist.getNextMediaFile();
        if (string == null) {
            return -1;
        }
        try {
            URI uRI = new URI(string);
            this.urlConnection = uRI.toURL().openConnection();
            this.channel = this.openChannel();
        }
        catch (Exception exception) {
            return -1;
        }
        if (this.currentPlaylist.isCurrentMediaFileDiscontinuity()) {
            return -1 * this.urlConnection.getContentLength();
        }
        return this.urlConnection.getContentLength();
    }

    private ReadableByteChannel openChannel() throws IOException {
        return Channels.newChannel(this.urlConnection.getInputStream());
    }

    private void adjustBitrate(long l) {
        int n = (int)((long)this.urlConnection.getContentLength() * 8L * 1000L / l);
        Playlist playlist = this.variantPlaylist.getPlaylistBasedOnBitrate(n);
        if (playlist != null && playlist != this.currentPlaylist) {
            if (this.currentPlaylist.isLive()) {
                playlist.update(this.currentPlaylist.getNextMediaFile());
                this.playlistThread.setReloadPlaylist(playlist);
            }
            playlist.setForceDiscontinuity(true);
            this.currentPlaylist = playlist;
        }
    }

    private static String stripParameters(String string) {
        int n = string.indexOf(63);
        if (n > 0) {
            string = string.substring(0, n);
        }
        return string;
    }

    private class Playlist {
        private boolean isLive = false;
        private volatile boolean isLiveWaiting = false;
        private volatile boolean isLiveStop = false;
        private long targetDuration = 0L;
        private URI playlistURI = null;
        private final Object lock = new Object();
        private List<String> mediaFiles = new ArrayList<String>();
        private List<Double> mediaFilesStartTimes = new ArrayList<Double>();
        private List<Boolean> mediaFilesDiscontinuities = new ArrayList<Boolean>();
        private boolean needBaseURI = true;
        private String baseURI = null;
        private double duration = 0.0;
        private int sequenceNumber = -1;
        private int sequenceNumberStart = -1;
        private boolean sequenceNumberUpdated = false;
        private boolean forceDiscontinuity = false;

        private Playlist(boolean bl, int n) {
            this.isLive = bl;
            this.targetDuration = n * 1000;
            if (bl) {
                this.duration = -1.0;
            }
        }

        private Playlist(URI uRI) {
            this.playlistURI = uRI;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void update(String string) {
            PlaylistParser playlistParser = new PlaylistParser();
            playlistParser.load(this.playlistURI);
            this.isLive = playlistParser.isLivePlaylist();
            this.targetDuration = playlistParser.getTargetDuration() * 1000;
            if (this.isLive) {
                this.duration = -1.0;
            }
            if (this.setSequenceNumber(playlistParser.getSequenceNumber())) {
                while (playlistParser.hasNext()) {
                    this.addMediaFile(playlistParser.getString(), playlistParser.getDouble(), playlistParser.getBoolean());
                }
            }
            if (string != null) {
                Object object = this.lock;
                synchronized (object) {
                    for (int i = 0; i < this.mediaFiles.size(); ++i) {
                        String string2 = this.mediaFiles.get(i);
                        if (!string.endsWith(string2)) continue;
                        HLSConnectionHolder.this.mediaFileIndex = i - 1;
                        break;
                    }
                }
            }
        }

        private boolean isLive() {
            return this.isLive;
        }

        private long getTargetDuration() {
            return this.targetDuration;
        }

        private void setPlaylistURI(URI uRI) {
            this.playlistURI = uRI;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void addMediaFile(String string, double d, boolean bl) {
            Object object = this.lock;
            synchronized (object) {
                if (this.needBaseURI) {
                    this.setBaseURI(this.playlistURI.toString(), string);
                }
                if (this.isLive) {
                    if (this.sequenceNumberUpdated) {
                        int n = this.mediaFiles.indexOf(string);
                        if (n != -1) {
                            for (int i = 0; i < n; ++i) {
                                this.mediaFiles.remove(0);
                                this.mediaFilesDiscontinuities.remove(0);
                                if (HLSConnectionHolder.this.mediaFileIndex == -1) {
                                    this.forceDiscontinuity = true;
                                }
                                if (HLSConnectionHolder.this.mediaFileIndex < 0) continue;
                                HLSConnectionHolder.this.mediaFileIndex--;
                            }
                        }
                        this.sequenceNumberUpdated = false;
                    }
                    if (this.mediaFiles.contains(string)) {
                        return;
                    }
                }
                this.mediaFiles.add(string);
                this.mediaFilesDiscontinuities.add(bl);
                if (this.isLive) {
                    if (this.isLiveWaiting) {
                        HLSConnectionHolder.this.liveSemaphore.release();
                    }
                } else {
                    this.mediaFilesStartTimes.add(this.duration);
                    this.duration += d;
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private String getNextMediaFile() {
            Object object;
            if (this.isLive) {
                object = this.lock;
                synchronized (object) {
                    this.isLiveWaiting = HLSConnectionHolder.this.mediaFileIndex + 1 >= this.mediaFiles.size();
                }
                if (this.isLiveWaiting) {
                    try {
                        HLSConnectionHolder.this.liveSemaphore.acquire();
                        this.isLiveWaiting = false;
                        if (this.isLiveStop) {
                            this.isLiveStop = false;
                            return null;
                        }
                    }
                    catch (InterruptedException interruptedException) {
                        this.isLiveWaiting = false;
                        return null;
                    }
                }
                if (HLSConnectionHolder.this.isPlaylistClosed) {
                    return null;
                }
            }
            object = this.lock;
            synchronized (object) {
                HLSConnectionHolder.this.mediaFileIndex++;
                if (HLSConnectionHolder.this.mediaFileIndex < this.mediaFiles.size()) {
                    if (this.baseURI != null) {
                        return this.baseURI + this.mediaFiles.get(HLSConnectionHolder.this.mediaFileIndex);
                    }
                    return this.mediaFiles.get(HLSConnectionHolder.this.mediaFileIndex);
                }
                return null;
            }
        }

        private double getDuration() {
            return this.duration;
        }

        private void setForceDiscontinuity(boolean bl) {
            this.forceDiscontinuity = bl;
        }

        private boolean isCurrentMediaFileDiscontinuity() {
            if (this.forceDiscontinuity) {
                this.forceDiscontinuity = false;
                return true;
            }
            return this.mediaFilesDiscontinuities.get(HLSConnectionHolder.this.mediaFileIndex);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private double seek(long l) {
            Object object = this.lock;
            synchronized (object) {
                if (this.isLive) {
                    if (l == 0L) {
                        HLSConnectionHolder.this.mediaFileIndex = -1;
                        if (this.isLiveWaiting) {
                            this.isLiveStop = true;
                            HLSConnectionHolder.this.liveSemaphore.release();
                        }
                        return 0.0;
                    }
                } else {
                    l += this.targetDuration / 2000L;
                    int n = this.mediaFilesStartTimes.size();
                    for (int i = 0; i < n; ++i) {
                        if (!((double)l >= this.mediaFilesStartTimes.get(i))) continue;
                        if (i + 1 < n) {
                            if (!((double)l < this.mediaFilesStartTimes.get(i + 1))) continue;
                            HLSConnectionHolder.this.mediaFileIndex = i - 1;
                            return this.mediaFilesStartTimes.get(i);
                        }
                        if ((double)(l - this.targetDuration / 2000L) < this.duration) {
                            HLSConnectionHolder.this.mediaFileIndex = i - 1;
                            return this.mediaFilesStartTimes.get(i);
                        }
                        if (Double.compare(l - this.targetDuration / 2000L, this.duration) != 0) continue;
                        return this.duration;
                    }
                }
            }
            return -1.0;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private int getMimeType() {
            Object object = this.lock;
            synchronized (object) {
                if (this.mediaFiles.size() > 0) {
                    if (HLSConnectionHolder.stripParameters(this.mediaFiles.get(0)).endsWith(".ts")) {
                        return 1;
                    }
                    if (HLSConnectionHolder.stripParameters(this.mediaFiles.get(0)).endsWith(".mp3")) {
                        return 2;
                    }
                }
            }
            return -1;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private String getMediaFileExtension() {
            Object object = this.lock;
            synchronized (object) {
                String string;
                int n;
                if (this.mediaFiles.size() > 0 && (n = (string = HLSConnectionHolder.stripParameters(this.mediaFiles.get(0))).lastIndexOf(".")) != -1) {
                    return string.substring(n);
                }
            }
            return null;
        }

        private boolean setSequenceNumber(int n) {
            if (this.sequenceNumberStart == -1) {
                this.sequenceNumberStart = n;
            } else if (this.sequenceNumber != n) {
                this.sequenceNumberUpdated = true;
                this.sequenceNumber = n;
            } else {
                return false;
            }
            return true;
        }

        private void close() {
            if (this.isLive) {
                HLSConnectionHolder.this.isPlaylistClosed = true;
                HLSConnectionHolder.this.liveSemaphore.release();
            }
        }

        private void setBaseURI(String string, String string2) {
            if (!string2.startsWith("http://") || !string2.startsWith("https://")) {
                this.baseURI = string.substring(0, string.lastIndexOf("/") + 1);
            }
            this.needBaseURI = false;
        }
    }

    private static class VariantPlaylist {
        private URI playlistURI = null;
        private int infoIndex = -1;
        private List<String> playlistsLocations = new ArrayList<String>();
        private List<Integer> playlistsBitrates = new ArrayList<Integer>();
        private List<Playlist> playlists = new ArrayList<Playlist>();
        private String mediaFileExtension = null;

        private VariantPlaylist(URI uRI) {
            this.playlistURI = uRI;
        }

        private void addPlaylistInfo(String string, int n) {
            this.playlistsLocations.add(string);
            this.playlistsBitrates.add(n);
        }

        private void addPlaylist(Playlist playlist) {
            if (this.mediaFileExtension == null) {
                this.mediaFileExtension = playlist.getMediaFileExtension();
            } else if (!this.mediaFileExtension.equals(playlist.getMediaFileExtension())) {
                this.playlistsLocations.remove(this.infoIndex);
                this.playlistsBitrates.remove(this.infoIndex);
                --this.infoIndex;
                return;
            }
            this.playlists.add(playlist);
        }

        private Playlist getPlaylist(int n) {
            if (this.playlists.size() > n) {
                return this.playlists.get(n);
            }
            return null;
        }

        private boolean hasNext() {
            ++this.infoIndex;
            return this.playlistsLocations.size() > this.infoIndex && this.playlistsBitrates.size() > this.infoIndex;
        }

        private URI getPlaylistURI() throws URISyntaxException, MalformedURLException {
            String string = this.playlistsLocations.get(this.infoIndex);
            if (string.startsWith("http://") || string.startsWith("https://")) {
                return new URI(string);
            }
            return new URI(this.playlistURI.toURL().toString().substring(0, this.playlistURI.toURL().toString().lastIndexOf("/") + 1) + string);
        }

        private Playlist getPlaylistBasedOnBitrate(int n) {
            int n2;
            int n3;
            int n4 = -1;
            int n5 = 0;
            for (n3 = 0; n3 < this.playlistsBitrates.size(); ++n3) {
                n2 = this.playlistsBitrates.get(n3);
                if (n2 >= n) continue;
                if (n4 != -1) {
                    if (n2 <= n5) continue;
                    n5 = n2;
                    n4 = n3;
                    continue;
                }
                n4 = n3;
            }
            if (n4 == -1) {
                for (n3 = 0; n3 < this.playlistsBitrates.size(); ++n3) {
                    n2 = this.playlistsBitrates.get(n3);
                    if (n2 >= n5 && n4 != -1) continue;
                    n5 = n2;
                    n4 = n3;
                }
            }
            if (n4 < 0 || n4 >= this.playlists.size()) {
                return null;
            }
            return this.playlists.get(n4);
        }
    }

    private static class PlaylistParser {
        private boolean isFirstLine = true;
        private boolean isLineMediaFileURI = false;
        private boolean isEndList = false;
        private boolean isLinePlaylistURI = false;
        private boolean isVariantPlaylist = false;
        private boolean isDiscontinuity = false;
        private int targetDuration = 0;
        private int sequenceNumber = 0;
        private int dataListIndex = -1;
        private List<String> dataListString = new ArrayList<String>();
        private List<Integer> dataListInteger = new ArrayList<Integer>();
        private List<Double> dataListDouble = new ArrayList<Double>();
        private List<Boolean> dataListBoolean = new ArrayList<Boolean>();

        private PlaylistParser() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void load(URI uRI) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            try {
                Charset charset;
                httpURLConnection = (HttpURLConnection)uRI.toURL().openConnection();
                httpURLConnection.setRequestMethod("GET");
                if (httpURLConnection.getResponseCode() != 200) {
                    MediaUtils.error(this, MediaError.ERROR_LOCATOR_CONNECTION_LOST.code(), "HTTP responce code: " + httpURLConnection.getResponseCode(), null);
                }
                if ((charset = this.getCharset(uRI.toURL().toExternalForm(), httpURLConnection.getContentType())) != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charset));
                }
                if (bufferedReader != null) {
                    boolean bl;
                    while (bl = this.parseLine(bufferedReader.readLine())) {
                    }
                }
            }
            catch (MalformedURLException malformedURLException) {
            }
            catch (IOException iOException) {
            }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                    Locator.closeConnection(httpURLConnection);
                }
            }
        }

        private boolean isVariantPlaylist() {
            return this.isVariantPlaylist;
        }

        private boolean isLivePlaylist() {
            return !this.isEndList;
        }

        private int getTargetDuration() {
            return this.targetDuration;
        }

        private int getSequenceNumber() {
            return this.sequenceNumber;
        }

        private boolean hasNext() {
            ++this.dataListIndex;
            return this.dataListString.size() > this.dataListIndex || this.dataListInteger.size() > this.dataListIndex || this.dataListDouble.size() > this.dataListIndex || this.dataListBoolean.size() > this.dataListIndex;
        }

        private String getString() {
            return this.dataListString.get(this.dataListIndex);
        }

        private Integer getInteger() {
            return this.dataListInteger.get(this.dataListIndex);
        }

        private Double getDouble() {
            return this.dataListDouble.get(this.dataListIndex);
        }

        private Boolean getBoolean() {
            return this.dataListBoolean.get(this.dataListIndex);
        }

        private boolean parseLine(String string) {
            if (string == null) {
                return false;
            }
            if (this.isFirstLine) {
                if (string.compareTo("#EXTM3U") != 0) {
                    return false;
                }
                this.isFirstLine = false;
                return true;
            }
            if (string.isEmpty() || string.startsWith("#") && !string.startsWith("#EXT")) {
                return true;
            }
            if (string.startsWith("#EXTINF")) {
                String[] arrstring;
                String[] arrstring2 = string.split(":");
                if (arrstring2.length == 2 && arrstring2[1].length() > 0 && (arrstring = arrstring2[1].split(",")).length >= 1) {
                    this.dataListDouble.add(Double.parseDouble(arrstring[0]));
                }
                this.isLineMediaFileURI = true;
            } else if (string.startsWith("#EXT-X-TARGETDURATION")) {
                String[] arrstring = string.split(":");
                if (arrstring.length == 2 && arrstring[1].length() > 0) {
                    this.targetDuration = Integer.parseInt(arrstring[1]);
                }
            } else if (string.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                String[] arrstring = string.split(":");
                if (arrstring.length == 2 && arrstring[1].length() > 0) {
                    this.sequenceNumber = Integer.parseInt(arrstring[1]);
                }
            } else if (string.startsWith("#EXT-X-STREAM-INF")) {
                String[] arrstring;
                this.isVariantPlaylist = true;
                int n = 0;
                String[] arrstring3 = string.split(":");
                if (arrstring3.length == 2 && arrstring3[1].length() > 0 && (arrstring = arrstring3[1].split(",")).length > 0) {
                    for (int i = 0; i < arrstring.length; ++i) {
                        String[] arrstring4;
                        arrstring[i] = arrstring[i].trim();
                        if (!arrstring[i].startsWith("BANDWIDTH") || (arrstring4 = arrstring[i].split("=")).length != 2 || arrstring4[1].length() <= 0) continue;
                        n = Integer.parseInt(arrstring4[1]);
                    }
                }
                if (n < 1) {
                    return false;
                }
                this.dataListInteger.add(n);
                this.isLinePlaylistURI = true;
            } else if (string.startsWith("#EXT-X-ENDLIST")) {
                this.isEndList = true;
            } else if (string.startsWith("#EXT-X-DISCONTINUITY")) {
                this.isDiscontinuity = true;
            } else if (this.isLinePlaylistURI) {
                this.isLinePlaylistURI = false;
                this.dataListString.add(string);
            } else if (this.isLineMediaFileURI) {
                this.isLineMediaFileURI = false;
                this.dataListString.add(string);
                this.dataListBoolean.add(this.isDiscontinuity);
                this.isDiscontinuity = false;
            }
            return true;
        }

        private Charset getCharset(String string, String string2) {
            if (string != null && HLSConnectionHolder.stripParameters(string).endsWith(".m3u8") || string2 != null && string2.equals("application/vnd.apple.mpegurl")) {
                if (Charset.isSupported(HLSConnectionHolder.CHARSET_UTF_8)) {
                    return Charset.forName(HLSConnectionHolder.CHARSET_UTF_8);
                }
            } else if ((string != null && HLSConnectionHolder.stripParameters(string).endsWith(".m3u") || string2 != null && string2.equals("audio/mpegurl")) && Charset.isSupported(HLSConnectionHolder.CHARSET_US_ASCII)) {
                return Charset.forName(HLSConnectionHolder.CHARSET_US_ASCII);
            }
            return null;
        }
    }

    private class PlaylistThread
    extends Thread {
        public static final int STATE_INIT = 0;
        public static final int STATE_EXIT = 1;
        public static final int STATE_RELOAD_PLAYLIST = 2;
        private BlockingQueue<Integer> stateQueue = new LinkedBlockingQueue<Integer>();
        private URI playlistURI = null;
        private Playlist reloadPlaylist = null;
        private final Object reloadLock = new Object();
        private volatile boolean stopped = false;

        private PlaylistThread() {
            this.setName("JFXMedia HLS Playlist Thread");
            this.setDaemon(true);
        }

        private void setPlaylistURI(URI uRI) {
            this.playlistURI = uRI;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void setReloadPlaylist(Playlist playlist) {
            Object object = this.reloadLock;
            synchronized (object) {
                this.reloadPlaylist = playlist;
            }
        }

        @Override
        public void run() {
            while (!this.stopped) {
                try {
                    int n = this.stateQueue.take();
                    switch (n) {
                        case 0: {
                            this.stateInit();
                            break;
                        }
                        case 1: {
                            this.stopped = true;
                            break;
                        }
                        case 2: {
                            this.stateReloadPlaylist();
                            break;
                        }
                    }
                }
                catch (Exception exception) {}
            }
        }

        private void putState(int n) {
            if (this.stateQueue != null) {
                try {
                    this.stateQueue.put(n);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
        }

        private void stateInit() {
            if (this.playlistURI == null) {
                return;
            }
            PlaylistParser playlistParser = new PlaylistParser();
            playlistParser.load(this.playlistURI);
            if (playlistParser.isVariantPlaylist()) {
                HLSConnectionHolder.this.variantPlaylist = new VariantPlaylist(this.playlistURI);
                while (playlistParser.hasNext()) {
                    HLSConnectionHolder.this.variantPlaylist.addPlaylistInfo(playlistParser.getString(), playlistParser.getInteger());
                }
            } else {
                if (HLSConnectionHolder.this.currentPlaylist == null) {
                    HLSConnectionHolder.this.currentPlaylist = new Playlist(playlistParser.isLivePlaylist(), playlistParser.getTargetDuration());
                    HLSConnectionHolder.this.currentPlaylist.setPlaylistURI(this.playlistURI);
                }
                if (HLSConnectionHolder.this.currentPlaylist.setSequenceNumber(playlistParser.getSequenceNumber())) {
                    while (playlistParser.hasNext()) {
                        HLSConnectionHolder.this.currentPlaylist.addMediaFile(playlistParser.getString(), playlistParser.getDouble(), playlistParser.getBoolean());
                    }
                }
                if (HLSConnectionHolder.this.variantPlaylist != null) {
                    HLSConnectionHolder.this.variantPlaylist.addPlaylist(HLSConnectionHolder.this.currentPlaylist);
                }
            }
            if (HLSConnectionHolder.this.variantPlaylist != null) {
                while (HLSConnectionHolder.this.variantPlaylist.hasNext()) {
                    try {
                        HLSConnectionHolder.this.currentPlaylist = new Playlist(HLSConnectionHolder.this.variantPlaylist.getPlaylistURI());
                        HLSConnectionHolder.this.currentPlaylist.update(null);
                        HLSConnectionHolder.this.variantPlaylist.addPlaylist(HLSConnectionHolder.this.currentPlaylist);
                    }
                    catch (URISyntaxException uRISyntaxException) {
                    }
                    catch (MalformedURLException malformedURLException) {}
                }
            }
            if (HLSConnectionHolder.this.variantPlaylist != null) {
                HLSConnectionHolder.this.currentPlaylist = HLSConnectionHolder.this.variantPlaylist.getPlaylist(0);
                HLSConnectionHolder.this.isBitrateAdjustable = true;
            }
            if (HLSConnectionHolder.this.currentPlaylist.isLive()) {
                this.setReloadPlaylist(HLSConnectionHolder.this.currentPlaylist);
                this.putState(2);
            }
            HLSConnectionHolder.this.readySignal.countDown();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void stateReloadPlaylist() {
            try {
                long l;
                Object object = this.reloadLock;
                synchronized (object) {
                    l = this.reloadPlaylist.getTargetDuration() / 2L;
                }
                Thread.sleep(l);
            }
            catch (InterruptedException interruptedException) {
                return;
            }
            Object object = this.reloadLock;
            synchronized (object) {
                this.reloadPlaylist.update(null);
            }
            this.putState(2);
        }
    }
}

