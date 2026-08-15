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

public class Cluster_Tester {

    File smallPgm = new File("input_files/small.pgm").getAbsoluteFile();
    File small1Pgm = new File("input_files/small1.pgm").getAbsoluteFile();

/*
     * Cluster Testing
     */
    // Constructor testing
    @Test
    void verifyClusterDefaultDoesNothing() {
        Cluster c = new Cluster();
        assertEquals(0, c.numInCluster);
    }
    @Test
    void verifyOneArgConstructorIncreasesCount() throws InvalidHistogramException {
        Histogram h = new Histogram(smallPgm);
        Cluster c = new Cluster(h);
        assertEquals(1, c.numInCluster);
    }
    //Adding to zero args constructor should result in numInCluster = 1
    @Test
    void verifyAddingToEmptyCluster() throws InvalidHistogramException {
        Histogram h = new Histogram(smallPgm);
        Cluster c = new Cluster();
        c.addToCluster(h);
        assertEquals(1, c.numInCluster);
    }
    //Verify that the average Histogram in cluster is what we expect
    //Steps: 
    //  //  Create Histogram object
    //  //  Create Cluster, add Histogram to it
    //  //  Create Comparitor using Histogram.normalized and Cluster.normalized
    //  //  Verify they are the same (Comparitor.similarity = 1)
    @Test
    void verifyAvgHistogramInCluster() throws InvalidHistogramException {
        Histogram h = new Histogram(smallPgm);
        Cluster c = new Cluster(h);
        Comparitor comp = new Comparitor(h.normalized, c.clusterHistogram);
        assertEquals(1, comp.similarity);
    }
    // Verify that removing a Histogram from a cluster returns a histogram object
    @Test
    void verifyRemovingHistogramReturnsCorrect() throws InvalidHistogramException, ClusterException {
        Histogram h = new Histogram(smallPgm);
        String expected = "small.pgm";
        Cluster c = new Cluster(h);
        Histogram result = c.removeFromCluster(expected);
        assertTrue(result.fileName.equals(expected));
    }
    // Verify removing a Histogram from a Cluster when the Cluster does not contain that Histogram fails
    @Test
    void verifyRemovingHistThatDoesNotExistFails() throws InvalidHistogramException, ClusterException {
        assertThrows(ClusterException.class, ()->{
            Histogram h = new Histogram(smallPgm);
            String nonexistToRemove = "example1.pgm";
            Cluster c = new Cluster(h);
            c.removeFromCluster(nonexistToRemove);
        });
    }
    // Verify that removing a Histogram from a cluster also removes the correct string from the Cluster's namesList
    @Test
    void verifyRemovingHistRemovesName() throws InvalidHistogramException, ClusterException {
        Histogram small = new Histogram(smallPgm);
        Histogram small1 = new Histogram(small1Pgm);
        String shouldNotExistInCluster = "small.pgm";
        Cluster c = new Cluster();
        c.addToCluster(small);
        c.addToCluster(small1);
        c.removeFromCluster(small.fileName);
        assertFalse(c.namesList.contains(shouldNotExistInCluster));
    }

}
