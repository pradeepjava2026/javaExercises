package karatquestions;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
We are writing software to analyze logs for toll booths on a highway. 
This highway is a divided highway with limited access; the only way on to or off of the highway is through a toll booth.
There are three types of toll booths:
* ENTRY (E in the diagram) toll booths, where a car goes through a booth as it enters the highway.
* EXIT (X in the diagram) toll booths, where a car goes through a booth as it exits the highway.
* MAINROAD (M in the diagram), which have sensors that record a license plate as a car drives through at full speed.

        Exit Booth                         Entry Booth
            |                                   |
            X                                   E
             \                                 /
---<------------<---------M---------<-----------<---------<----
                                         (West-bound side)
===============================================================
                                         (East-bound side)
------>--------->---------M--------->--------->--------->------
             /                                 \
            E                                   X
            |                                   |
        Entry Booth                         Exit Booth
For our first task:
1-1) Read through and understand the code and comments below. Feel free to run the code and tests.
1-2) The tests are not passing due to a bug in the code. Make the necessary changes to LogEntry to fix the bug.
*/

/**
2-1)  Write a function to count number of journeys.8
*/

/**
3-1) We would like to catch people who are driving at unsafe speeds on the highway. To help us do that, 
we would like to identify journeys where a driver does either of the following:
* Drive 130 km/h or greater in any individual 10km segment of tollway.
* Drive 120 km/h or greater in any two 10km segments of tollway.

For example, consider the following journey:
1000.000 TST002 270W ENTRY
1275.000 TST002 260W EXIT

In this case, the driver of TST002 drove 10 km in 275 seconds. We can calculate
that this driver drove an average speed of ~130.91km/hr over this segment:

10 km * 3600 sec/hr
------------------- = 130.91 km/hr
      275 sec

Note that:
* A license plate may have multiple journeys in one file, and if they drive at unsafe speeds in both journeys, both should be counted.
* We do not mark speeding if they are not on the highway (i.e. for any driving between an EXIT and ENTRY event).
* Speeding is only marked once per journey. For example, if there are 4 segments 120km/h or greater, 
* or multiple segments 130km/h or greater, the journey is only counted once.

q. Write a function catchSpeeders in LogFile that returns a collection of license plates that drove at unsafe speeds during a journey in the LogFile.
     If the same license plate drives at unsafe speeds during two different journeys,
      the license plate should appear twice (once for each journey they drove at unsafe speeds).
*/

/**
4-1) Write a function failing_sesnors in LogFile that determines how many log entries were missing for each sensor,
 and return the count, location, and direction of the sensor with the most missing entries.
*/

