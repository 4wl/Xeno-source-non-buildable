/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.URLs;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DirectoryURLConnection
extends URLConnection {
    private static final String[] patStrings = new String[]{"([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*((?:\\d\\d:\\d\\d)|(?:\\d{4}))\\s*(\\p{Print}*)", "(\\d{2}/\\d{2}/\\d{4})\\s*(\\d{2}:\\d{2}[ap])\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)", "(\\d{2}-\\d{2}-\\d{2})\\s*(\\d{2}:\\d{2}[AP]M)\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)"};
    private static final int[][] patternGroups = new int[][]{{7, 4, 5, 6, 1}, {4, 3, 1, 2, 0}, {4, 3, 1, 2, 0}};
    private static final Pattern[] patterns;
    private static final Pattern linkp;
    private static final String styleSheet = "<style type=\"text/css\" media=\"screen\">TABLE { border: 0;}TR.header { background: #FFFFFF; color: black; font-weight: bold; text-align: center;}TR.odd { background: #E0E0E0;}TR.even { background: #C0C0C0;}TD.file { text-align: left;}TD.fsize { text-align: right; padding-right: 1em;}TD.dir { text-align: center; color: green; padding-right: 1em;}TD.link { text-align: center; color: red; padding-right: 1em;}TD.date { text-align: justify;}</style>";
    private final URLConnection inner;
    private final boolean sure;
    private String dirUrl = null;
    private boolean toHTML = true;
    private final boolean ftp;
    private InputStream ins = null;

    DirectoryURLConnection(URLConnection uRLConnection, boolean bl) {
        super(uRLConnection.getURL());
        this.dirUrl = uRLConnection.getURL().toExternalForm();
        this.inner = uRLConnection;
        this.sure = !bl;
        this.ftp = true;
    }

    DirectoryURLConnection(URLConnection uRLConnection) {
        super(uRLConnection.getURL());
        this.dirUrl = uRLConnection.getURL().toExternalForm();
        this.ftp = false;
        this.sure = true;
        this.inner = uRLConnection;
    }

    @Override
    public void connect() throws IOException {
        this.inner.connect();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (this.ins == null) {
            this.ins = this.ftp ? new DirectoryInputStream(this.inner.getInputStream(), !this.sure) : new DirectoryInputStream(this.inner.getInputStream(), false);
        }
        return this.ins;
    }

    @Override
    public String getContentType() {
        try {
            if (!this.sure) {
                this.getInputStream();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (this.toHTML) {
            return "text/html";
        }
        return this.inner.getContentType();
    }

    @Override
    public String getContentEncoding() {
        return this.inner.getContentEncoding();
    }

    @Override
    public int getContentLength() {
        return this.inner.getContentLength();
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return this.inner.getHeaderFields();
    }

    @Override
    public String getHeaderField(String string) {
        return this.inner.getHeaderField(string);
    }

    static {
        linkp = Pattern.compile("(\\p{Print}+) \\-\\> (\\p{Print}+)$");
        patterns = new Pattern[patStrings.length];
        for (int i = 0; i < patStrings.length; ++i) {
            DirectoryURLConnection.patterns[i] = Pattern.compile(patStrings[i]);
        }
    }

    private final class DirectoryInputStream
    extends PushbackInputStream {
        private final byte[] buffer;
        private boolean endOfStream;
        private ByteArrayOutputStream bytesOut;
        private PrintStream out;
        private ByteArrayInputStream bytesIn;
        private final StringBuffer tmpString;
        private int lineCount;

        private DirectoryInputStream(InputStream inputStream, boolean bl) {
            CharSequence charSequence;
            super(inputStream, 512);
            this.endOfStream = false;
            this.bytesOut = new ByteArrayOutputStream();
            this.out = new PrintStream(this.bytesOut);
            this.bytesIn = null;
            this.tmpString = new StringBuffer();
            this.lineCount = 0;
            this.buffer = new byte[512];
            if (bl) {
                charSequence = new StringBuffer();
                int n = 0;
                try {
                    n = super.read(this.buffer, 0, this.buffer.length);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                if (n <= 0) {
                    DirectoryURLConnection.this.toHTML = false;
                } else {
                    for (int i = 0; i < n; ++i) {
                        charSequence.append((char)this.buffer[i]);
                    }
                    String string = charSequence.toString();
                    DirectoryURLConnection.this.toHTML = false;
                    for (Pattern pattern : patterns) {
                        Matcher matcher = pattern.matcher(string);
                        if (!matcher.find()) continue;
                        DirectoryURLConnection.this.toHTML = true;
                        break;
                    }
                    try {
                        super.unread(this.buffer, 0, n);
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
            }
            if (DirectoryURLConnection.this.toHTML) {
                int n;
                charSequence = null;
                URL uRL = null;
                if (!DirectoryURLConnection.this.dirUrl.endsWith("/")) {
                    DirectoryURLConnection.this.dirUrl = DirectoryURLConnection.this.dirUrl + "/";
                }
                try {
                    uRL = URLs.newURL(DirectoryURLConnection.this.dirUrl);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                String string = uRL.getPath();
                if (string != null && !string.isEmpty() && (n = string.lastIndexOf("/", string.length() - 2)) >= 0) {
                    int n2 = string.length() - n - 1;
                    n = DirectoryURLConnection.this.dirUrl.indexOf(string);
                    charSequence = DirectoryURLConnection.this.dirUrl.substring(0, n + string.length() - n2) + DirectoryURLConnection.this.dirUrl.substring(n + string.length());
                }
                this.out.print("<html><head><title>index of ");
                this.out.print(DirectoryURLConnection.this.dirUrl);
                this.out.print("</title>");
                this.out.print(DirectoryURLConnection.styleSheet);
                this.out.print("</head><body><h1>Index of ");
                this.out.print(DirectoryURLConnection.this.dirUrl);
                this.out.print("</h1><hr></hr>");
                this.out.print("<TABLE width=\"95%\" cellpadding=\"5\" cellspacing=\"5\">");
                this.out.print("<TR class=\"header\"><TD>File</TD><TD>Size</TD><TD>Last Modified</TD></TR>");
                if (charSequence != null) {
                    ++this.lineCount;
                    this.out.print("<TR class=\"odd\"><TD colspan=3 class=\"file\"><a href=\"");
                    this.out.print((String)charSequence);
                    this.out.print("\">Up to parent directory</a></TD></TR>");
                }
                this.out.close();
                this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
                this.out = null;
                this.bytesOut = null;
            }
        }

        private void parseFile(String string) {
            int n;
            this.tmpString.append(string);
            while ((n = this.tmpString.indexOf("\n")) >= 0) {
                String string2 = this.tmpString.substring(0, n);
                this.tmpString.delete(0, n + 1);
                String string3 = string2;
                String string4 = null;
                String string5 = null;
                boolean bl = false;
                boolean bl2 = false;
                URL uRL = null;
                if (string3 == null) continue;
                ++this.lineCount;
                try {
                    uRL = URLs.newURL(DirectoryURLConnection.this.dirUrl + URLEncoder.encode(string3, "UTF-8"));
                    URLConnection uRLConnection = uRL.openConnection();
                    uRLConnection.connect();
                    string5 = uRLConnection.getHeaderField("last-modified");
                    string4 = uRLConnection.getHeaderField("content-length");
                    if (string4 == null) {
                        bl = true;
                    }
                    uRLConnection.getInputStream().close();
                }
                catch (IOException iOException) {
                    bl2 = true;
                }
                if (this.bytesOut == null) {
                    this.bytesOut = new ByteArrayOutputStream();
                    this.out = new PrintStream(this.bytesOut);
                }
                this.out.print("<TR class=\"" + (this.lineCount % 2 == 0 ? "even" : "odd") + "\"><TD class=\"file\">");
                if (bl2) {
                    this.out.print(string3);
                } else {
                    this.out.print("<a href=\"");
                    this.out.print(uRL.toExternalForm());
                    this.out.print("\">");
                    this.out.print(string3);
                    this.out.print("</a>");
                }
                if (bl) {
                    this.out.print("</TD><TD class=\"dir\">&lt;Directory&gt;</TD>");
                } else {
                    this.out.print("</TD><TD class=\"fsize\">" + (string4 == null ? " " : string4) + "</TD>");
                }
                this.out.print("<TD class=\"date\">" + (string5 == null ? " " : string5) + "</TD></TR>");
            }
            if (this.bytesOut != null) {
                this.out.close();
                this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
                this.out = null;
                this.bytesOut = null;
            }
        }

        private void parseFTP(String string) {
            int n;
            this.tmpString.append(string);
            while ((n = this.tmpString.indexOf("\n")) >= 0) {
                String string2 = this.tmpString.substring(0, n);
                this.tmpString.delete(0, n + 1);
                String string3 = null;
                String string4 = null;
                String string5 = null;
                String string6 = null;
                boolean bl = false;
                Matcher matcher = null;
                for (int i = 0; i < patterns.length; ++i) {
                    matcher = patterns[i].matcher(string2);
                    if (!matcher.find()) continue;
                    string3 = matcher.group(patternGroups[i][0]);
                    string5 = matcher.group(patternGroups[i][1]);
                    string6 = matcher.group(patternGroups[i][2]);
                    if (patternGroups[i][3] > 0) {
                        string6 = string6 + " " + matcher.group(patternGroups[i][3]);
                    }
                    if (patternGroups[i][4] > 0) {
                        String string7 = matcher.group(patternGroups[i][4]);
                        bl = string7.startsWith("d");
                    }
                    if (!"<DIR>".equals(string5)) continue;
                    bl = true;
                    string5 = null;
                }
                if (string3 == null) continue;
                matcher = linkp.matcher(string3);
                if (matcher.find()) {
                    string3 = matcher.group(1);
                    string4 = matcher.group(2);
                }
                if (this.bytesOut == null) {
                    this.bytesOut = new ByteArrayOutputStream();
                    this.out = new PrintStream(this.bytesOut);
                }
                ++this.lineCount;
                this.out.print("<TR class=\"" + (this.lineCount % 2 == 0 ? "even" : "odd") + "\"><TD class=\"file\"><a href=\"");
                try {
                    this.out.print(DirectoryURLConnection.this.dirUrl + URLEncoder.encode(string3, "UTF-8"));
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    // empty catch block
                }
                if (bl) {
                    this.out.print("/");
                }
                this.out.print("\">");
                this.out.print(string3);
                this.out.print("</a>");
                if (string4 != null) {
                    this.out.print(" &rarr; " + string4 + "</TD><TD class=\"link\">&lt;Link&gt;</TD>");
                } else if (bl) {
                    this.out.print("</TD><TD class=\"dir\">&lt;Directory&gt;</TD>");
                } else {
                    this.out.print("</TD><TD class=\"fsize\">" + string5 + "</TD>");
                }
                this.out.print("<TD class=\"date\">" + string6 + "</TD></TR>");
            }
            if (this.bytesOut != null) {
                this.out.close();
                this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
                this.out = null;
                this.bytesOut = null;
            }
        }

        private void endOfList() {
            if (DirectoryURLConnection.this.ftp) {
                this.parseFTP("\n");
            } else {
                this.parseFile("\n");
            }
            if (this.bytesOut == null) {
                this.bytesOut = new ByteArrayOutputStream();
                this.out = new PrintStream(this.bytesOut);
            }
            this.out.print("</TABLE><br><hr></hr></body></html>");
            this.out.close();
            this.bytesIn = new ByteArrayInputStream(this.bytesOut.toByteArray());
            this.out = null;
            this.bytesOut = null;
        }

        @Override
        public int read(byte[] arrby) throws IOException {
            return this.read(arrby, 0, arrby.length);
        }

        @Override
        public int read(byte[] arrby, int n, int n2) throws IOException {
            int n3 = 0;
            if (!DirectoryURLConnection.this.toHTML) {
                return super.read(arrby, n, n2);
            }
            if (this.bytesIn != null) {
                n3 = this.bytesIn.read(arrby, n, n2);
                if (n3 == -1) {
                    this.bytesIn.close();
                    this.bytesIn = null;
                    if (this.endOfStream) {
                        return -1;
                    }
                } else {
                    return n3;
                }
            }
            if (!this.endOfStream) {
                n3 = super.read(this.buffer, 0, this.buffer.length);
                if (n3 == -1) {
                    this.endOfStream = true;
                    this.endOfList();
                    return this.read(arrby, n, n2);
                }
                if (DirectoryURLConnection.this.ftp) {
                    this.parseFTP(new String(this.buffer, 0, n3));
                } else {
                    this.parseFile(new String(this.buffer, 0, n3));
                }
                if (this.bytesIn != null) {
                    return this.read(arrby, n, n2);
                }
            }
            return 0;
        }
    }
}

