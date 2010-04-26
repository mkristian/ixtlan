/**
 *
 */
package de.saumya.gwt.persistence.client;

import java.sql.Date;
import java.sql.Timestamp;

public class TimestampFactory {
    final String value;

    public TimestampFactory(final String value) {
        if (value.contains(".")) {
            this.value = value + "000000000".substring(0, 29 - value.length());
        }
        else {
            this.value = value + ".000000000";
        }
    }

    @SuppressWarnings("deprecation")
    public Timestamp toTimestamp() {
        return new Timestamp(toInt(0, 4) - 1900,
                toInt(5) - 1,
                toInt(8),
                toInt(11),
                toInt(14),
                toInt(17),
                toInt(20, 9));
    }

    public Date toDate() {
        return new Date(toTimestamp().getTime());
    }

    private int toInt(final int from) {
        return toInt(from, 2);
    }

    private int toInt(final int from, final int len) {
        return Integer.parseInt(this.value.substring(from, from + len));
    }
}
