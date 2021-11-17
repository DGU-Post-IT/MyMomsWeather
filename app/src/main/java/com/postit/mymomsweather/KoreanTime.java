package com.postit.mymomsweather;

public class KoreanTime {
    static long koreaToday() {
        return (System.currentTimeMillis()+ 32400000)/ 1000 / 60 / 60 / 24;
    }

    static long toKoreaDay(long millis) {
        return (millis+ 32400000)/ 1000 / 60 / 60 / 24;
    }
}
