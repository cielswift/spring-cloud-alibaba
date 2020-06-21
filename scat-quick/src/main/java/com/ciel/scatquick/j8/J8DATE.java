package com.ciel.scatquick.j8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class J8DATE {

    public static void main(String[] args) {

        LocalDateTime of1 = LocalDateTime.of(2019, 12, 12, 8, 30, 5);
        System.out.println(of1);

        LocalDateTime localDateTime = of1.withYear(2050); //修改日期
        System.out.println(localDateTime);

        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek(); //获取星期

        LocalDate now = LocalDate.now();
        System.out.println(now.with(DayOfWeek.MONDAY)); //设置为周一 ,返回

        LocalDate with = now.with(DayOfWeek.SUNDAY); //设置为周日,返回
        System.out.println(with);

        LocalDate first = now.with(TemporalAdjusters.firstDayOfMonth()); //设置为第一日,返回
        LocalDate last = now.with(TemporalAdjusters.lastDayOfMonth()); //设置为最后一日,返回

        LocalDateTime localDateTime1 = localDateTime.plusYears(2); //增加时间
        LocalDateTime localDateTime2 = localDateTime1.minusMonths(24); //减去时间

        int i = localDateTime2.compareTo(localDateTime1); //比较日期
        System.out.println(i);

        boolean after = localDateTime2.isAfter(localDateTime1); //l2 是否在 l1的后面
        boolean before = localDateTime2.isBefore(localDateTime1); //前面
        boolean equal = localDateTime2.isEqual(localDateTime1); //相等

        System.out.println(after + "==" + before + ";;" + equal);

        Duration between = Duration.between(localDateTime2, localDateTime1); //日期比较器,用这个, 返回全部相差
        //时间的区间，用来度量秒和纳秒之间的时间值
        long toDays = between.toDays(); //两个日期相差天数
        System.out.println(toDays);

        Period period = Period.between(now, with); //日期比较器,不推荐, 只返回相差的单位,跳过年和月
        //段时间的区间，用来度量年月日和几天之间的时间值
        int years = period.getDays();
        System.out.println(years);

        DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //格式化
        String format = localDateTime2.format(df2);
        System.out.println(format);

        LocalDateTime parse = LocalDateTime.parse("2019-12-01 12:13:48",df2); //解析
        System.out.println(parse);

        Instant instant = Instant.now(); //时间戳

        System.out.println("aa::"+instant);

        //时间校正器

        LocalDateTime xz = LocalDateTime.now();
        LocalDateTime localDateTime3 = xz.plusMonths(1).withDayOfMonth(1);//调整到下一个月第一天
        System.out.println(localDateTime3);

        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds(); //返回所有时区
        availableZoneIds.forEach(System.err::println);

        ZonedDateTime zonedDateTime = ZonedDateTime.now(Clock.systemUTC()); //世界标准时间
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now();
        ZonedDateTime zonedDateTime3 = ZonedDateTime.now(ZoneId.of("America/Cuiaba"));
        zonedDateTime3.withZoneSameInstant(ZoneId.of("America/Cuiaba")); //修改时区,也改时间
        zonedDateTime3.withZoneSameLocal(ZoneId.of("America/Cuiaba"));//只改时区,不改时间
        System.out.println(zonedDateTime3);


        //Date 转 LocalDateTime
        Date date = new Date();
        LocalDateTime localDateTime11 =
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        System.out.println(localDateTime);


    }
}