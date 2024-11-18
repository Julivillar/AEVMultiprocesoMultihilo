package es.florida.AEV1Simulacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimulationUtils {

    /**
     * @param type
     * @return the calculus for the simulation
     */
    public static double simulation(int type) {
        double calc = 0.0;
        double simulationTime = Math.pow(5, type);
        double startTime = System.currentTimeMillis();
        double endTime = startTime + simulationTime;
        while (System.currentTimeMillis() < endTime) {
            calc = Math.sin(Math.pow(Math.random(), 2));
        }
        return calc;
    }

    /**
     * @return the formatted timestamp for the file name
     */
    public static String convertTimeStamp() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SS");
        String formattedTimestamp = now.format(formatter);

        return formattedTimestamp;
    }

    /**
     * @param startTimeSimulation
     * @param endTimeSimulation
     * @return the duration of the simulation converted
     */
    public static String calcDuration(long startTimeSimulation, long endTimeSimulation) {
        long totalDuration = endTimeSimulation - startTimeSimulation;
        long durationSeconds = totalDuration / 1000000000;
        long durationHundredths = (totalDuration / 1000000);

        return durationSeconds + "_" + durationHundredths;
    }
}
