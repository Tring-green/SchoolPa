package com.example.schoolpa.Fragment.Data;

import com.example.schoolpa.Bean.WebBean;
import com.example.schoolpa.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class OfficialDocumentData {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<OfficialDocumentItem> ITEMS = new ArrayList<OfficialDocumentItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, OfficialDocumentItem> ITEM_MAP = new HashMap<String, OfficialDocumentItem>();

    private static final int COUNT = 25;


    static {
        // Add some sample items.

        for (int i = 0; i <= COUNT ; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(OfficialDocumentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.unit, item);
    }

    public static void addMore() {
        for (int i = 0; i < 5; i++) {
            addItem(createDummyItem(ITEMS.size()+i));
        }
    }

    public static OfficialDocumentItem createDummyItem(int position) {
        System.out.println(position);
        List<WebBean> webDetails = HttpUtils.getWebDetails();
        if (position >= webDetails.size()) {
            return null;
        }
        WebBean webBean = webDetails.get(position);
        System.out.println(position + "--" + webBean.getId());
        return new OfficialDocumentItem(webBean.getUnit(), webBean.getTitle(), webBean.getContent());
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class OfficialDocumentItem {
        public final String unit;
        public final String title;
        public final String content;

        public OfficialDocumentItem(String unit, String title, String content) {
            this.unit = unit;
            this.title = title;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
