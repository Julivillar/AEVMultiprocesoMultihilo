package es.florida.AEV1Simulacion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulacionMP {

	private int primaryProtein;
	private int secondaryProtein;
	private int tertiaryProtein;
	private int quaternaryProtein;
	private String totalDurationSimulationMP = "";
	private int processNumStart;
	public List<Integer> proteinList = new ArrayList<>();

	/**
	 * @param primaryProtein
	 * @param secondaryProtein
	 * @param tertiaryProtein
	 * @param quaternaryProtein
	 * @param processNumStart
	 */
	public SimulacionMP(int primaryProtein, int secondaryProtein, int tertiaryProtein,
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

	public String getTotalDurationSimulationMP() {
		return totalDurationSimulationMP;
	}

	public void setTotalDurationSimulationMP(String totalDurationSimulationMP) {
		this.totalDurationSimulationMP = totalDurationSimulationMP;
	}

	public static void main(String[] args) {
		long startTimeSimulation = System.nanoTime();

		double resultSimulacion = SimulationUtils.simulation(Integer.parseInt(args[0]));

		long endTimeSimulation = System.nanoTime();

		String totalDuration = SimulationUtils.calcDuration(startTimeSimulation, endTimeSimulation);

		System.out.println(args[1] + "\n" + SimulationUtils.convertTimeStamp() + "\n"
				+ totalDuration + "\n" + resultSimulacion);
	}

	/**
	 * @return total duration time for the simulation
	 */
	public String runSimulationMP() {
		int proteinToCalculate = 1;
		List<Process> processes = new ArrayList<>();

		try {
			long startTimeSimulation = System.nanoTime();
			for (int protein : proteinList) {
				for (int i = 0; i < protein; i++) {
					processNumStart++;
					try {
						Process p = simulacionMP(proteinToCalculate);
						processes.add(p);
					} catch (IOException er) {
					}
				}
				proteinToCalculate++;
			}

			for (Process p : processes) {
				try {
					p.waitFor();
				} catch (InterruptedException ex) {
					// ex.printStackTrace();
				}
			}

			long endTimeSimulation = System.nanoTime();
			setTotalDurationSimulationMP(SimulationUtils.calcDuration(startTimeSimulation, endTimeSimulation));

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return getTotalDurationSimulationMP();
	}

	/**
	 * @param proteinType will be the type of protein that will be processed
	 * @return p for the process created in the method
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public Process simulacionMP(int proteinType) throws InterruptedException, IOException {
		File simulationDirectory = new File("src");
		String startTimeStamp = SimulationUtils.convertTimeStamp();
		String fileName = "PROT_MP_" + proteinType + "n" + this.processNumStart + "_" + startTimeStamp + ".sim";
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
}
