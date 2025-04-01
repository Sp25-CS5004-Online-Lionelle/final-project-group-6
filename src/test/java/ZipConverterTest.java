import model.ZipConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZipConverterTest {
    
    @Test
    void testValidZipCode() {
        // Known zip code for Seattle, WA
        String result = ZipConverter.convertZipToState("98101");
        assertEquals("WA", result);
    }

    @Test
    void testAnotherValidZipCode() {
        // Known zip code for New York, NY
        String result = ZipConverter.convertZipToState("10001");
        assertEquals("NY", result);
    }

    @Test
    void testInvalidZipCodeTooShort() {
        String result = ZipConverter.convertZipToState("123");
        assertNull(result);
    }

    @Test
    void testInvalidZipCodeTooLong() {
        String result = ZipConverter.convertZipToState("123456");
        assertNull(result);
    }

    @Test
    void testInvalidZipCodeLetters() {
        String result = ZipConverter.convertZipToState("abcde");
        assertNull(result);
    }

    @Test
    void testEmptyZipCode() {
        String result = ZipConverter.convertZipToState("");
        assertNull(result);
    }

    @Test
    void testNullZipCode() {
        String result = ZipConverter.convertZipToState(null);
        assertNull(result);
    }
}
