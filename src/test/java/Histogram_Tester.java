import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Histogram_Tester {

    File smallPgm = new File("input_files/small.pgm").getAbsoluteFile();
    File small1Pgm = new File("input_files/small1.pgm").getAbsoluteFile();
    Integer[] expectedSmallArr = {120, 255, 82, 120, 240, 83, 75, 80, 115, 209, 155, 123, 240, 89, 76, 24, 15, 7, 90, 64, 45, 32, 27, 70, 142};
    ArrayList<Integer> expectedSmallList = new ArrayList<>();

    

    File validFileP9 = new File("input_files/test/class0_1.pgm");
    
    
    public ArrayList<String> createList(File inputFile) throws FileNotFoundException {
        Scanner scan = new Scanner(inputFile);
        ArrayList<String> listToReturn = new ArrayList<>();
        while(scan.hasNextLine()) {
            listToReturn.add(scan.nextLine().trim());
        }
        if(listToReturn.get(listToReturn.size()-1).isEmpty()) {
            listToReturn.remove(listToReturn.size()-1);
        }
        return listToReturn;
    }

/*
     * Histogram Testing
     */
    // Default Histogram constructor should fail
    @Test
    void verifyDefaultHistFails() {
        assertThrows(InvalidHistogramException.class, ()->{
            Histogram hist = new Histogram();
        });
    }
    // Constructor with valid file input succeeds
    @Test
    void verifyConstructorSuccess() throws InvalidHistogramException{
        File small = new File("input_files/small1.pgm");
        Histogram hist = new Histogram(small);
        double sum = 6;
        assertEquals(sum, hist.sum);
    }

    //Histogram created successfully
    @Test
    void verifyHistogramCreation() throws InvalidHistogramException{
        ArrayList<Integer> expected = Histogram.createListFromFile(smallPgm);
        for(int i=0; i<expectedSmallArr.length; i++) {
            assertEquals(expectedSmallArr[i], expected.get(i));
        }
    }
    //Nonexistant files fail
    @Test
    void verifyNoFileFailsListCreation() throws InvalidHistogramException{
        assertThrows(InvalidHistogramException.class, ()->{
            File f = new File("nonextistant.pgm");
            Histogram.createListFromFile(f);
        });
    }
    //RangeCounter method success
    @Test
    void verifyRangeCounterSuccess() {
        int[] histogram = new int[64];
        ArrayList<Integer> test = new ArrayList<>();
        for(Integer i : expectedSmallArr) {
            test.add(i);
            histogram[i/4] += 1;
        }
        int[] result = Histogram.rangeCounter(test);
        for(int i=0; i<histogram.length; i++) {
            assertEquals(histogram[i], result[i]);
        }
        
    }
    //ConvertToDouble success
    @Test
    void verifyConvertToDoubleSuccess() {
        for(Integer i : expectedSmallArr) {
            expectedSmallList.add(i);
        }
        int[] hist = Histogram.rangeCounter(expectedSmallList);

        double[] expected = new double[64];
        for(int i=0; i<expected.length; i++) {
            expected[i] = (double)hist[i];
        }
        
        double[] result = Histogram.convertToDouble(hist);
        for(int i=0; i<expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    //Verify sum is calculated as expected
    @Test
    void verifyCalculateSums() {
        double[] start = {1.0, 2.0, 3.0, 4.0};
        Double expected = 10.0;
        assertEquals(expected, Histogram.getSum(start));
    }
    //Verify Normalization process goes as expected
    @Test
    void verifyNormalized() {
        double[] start = {1.0, 2.0, 3.0, 4.0};
        double[] expected = new double[start.length];
        Double sum = 10.0;
        for(int i=0; i<start.length; i++) {
            expected[i] = start[i] / sum;
        }
        double[] result = Histogram.normalize(start, sum);
        for(int i=0; i<start.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    // Verify HashMap is expected value
    @Test
    void verifyHashMap() throws InvalidHistogramException {
        Histogram hist = new Histogram(validFileP9);
        for(int i=0; i<hist.normalized.length; i++) {
            assertEquals(hist.normalized[i], hist.histMap.get(i));
        }
    }

    //Verify toString returns expected String
    @Test
    void verifyToString() throws InvalidHistogramException {
        double eV = 1.0/6.0;
        String expected = eV + " 0.0 0.0 0.0 0.0 " + eV + " 0.0 0.0 0.0 0.0 " + eV + " 0.0 0.0 0.0 0.0 " + eV + " 0.0 0.0 0.0 0.0 " + eV + " 0.0 0.0 0.0 0.0 " + eV;
        for(int i=0; i<38; i++) {
            expected = expected + " 0.0";
        }
        Histogram hist = new Histogram(small1Pgm);
        System.out.println(expected);
        System.out.println(hist);
        assertTrue(hist.toString().equals(expected));
    }

    // OLD Files and declarations from previous iterations

    /*String validFileName = "input_files/correctfiles-pa6.txt";
    File validFile = new File(validFileName).getAbsoluteFile();

    String nonPgmFileName = "input_files/nonpgmerror.txt";
    File nonPgmFile = new File(nonPgmFileName).getAbsoluteFile();

    String nonexistantName = "nonexistant.txt";
    File nonexistant = new File(nonexistantName).getAbsoluteFile();

    String emptyFileName = "input_files/empty.txt";
    File emptyFile = new File(emptyFileName).getAbsoluteFile();

    String txtWithEmptyPgm = "input_files/containsemptyfile.txt";
    File containsEmpty = new File(txtWithEmptyPgm).getAbsoluteFile();

    String txtWithNonexistantPgm = "input_files/containsnonexistant.txt";
    File containsNonexistant = new File(txtWithNonexistantPgm).getAbsoluteFile();

    String txtWithInvalidFormat = "input_files/containsInvalidFormat.txt";
    File containsInvalidFormat = new File(txtWithInvalidFormat).getAbsoluteFile();

    String txtWithInvalidWidth = "input_files/containsInvalidWidth.txt";
    File containsInvalidWidth = new File(txtWithInvalidWidth).getAbsoluteFile();

    String txtWithInvalidHeight = "input_files/containsInvalidHeight.txt";
    File containsInvalidHeight = new File(txtWithInvalidHeight).getAbsoluteFile();

    String txtWithInvalidPx = "input_files/containsInvalidPx.txt";
    File containsInvalidPx = new File(txtWithInvalidPx).getAbsoluteFile();

    String txtWithInvalidRange = "input_files/containsInvalidValues.txt";
    File containsInvalidRange = new File(txtWithInvalidRange).getAbsoluteFile();

    String txtWithNonInts = "input_files/containsNonInts.txt";
    File containsNonInts = new File(txtWithNonInts).getAbsoluteFile();

    String txtWithInvalidDims = "input_files/containsInvalidDims.txt";
    File containsBadDims = new File(txtWithInvalidDims).getAbsoluteFile();

    String txtContainsTxt = "input_files/containsTxtFile.txt";
    File containsTxt = new File(txtContainsTxt).getAbsoluteFile();

    String tooShort = "input_files/containsTooShortName.txt";
    File shortName = new File(tooShort).getAbsoluteFile();*/

    /*String validP7 = "input_files/correctfiles.txt";
    File validFileP7 = new File(validP7);
    File class1_1 = new File("input_files/class1_1.pgm");*/

}
