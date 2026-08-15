import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.*;

public class PgmValidator_Tester {

    String path = "input_files/invalidTxts/";
    String valid = "input_files/test/test.txt";

    // Default constructor doesn't do much
    @Test
    void defaultConstructorDoesNothing() {
        PgmValidator pgmVal = new PgmValidator();
        assertFalse(pgmVal.isValid);
    }

    // Regular constructor behaves as expected
    // This also verifies with a correct file that all methods work, and isValid is set correctly
    @Test
    void objectCreationSuccess() throws FileNotFoundException, InvalidFileException, PgmException, ExtensionException {
        PgmValidator pgm = new PgmValidator(valid);
        pgm.validate();
        assertTrue(pgm.isValid);
    }

    // Empty / nonexistant errors
    String emptyFile = path + "containsEmptyFile.txt";
    String nonexistant = path + "containsNonexistant.txt";
    // Txt file containing an empty pgm file should fail
    @Test
    void emptyPgmFails() {
        assertThrows(InvalidFileException.class, ()->{
            PgmValidator pgm = new PgmValidator(emptyFile);
            pgm.validate();
        });
    }
    // Txt file containing nonexistant pgm should fail
    @Test
    void nonexistantPgmFails() {
        assertThrows(InvalidFileException.class, ()->{
            PgmValidator pgm = new PgmValidator(nonexistant);
            pgm.validate();
        });
    }

    // Name errors
    String txtFile = path + "containsTxtFile.txt";
    String tooShort = path + "containsTooShortName.txt";
    String invalidClass = path + "containsInvalidClass.txt";
    String nonClassName = path + "containsNonClassName.txt";
    String negativeName = path + "containsNegativeName.txt";
    // Text file containing another .txt file should fail
    @Test
    void txtTurduckenFails() {
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(txtFile);
            pgm.validate();
        });
    }
    // txt file containing a name that is too short should fail
    @Test
    void tooShortNameFails() {
        assertThrows(ExtensionException.class, ()->{
            PgmValidator pgm = new PgmValidator(tooShort);
            pgm.validate();
        });
    }
    // pgm file with an invalid class indicator should fail
    @Test
    void invalidClassFails() {
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(invalidClass);
            pgm.validate();
        });
    }
    // pgm file with a non-class name should fail
    // failing - throwing InvalidFileException?
    @Test
    void nonClassNameFails() {
        assertThrows(InvalidFileException.class, ()->{ // Should throw PgmException
            PgmValidator pgm = new PgmValidator(nonClassName);
            pgm.validate();
        });
    }
    // non-positive values in the name should be rejected
    @Test
    void negativeNameFails() {
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(negativeName);
            pgm.validate();
        });
    }

    // Content errors
    String nonInts = path + "containsNonInts.txt";
    String invalidFormat = path + "containsInvalidFormat.txt";
    String invalidWidth = path + "containsInvalidWidth.txt";
    String invalidHeight = path + "containsInvalidHeight.txt";
    String invalidPx = path + "containsInvalidPx.txt";
    String invalidDims = path + "containsInvalidDims.txt";
    // File containing non-integer values should fail
    @Test
    void nonIntegerValueFails(){
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(nonInts);
            pgm.validate();
        });
    }
    // File with non "P2" format header should fail
    @Test
    void invalidFormatFails(){
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(invalidFormat);
            pgm.validate();
        });
    }
    // File with invalid width value fails
    @Test
    void invalidWidthFails(){
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(invalidWidth);
            pgm.validate();
        });
    }
    // File with invalid height value fails
    @Test
    void invalidHeightFails(){
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(invalidHeight);
            pgm.validate();
        });
    }
    // File with invalid Px value fails
    @Test
    void invalidPxFails(){
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(invalidPx);
            pgm.validate();
        });
    }
    // File with invalid Dimensions fails
    @Test
    void invalidDimsFail(){
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator(invalidDims);
            pgm.validate();
        });
    }

    // Check contents of the imagesByClass map
    final Integer expectedClass = 0;
    final Integer expectedNumberOfValues = 15;
    @Test
    void imagesByClassMapKeysCreatedProperly() throws FileNotFoundException, InvalidFileException, PgmException, ExtensionException {
        PgmValidator pgm = new PgmValidator(valid);
        pgm.validate();
        System.out.println(pgm.imagesByClass.keySet().iterator().next());
        assertEquals(pgm.imagesByClass.keySet().iterator().next(), expectedClass);
    }
    @Test
    void imagesByClassMapValuesCreatedProperly() throws FileNotFoundException, InvalidFileException, PgmException, ExtensionException {
        PgmValidator image = new PgmValidator(valid);
        image.validate();
        Integer sizeOfList = image.imagesByClass.get(0).size();
        for(File f : image.imagesByClass.get(0)){
            System.out.println(f.getName());
        }
        assertEquals(expectedNumberOfValues, sizeOfList);
    }
}
