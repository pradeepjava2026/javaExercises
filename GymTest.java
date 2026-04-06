package karatExercises;

import java.util.Map;

public class GymTest {
    public static void main(String[] args) {

        Membership system = new Membership();

        // Add members
        system.addMember(new Member(12, "A", MembershipStatus.GOLD));
        system.addMember(new Member(22, "B", MembershipStatus.SILVER));
        system.addMember(new Member(31, "C", MembershipStatus.BRONZE));
        system.addMember(new Member(4, "D", MembershipStatus.BRONZE)); // no workouts

        // Add workouts
        system.addWorkout(12, new Workout(1, 0, 20));
        system.addWorkout(12, new Workout(2, 0, 30)); // avg = 25

        system.addWorkout(22, new Workout(3, 0, 50)); // avg = 50

        system.addWorkout(31, new Workout(4, 0, 70));
        system.addWorkout(31, new Workout(5, 0, 75)); // avg = 72.5

        // Get averages
        Map<Integer, Double> avg = system.getAverageWorkoutDurations();

        assert avg.get(12) == 25.0 : "Test failed for member 12";
        assert avg.get(22) == 50.0 : "Test failed for member 22";
        assert avg.get(31) == 72.5 : "Test failed for member 31";
        assert !avg.containsKey(4) : "Member 4 should not be present";

        // Membership stats
        Map<String, Double> stats = system.getMembershipStatistics();

        assert stats.get("totalMembers") == 4.0;
        assert stats.get("paidMembers") == 2.0;
        assert stats.get("conversionRate") == 50.0;

        System.out.println("All tests passed!");
    }
}
