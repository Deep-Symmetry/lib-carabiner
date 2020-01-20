package org.deepsymmetry.libcarabiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.bpsm.edn.Keyword;
import us.bpsm.edn.Symbol;
import us.bpsm.edn.parser.Parseable;
import us.bpsm.edn.parser.Parser;
import us.bpsm.edn.parser.Parsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Simplifies the interpretation of messages sent by Carabiner by converting them from their
 * <a href="https://github.com/edn-format/edn#edn">Extensible Data Notation</a> format
 * to more familiar Java structures. Each message consists of a type symbol, optionally followed
 * by a map of keywords and values. Constructing a {@code Message} object provides easy access to those values,
 * with the symbol and keywords replaced by ordinary interned Java {@link String} instances.</p>
 *
 * <p>For example, if instantiated with the string
 * {@code "status { :peers 0 :bpm 120.000000 :start 73743731220 :beat 597.737570 }"}, a {@code Message} instance
 * would have a {@link #messageType} of {@code "status"}, and its {@link #details} would be a {@link Map} with keys
 * {@code "peers"} (holding the {@link Long} value {@code 0}),
 * {@code "bpm"} (holding the {@link Double} value {@code 120.0}),
 * {@code "start"} (holding the {@link Long} value {@code 73743731220}), and
 * {@code "beat"} (holding the {@link Double} value {@code 597.737570}).</p>
 *
 * <p>An instance constructed with the string {@code "bad-beat"} would have a {@link #messageType} of {@code "bad-beat"}
 * and a {@code null} {@link #details}.</p>
 *
 * <p>Once constructed, Message objects are immutable data holders.</p>
 *
 * <p>Created by James Elliott on 2020-01-19.</p>
 */
public class Message {

    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    /**
     * Identifies the type of the message that was received, which was the symbol that the message started with.
     * This value is interned, so message types can be compared for equality by object identity.
     */
    public final String messageType;

    /**
     * Holds the details sent after the message type, if any. Will be {@code null} if there were no details.
     * Keys in the map are interned strings, so they can be compared for equality by object identity.
     */
    public final Map<String, Object> details;

    /**
     * Construct an instance given an <a href="https://github.com/edn-format/edn#edn">edn</a>
     * response line you have received from Carabiner.
     *
     * @param response the full text of a line that Carabiner has sent you
     */
    public Message(String response) {
        final Parseable parseable = Parsers.newParseable(response);
        final Parser parser = Parsers.newParser(Parsers.defaultConfiguration());

        // Start by reading the message type.
        Object read = parser.nextValue(parseable);
        if (read instanceof Symbol) {
            messageType = ((Symbol) read).getName().intern();
        } else {
            throw new IllegalArgumentException("Carabiner messages must begin with a symbol. Received:" + response);
        }

        // Now see if there is a payload.
        read = parser.nextValue(parseable);
        if (read == Parser.END_OF_INPUT) {
            details = null;
        } else if (read instanceof Map) {
            details = Collections.unmodifiableMap(((Map<Keyword, Object>) read).entrySet().stream().collect(
                    HashMap::new,
                    (map, e) -> map.put(e.getKey().getName().intern(), e.getValue()),
                    Map::putAll
            ));
        } else {
            System.out.println(read);
            throw new IllegalArgumentException("Carabiner message details, if present, must be a map. Received: " + response);
        }

        read = parser.nextValue(parseable);
        if (read != Parser.END_OF_INPUT) {
            throw new IllegalArgumentException("Carabiner messages must consist of a symbol optionally followed by a map. Received: " +
                    response);
        }
    }
}
