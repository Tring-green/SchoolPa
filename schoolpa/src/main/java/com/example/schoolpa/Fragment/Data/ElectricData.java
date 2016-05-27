package com.example.schoolpa.Fragment.Data;

import com.example.schoolpa.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ElectricData {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ElectricItem> ITEMS = new ArrayList<ElectricItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, String> ITEM_MAP = HttpUtils.sBuildingMap;

    private static final int COUNT = HttpUtils.sBuildingMap.size();

    static {
        addItem();

    }

    private static void addItem() {
        Set<String> strings = HttpUtils.sBuildingMap.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String buildingId = ITEM_MAP.get(key);
            ITEMS.add(new ElectricItem(key, buildingId));
        }
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class ElectricItem {
        public final String buildingName;
        public final String buildingId;

        public ElectricItem(String buildingName,
                            String buildingId) {
            this.buildingName = buildingName;
            this.buildingId = buildingId;

        }

        @Override
        public String toString() {
            return "ElectricItem{" +
                    "buildingName='" + buildingName + '\'' +
                    ", buildingId='" + buildingId + '\'' +
                    '}';
        }
    }
}
