package com.web.cloudtube.core.util;

import java.util.Calendar;
import java.util.Date;

public class DateAndTimeUtil {

    public static Date getDateWithExpiry(int expiresInDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, expiresInDays);
        return c.getTime();
    }
}
