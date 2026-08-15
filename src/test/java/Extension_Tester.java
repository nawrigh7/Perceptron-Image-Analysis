import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;


public class Extension_Tester {

    String expectedExtension = ".txt";
    String textFile = "test.txt";
    String fmvFile = "test.fmv";
    String tooShort = "sho";
    ExtensionChecker validExt = new ExtensionChecker(expectedExtension);

    // Verify Object Creation Success
    @Test
    void objectCreationSuccess() {
        assertTrue(validExt.expectedExtension.equals(expectedExtension));
    }
    // Verify checkExtension returns correctly
    @Test
    void correctExtensionReturnsTrue() throws ExtensionException {
        assertTrue(validExt.checkExtension(textFile));
    }
    @Test
    void incorrectExtensionReturnsFalse() throws ExtensionException {
        assertFalse(validExt.checkExtension(fmvFile));
    }

    // A file name that is too short should throw an exception
    @Test
    void stringTooSmallThrows() throws ExtensionException {
        assertThrows(ExtensionException.class, ()->{
            validExt.checkExtension(tooShort);
        });
    }
}
