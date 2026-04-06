package karatExercises;

import java.util.*;

public class SharedCourses {
/*
 * A student can have multiple courses
   We must compare every pair of students
   If no common courses → return empty list
   Order of students in key doesn't matter (but keep consistent)
   STEP 1: Build student → courses map
   STEP 2: Prepare result map 
   STEP 3: Generate all unique student pairs
   STEP 4: Find intersection
   STEP 5: Store result
 */
	
    public static Map<String, List<String>> findPairs(List<List<String>> enrollments) {

        // Map to store student -> set of courses
        // Using Set to avoid duplicate courses and for fast lookup
        Map<String, Set<String>> studentCourses = new HashMap<>();

        // ---------------- STEP 1: Build student → courses map ----------------
        for (List<String> entry : enrollments) {

            // Extract student ID (index 0)
            String student = entry.get(0);

            // Extract course name (index 1)
            String course = entry.get(1);

            // If student not present in map, initialize empty set
            studentCourses.putIfAbsent(student, new HashSet<>());

            // Add course to the student's set
            studentCourses.get(student).add(course);
        }

        // ---------------- STEP 2: Prepare result map ----------------
        // This will store final answer: "student1,student2" -> common courses
        Map<String, List<String>> result = new HashMap<>();

        // Convert student IDs to a list for easy pair generation
        List<String> students = new ArrayList<>(studentCourses.keySet());

        // ---------------- STEP 3: Generate all unique student pairs ----------------
        for (int i = 0; i < students.size(); i++) {

            for (int j = i + 1; j < students.size(); j++) {

                // Get first student
                String s1 = students.get(i);

                // Get second student
                String s2 = students.get(j);

                // Get courses of first student
                Set<String> courses1 = studentCourses.get(s1);

                // Get courses of second student
                Set<String> courses2 = studentCourses.get(s2);

                // List to store common courses
                List<String> commonCourses = new ArrayList<>();

                // ---------------- STEP 4: Find intersection ----------------
                // Iterate through courses of first student
                for (String course : courses1) {

                    // Check if second student also has this course
                    if (courses2.contains(course)) {

                        // If yes, add to common list
                        commonCourses.add(course);
                    }
                }

                // ---------------- STEP 5: Store result ----------------
                // Create key in format "student1,student2"
                String key = s1 + "," + s2;

                // Put pair and their common courses into result map
                result.put(key, commonCourses);
            }
        }

        // ---------------- STEP 6: Return final result ----------------
        return result;
    }
}




