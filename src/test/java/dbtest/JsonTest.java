package dbtest;

import com.example.mrc.attendencesystem.entity.TranObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.StringReader;

public class JsonTest {
    String json = "{\n" +
            "  \"type\": \"GET_GROUP_MESSAGE\",\n" +
            "  \"isSuccess\": true,\n" +
            "  \"groupMessageArrayList\": [\n" +
            "    {\n" +
            "      \"messageId\": 41,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 40,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"12\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 39,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 38,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 37,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"12\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 36,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 35,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 34,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 33,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 32,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 31,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 30,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 29,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 28,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 27,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 26,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 25,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 24,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 23,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"messageId\": 22,\n" +
            "      \"groupId\": 2,\n" +
            "      \"fromId\": \"1\",\n" +
            "      \"contentType\": 1,\n" +
            "      \"content\": \"123546\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";
    @Test
    public void test(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()  //格式化输出（序列化）
                .setDateFormat("yyyy-MM-dd HH:mm:ss") //日期格式化输出
                .create();
        // Log.d("client", "onStartCommand:msg " +msg);
        JsonReader jsonReader = new JsonReader(new StringReader(json));//其中jsonContext为String类型的Json数据
        jsonReader.setLenient(true);
        TranObject responseObject = gson.fromJson(jsonReader,TranObject.class);
        responseObject.getGroupMessageArrayList().forEach(e->
                System.out.println("contentType:"+e.getContentType()
                +"content:"+e.getContent()+"groupId:"+e.getGroupId()
                        +"fromId:"+e.getFromId()+"messageId:"+e.getMessageId()));
        System.out.println(responseObject.getType());
    }

    @Test
    public void jingwei(){
        double lon1 = (Math.PI / 180) * 114.316561;
        double lon2 = (Math.PI / 180) * 114.316641;
        double lat1 = (Math.PI / 180) * 34.824544;
        double lat2 = (Math.PI / 180) * 34.824433;

        // 地球半径
        double R = 6371;
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1))
                * R;
        //转换成千米（根据自己需求）
        System.out.println(d * 1000);
    }
}
