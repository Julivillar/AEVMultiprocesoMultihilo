package es.florida.AEV1Simulacion;

public class SimulacionMP2 {

	public static void main(String[] args) {
		long startTimeSimulation = System.nanoTime();

		double resultSimulacion = SimulationUtils.simulation(Integer.parseInt(args[0]));

		long endTimeSimulation = System.nanoTime();

		String totalDuration = SimulationUtils.calcDuration(startTimeSimulation, endTimeSimulation);

		System.out.println(args[1] + "\n" + SimulationUtils.convertTimeStamp() + "\n"
				+ totalDuration + "\n" + resultSimulacion);
	}
}
