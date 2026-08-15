import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.*;

public class FileValidator_Tester {

    String test = "input_files/test/test.txt";
    String train = "input_files/train/train.txt";
    String empty = "input_files/invalidTxts/empty.txt";
    String nonexistant = "input_files/nothere.txt";

    // Default constructor does very little
    @Test
    void defaultConstructorDoesNothing() {
        FileValidator fv = new FileValidator();
        assertTrue(fv.fileName.equals(""));
    }
    // Successful object creation should yield true boolean
    @Test
    void objectCreationSuccess() throws FileNotFoundException, InvalidFileException {
        FileValidator fv = new FileValidator(test);
        assertTrue(fv.isValid);
    }

    // Nonexistant files should fail
    @Test
    void nonexistantFilesFail() {
        assertThrows(InvalidFileException.class, ()->{
            FileValidator fv = new FileValidator(nonexistant);
        });
    }
    // Empty files should fail
    @Test
    void emptyFilesFail() {
        assertThrows(InvalidFileException.class, ()->{
            FileValidator fv = new FileValidator(empty);
        });
    }

}
