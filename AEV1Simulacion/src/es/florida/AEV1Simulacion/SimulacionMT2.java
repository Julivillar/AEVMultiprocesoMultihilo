package es.florida.AEV1Simulacion;

import java.io.FileWriter;

public class SimulacionMT2 implements Runnable {

    private int proteinType;
    private int processCounter;

    public SimulacionMT2(int proteinType, int processCounter) {
        this.proteinType = proteinType;
        this.processCounter = processCounter;
    }

    @Override
    public void run() {
        //String nombre = Thread.currentThread().getName();
        createFile();
    }

    synchronized public void createFile() {
        String startTimeStamp = SimulationUtils.convertTimeStamp();
        String fileName = "PROT_MT_" + this.proteinType + "n" + this.processCounter + "_" + startTimeStamp + ".sim";

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
}
