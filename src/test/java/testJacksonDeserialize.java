import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class testJacksonDeserialize {

    @JsonIgnoreProperties(ignoreUnknown = true) // this annotation allows jackson to ignore unknown fields
    record TestRecord(String id, String name){};

    ObjectMapper objectMapper = new ObjectMapper();

    @Test    
    public void testDeserialize() {
        String original = """
        {
        "id":"0.0.0.0",
        "ignore": "this",
        "name":"some Name",
        "other": "ignore"
        }
        """.replace("\n", "");

        try {
            TestRecord actual = objectMapper.readValue(original, TestRecord.class);
            System.out.println(actual);
            TestRecord expected = new TestRecord("0.0.0.0", "some Name");

            assertEquals(expected, actual); 
        } catch (Exception e) {
            System.out.println("error mapping json to object");
            System.out.println(e);
        }
    }

    @Test    
    public void testDeserializeList() {
        String original = """
        [{
        "id":"0.0.0.0",
        "ignore": "this",
        "name":"some Name",
        "other": "0"
        },
        {
        "id":"0.0.0.0",
        "ignore": "this",
        "name":"Name2",
        "other": "ignore"
        },
        {
        "id":"1290320",
        "ignore": "this",
        "name":"some Name",
        "other": "ignore"
        }]
        """.replace("\n", "");

        List<TestRecord> actual = null;
        try {
            actual = objectMapper.readValue(original, new TypeReference<List<TestRecord>>() {});
        } catch (Exception e) {
            System.out.println("error mapping json to object");
            System.out.println(e);
        }
        List<TestRecord> expected = List.of(
            new TestRecord("0.0.0.0", "some Name"),
            new TestRecord("0.0.0.0", "Name2"),
            new TestRecord("1290320", "some Name")
        );

        assertEquals(expected, actual); 
    }
}