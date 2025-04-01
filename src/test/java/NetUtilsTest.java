import model.NetUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NetUtilsTest {
    
    @Test
    void testStateAndZipReturnSameData() {
        // Using Washington state (WA) and a Seattle zip code
        String stateResponse = NetUtils.getParksByState("WA");
        String zipResponse = NetUtils.getParksByZip("98101");
        
        // Both responses should be equal since they're querying the same state
        assertEquals(stateResponse, zipResponse);
    }

    @Test
    void testInvalidStateCode() {
        String response = NetUtils.getParksByState("XX")
            .replaceAll("\\s+", "");
        
        String expectedEmptyResponse = "{\"total\":\"0\",\"limit\":\"20\",\"start\":\"0\",\"data\":[]}"
            .replaceAll("\\s+", "");
        
        assertEquals(expectedEmptyResponse, response);
    }
    

}
