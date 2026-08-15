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

public class Perceptron_Tester {

    /*
     * Perceptron testing
     */

    // Default constructor should initialize imageClass to -1
    @Test
    void defaultConstructorImageClassNegative() {
        Perceptron percy = new Perceptron();
        assertEquals(-1, percy.imageClass);
    }
    // Invalid image class should fail
    /*@Test
    void invalidImageClassFails() {
        assertThrows(PgmException.class, ()->{
            PgmValidator pgm = new PgmValidator("input_files/invalidTxts/containsInvalidClass.txt");
            ArrayList<Histogram> histList = new ArrayList<>();
            for(File f : pgm.imageList) {
                histList.add(new Histogram(f));
            }
            Perceptron percy = new Perceptron(histList, 1);
            percy.epoch();
        });
    }*/
    // Set method works appropriately
    @Test
    void setImageClassWorksAsExpected() {
        Perceptron percy = new Perceptron();
        percy.setImageClass(2);
        assertEquals(2, percy.imageClass);
    }
    // Verify Histogram List is populated correctly
    @Test
    void verifyHistListSuccess() throws InvalidFileException, IOException, InvalidHistogramException, PgmException, ExtensionException {
        PgmValidator trainer = new PgmValidator("input_files/train/train.txt");
        ArrayList<Histogram> histList = new ArrayList<>();
        for(File f : trainer.imageList) {
            histList.add(new Histogram(f));
        }
        Perceptron percy = new Perceptron(histList, 1);
        assertEquals(histList.size(), percy.histList.size());
    }
    // Verify printable from toString() matches
    @Test
    void verifyToString() throws InvalidFileException, IOException, InvalidHistogramException, PgmException, ExtensionException {
        PgmValidator trainer = new PgmValidator("input_files/train/train.txt");
        ArrayList<Histogram> histList = new ArrayList<>();
        for(File f : trainer.imageList) {
            histList.add(new Histogram(f));
        }
        Perceptron percy = new Perceptron(histList, 1);
        String expected = "";
        for(int i=0; i<64; i++) {
            expected = expected + String.format("%.6f", percy.weightMap.get(i)) + " ";
        }
        expected = expected + String.format("%.6f", percy.bias);
        assertTrue(expected.trim().equals(percy.toString()));
    }
}
