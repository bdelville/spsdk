package eu.hithredin.spsdk.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by benoit on 1/6/16.
 */
public class UtilsOther {


    public static void openUrlLink(Context ctx, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(intent);
    }

    private static Random r;

    public static int getRandomInt() {
        if (r == null) {
            r = new Random();
            r.setSeed((new Date()).getTime());
        }
        return r.nextInt();
    }

    public static boolean getRandomBool() {
        if (r == null) {
            r = new Random();
            r.setSeed((new Date()).getTime());
        }
        return r.nextBoolean();
    }

    public enum DATE_RANGE {
        today, tomorrow, two_day_after, yesterday, after, before
    }

    /**
     * Common helper to know the date range from now
     * @param date
     * @return
     */
    public static DATE_RANGE dateToDayRange(Date date) {
        if (date == null) {
            return DATE_RANGE.before;
        }

        // Check if yesterday, today, tomorrow
        Calendar dateFilter = Calendar.getInstance();
        dateFilter.setTime(date);
        Calendar now = Calendar.getInstance();

        int days;
        String title = null;

        // Si on est dans la même année
        if (now.get(Calendar.YEAR) == dateFilter.get(Calendar.YEAR)) {
            // On verifie la différence de jours entre les 2 dates
            days = dateFilter.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
        } else {
            days = dateFilter.get(Calendar.DAY_OF_YEAR) + (now.getActualMaximum(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR));
        }

        if (days < -1) {
            return DATE_RANGE.before;
        }
        if (days == -1) {
            return DATE_RANGE.yesterday;
        }
        if (days == 0) {
            return DATE_RANGE.today;
        }
        if (days == 1) {
            return DATE_RANGE.tomorrow;
        }
        if (days == 2) {
            return DATE_RANGE.two_day_after;
        }

        return DATE_RANGE.after;
    }

    public static int daysPassed(Date date) {
        if (date == null) {
            return 0;
        }

        // Check if yesterday, today, tomorrow
        Calendar dateFilter = Calendar.getInstance();
        dateFilter.setTime(date);
        Calendar now = Calendar.getInstance();

        int days;
        // Si on est dans la même année
        if (now.get(Calendar.YEAR) == dateFilter.get(Calendar.YEAR)) {
            // On verifie la différence de jours entre les 2 dates
            days = dateFilter.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
        } else {
            days = dateFilter.get(Calendar.DAY_OF_YEAR) + (now.getActualMaximum(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR));
        }

        return days;
    }

}
