/*
 * Copyright © 2016 Liaison Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.liaison.javabasics.serialization;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import com.liaison.javabasics.commons.Uninstantiable;
import com.liaison.javabasics.commons.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.EnumSet;

/**
 * TODO
 * @author Mike Davis; Liaison Technologies, Inc.
 * @author Branden Smith; Liaison Technologies, Inc.
 */
public final class BytesUtil extends Uninstantiable {

    /**
     * TODO
     * Thread-safe, per the API:
     * https://docs.oracle.com/javase/8/docs/api/java/util/Base64.Encoder.html
     */
    public static final Base64.Encoder BASE64_ENC;
    /**
     * TODO
     * Thread-safe, per the API:
     * https://docs.oracle.com/javase/8/docs/api/java/util/Base64.Decoder.html
     */
    public static final Base64.Decoder BASE64_DEC;

    /**
     * TODO
     */
    public static final byte[] HBASE_EMPTY = new byte[0];

    /**
     * Depending on the JVM implementation, Java arrays may be limited in size to slightly under
     * {@link Integer#MAX_VALUE} (the maximum possible address). For safety, follow the convention
     * established in the implementation of {@link java.util.ArrayList}, and limit array sizes to
     * <code>Integer.MAX_VALUE - 8</code>.
     * @see <a href="http://stackoverflow.com/questions/3038392/do-java-arrays-have-a-maximum-size">http://stackoverflow.com/questions/3038392/do-java-arrays-have-a-maximum-size</a>
     * @see <a href="http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/ArrayList.java#l229">http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/ArrayList.java#l229</a>
     */
    public static final long BYTE_CONCAT_MAX_LENGTH = Integer.MAX_VALUE - 8;
    
    /**
     * TODO
     * @param ref1
     * @param ref2
     * @return
     */
    public static boolean refEquals(final byte[] ref1, final byte[] ref2) {
        return Util.refEquals(ref1, ref2);
    }
    
    /**
     * TODO
     * @param bytes
     * @return
     */
    public static byte[] simplify(byte[] bytes) {
        if ((bytes != null) && (bytes.length <= 0)) {
            bytes = null;
        }
        return bytes;
    }
    
