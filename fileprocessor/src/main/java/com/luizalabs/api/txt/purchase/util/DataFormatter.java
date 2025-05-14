package com.luizalabs.api.txt.purchase.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DataFormatter {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static LocalDate format(String stringDate) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
