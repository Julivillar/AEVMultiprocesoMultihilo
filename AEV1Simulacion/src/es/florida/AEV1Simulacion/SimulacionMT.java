package es.florida.AEV1Simulacion;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SimulacionMT implements Runnable {

    private int proteinType;
    private int primaryProtein;
    private int secondaryProtein;
    private int tertiaryProtein;
    private int quaternaryProtein;
    private int processNumStart;
    private String totalDurationSimulationMT = "";
    public List<Integer> proteinList = new ArrayList<>();

    /**
     * @param primaryProtein
     * @param secondaryProtein
     * @param tertiaryProtein
     * @param quaternaryProtein
     * @param processNumStart
     */
    public SimulacionMT(int primaryProtein, int secondaryProtein, int tertiaryProtein,
            int quaternaryProtein, int processNumStart) {

        this.primaryProtein = primaryProtein;
        this.secondaryProtein = secondaryProtein;
        this.tertiaryProtein = tertiaryProtein;
        this.quaternaryProtein = quaternaryProtein;
        this.processNumStart = processNumStart;
        this.proteinList.add(this.primaryProtein);
        this.proteinList.add(this.secondaryProtein);
        this.proteinList.add(this.tertiaryProtein);
        this.proteinList.add(this.quaternaryProtein);
    }

    /**
     * @param proteinType
     * @param processNumStart
     */
    public SimulacionMT(int proteinType, int processNumStart) {
        this.proteinType = proteinType;
        this.processNumStart = processNumStart;
    }

    public String getTotalDurationSimulationMT() {
        return totalDurationSimulationMT;
    }

    public void setTotalDurationSimulationMT(String totalDurationSimulationMT) {
        this.totalDurationSimulationMT = totalDurationSimulationMT;
    }

    @Override
    public void run() {
        // String nombre = Thread.currentThread().getName();
        createFile();
    }

    /**
     * This function will be called in each thread to create the file containing the required info
     */
    synchronized public void createFile() {
        String startTimeStamp = SimulationUtils.convertTimeStamp();
        String fileName = "PROT_MT_" + this.proteinType + "n" + this.processNumStart + "_" + startTimeStamp + ".sim";

        try {
            FileWriter fw = new FileWriter(fileName);
            long startTimeSimulation = System.nanoTime();
            double resultSimulacion = SimulationUtils.simulation(this.proteinType);
            long endTimeSimulation = System.nanoTime();

            String endTimeStamp = SimulationUtils.convertTimeStamp();

            String durationFormatted = SimulationUtils.calcDuration(startTimeSimulation, endTimeSimulation);

            fw.write(startTimeStamp + "\n" +
                    endTimeStamp + "\n" +
                    durationFormatted + "\n" +
                    String.valueOf(resultSimulacion));

            fw.close();

        } catch (Exception e) {
            e.getMessage();
        }

    }

    /**
     * @param proteinType
     * @param processNumStart this will be used for the file name
     * @return T the thread with the task
     */
    synchronized public Thread simulacionMT(int proteinType, int processNumStart) {
        SimulacionMT mt = new SimulacionMT(proteinType, processNumStart);
        Thread t = new Thread(mt);

        t.setName("thread " + processNumStart);
        t.start();
        return t;
    }

    /**
     * @return the total time for the duration of the simulation
     */
    public String runSimulationMT() {
        int proteinToCalculate = 1;
        List<Thread> threads = new ArrayList<>();
        long startTimeSimulation = System.nanoTime();
        for (int protein : proteinList) {
            for (int i = 0; i < protein; i++) {
                processNumStart++;
                Thread t = simulacionMT(proteinToCalculate, processNumStart);
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
        setTotalDurationSimulationMT(SimulationUtils.calcDuration(startTimeSimulation, endTimeSimulation));

        return getTotalDurationSimulationMT();
    }
}