    /**
     * Convert the string to bytes using the supplied Charset. If the string is null, returns null.
     * @param str String value
     * @param charset the charset to use to conver the String to bytes
     * @return byte[] representation of the String value, converted using the Charset specified
     * @throws IllegalArgumentException if the provided Charset is null
     */
    public static byte[] toBytes(final String str, Charset charset) throws IllegalArgumentException {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            throw new IllegalArgumentException("Charset reference expected for conversion");
        }
        return str.getBytes(charset);
    }
    
    /**
     * Convert the string to bytes using the default Charset specified by
     * {@link Constants#CHARSET_DEFAULT} ({@value Constants#CHARSET_DEFAULT_STR}). If the string is
     * null, returns null.
     * @param str String value
     * @return byte[] representation of the String value, converted using the Charset specified by
     * {@link Constants#CHARSET_DEFAULT} ({@value Constants#CHARSET_DEFAULT_STR})
     */
    public static byte[] toBytes(final String str) {
        return toBytes(str, Constants.CHARSET_DEFAULT);
    }
    
    /**
     * TODO
     * @param byteArr
     * @return
     */
    public static byte[] forHBase(final byte[] byteArr) {
        if (byteArr == null) {
            return HBASE_EMPTY;
        }
        return byteArr;
    }

    /**
     * Convert the bytes to String using the supplied Charset. If the bytes are null, returns null.
     * @param bytes byte[] representation of the String value
     * @param charset the charset to use to conver the bytes to String
     * @return the String value represented by the byte[], converted using the Charset specified
     * @throws IllegalArgumentException if the provided Charset is null
     */
    public static String toString(final byte[] bytes, Charset charset) throws IllegalArgumentException {
        if (bytes == null) {
            return null;
        }
        if (charset == null) {
            throw new IllegalArgumentException("Charset reference expected for conversion");
        }
        return new String(bytes, charset);
    }
    
    /**
     * Convert the bytes to String using the default Charset specified by
     * {@link Constants#CHARSET_DEFAULT} ({@value Constants#CHARSET_DEFAULT_STR}). 
     * If the bytes are null, returns null.
     * @param bytes byte[] representation of the String value
     * @return the String value represented by the byte[], converted using the Charset specified by
     * {@link Constants#CHARSET_DEFAULT} ({@value Constants#CHARSET_DEFAULT_STR})
     */
    public static String toString(final byte[] bytes) {
        return toString(bytes, Constants.CHARSET_DEFAULT);
    }

    /**
     * TODO
     * @param bytes
     * @param offset
     * @return
     */
    public static String toString(final byte[] bytes, final int offset) {
        if (bytes != null && offset >= 0 && offset <= bytes.length) {
            return new String(bytes, offset, bytes.length - offset, Constants.CHARSET_DEFAULT);
        }
        return null;
    }

    /**
     * TODO
     * @param value
     * @return
     */
    public static byte[] toBytes(final long value) {
        return Longs.toByteArray(value);
    }

    /**
     * TODO
     * @param value
     * @return
     */
    public static byte[] toBytes(final int value) {
        return Ints.toByteArray(value);
    }

    /**
     * TODO
     * @param value
     * @return
     */
    public static byte[] toBytes(final short value) {
        return Shorts.toByteArray(value);
    }

    /**
     * TODO
     * @param bytes
     * @return
     */
    public static Long toLong(final byte[] bytes) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && bytes.length >= 8) {
            return Long.valueOf(Longs.fromByteArray(bytes));
        }
        return null;
    }

    /**
     * TODO
     * @param bytes
     * @param offset
     * @return
     */
    public static Long toLong(final byte[] bytes, final int offset) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && offset >= 0 && bytes.length >= offset + 8) {
            return Long.valueOf(Longs.fromBytes(
                bytes[offset + 0],
                bytes[offset + 1],
                bytes[offset + 2],
                bytes[offset + 3],
                bytes[offset + 4],
                bytes[offset + 5],
                bytes[offset + 6],
                bytes[offset + 7]));
        }
        return null;
    }

    /**
     * TODO
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @param b5
     * @param b6
     * @param b7
     * @param b8
     * @return
     */
    public static long toLong(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        return Longs.fromBytes(b1, b2, b3, b4, b5, b6, b7, b8);
    }

    /**
     * TODO
     * @param bytes
     * @return
     */
    public static Integer toInteger(final byte[] bytes) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && bytes.length >= 4) {
            return Integer.valueOf(Ints.fromByteArray(bytes));
        }
        return null;
    }
    
    /**
     * TODO
     * @param bytes
     * @param offset
     * @return
     */
    public static Integer toInteger(final byte[] bytes, final int offset) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && offset >= 0 && bytes.length >= offset + 4) {
            return Integer.valueOf(Ints.fromBytes(
                bytes[offset + 0],
                bytes[offset + 1],
                bytes[offset + 2],
                bytes[offset + 3]));
        }
        return null;
    }

    /**
     * TODO
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @return
     */
    public static int toInteger(byte b1, byte b2, byte b3, byte b4) {
        return Ints.fromBytes(b1, b2, b3, b4);
    }

    /**
     * TODO
     * @param bytes
     * @return
     */
    public static Short toShort(final byte[] bytes) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && bytes.length >= 2) {
            return Short.valueOf(Shorts.fromByteArray(bytes));
        }
        return null;
    }
    
    /**
     * TODO
     * @param bytes
     * @param offset
     * @return
     */
    public static Short toShort(final byte[] bytes, final int offset) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && offset >= 0 && bytes.length >= offset + 2) {
            return Short.valueOf(Shorts.fromBytes(
                bytes[offset + 0],
                bytes[offset + 1]));
        }
        return null;
    }

    /**
     * TODO
     * @param b1
     * @param b2
     * @return
     */
    public static short toShort(byte b1, byte b2) {
        return Shorts.fromBytes(b1, b2);
    }

    /**
     * TODO
     * @param time
     * @return
     */
    public static byte[] toBytes(final Instant time) {
        // TODO: determine constant values programmatically, or make them constant
        if (time != null) {
            byte[] bytes = new byte[8 + 4];
            System.arraycopy(toBytes(time.getEpochSecond()), 0, bytes, 0, 8);
            System.arraycopy(toBytes(time.getNano()), 0, bytes, 8, 4);
            return bytes;
        }
        return null;
    }
    
    /**
     * TODO
     * @param bytes
     * @return
     */
    public static Instant toInstant(final byte[] bytes) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && bytes.length >= 8 + 4) {
            long millisecs = Longs.fromByteArray(bytes);
            int nano = Ints.fromBytes(bytes[8], bytes[9], bytes[10], bytes[11]);
            return Instant.ofEpochSecond(millisecs, nano);
        }
        return null;
    }

    /**
     * TODO
     * @param bytes
     * @param offset
     * @return
     */
    public static Instant toInstant(final byte[] bytes, final int offset) {
        // TODO: determine constant values programmatically, or make them constant
        if (bytes != null && offset >= 0 && bytes.length >= offset + 8 + 4) {
            return Instant.ofEpochSecond(toLong(bytes, offset).longValue(),
                                         toInteger(bytes, offset + 8).longValue());
        }
        return null;
    }
    
    /**
     * TODO
     * @param bytes
     * @return
     */
    public static String encode(final byte[] bytes) {
        return new String(BASE64_ENC.encode(bytes), Constants.CHARSET_DEFAULT);
    }
    
    /**
     * TODO
     * @param text
     * @return
     */
    public static byte[] decode(final String text) {
        return BASE64_DEC.decode(text);
    }
    
    /**
     * TODO
     * @param inBytes
     * @return
     */
    public static byte[] copy(final byte[] inBytes) {
        if (inBytes == null) {
            return null;
        } else {
            return Arrays.copyOf(inBytes, inBytes.length);
        }
    }
    
    /**
     * TODO
     * @param bytes
     * @return
     */
    public static byte[] concat(final byte[]... bytes) {
        String logMsg;
        final long length;
        if (bytes != null) {
            // get the total byte count         
            length = Arrays.stream(bytes).mapToLong(b -> (b != null ? b.length : 0)).sum();
            if (length > BYTE_CONCAT_MAX_LENGTH) {
                logMsg = "byte[] cannot contain more than " + BYTE_CONCAT_MAX_LENGTH + " bytes";
                throw new IllegalArgumentException(logMsg);
            }
            final byte[] concatenated = new byte[(int) length];
            int start = 0;
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] != null) {
                    System.arraycopy(bytes[i], 0, concatenated, start, bytes[i].length);
                    start += bytes[i].length;
                }
            }
            return concatenated;
        }
        return null;
    }

    /**
     * TODO
     * @param value
     * @return
     */
    public static <S extends Serializable> byte[] serialize(S value) {
        if (value != null) {
            try (
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bytes);
            ) {
                out.writeObject(value);
                out.close();
                return bytes.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * TODO
     * @param bytes
     * @param asClass
     * @return
     */
    public static <S extends Serializable> S deserialize(byte[] bytes, final Class<S> asClass) {
        if (bytes != null) {
            try (
                final ObjectInputStream input =
                    new ObjectInputStream(new ByteArrayInputStream(bytes))
            ) {
                return asClass.cast(input.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * TODO
     * @param blocks
     * @return
     */
    public static long count(final Collection<byte[]> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return 0L;
        }
        return blocks.stream().mapToLong(block -> block.length).sum();
    }
    
    /**
     * TODO
     * @param first
     * @param second
     * @return
     */
    public static byte[] zipBytes(final byte[] first, final byte[] second) {
        if (second == null || second.length == 0) {
            return first;
        }
        if (first == null || first.length == 0) {
            return second;
        }
        final int firstLength = first.length;
        final int secondLength = second.length;
        final int length = firstLength + secondLength;
        final byte[] zipped = new byte[length];
        for (int z = 0, i = 0, j = 0; z < length; z++) {
            if (i < firstLength && (i <= j || j >= secondLength)) {
                zipped[z] = first[i++];
            } else {
                zipped[z] = second[j++];
            }
        }
        return zipped;
    }
    
    /**
     * TODO
     * @param bytes
     * @param selectedCopyStrategy
     * @param copyIfTheseStrategies
     * @return
     * @throws IllegalArgumentException
     */
    private static byte[] byteActionWithContext(final byte[] bytes, final DefensiveCopyStrategy selectedCopyStrategy, final EnumSet<DefensiveCopyStrategy> copyIfTheseStrategies) throws IllegalArgumentException {
        Util.ensureNotNull(copyIfTheseStrategies,
                           "Util#byteActionWithContext",
                           "copyIfTheseStrategies",
                           EnumSet.class);
        Util.ensureNotNull(selectedCopyStrategy,
                           "Util#byteActionWithContext",
                           "context",
                           DefensiveCopyStrategy.class);
        if (copyIfTheseStrategies.contains(selectedCopyStrategy)) {
            return copy(bytes);
        } else {
            return bytes;
        }
    }
    
    /**
     * Prepares the given <b>input</b> byte array to be <b>SET</b> in a framework object. Depending
     * on the specification of the provided context, optionally makes a defensive copy rather than
     * setting the framework object to use the exact reference provided. Specifically, if the
     * defensive copy strategy specified by the context is one of the strategies specified in
     * {@link DefensiveCopyStrategy#COPY_ON_SET}, then make the copy; otherwise, set using the
     * original reference. 
     * @param clientBytes original byte[] reference to which a framework object's internal byte
     * array reference is being set; a defensive copy may be made, depending on the context
     * @param selectedCopyStrategy {@link DefensiveCopyStrategy} which specifies whether a
     * defensive copy of the input byte array will be made prior to setting
     * @return the value to use when setting the framework object; either the original reference or
     * a defensive copy, depending on the logic specified above
     * @throws IllegalArgumentException if context is null
     */
    public static byte[] setInternalByteArray(final byte[] clientBytes, final DefensiveCopyStrategy selectedCopyStrategy) throws IllegalArgumentException {
        return byteActionWithContext(clientBytes,
                                     ((selectedCopyStrategy == null)?
                                         DefensiveCopyStrategy.ALWAYS:
                                         selectedCopyStrategy),
                                     DefensiveCopyStrategy.COPY_ON_SET);
    }
    
    /**
     * Prepares the given <b>existing/internal</b> byte array to be <b>RETURNED</b> from a
     * framework object. Depending on the specification of the provided context, optionally makes a
     * defensive copy rather than returning the internal byte array maintained within the framework
     * object. Specifically, if the defensive copy strategy specified by the context is one of the
     * strategies specified in {@link DefensiveCopyStrategy#COPY_ON_GET}, then make the copy;
     * otherwise, return the original reference. 
     * @param storedBytes a framework object's internal byte array; a defensive copy may be made,
     * depending on the context
     * @param selectedCopyStrategy {@link DefensiveCopyStrategy} which specifies whether a
     * defensive copy of the internal byte array will be made prior to the get return
     * @return the value which the get operation should return to the client; either the given
     * reference to the internal byte array or a defensive copy, depending on the logic specified
     * above
     * @throws IllegalArgumentException if context is null
     */
    public static byte[] getInternalByteArray(final byte[] storedBytes, final DefensiveCopyStrategy selectedCopyStrategy) throws IllegalArgumentException {
        return byteActionWithContext(storedBytes,
                                     ((selectedCopyStrategy == null)?
                                         DefensiveCopyStrategy.ALWAYS:
                                         selectedCopyStrategy),
                                     DefensiveCopyStrategy.COPY_ON_GET);
    }

    static {
        BASE64_ENC = Base64.getEncoder();
        BASE64_DEC = Base64.getDecoder();
    }
    
    private BytesUtil() { }
}
