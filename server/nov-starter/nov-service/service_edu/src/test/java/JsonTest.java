import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.edu.entity.EduSubject;

import java.util.List;

public class JsonTest {

    private static String testJson = "[{\"id\":100,\"title\":\"测试科目\",\"parentId\":0,\"children\":[],\"sort\":0,\"createTime\":\"2021-12-17 09:25:05\",\"updateTime\":\"2021-12-17 09:25:11\"},{\"id\":101,\"title\":\"前端\",\"parentId\":0,\"children\":[{\"id\":103,\"title\":\"html\",\"parentId\":101,\"children\":[],\"sort\":0,\"createTime\":\"2021-12-17 09:35:37\",\"updateTime\":\"2021-12-17 09:35:40\"},{\"id\":104,\"title\":\"css\",\"parentId\":101,\"children\":[],\"sort\":0,\"createTime\":\"2021-12-17 09:35:53\",\"updateTime\":\"2021-12-17 09:35:56\"},{\"id\":105,\"title\":\"js\",\"parentId\":101,\"children\":[],\"sort\":0,\"createTime\":\"2021-12-17 09:36:11\",\"updateTime\":\"2021-12-17 09:36:13\"}],\"sort\":0,\"createTime\":\"2021-12-17 09:34:57\",\"updateTime\":\"2021-12-17 09:35:02\"},{\"id\":102,\"title\":\"后端\",\"parentId\":0,\"children\":[{\"id\":106,\"title\":\"java\",\"parentId\":102,\"children\":[],\"sort\":0,\"createTime\":\"2021-12-17 09:36:32\",\"updateTime\":\"2021-12-17 09:36:35\"}],\"sort\":0,\"createTime\":\"2021-12-17 09:35:17\",\"updateTime\":\"2021-12-17 09:35:21\"}]";

    public static void main(String[] args) throws JsonProcessingException, NoSuchFieldException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(testJson);
        List<EduSubject> list = objectMapper.convertValue(
                jsonNode,
                new TypeReference<List<EduSubject>>(){}
        );
       List<EduSubject> target = (List<EduSubject>) TreeUtils.toCollection(list,EduSubject.class);
        System.out.println(target.size());
    }


}
