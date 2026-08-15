import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.*;

public class CS_214_Project_Tester {
    String test = "input_files/test/test.txt";
    String train = "input_files/train/train.txt";
    String pgm = "input_files/test/class0_1.pgm";

    //Zero Args fail
    @Test
    void zeroArgsFail() {
        assertThrows(ArgsException.class, ()->{
            String[] args = {};
            CS_214_Project.main(args);
        });
    }
    // Too many args fail (>3 for pa9)
    @Test
    void tooManyArgsFail() {
        assertThrows(ArgsException.class, ()->{
            String[] args = {"this", "should", "not", "work"};
            CS_214_Project.main(args);
        });
    }
    // 3rd arg must be an integer
    @Test
    void thirdArgNonInt() {
        assertThrows(ArgsException.class, ()->{
            String[] args = {train, test, "Two"};
            CS_214_Project.main(args);
        });
    }
    // Negative values rejected for class
    @Test
    void thirdArgNegative() {
        assertThrows(ArgsException.class, ()->{
            String[] args = {train, test, "-1"};
            CS_214_Project.main(args);
        });
    }
    // First and Second args should be Strings that represent .txt file filenames
    @Test
    void firstArgNonTxtFails() {
        assertThrows(ExtensionException.class, ()->{
            String[] args = {pgm, test, "2"};
            CS_214_Project.main(args);
        });
    }
    @Test
    void secondArgNonTxtFails() {
        assertThrows(ExtensionException.class, ()->{
            String[] args = {train, pgm, "2"};
            CS_214_Project.main(args);
        });
    }

    // Default constructor does nothing
    @Test
    void defaultConstructorDoesNothing() {
        CS_214_Project proj = new CS_214_Project();
        assertTrue(CS_214_Project.expectedExtension.equals(".txt"));
    }

    // Valid args pass
    @Test
    void validArgsPass() throws ExtensionException, FileNotFoundException, InvalidFileException, PgmException, InvalidHistogramException, ArgsException {
        String[] args = {train, test, "2"};
        CS_214_Project.main(args);
        assertEquals(CS_214_Project.numClusters, Integer.parseInt(args[2]));
    }

    // Through main, program creates the HashMap of Histograms by class appropriately
    @Test
    void trainingHistogramsByClassCreatedCorrectly() throws FileNotFoundException, ExtensionException, InvalidFileException, PgmException, InvalidHistogramException, ArgsException {
        String[] args = {"input_files/train/train.txt", "input_files/test/test.txt", "2"};
        CS_214_Project.main(args);
        assertEquals(1, CS_214_Project.trainingHistogramsByClass.keySet().iterator().next());
    }

    // Less than 2 classes in the training set should be an error
    @Test
    void tooFewClassesInTrainingSetFails() {
        assertThrows(PgmException.class, ()->{
            String[] args = {"input_files/test/test.txt", "input_files/train/train.txt", "2"};
            CS_214_Project.main(args);
        });
    }

}