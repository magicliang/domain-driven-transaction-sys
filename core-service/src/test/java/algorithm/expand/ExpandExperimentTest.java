package algorithm.expand;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ExpandExperimentTest {

    @Test
    public void testExpandToCombinationsWithEmptyInput() {
        List<List<String>> input = new ArrayList<>();
        List<List<String>> result = ExpandExperiment.expandToCombinations(input);
        System.out.println("Empty input combinations: " + result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).size());
    }

    @Test
    public void testExpandToCombinationsWithSingleList() {
        List<List<String>> input = new ArrayList<>();
        List<String> singleList = new ArrayList<>();
        singleList.add("A");
        singleList.add("B");
        input.add(singleList);

        List<List<String>> result = ExpandExperiment.expandToCombinations(input);
        System.out.println("Single list combinations: " + result);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).get(0));
        assertEquals("B", result.get(1).get(0));
    }

    @Test
    public void testExpandToCombinationsWithMultipleLists() {
        List<List<String>> input = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list1.add("A");
        list1.add("B");
        input.add(list1);

        List<String> list2 = new ArrayList<>();
        list2.add("X");
        list2.add("Y");
        input.add(list2);

        List<List<String>> result = ExpandExperiment.expandToCombinations(input);
        System.out.println("Multiple lists combinations: " + result);
        assertEquals(4, result.size());
        assertEquals("A", result.get(0).get(0));
        assertEquals("X", result.get(0).get(1));
        assertEquals("B", result.get(1).get(0));
        assertEquals("X", result.get(1).get(1));
        assertEquals("A", result.get(2).get(0));
        assertEquals("Y", result.get(2).get(1));
        assertEquals("B", result.get(3).get(0));
        assertEquals("Y", result.get(3).get(1));
    }
}