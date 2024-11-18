package es.florida.AEV1Simulacion;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SimulationUtils3 {
    private int primaryProtein;
    private int secondaryProtein;
    private int tertiaryProtein;
    private int quaternaryProtein;
    private String totalDurationSimulationMP = "";
    private String totalDurationSimulationMT = "";
    public List<Integer> proteinList = new ArrayList<>();
    int processCounter = 0;

    public SimulationUtils3(int primaryProtein, int secondaryProtein, int tertiaryProtein,
            int quaternaryProtein) {

        this.primaryProtein = primaryProtein;
        this.secondaryProtein = secondaryProtein;
        this.tertiaryProtein = tertiaryProtein;
        this.quaternaryProtein = quaternaryProtein;

        setProteinList();
    }

    public void setProteinList() {
        this.proteinList.add(this.primaryProtein);
        this.proteinList.add(this.secondaryProtein);
        this.proteinList.add(this.tertiaryProtein);
        this.proteinList.add(this.quaternaryProtein);
    }

    public String getTotalDurationSimulationMP() {
        return totalDurationSimulationMP;
    }

    public void setTotalDurationSimulationMP(String totalDurationSimulationMP) {
        this.totalDurationSimulationMP = totalDurationSimulationMP;
    }

    public String getTotalDurationSimulationMT() {
        return totalDurationSimulationMT;
    }

    public void setTotalDurationSimulationMT(String totalDurationSimulationMT) {
        this.totalDurationSimulationMT = totalDurationSimulationMT;
    }

    public String runSimulationMT() {
        int proteinToCalculate = 1;
        List<Thread> threads = new ArrayList<>();
        long startTimeSimulation = System.nanoTime();
        for (int protein : proteinList) {
            for (int i = 0; i < protein; i++) {
                processCounter++;
                Thread t = simulacionMT(proteinToCalculate, processCounter);
                threads.add(t);
            }
            proteinToCalculate++;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                // ex.printStackTrace();
            }
        }

        long endTimeSimulation = System.nanoTime();
        setTotalDurationSimulationMT(calcDuration(startTimeSimulation,endTimeSimulation));

        return getTotalDurationSimulationMT();
    }

    public String runSimulationMP() {
        int proteinToCalculate = 1;
        try {
            long startTimeSimulation = System.nanoTime();
            for (int protein : proteinList) {
                for (int i = 0; i < protein; i++) {
                    processCounter++;
                    try {
                        Process builder = simulacionMP(proteinToCalculate, processCounter);
                        if (protein == 4) {
                            builder.waitFor();
                        }
                    } catch (IOException er) {
                    }
                }
                proteinToCalculate++;
            }

            long endTimeSimulation = System.nanoTime();
            setTotalDurationSimulationMP(calcDuration(startTimeSimulation, endTimeSimulation));
            

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return getTotalDurationSimulationMP();
    }

    public static Process simulacionMP(int proteinType, int processCounter) throws InterruptedException, IOException {
        File simulationDirectory = new File("src");
        String startTimeStamp = convertTimeStamp();
        String fileName = "PROT_MP_" + proteinType + "n" + processCounter + "_" + startTimeStamp + ".sim";
        File resultFile = new File(fileName);
        String clase = "es.florida.AEV1Simulacion.SimulacionMP";
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = clase;
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        command.add(String.valueOf(proteinType));
        command.add(String.valueOf(startTimeStamp));

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(simulationDirectory);
        builder.redirectOutput(resultFile);
        Process p = builder.start();

        return p;
    }

    synchronized public static Thread simulacionMT(int proteinType, int processCounter) {
        SimulacionMT2 mt = new SimulacionMT2(proteinType, processCounter);
        Thread t = new Thread(mt);

        t.setName("thread " + processCounter);
        t.start();
        return t;
    }

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

    public static String convertTimeStamp() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SS");
        String formattedTimestamp = now.format(formatter);

        return formattedTimestamp;
    }

    public static String calcDuration(long startTimeSimulation, long endTimeSimulation) {
        long totalDuration = endTimeSimulation - startTimeSimulation;
        long durationSeconds = totalDuration / 1000000000;
        long durationHundredths = (totalDuration / 1000000);

        return durationSeconds + "_" + durationHundredths;
    }
}
