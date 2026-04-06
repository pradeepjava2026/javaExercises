package karatExercises;


import java.util.*;

class LogEntry {

    private float timestamp;
    private String licensePlate;
    private String boothType;
    private int location;
    private String direction;

    public LogEntry(String logLine) {
        String[] tokens = logLine.split(" ");

        this.timestamp = Float.parseFloat(tokens[0]);
        this.licensePlate = tokens[1];
        this.boothType = tokens[3];

        String loc = tokens[2];
        this.location = Integer.parseInt(loc.substring(0, loc.length() - 1));

        String dir = loc.substring(loc.length() - 1);
        this.direction = dir.equals("E") ? "EAST" : "WEST";
    }

    public float getTimestamp() { return timestamp; }
    public String getLicensePlate() { return licensePlate; }
    public String getBoothType() { return boothType; }
    public int getLocation() { return location; }
    public String getDirection() { return direction; }
}

class LogFile {

    List<LogEntry> logs = new ArrayList<>();

    // Add log manually (easy for IDE testing)
    public void add(String logLine) {
        logs.add(new LogEntry(logLine));
    }

    // -------------------------------
    // 1. Count Journeys
    // -------------------------------
    public int countJourneys() {
        Set<String> active = new HashSet<>();
        int count = 0;

        for (LogEntry e : logs) {
            String key = e.getLicensePlate() + "|" + e.getDirection();

            if (e.getBoothType().equals("ENTRY")) {
                active.add(key);
            } else if (e.getBoothType().equals("EXIT")) {
                if (active.contains(key)) {
                    count++;
                    active.remove(key);
                }
            }
        }
        return count;
    }

    // -------------------------------
    // 2. Catch Speeders
    // -------------------------------
    public List<String> catchSpeeders() {

        List<String> result = new ArrayList<>();

        Set<String> active = new HashSet<>();
        Map<String, Integer> lastLoc = new HashMap<>();
        Map<String, Float> lastTime = new HashMap<>();
        Map<String, Integer> count120 = new HashMap<>();

        for (LogEntry e : logs) {

            String key = e.getLicensePlate() + "|" + e.getDirection();

            if (e.getBoothType().equals("ENTRY")) {
                active.add(key);
                lastLoc.put(key, e.getLocation());
                lastTime.put(key, e.getTimestamp());
                count120.put(key, 0);
                continue;
            }

            if (!active.contains(key)) continue;

            int dist = Math.abs(e.getLocation() - lastLoc.get(key));
            float time = e.getTimestamp() - lastTime.get(key);

            lastLoc.put(key, e.getLocation());
            lastTime.put(key, e.getTimestamp());

            if (dist == 10 && time > 0) {
                float speed = (dist * 3600) / time;

                if (speed >= 130) {
                    result.add(e.getLicensePlate());
                    active.remove(key);
                } else if (speed >= 120) {
                    int c = count120.get(key) + 1;
                    count120.put(key, c);
                    if (c >= 2) {
                        result.add(e.getLicensePlate());
                        active.remove(key);
                    }
                }
            }

            if (e.getBoothType().equals("EXIT")) {
                active.remove(key);
            }
        }

        return result;
    }

    // -------------------------------
    // 3. Failing Sensors
    // -------------------------------
    public String failingSensors() {

        Map<String, Integer> missing = new HashMap<>();
        Map<String, Integer> lastLoc = new HashMap<>();
        Set<String> active = new HashSet<>();

        for (LogEntry e : logs) {

            String key = e.getLicensePlate() + "|" + e.getDirection();

            if (e.getBoothType().equals("ENTRY")) {
                active.add(key);
                lastLoc.put(key, e.getLocation());
                continue;
            }

            if (!active.contains(key)) continue;

            int prev = lastLoc.get(key);
            int curr = e.getLocation();

            if (Math.abs(curr - prev) > 10) {
                int step = curr > prev ? 10 : -10;
                int x = prev + step;

                while ((step > 0 && x < curr) || (step < 0 && x > curr)) {
                    String sensor = x + "|" + e.getDirection();
                    missing.put(sensor, missing.getOrDefault(sensor, 0) + 1);
                    x += step;
                }
            }

            lastLoc.put(key, curr);

            if (e.getBoothType().equals("EXIT")) {
                active.remove(key);
            }
        }

        int max = 0;
        String res = "";

        for (String k : missing.keySet()) {
            if (missing.get(k) > max) {
                max = missing.get(k);
                res = k;
            }
        }

        if (res.isEmpty()) return "0";

        String[] parts = res.split("\\|");
        return max + " " + parts[0] + (parts[1].equals("EAST") ? "E" : "W");
    }
}

public class HighwayProb {

    public static void main(String[] args) {

        LogFile log = new LogFile();

        // Sample data
        log.add("1000.000 CAR1 200E ENTRY");
        log.add("1100.000 CAR1 210E MAINROAD");
        log.add("1200.000 CAR1 220E MAINROAD");
        log.add("1300.000 CAR1 230E EXIT");

        log.add("2000.000 CAR2 200E ENTRY");
        log.add("2100.000 CAR2 230E EXIT");

        // -----------------------
        // RUN OUTPUT
        // -----------------------
        System.out.println("Journeys: " + log.countJourneys());
        System.out.println("Speeders: " + log.catchSpeeders());
        System.out.println("Failing Sensors: " + log.failingSensors());
    }
}
