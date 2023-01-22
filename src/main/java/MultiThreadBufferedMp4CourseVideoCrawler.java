import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/*********************************************************
   有使用IO缓冲和多线程的mp4课程视频网络爬虫（备注：1. 自底向上；2. 数据已脱敏。）
              author: zhouhuajian
 *********************************************************/

public class MultiThreadBufferedMp4CourseVideoCrawler {
    private static Map<String, String> cookies = new HashMap<>();

    private static void downloadVideo(String videoUrl) throws IOException {
        Connection.Response response = Jsoup.connect(videoUrl).timeout(100000000).maxBodySize(500000000).cookies(cookies).ignoreContentType(true).execute();
        FileOutputStream fos = new FileOutputStream(new java.io.File("..." + System.currentTimeMillis() + ".mp4"));
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(response.bodyAsBytes());
        bos.close();
    }

    private static void downloadVideoFromVideoPlayPage(String videoPlayPageUrl) throws IOException {
        // 视频播放页
        String src = Jsoup.connect(videoPlayPageUrl).ignoreContentType(true).cookies(cookies).get().selectXpath("//video/source").attr("src").toString();
        downloadVideo("https://..." + src.substring(2));
    }

    private static void downloadVideosFromCourseDataApi(String courseDataApi) throws IOException {
        String text = Jsoup.connect(courseDataApi).ignoreContentType(true).cookies(cookies).get().body().text();
        JSONObject jsonObject = JSONObject.parseObject(text);
        String value1 = jsonObject.getString("key1");
        JSONArray jsonArray = jsonObject.getJSONObject("key2").getJSONArray("key3");
        for (int i = 0; i < jsonArray.size(); i++) {
            String value2 = jsonArray.getJSONObject(i).getJSONObject("key4").getString("key5");
            downloadVideoFromVideoPlayPage("https://..." + value1 + "..." + value2 + "...");
        }
    }

    private static void downloadVideosFromCourseDetailPage(String courseDetailPageUrl) throws IOException {
        String text = Jsoup.connect(courseDetailPageUrl).ignoreContentType(true).cookies(cookies).get().toString();
        // 根据indexOf和substring，获取页面中某个特定区域的数据
        int index1 = text.indexOf("...");
        int index2 = text.indexOf("...", index1);
        String id = text.substring(index1 + "...".length(), index2);
        downloadVideosFromCourseDataApi("https://..." + id + "...");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        // 课程详情页面链接 支持多个课程详情页链接
        String[] courseDetailPageUrls = {
            "https://...",
            "https://...",
            "https://...",
        };

        cookies.put("k1", "v1");
        cookies.put("k2", "v2");

        long startTime = System.currentTimeMillis();

        List<String> courseDetailPageUrlLinkedList = Collections.synchronizedList(new LinkedList<>(Arrays.asList(courseDetailPageUrls)));

        Runnable runnable = () -> {
            try {
                while (true) {
                    String courseDetailPageUrl = courseDetailPageUrlLinkedList.remove(0);
                    downloadVideosFromCourseDetailPage(courseDetailPageUrl);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("子线程正常退出");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread1.join();
        thread1.join();

        long endTime = System.currentTimeMillis();
        System.out.printf("耗时 %f 秒", (endTime - startTime) / 1000.0);
    }

}
