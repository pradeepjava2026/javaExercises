package karatExercises;

import java.util.*;

public class TreasureRoomsProblem {

    public static List<String> filter_rooms(List<List<String>> instructions,
                                            List<String> treasure_rooms) {

        // Step 1: Data structures
        Map<String, Integer> incomingCount = new HashMap<>();
        Map<String, String> nextRoom = new HashMap<>();
        Set<String> treasureSet = new HashSet<>(treasure_rooms);

        // Step 2: Build maps
        for (List<String> instruction : instructions) {
            String source = instruction.get(0);
            String destination = instruction.get(1);

            // Store next room mapping
            nextRoom.put(source, destination);

            // Count incoming edges
            incomingCount.put(destination,
                    incomingCount.getOrDefault(destination, 0) + 1);
        }

        // Step 3: Find valid rooms
        List<String> result = new ArrayList<>();

        for (String room : nextRoom.keySet()) {

            int inDegree = incomingCount.getOrDefault(room, 0);
            String next = nextRoom.get(room);

            if (inDegree >= 2 && treasureSet.contains(next)) {
                result.add(room);
            }
        }

        return result;
    }
    
    public static void main(String[] args) {

        List<List<String>> instructions1 = Arrays.asList(
            Arrays.asList("jasmin", "tulip"),
            Arrays.asList("lily", "tulip"),
            Arrays.asList("tulip", "tulip"),
            Arrays.asList("rose", "rose"),
            Arrays.asList("violet", "rose"),
            Arrays.asList("sunflower", "violet"),
            Arrays.asList("daisy", "violet"),
            Arrays.asList("iris", "violet")
        );

        List<String> treasure1 = Arrays.asList("lily", "tulip", "violet", "rose");

        assert filter_rooms(instructions1, treasure1)
                .equals(Arrays.asList("tulip", "violet"));


        List<String> treasure2 = Arrays.asList("lily", "jasmin", "violet");

        assert filter_rooms(instructions1, treasure2)
                .isEmpty();


        List<List<String>> instructions2 = Arrays.asList(
            Arrays.asList("jasmin", "tulip"),
            Arrays.asList("lily", "tulip"),
            Arrays.asList("tulip", "violet"),
            Arrays.asList("violet", "violet")
        );

        List<String> treasure3 = Arrays.asList("violet");

        assert filter_rooms(instructions2, treasure3)
                .equals(Arrays.asList("tulip"));

        System.out.println("All test cases passed!");
    }
}