class LogEntry {
  /**
   * Represents an entry from a single log line. Log lines look like this in the file:
   *
   * 34400.409 SXY288 210E ENTRY
   *
   * Where:
   * * 34400.409 is the timestamp in seconds since the software was started.
   * * SXY288 is the license plate of the vehicle passing through the toll booth.
   * * 210E is the location and traffic direction of the toll booth. Here, the toll
   *     booth is at 210 kilometers from the start of the tollway, and the E indicates
   *     that the toll booth was on the east-bound traffic side. Tollbooths are placed
   *     every ten kilometers.
   * * ENTRY indicates which type of toll booth the vehicle went through. This is one of
   *     "ENTRY", "EXIT", or "MAINROAD".
   **/
  private final float timestamp;
  private final String licensePlate;
  private final String boothType;
  private final int location;
  private final String direction;
  public LogEntry(String logLine) {
    String[] tokens = logLine.split(" ");
    this.timestamp = Float.parseFloat(tokens[0]);;
    this.licensePlate = tokens[1];
    this.boothType = tokens[3];
    this.location =
      Integer.parseInt(tokens[2].substring(0, tokens[2].length() - 1));
    
    String directionLetter = tokens[2].substring(tokens[2].length() - 1);
    if (directionLetter.equals("E")) {
      this.direction = "EAST";
    } else if (directionLetter.equals("W")) {
      this.direction = "WEST";
    } else {
      throw new IllegalArgumentException();
    }
  }
 
public float getTimestamp() {
    return timestamp;
  }
  public String getLicensePlate() {
    return licensePlate;
  }
  public String getBoothType() {
    return boothType;
  }
  public int getLocation() {
    return location;
  }
  public String getDirection() {
    return direction;
  }
  @Override
  public String toString() {
    return String.format(
      "<LogEntry timestamp: %f  license: %s  location: %d  direction: %s  booth type: %s>",
      timestamp,
      licensePlate,
      location,
      direction,
      boothType
    );
  }
}
class LogFile {
  static /*
   * Represents a file containing a number of log lines, converted to LogEntry
   * objects.
   */
  List<LogEntry> logEntries;
  public LogFile(BufferedReader reader) throws IOException {
    this.logEntries = new ArrayList<>();
    String line = reader.readLine();
    while (line != null) {
      LogEntry logEntry = new LogEntry(line.strip());
      this.logEntries.add(logEntry);
      line = reader.readLine();
    }
  }
  public LogFile() {
	// TODO Auto-generated constructor stub
}
public LogEntry get(int index) {
    return this.logEntries.get(index);
  }
  public int size() {
    return this.logEntries.size();
  }
//4-1) failing_sesnors()
  // Failing sensors (missing log entries per sensor)
  // ------------------------------------------------------------
  // Sensors are every 10 km.
  // If a car was seen at 200E and then next seen at 230E (same journey),
  // then it "should" have been seen at 210E and 220E too.
  // If those logs are missing, those sensors are likely failing.
  //
  // We count missing occurrences for each intermediate 10km point.
  //
  // Returns: "<missingCount> <location><E/W>"
  // Example: "17 220E"
  public String failing_sensors() {
    Map<String, Integer> missingCount = new HashMap<>();

    // Track active journey per (plate + direction)
    Set<String> active = new HashSet<>();
    Map<String, Integer> lastLoc = new HashMap<>();

    for (LogEntry e : logEntries) {
      String plate = e.getLicensePlate();
      String dir = e.getDirection();  // "EAST"/"WEST"
      String type = e.getBoothType();
      int loc = e.getLocation();

      String key = plate + "|" + dir;

      if ("ENTRY".equals(type)) {
        active.add(key);
        lastLoc.put(key, loc);
        continue;
      }

      if (!active.contains(key)) {
        continue; // off-highway
      }

      Integer prevLocObj = lastLoc.get(key);
      if (prevLocObj == null) {
        lastLoc.put(key, loc);
      } else {
        int prevLoc = prevLocObj;
        int delta = loc - prevLoc;

        // If jump is more than 10km, intermediate sensors are "missing"
        if (Math.abs(delta) > 10) {
          int step = (delta > 0) ? 10 : -10;
          int expected = prevLoc + step;

          while ((step > 0 && expected < loc) || (step < 0 && expected > loc)) {
            // Count missing for that sensor
            String sensorKey = expected + "|" + dir; // per location+direction
            missingCount.put(sensorKey, missingCount.getOrDefault(sensorKey, 0) + 1);
            expected += step;
          }
        }

        lastLoc.put(key, loc);
      }

      if ("EXIT".equals(type)) {
        active.remove(key);
        lastLoc.remove(key);
      }
    }

    // Find sensor with max missing
    int bestMissing = 0;
    int bestLocation = -1;
    String bestDir = "UNKNOWN";

    for (Map.Entry<String, Integer> entry : missingCount.entrySet()) {
      int c = entry.getValue();
      if (c > bestMissing) {
        bestMissing = c;
        String[] parts = entry.getKey().split("\\|", 2);
        bestLocation = Integer.parseInt(parts[0]);
        bestDir = parts[1];
      }
    }

    String dirLetter = "?";
    if ("EAST".equals(bestDir)) dirLetter = "E";
    else if ("WEST".equals(bestDir)) dirLetter = "W";

    return bestMissing + " " + bestLocation + dirLetter;
  }
  
//3-1) method: catchSpeeders():

public List<String> catchSpeeders() {

    List<String> result = new ArrayList<>();

    // Track an "active journey" per (plate + direction)
    Set<String> active = new HashSet<>();

    // last location/time for each active journey
    Map<String, Integer> lastLoc = new HashMap<>();
    Map<String, Float> lastTs = new HashMap<>();

    // count of segments >= 120 km/h (10km segments only) per journey
    Map<String, Integer> segments120 = new HashMap<>();

    // already marked as speeder for this journey (so we add only once per journey)
    Set<String> markedThisJourney = new HashSet<>();

    for (LogEntry e : LogFile.logEntries) {
      String plate = e.getLicensePlate();
      String dir = e.getDirection();
      String type = e.getBoothType();
      String key = plate + "|" + dir;

      if ("ENTRY".equals(type)) {
        active.add(key);
        lastLoc.put(key, e.getLocation());
        lastTs.put(key, e.getTimestamp());
        segments120.put(key, 0);
        markedThisJourney.remove(key);
        continue;
      }

      // Must ignore driving when NOT on highway (between EXIT and next ENTRY).
      if (!active.contains(key)) {
        continue;
      }

      // Compute segment speed from last sensor to this sensor
      Integer prevLocObj = lastLoc.get(key);
      Float prevTsObj = lastTs.get(key);

      if (prevLocObj != null && prevTsObj != null) {
        int distKm = Math.abs(e.getLocation() - prevLocObj);
        float dtSec = e.getTimestamp() - prevTsObj;

        // Update last seen for next iteration
        lastLoc.put(key, e.getLocation());
        lastTs.put(key, e.getTimestamp());

        // Only evaluate if time is positive and it is exactly a 10km segment (rule says "10km segment").        
	if (dtSec > 0 && distKm == 10 && !markedThisJourney.contains(key)) {
          float speedKmh = (distKm * 3600.0f) / dtSec;

          // Rule 1: >=130 in any 10km segment
          if (speedKmh >= 130.0f) {
            result.add(plate);
            markedThisJourney.add(key);
          } else if (speedKmh >= 120.0f) {
            // Rule 2: >=120 in any two 10km segments
            int c = segments120.getOrDefault(key, 0) + 1;
            segments120.put(key, c);
            if (c >= 2) {
              result.add(plate);
              markedThisJourney.add(key);
            }
          }
        }
      } else {
        // initialize if something missing
        lastLoc.put(key, e.getLocation());
        lastTs.put(key, e.getTimestamp());
      }

      if ("EXIT".equals(type)) {
        // end journey
        active.remove(key);
        lastLoc.remove(key);
        lastTs.remove(key);
        segments120.remove(key);
        markedThisJourney.remove(key);
      }
    }

    return result;
  }
  
}
 public class Highway {
  public static void main(String[] argv) throws IOException {
	  LogFile logFile = new LogFile();
	  logFile.catchSpeeders();
   // testLogFile();
    testLogEntry();
    
  }
 
  public static void testLogFile() throws IOException {
    System.out.println("Running testLogFile");
    try (
      BufferedReader reader = new BufferedReader(
        new FileReader("/content/test/tollbooth_small.log")
      );
    ) {
      LogFile logFile = new LogFile(reader);
      assertEquals(13, logFile.size());
      for (LogEntry entry : logFile.logEntries) {
        assert (entry instanceof LogEntry);
      }
    }
  }
  
  public static void testLogEntry() {
    System.out.println("Running testLogEntry");
    String logLine = "44776.619 KTB918 310E MAINROAD";
    LogEntry logEntry = new LogEntry(logLine);
    assertEquals(44776.619f, logEntry.getTimestamp(), 0.0001);
    assertEquals("KTB918", logEntry.getLicensePlate());
    assertEquals(310, logEntry.getLocation());
    assertEquals("EAST", logEntry.getDirection());
    assertEquals("MAINROAD", logEntry.getBoothType());
    logLine = "52160.132 ABC123 400W ENTRY";
    logEntry = new LogEntry(logLine);
    assertEquals(52160.132f, logEntry.getTimestamp(), 0.0001);
    assertEquals("ABC123", logEntry.getLicensePlate());
    assertEquals(400, logEntry.getLocation());
    assertEquals("WEST", logEntry.getDirection());
    assertEquals("ENTRY", logEntry.getBoothType());
  }


/*
Solution:
1.2)
STEP 1) assertEquals(44776.619f, logEntry.getTimestamp(), 0.0001); -> The bug is in LogEntry.timestamp and getTimestamp(), test expect a numeric timestamp.
STEP 2) Change the field type -> from -> private final String timestamp; to -> private final float timestamp;
STEP 3) Parse the timestamp in the constructor: from -> this.timestamp = tokens[0]; to -> this.timestamp = Float.parseFloat(tokens[0]);
STEP 4) Change the getter return type
	From -> 
	public String getTimestamp() {
		return timestamp;
	}
	to -> 
	public float getTimestamp() {
		return timestamp;
	}
2-1) method count Journeys:
*/
public static int countJourneys(List<LogEntry> logs) {
	int completedJourneysCount = 0;
	
	if (logs.size() > 0) {
		Set<String> visitedVehicles = new HashSet<>();
		for (LogEntry logEntry : logs) {
			switch (logEntry.getBoothType()) {
			case "ENTRY":
				visitedVehicles.add(logEntry.getLicensePlate());
				break;
			case "EXIT":
				if (visitedVehicles.contains(logEntry.getLicensePlate())) {
					completedJourneysCount++;
					visitedVehicles.remove(logEntry.getLicensePlate());
				}
				break;
			default:
				// For main road
				break;
			}
		}
	}
	return completedJourneysCount;
}


}

