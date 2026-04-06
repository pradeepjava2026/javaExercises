package karatExercises;


import java.util.*;

/**
 * Represents an obstacle course
 */
class Course {
  public String title;           // Name of the course
  public int obstacleCount;      // Total number of obstacles

  public Course(String courseTitle, int obstacles) {
    this.title = courseTitle;
    this.obstacleCount = obstacles;
  }

  /**
   * Two courses are equal if title and obstacle count match
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Course)) return false;
    Course c = (Course) o;
    return Objects.equals(c.title, this.title) &&
           c.obstacleCount == this.obstacleCount;
  }

  /**
   * Hashcode based on title and obstacle count
   */
  @Override
  public int hashCode() {
    return Objects.hash(title, obstacleCount);
  }
}

/**
 * Represents a single run attempt of a course
 */
class Run {
  public Course course;                  // Course for this run
  public boolean complete;               // Whether run finished all obstacles
  public List<Integer> obstacleTimes;    // Time taken per obstacle

  public Run(Course runCourse) {
    this.course = runCourse;
    this.complete = false;
    this.obstacleTimes = new ArrayList<>();
  }

  /**
   * Adds time for an obstacle
   * Marks run complete when all obstacles are filled
   */
  public void addObstacleTime(int obstacleTime) {
    if (complete) {
      // Prevent adding more data after completion
      throw new IllegalStateException("Cannot add obstacle to complete run");
    }

    obstacleTimes.add(obstacleTime);

    // Mark complete when all obstacles are done
    if (obstacleTimes.size() == course.obstacleCount) {
      complete = true;
    }
  }

  /**
   * Returns total time (partial or complete)
   */
  public int getRunTime() {
    return obstacleTimes.stream().mapToInt(Integer::intValue).sum();
  }
}

/**
 * Stores multiple runs for a course and provides analytics
 */
class RunCollection {
  public Course course;
  public List<Run> runs;

  public RunCollection(Course collectionCourse) {
    this.course = collectionCourse;
    this.runs = new ArrayList<>();
  }

  /**
   * Returns number of runs
   */
  public int getNumRuns() {
    return runs.size();
  }

  /**
   * Adds a run to collection
   * Ensures course consistency
   */
  public void addRun(Run run) {
    if (!run.course.equals(course)) {
      throw new IllegalArgumentException("Course mismatch");
    }
    runs.add(run);
  }

  /**
   * Returns the fastest completed run
   *
   * FIX: Only consider COMPLETE runs
   */
  public int personalBest() {
    return runs.stream()
        .filter(r -> r.complete)        // Ignore incomplete runs
        .mapToInt(Run::getRunTime)      // Convert to total time
        .min()                          // Find minimum
        .orElse(Integer.MAX_VALUE);     // If no runs exist
  }

  /**
   * Best possible run if each obstacle is done perfectly
   *
   * Approach:
   * - For each obstacle index
   * - Pick minimum time across all runs (even incomplete)
   * - Sum them
   */
  public int bestOfBests() {
    int total = 0;

    // Iterate through each obstacle index
    for (int i = 0; i < course.obstacleCount; i++) {
      int minTime = Integer.MAX_VALUE;

      // Check all runs for this obstacle
      for (Run run : runs) {
        if (run.obstacleTimes.size() > i) {
          minTime = Math.min(minTime, run.obstacleTimes.get(i));
        }
      }

      // Add best time for this obstacle
      if (minTime != Integer.MAX_VALUE) {
        total += minTime;
      }
    }

    return total;
  }

  /**
   * Calculates probability that current run becomes personal best
   *
   * Uses Monte Carlo Simulation:
   * - Run 10,000 trials
   * - Fill missing obstacles randomly using past data
   * - Check if total <= personal best
   */
  public double chanceOfPersonalBest(Run currentRun) {
    int trials = 10000;
    int success = 0;

    int best = personalBest();
    Random rand = new Random();

    // Repeat simulation multiple times
    for (int t = 0; t < trials; t++) {

      int totalTime = currentRun.getRunTime();

      // Fill remaining obstacles
      for (int i = currentRun.obstacleTimes.size(); i < course.obstacleCount; i++) {

        List<Integer> possibleTimes = new ArrayList<>();

        // Collect all historical times for this obstacle
        for (Run run : runs) {
          if (run.obstacleTimes.size() > i) {
            possibleTimes.add(run.obstacleTimes.get(i));
          }
        }

        // Randomly pick one time
        if (!possibleTimes.isEmpty()) {
          int randomTime = possibleTimes.get(rand.nextInt(possibleTimes.size()));
          totalTime += randomTime;
        }
      }

      // Check if simulated run beats or matches personal best
      if (totalTime <= best) {
        success++;
      }
    }

    // Return probability
    return (double) success / trials;
  }
}

/**
 * Main class with test cases
 */
public class RaceProblem {

  public static void main(String[] args) {
    testRun();
    testRunCollection();
    testBestOfBests();
    testChanceOfPersonalBest();

    System.out.println("All tests passed!");
  }

  /**
   * Tests Run class functionality
   */
  public static void testRun() {
    System.out.println("Running testRun");

    Course course = new Course("Test", 2);
    Run run = new Run(course);

    run.addObstacleTime(3);
    assert !run.complete : "Run should not be complete yet";

    run.addObstacleTime(5);
    assert run.complete : "Run should be complete";

    assert run.getRunTime() == 8 : "Total time should be 8";

    try {
      run.addObstacleTime(4);
      assert false : "Should throw exception";
    } catch (IllegalStateException e) {
      // expected
    }
  }

  /**
   * Utility method to create RunCollection from array
   */
  public static RunCollection makeRunCollection(Course course, int[][] data) {
    RunCollection rc = new RunCollection(course);

    for (int[] runData : data) {
      Run run = new Run(course);
      for (int t : runData) {
        run.addObstacleTime(t);
      }
      rc.addRun(run);
    }

    return rc;
  }

  /**
   * Tests personalBest logic
   */
  public static void testRunCollection() {
    System.out.println("Running testRunCollection");

    int[][] data = {
        {3, 4, 5, 6},
        {4, 4, 4, 5},
        {4, 5, 4, 6},
        {5, 5, 3} // incomplete
    };

    Course course = new Course("Test", 4);
    RunCollection rc = makeRunCollection(course, data);

    assert rc.getNumRuns() == 4 : "Should have 4 runs";
    assert rc.personalBest() == 17 : "Best complete run should be 17";
  }

  /**
   * Tests bestOfBests logic
   */
  public static void testBestOfBests() {
    System.out.println("Running testBestOfBests");

    int[][] data = {
        {3, 4, 5, 6},
        {4, 4, 4, 5},
        {4, 5, 4, 6},
        {5, 5, 3}
    };

    Course course = new Course("Test", 4);
    RunCollection rc = makeRunCollection(course, data);

    assert rc.bestOfBests() == 15 : "Best of bests should be 15";
  }

  /**
   * Tests probability simulation
   */
  public static void testChanceOfPersonalBest() {
    System.out.println("Running testChanceOfPersonalBest");

    int[][] data = {
        {3, 4, 5, 6},
        {4, 4, 4, 5},
        {4, 5, 4, 6}
    };

    Course course = new Course("Test", 4);
    RunCollection rc = makeRunCollection(course, data);

    Run current = new Run(course);
    current.addObstacleTime(3); // partial run

    double chance = rc.chanceOfPersonalBest(current);

    System.out.println("Chance: " + chance);

    assert chance >= 0.0 && chance <= 1.0 : "Invalid probability";
  }
}
