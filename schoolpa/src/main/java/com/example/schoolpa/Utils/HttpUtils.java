package com.example.schoolpa.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.schoolpa.Base.HttpClass;
import com.example.schoolpa.Bean.RoomBean;
import com.example.schoolpa.Bean.WebBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2016/3/2.
 */
public class HttpUtils {

    private static HttpClass httpClass = new HttpClass();
    private static List<String> dirList;
    private static List<String> fileList = new ArrayList<>();
    private static String downloadUrl;
    private static LinkedList<WebBean> webDetails = new LinkedList<WebBean>();
    private static String path;
    private static LinkedList<WebBean> webAdd = new LinkedList<WebBean>();
    private static WebBean webBean;
    private static String s = "";
    public static boolean hasFind = false;
    public static List<String> couNames;
    private static Handler mHandler;
    private static final int OBTAINSUCCESSED = 0;
    public static Map<String, String> sBuildingMap = new HashMap<String, String>();
    public static Map<String, List<RoomBean>> sRoomMap = new HashMap<>();
    private static String sRoomId;
    private static HttpClass sHttpClassPOST;
    private static String sBuildingId;
    private static HttpClass sHttpClass;
    public static List<String> electricList = new ArrayList<>();

    private static String TAG = "HttpUtils";

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    public static LinkedList<WebBean> getWebDetails() {
        return webDetails;
    }

    public static void downloadDetailInfo(final Context context, String url) {

        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {

            private Iterator<String> it;

            @Override
            public void onSuccess(HttpURLConnection conn) {
                InputStream inputStream;
                try {
                    inputStream = conn.getInputStream();
                    String result = StreamUtils.readFromStream(inputStream);
                    JSONObject jo = new JSONObject(result);
                    String maxId = jo.getString("maxId");
                    String len = jo.getString("len");
                    String NO = jo.getString("NO");
                    String lastId = SharedPreferenceUtils.getString(context,
                            "maxId");

                    String lastLen = SharedPreferenceUtils.getString(context,
                            "len");

                    String lastNo = SharedPreferenceUtils.getString(context,
                            "NO");

                    LogUtils.d(TAG, "找到当前最大的ID");

                    downloadUrl = "http://10.0.2.2:8080/SchoolWebs/webDetails.txt";
                    downloadList(true);

                    LogUtils.d(TAG, "当前Id为" + lastId + "---最大Id为" + maxId);
                    if (lastId.equals(maxId)) {
                        System.out.println("lastId: " + lastId + "maxId: " + maxId);
                        System.out.println("lastLen： " + lastLen + "len: " + len);
                        if (lastLen == len) {
                            return;
                        } else {
                            downloadUrl = "http://10.0.2.2:8080/SchoolWebs/" + maxId +
                                    "/webDetails.txt";
                            downloadDetail(Integer.parseInt(maxId));
                        }
                    } else if (lastId.compareTo(maxId) < 0) {
                        String beginNo, stopNo;
                        stopNo = dirList.get(dirList.size() - 1);
                        System.out.println("StopNo: " + stopNo);
                        it = dirList.iterator();
                        if (lastId.equals("0")) {
                            beginNo = dirList.get(0);
                        } else {
                            int currentpostion = 0;
                            while (it.hasNext()) {
                                String next = it.next();
                                if (next.compareTo(lastId) >= 0) {
                                    break;
                                }
                                currentpostion++;
                            }
                            beginNo = dirList.get(currentpostion);
                        }

                        SharedPreferenceUtils.putString(context, "maxId", maxId);
                        SharedPreferenceUtils.putString(context, "len", len);
                        SharedPreferenceUtils.putString(context, "NO", lastNo);

                        for (int i = Integer.valueOf(beginNo); i <= Integer
                                .valueOf(stopNo); i =
                                     Integer.valueOf(it.next())) {
                            System.out.println("currentId: " + i);
                            LogUtils.d(TAG, i + "");

                            downloadUrl = "http://10.0.2.2:8080/SchoolWebs/" + i +
                                    "/webDetails.txt";
                            downloadList(false);
                            downloadDetail(i);


                            if (i == Integer.valueOf(stopNo)) {
                                break;
                            }
                        }
                        webAdd.addAll(webDetails);
                        webDetails = webAdd;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
            }
        }).startConnenction(url);

    }

    public static void downloadList(final boolean isDir) {

        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                InputStream inputStream;
                try {
                    inputStream = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader(inputStream));
                    if (isDir) {
                        dirList = new ArrayList<>();
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println("dirLine:" + line);
                            dirList.add(line);
                        }

                    } else {
                        fileList = new ArrayList<>();
                        String line;
                        while ((line = br.readLine()) != null) {
                            fileList.add(line);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {

            }
        });
        httpClass.startConnenction(downloadUrl);

    }

