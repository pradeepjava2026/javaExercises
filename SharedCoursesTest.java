package karatExercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SharedCoursesTest {

    public static void main(String[] args) {

        // ---------------- TEST CASE 1 ----------------
        List<List<String>> enrollments1 = Arrays.asList(
                Arrays.asList("58", "Linear Algebra"),
                Arrays.asList("94", "Art History"),
                Arrays.asList("94", "Operating Systems"),
                Arrays.asList("17", "Software Design"),
                Arrays.asList("58", "Mechanics"),
                Arrays.asList("58", "Economics"),
                Arrays.asList("17", "Linear Algebra"),
                Arrays.asList("17", "Political Science"),
                Arrays.asList("94", "Economics"),
                Arrays.asList("25", "Economics"),
                Arrays.asList("58", "Software Design")
        );

        Map<String, List<String>> result1 = SharedCourses.findPairs(enrollments1);

        assert compare(result1.get("58,17"), Arrays.asList("Software Design", "Linear Algebra"));
        assert compare(result1.get("58,94"), Arrays.asList("Economics"));
        assert compare(result1.get("58,25"), Arrays.asList("Economics"));
        assert compare(result1.get("94,25"), Arrays.asList("Economics"));
        assert compare(result1.get("17,94"), Collections.emptyList());
        assert compare(result1.get("17,25"), Collections.emptyList());

        System.out.println("Test Case 1 Passed ✅");

        // ---------------- TEST CASE 2 ----------------
        List<List<String>> enrollments2 = Arrays.asList(
                Arrays.asList("0", "Advanced Mechanics"),
                Arrays.asList("0", "Art History"),
                Arrays.asList("1", "Course 1"),
                Arrays.asList("1", "Course 2"),
                Arrays.asList("2", "Computer Architecture"),
                Arrays.asList("3", "Course 1"),
                Arrays.asList("3", "Course 2"),
                Arrays.asList("4", "Algorithms")
        );

        Map<String, List<String>> result2 = SharedCourses.findPairs(enrollments2);

        assert compare(result2.get("1,0"), Collections.emptyList());
        assert compare(result2.get("2,0"), Collections.emptyList());
        assert compare(result2.get("2,1"), Collections.emptyList());
        assert compare(result2.get("3,0"), Collections.emptyList());
        assert compare(result2.get("3,1"), Arrays.asList("Course 1", "Course 2"));
        assert compare(result2.get("3,2"), Collections.emptyList());
        assert compare(result2.get("4,0"), Collections.emptyList());
        assert compare(result2.get("4,1"), Collections.emptyList());
        assert compare(result2.get("4,2"), Collections.emptyList());
        assert compare(result2.get("4,3"), Collections.emptyList());

        System.out.println("Test Case 2 Passed ✅");

        // ---------------- TEST CASE 3 ----------------
        List<List<String>> enrollments3 = Arrays.asList(
                Arrays.asList("23", "Software Design"),
                Arrays.asList("3", "Advanced Mechanics"),
                Arrays.asList("2", "Art History"),
                Arrays.asList("33", "Another")
        );

        Map<String, List<String>> result3 = SharedCourses.findPairs(enrollments3);

        assert compare(result3.get("23,3"), Collections.emptyList());
        assert compare(result3.get("23,2"), Collections.emptyList());
        assert compare(result3.get("23,33"), Collections.emptyList());
        assert compare(result3.get("3,2"), Collections.emptyList());
        assert compare(result3.get("3,33"), Collections.emptyList());
        assert compare(result3.get("2,33"), Collections.emptyList());

        System.out.println("Test Case 3 Passed ✅");

        // ---------------- EDGE CASE ----------------
        List<List<String>> enrollmentsEmpty = new ArrayList<>();

        Map<String, List<String>> resultEmpty = SharedCourses.findPairs(enrollmentsEmpty);

        assert resultEmpty.isEmpty();

        System.out.println("Edge Case Passed ✅");
    }

    // Helper method to compare lists ignoring order
    private static boolean compare(List<String> actual, List<String> expected) {

        if (actual == null || expected == null) {
            return actual == expected;
        }

        // Convert to sets to ignore order
        return new HashSet<>(actual).equals(new HashSet<>(expected));
    }
}
