package com.postit.mymomsweather.util;

public class KoreanTime {
    public static long koreaToday() {
        return (System.currentTimeMillis()+ 32400000)/ 1000 / 60 / 60 / 24;
    }

    public static long toKoreaDay(long millis) {
        return (millis+ 32400000)/ 1000 / 60 / 60 / 24;
    }
}