    public static void downloadDetail(int dir) {
        for (int i = 0; i < fileList.size(); i++) {
            System.out.println("downloadDetail: " + i);
            path = "http://10.0.2.2:8080/SchoolWebs/" + dir + "/" + fileList.get(i) + "/";

            webBean = new WebBean(
                    add("id", 0),
                    add("title", 1),
                    add("unit", 2),
                    add("content", 3));
            webAdd.addFirst(webBean);
        }
    }

    private static String add(String name, final int which) {
        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    InputStream inputStream = conn.getInputStream();
                    s = StreamUtils.readFromStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {

            }
        }).startConnenction(path + name + ".txt");
        return s;
    }


    public static String stuNum;

    public static boolean findStuNum(final String findNum, String mapPath) {
        stuNum = findNum;
        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    StreamUtils.getString(conn.getInputStream(), new StreamUtils
                            .OnGetStringListener() {
                        @Override
                        public void onGetString(String line) {
                            String stuNum = RegexUtils.RegexGroup(line, "\"(.+?)\"", 1);
                            if (stuNum.contains(findNum)) {
                                hasFind = true;
                                List<String> couNums = RegexUtils.RegexGroups(line, "\\{\"(.+?)" +
                                        "\"\\}", 1);
                                if (couNums == null) {
                                    hasFind = false;
                                    return;
                                } else {
                                    findCouWhere(couNums);
                                }
                            }
                        }

                        @Override
                        public void onGeted() {
                            Message msg = Message.obtain();
                            msg.what = OBTAINSUCCESSED;
                            mHandler.sendMessage(msg);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {

            }
        }).startConnenction(mapPath);
        return hasFind;
    }

    private static void findCouWhere(List<String> couNums) {
        couNames = new ArrayList<>();
        for (int i = 1; i < couNums.size(); i++) {
            final String couNum = couNums.get(i);
            final String find = couNum.substring(0, 6);
            httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
                @Override
                public void onSuccess(HttpURLConnection conn) {
                    try {
                        StreamUtils.getString(conn.getInputStream(), new StreamUtils
                                .OnGetStringListener() {
                            @Override
                            public void onGetString(String line) {
                                if (line.contains(find)) {
                                    String findName = null;
                                    try {
                                        findName = URLEncoder.encode(line.substring(0, line
                                                .indexOf(":")), "utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    String findUrl = UrlUtils.BASEDIRPATHNAME + findName + "/" +
                                            "coudetails2.txt";
                                    couNames.add(getCouName(findUrl, couNum));
                                    return;
                                }
                            }

                            @Override
                            public void onGeted() {

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSetDetails(HttpURLConnection conn) {

                }

                @Override
                public void onFail(IOException e) {

                }
            }).startConnenction(UrlUtils.BASEDIRPATHNAME + "coumap.txt");
        }
    }

    public static String couName;

    public static String getCouName(String findUrl, final String couNum) {
        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    StreamUtils.getString(conn.getInputStream(), new StreamUtils
                            .OnGetStringListener() {

                        @Override
                        public void onGetString(String line) {
                            if (line.contains(couNum)) {
                                JsonUtils jsonUtils = new JsonUtils(line);
                                couName = jsonUtils.getObj("课程名称");
                                String couClass = jsonUtils.getObj("课时教室");
                                if (couClass != null) {
                                    couName += ":" + couClass;
                                    return;
                                }

                            }
                        }

                        @Override
                        public void onGeted() {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {

            }
        }).startConnenction(findUrl);
        return couName;
    }


    public static void findBuildingName() {
        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    StreamUtils.getString(conn.getInputStream(), new StreamUtils
                            .OnGetStringListener() {
                        @Override
                        public void onGetString(String line) {
                            JsonUtils jsonUtils = new JsonUtils(line);
                            String buildingName = jsonUtils.getObj("buildingName");
                            String buildingId = jsonUtils.getObj("buildingId");
                            sBuildingMap.put(buildingName, buildingId);
                            List<RoomBean> roomBeans = new ArrayList<>();
                            sRoomMap.put(buildingName, roomBeans);
                        }

                        @Override
                        public void onGeted() {

                        }
                    });
                } catch (IOException e) {
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {

            }
        }).startConnenction(UrlUtils.BASEELECTRICPATH + "buildingDetails.txt");
    }


    public static void findRoomName() {
        httpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                try {
                    StreamUtils.getString(conn.getInputStream(), new StreamUtils
                            .OnGetStringListener() {
                        @Override
                        public void onGetString(String line) {
                            JsonUtils jsonUtils = new JsonUtils(line);
                            String buildingName = jsonUtils.getObj("buildingName");
                            String roomName = jsonUtils.getObj("roomName");
                            String roomId = jsonUtils.getObj("roomId");
                            List<RoomBean> roomBeans = sRoomMap.get(buildingName);
                            RoomBean roomBean = new RoomBean(roomName,
                                    roomId);
                            roomBeans.add(roomBean);
                            sRoomMap.put(buildingName, roomBeans);
                        }

                        @Override
                        public void onGeted() {
                            Set<String> keySet = sRoomMap.keySet();
                            Iterator<String> iterator = keySet.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                List<RoomBean> roomBeans = sRoomMap.get(key);
                            }
                        }
                    });
                } catch (IOException e) {
                }
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {

            }

            @Override
            public void onFail(IOException e) {

            }
        }).startConnenction(UrlUtils.BASEELECTRICPATH + "roomDetails.txt");
    }

    public static void getElectric(final String buildingName, final String roomName) {
        sBuildingId = sBuildingMap.get(buildingName);
        List<RoomBean> roomBeans = sRoomMap.get(buildingName);
        for (RoomBean roomBean : roomBeans) {
            String rName = roomBean.getRoomName();
            if (rName.equals(roomName)) {
                sRoomId = roomBean.getRoomId();
                break;
            }
        }
        sHttpClassPOST = new HttpClass();
        sHttpClassPOST.isPost = true;

        sHttpClassPOST.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                getData(buildingName, roomName, sRoomId, -6);
            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {
                sHttpClassPOST.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                sHttpClassPOST.setRequestBody("buildingName=&buildingId="
                        + sBuildingId + "&roomName=" + roomName);
            }

            @Override
            public void onFail(IOException e) {

            }
        }).startConnenction("http://szudk.szu.edu.cn:9090/cgcSims/login.do");
    }

    private static void getData(final String buildingName, final String roomName, final String
            roomId, int len) {
        Map<String, String> requestPropety = new HashMap<String, String>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date beginDate = new Date();

        Calendar Date = Calendar.getInstance();
        Date.setTime(beginDate);
        Date.add(Calendar.DAY_OF_MONTH, len);

        final String beginTime = dateFormat.format(Date.getTime());
        final String endTime = dateFormat.format(beginDate.getTime());

        sHttpClass = new HttpClass();
        sHttpClass.isPost = true;

        sHttpClass.setOnVisitingListener(new HttpClass.OnVisitingListener() {
            @Override
            public void onSuccess(HttpURLConnection conn) {
                InputStream inputStream = null;
                try {
                    inputStream = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String string = StreamUtils.getString(inputStream);
                Document document = Jsoup.parse(string);
                Elements elements = document.select("tr");
                for (int j = 6; j < elements.size() - 2; j++) {
                    Element element = elements.get(j);
                    electricList.add(element.text());
                }

            }

            @Override
            public void onSetDetails(HttpURLConnection conn) {
                sHttpClass.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                String str = "beginTime=" + beginTime + "&endTime=" + endTime
                        + "&type=2&client=192.168.84.1&roomId=" + roomId
                        + "&roomName=" + roomName + "&building=";
                sHttpClass.setRequestBody(str);

            }

            @Override
            public void onFail(IOException e) {

            }
        });
        sHttpClass.startConnenction("http://szudk.szu.edu.cn:9090/cgcSims/selectList.do");


    }


    public static void sendPostParams(Map<String, String> map, HttpURLConnection conn) throws IOException {
        OutputStream out = conn.getOutputStream();
        out.write(StringUtils.handlePair(map).getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }
}
