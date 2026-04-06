package karatExercises;

import java.util.*;

public class StoryBook {

    public static int stories(List<Integer> endings, List<List<Integer>> choices, int option) {

        // Step 1: Convert endings to set
        Set<Integer> endingSet = new HashSet<>(endings);

        // Step 2: Convert choices to map
        Map<Integer, int[]> choiceMap = new HashMap<>();
        for (List<Integer> c : choices) {
            choiceMap.put(c.get(0), new int[]{c.get(1), c.get(2)});
        }

        // Step 3: Track visited pages (for loop detection)
        Set<Integer> visited = new HashSet<>();

        int page = 1;

        while (true) {

            // If ending page → return
            if (endingSet.contains(page)) {
                return page;
            }

            // If already visited → loop
            if (visited.contains(page)) {
                return -1;
            }

            visited.add(page);

            // If choice exists
            if (choiceMap.containsKey(page)) {
                int[] opts = choiceMap.get(page);
                page = (option == 1) ? opts[0] : opts[1];
            } else {
                // Move to next page
                page++;
            }
        }
    }

    public static void main(String[] args) {

        List<Integer> endings1 = Arrays.asList(6, 15, 21, 30);

        List<List<Integer>> choices1_1 = Arrays.asList(
                Arrays.asList(3, 7, 8),
                Arrays.asList(9, 4, 2)
        );

        List<List<Integer>> choices1_2 = Arrays.asList(
                Arrays.asList(3, 14, 2)
        );

        List<List<Integer>> choices1_3 = Arrays.asList(
                Arrays.asList(5, 11, 28),
                Arrays.asList(9, 19, 29),
                Arrays.asList(14, 16, 20),
                Arrays.asList(18, 7, 22),
                Arrays.asList(25, 6, 30)
        );

        List<List<Integer>> choices1_4 = Arrays.asList(
                Arrays.asList(2, 10, 15),
                Arrays.asList(3, 4, 10),
                Arrays.asList(4, 3, 15),
                Arrays.asList(10, 3, 15)
        );

        List<Integer> endings2 = Arrays.asList(11);
        List<List<Integer>> choices2_1 = Arrays.asList(
                Arrays.asList(2, 3, 4),
                Arrays.asList(5, 10, 2)
        );
        List<List<Integer>> choices2_2 = new ArrayList<>();

        List<Integer> endings3 = Arrays.asList(4, 11);
        List<List<Integer>> choices3_1 = Arrays.asList(
                Arrays.asList(10, 6, 8)
        );

        List<Integer> endings4 = Arrays.asList(20);
        List<List<Integer>> choices4_1 = Arrays.asList(
                Arrays.asList(2, 6, 3),
                Arrays.asList(3, 1, 4),
                Arrays.asList(4, 10, 5),
                Arrays.asList(6, 3, 7)
        );

        // ✅ Assert Test Cases

        assert stories(endings1, choices1_1, 1) == 6;
        assert stories(endings1, choices1_1, 2) == -1;

        assert stories(endings1, choices1_2, 1) == 15;
        assert stories(endings1, choices1_2, 2) == -1;

        assert stories(endings1, choices1_3, 1) == 21;
        assert stories(endings1, choices1_3, 2) == 30;

        assert stories(endings1, choices1_4, 1) == -1;
        assert stories(endings1, choices1_4, 2) == 15;

        assert stories(endings2, choices2_1, 1) == 11;
        assert stories(endings2, choices2_1, 2) == -1;

        assert stories(endings2, choices2_2, 1) == 11;
        assert stories(endings2, choices2_2, 2) == 11;

        assert stories(endings3, choices3_1, 1) == 4;
        assert stories(endings3, choices3_1, 2) == 4;

        assert stories(endings4, choices4_1, 1) == -1;

        System.out.println("All test cases passed!");
    }
}