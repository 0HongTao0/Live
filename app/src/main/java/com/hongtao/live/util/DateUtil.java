package com.hongtao.live.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created 2020/3/17.
 *
 * @author HongTao
 */
public class DateUtil {
    public static String getDate(long times) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date(times));
    }
}
