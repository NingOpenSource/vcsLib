package org.yanning.gradle.vcs_lib.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * This class is to provide function output properties by input order and add comment to each property.
 *
 * @author zhengfan1
 */
public class OrderProperties extends LinkProperties {

    private LinkedHashMap<String, String> commentMap = new LinkedHashMap<String, String>();

    /**
     * Version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public OrderProperties() {
        super();
    }

    /**
     * Constructor.
     *
     * @param properties the java propertis.
     */
    public OrderProperties(LinkProperties properties) {
        super(properties);
        //Initialize the comment.
        Iterator<Object> iterator = properties.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            this.commentMap.put((String) key, null);
        }
    }

    /**
     * Add comment to a property.
     *
     * @param key     the key of the property.
     * @param comment the comment of the property.
     * @return true = add it
     * false  don't have this key.
     */
    public boolean addComment(String key, String comment) {
        if (this.containsKey(key)) {
            this.commentMap.put(key, comment);
            return true;
        }
        return false;
    }

    /**
     * To set property.
     *
     * @param key     the key of property.
     * @param value   the value of property.
     * @param comment the comment of property.
     * @return value object
     */
    public Object setPropertyWithComment(String key, String value, String comment) {
        this.commentMap.put(key, comment);
        return super.setProperty(key, value);
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        return setPropertyWithComment(key, value, "");
    }

    /**
     * To output according to the order of input.
     *
     * @param writer   the writer
     * @param comments the comments of this property file.
     * @throws IOException exception.
     */
    public void orderStore(Writer writer, String comments) throws IOException {
        BufferedWriter bufferedWriter = (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
        if (comments != null) {
            OrderProperties.writeComments(bufferedWriter, comments);
        }
        bufferedWriter.write("#" + new Date().toString());
        bufferedWriter.newLine();
        bufferedWriter.newLine();

        synchronized (this) {
            Iterator<String> iterator = this.commentMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = this.getProperty(key);
                String comment = this.commentMap.get(key);
                key = saveConvert(key, true, false);
                value = saveConvert(value, false, false);
                key = saveConvert(key, true, false);
                if (comment != null && !comment.equals("")) {
                    writeComments(bufferedWriter, comment);
                }
                bufferedWriter.write(key + "=" + value);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
    }

    /*
     * Converts unicodes to encoded \\uxxxx and escapes
     * special characters with a preceding slash
     * !!! Copy from java source code.
     */
    private String saveConvert(String theString,
                               boolean escapeSpace,
                               boolean escapeUnicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    /*
     * !!!Copy from java source code.
     */
    private static void writeComments(BufferedWriter bw, String comments)
            throws IOException {
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;
        char[] uu = new char[6];
        uu[0] = '\\';
        uu[1] = 'u';
        while (current < len) {
            char c = comments.charAt(current);
            if (c > '\u00ff' || c == '\n' || c == '\r') {
                if (last != current)
                    bw.write(comments.substring(last, current));
                if (c > '\u00ff') {
                    uu[2] = toHex((c >> 12) & 0xf);
                    uu[3] = toHex((c >> 8) & 0xf);
                    uu[4] = toHex((c >> 4) & 0xf);
                    uu[5] = toHex(c & 0xf);
                    bw.write(new String(uu));
                } else {
                    bw.newLine();
                    if (c == '\r' &&
                            current != len - 1 &&
                            comments.charAt(current + 1) == '\n') {
                        current++;
                    }
                    if (current == len - 1 ||
                            (comments.charAt(current + 1) != '#' &&
                                    comments.charAt(current + 1) != '!'))
                        bw.write("#");
                }
                last = current + 1;
            }
            current++;
        }
        if (last != current)
            bw.write(comments.substring(last, current));
        bw.newLine();
    }

    /*
     * !!! Copy from java source code.
     */
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /**
     * A table of hex digits
     */
    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };


}