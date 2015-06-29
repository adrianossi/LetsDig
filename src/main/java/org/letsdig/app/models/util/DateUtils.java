package org.letsdig.app.models.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by adrian on 6/28/15.
 */
abstract public class DateUtils {

    // variable to uniformly format dates. Usage: String formattedDate = dateFormatter.format(Date timeStamp);
    static DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);

    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }
}
