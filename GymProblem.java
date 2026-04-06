package karatExercises;

import java.util.*;

//Enum for membership status
enum MembershipStatus {
 BRONZE, SILVER, GOLD
}

//Workout class
class Workout {
 int id;
 int startTime;
 int endTime;

 public Workout(int id, int startTime, int endTime) {
     this.id = id;
     this.startTime = startTime;
     this.endTime = endTime;
 }

 public int getDuration() {
     return endTime - startTime;
 }
}

//Member class
class Member {
 int memberId;
 String name;
 MembershipStatus membershipStatus;
 List<Workout> workouts;

 public Member(int memberId, String name, MembershipStatus membershipStatus) {
     this.memberId = memberId;
     this.name = name;
     this.membershipStatus = membershipStatus;
     this.workouts = new ArrayList<>();
 }

 public void addWorkout(Workout workout) {
     workouts.add(workout);
 }

 public List<Workout> getWorkouts() {
     return workouts;
 }
}

//Membership class (MAIN LOGIC)
class Membership {

 Map<Integer, Member> members = new HashMap<>();

 // Add member
 public void addMember(Member member) {
     members.put(member.memberId, member);
 }

 // Update membership status
 public void updateMembershipStatus(int memberId, MembershipStatus status) {
     Member m = members.get(memberId);
     if (m != null) {
         m.membershipStatus = status;
     }
 }

 //  Function 1: addWorkout
 public void addWorkout(int memberId, Workout workout) {
     Member m = members.get(memberId);
     if (m != null) {
         m.addWorkout(workout);
     }
     // else ignore
 }

 //  Function 2: getAverageWorkoutDurations
 public Map<Integer, Double> getAverageWorkoutDurations() {
     Map<Integer, Double> result = new HashMap<>();

     for (Member m : members.values()) {
         List<Workout> workouts = m.getWorkouts();

         if (workouts.isEmpty()) {
             continue; // skip
         }

         int total = 0;
         for (Workout w : workouts) {
             total += w.getDuration();
         }

         double avg = (double) total / workouts.size();
         result.put(m.memberId, avg);
     }

     return result;
 }

 // Membership statistics
 public Map<String, Double> getMembershipStatistics() {
     Map<String, Double> stats = new HashMap<>();

     int totalMembers = members.size();
     int paidMembers = 0;

     for (Member m : members.values()) {
         if (m.membershipStatus == MembershipStatus.SILVER ||
             m.membershipStatus == MembershipStatus.GOLD) {
             paidMembers++;
         }
     }

     double conversionRate = totalMembers == 0 ? 0 :
             ((double) paidMembers / totalMembers) * 100;

     stats.put("totalMembers", (double) totalMembers);
     stats.put("paidMembers", (double) paidMembers);
     stats.put("conversionRate", conversionRate);

     return stats;
 }
}

