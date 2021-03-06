/**
 * 
 */
package com.oauth.consumer.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.oauth.commons.OAuth;

/**
 * @author Deepak R Shankar
 *
 */
public class ExcerptInputStream extends BufferedInputStream{
	 /**
     * A marker that's appended to the excerpt if it's less than the complete
     * stream.
     */
    public static final byte[] ELLIPSIS = OAuth.encodeCharacters(" ...");

    public ExcerptInputStream(InputStream in) throws IOException {
        super(in);
        mark(LIMIT);
        int total = 0;
        int read;
        while ((read = read(excerpt, total, LIMIT - total)) != -1 && ((total += read) < LIMIT));
        if (total == LIMIT) {
            // Only add the ellipsis if there are at least LIMIT bytes
            System.arraycopy(ELLIPSIS, 0, excerpt, total, ELLIPSIS.length);
        } else {
            byte[] tmp = new byte[total];
            System.arraycopy(excerpt, 0, tmp, 0, total);
            excerpt = tmp;
        }
        reset();
    }

    private static final int LIMIT = 1024;
    private byte[] excerpt = new byte[LIMIT + ELLIPSIS.length];

    /** The first few bytes of data, plus ELLIPSIS if there are more bytes. */
    public byte[] getExcerpt()
    {
        return excerpt;
    }

}
