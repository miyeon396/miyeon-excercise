package etc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperExample {
    public static void main(String[] args) {
        String str = "projects/momovn-cloud/locations/asia-southeast1-c";

        String[] arr = str.split("/");

        String[] arr2 = arr[arr.length-1].split("-");
        System.out.println(arr[arr.length-1].substring(0, arr[arr.length-1].lastIndexOf("-")));


        String test222 = "projects/opsnow-gcp-dev-pjt/locations/us-central1/functions/function-1";

        Map<String, String> testMap = new HashMap<>();
        testMap.put("a", "aaa");
        testMap.put("b", "bbb");
        System.out.println(testMap.get("ccc"));

        //zone.substring(0, zone.lastIndexOf("-"));
    }
//    ObjectMapper mapper = new ObjectMapper();
//
//    String messageStr = mapper.writeValueAsString(message);
//    UsageCommonModel usageCommonModel = mapper.readValue(messageStr, UsageCommonModel.class);
//
//    List<UsageBigQueryTableModel> rsrcList = mapper.readValue(mapper.writeValueAsString(message.getData()), new TypeReference<List<UsageBigQueryTableModel>>() {});


//    ////  "creationTimestamp": "2019-08-11T18:08:45.572-07:00",
////    String -> LocalDateTime -> format yyyy-MM-dd HH:mm:ss
//    String creationTimeStamp = "2019-08-11T18:08:45.572-07:00";
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone();
//    LocalDateTime ldT =

            //String str = "2021-11-05 13:47:13.248";
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    //LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
    //System.out.println(dateTime);

}
