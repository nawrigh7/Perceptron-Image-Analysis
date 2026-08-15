import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class Comparitor_Tester {

    /*
     * Tests for Comparitor Class
     */
    // Constructor Testing
    //Default shouldn't do much
    @Test
    void verifyDefaultComparitorDoesNothing(){
        Comparitor comp = new Comparitor();
        double defaultSimilarity = 2.0;
        assertEquals(comp.similarity, defaultSimilarity);
    }
    // Given two histograms, should automatically calculate similarity
    @Test
    void verifyComparitorConstructorSuccess() throws InvalidHistogramException{
        File one = new File("input_files/small1.pgm");
        File two = new File("input_files/small2.pgm");
        Histogram histOne = new Histogram(one);
        Histogram histTwo = new Histogram(two);
        double expected = 1.0/6;
        Comparitor comp = new Comparitor(histOne.normalized, histTwo.normalized);
        assertEquals(expected, comp.similarity);
    }

    // Verify expected results from comparing two histograms
    @Test
    void verifyCompareReturnsExpectedResult() {
        double[] histOne = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] histTwo = {5.0, 4.0, 3.0, 2.0, 1.0};
        double expected = 9.0;
        assertEquals(expected, Comparitor.compare(histOne, histTwo));
    }
    // Verify expected results, with previous values flipped
    @Test
    void verifyFlippedCompareReturnsExpectedResults() {
        double[] histOne = {5.0, 4.0, 3.0, 2.0, 1.0};
        double[] histTwo = {1.0, 2.0, 3.0, 4.0, 5.0};
        double expected = 9.0;
        assertEquals(expected, Comparitor.compare(histOne, histTwo));
    }

}
