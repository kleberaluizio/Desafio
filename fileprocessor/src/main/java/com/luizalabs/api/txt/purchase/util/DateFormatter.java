package com.luizalabs.api.txt.purchase.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DataFormatter {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static LocalDate format(String stringDate) throws ParseException {
        Date date = simpleDateFormat.parse(stringDate);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
