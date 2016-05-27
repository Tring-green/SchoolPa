package com.example.schoolpa.Fragment.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ScheduleData {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ScheduleItem> ITEMS = new ArrayList<ScheduleItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final int ROW_COUNT = 7;
    public static final int COLUMN_COUNT = 6;
    public static final int COUNT = ROW_COUNT * COLUMN_COUNT;

    static {
        addItem(createScheduleItem("日期", "", "", "", 0));
        addItem(createScheduleItem("一", "", "", "", 1));
        addItem(createScheduleItem("二", "", "", "", 2));
        addItem(createScheduleItem("三", "", "", "", 3));
        addItem(createScheduleItem("四", "", "", "", 4));
        addItem(createScheduleItem("五", "", "", "", 5));
//        addItem(createScheduleItem("六", "", "", "", 6));
//        addItem(createScheduleItem("日", "", "", "", 7));
        for (int i = COLUMN_COUNT; i <= COUNT; i++) {
            addItem(createScheduleItem("", "", "", "", i));
        }
        for (int i = 1; i <= 6; i++) {
            String name = "" + (i * 2 - 1) + "、" + (i * 2) + "节";
            ITEMS.remove(i * COLUMN_COUNT);
            ITEMS.add(i * COLUMN_COUNT, createScheduleItem(name, "", "", "", i));
        }
    }


    private static void addItem(ScheduleItem item) {
        ITEMS.add(item);
    }

    private static ScheduleItem createScheduleItem(String couName, String week, String date,
                                                   String classpos, int pos) {
        return new ScheduleItem(couName, week, date, classpos, pos);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class ScheduleItem {
        public final String couName;
        public final String week;
        public final String date;
        public final String classpos;
        public final int pos;

        public ScheduleItem(String couName, String week, String date, String classpos, int pos) {
            this.couName = couName;
            this.week = week;
            this.date = date;
            this.classpos = classpos;
            this.pos = pos;
        }

        @Override
        public String toString() {
            return "ScheduleItem{" +
                    "couName='" + couName + '\'' +
                    ", week='" + week + '\'' +
                    ", date='" + date + '\'' +
                    ", classpos='" + classpos + '\'' +
                    ", pos=" + pos +
                    '}';
        }
    }
}
