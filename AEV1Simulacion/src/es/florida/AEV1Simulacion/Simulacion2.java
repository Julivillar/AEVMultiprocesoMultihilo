package es.florida.AEV1Simulacion;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.TextArea;
import javax.swing.SpinnerNumberModel;

public class Simulacion2 {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Simulacion window = new Simulacion();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Simulacion2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 980, 546);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Select an amount for each protein");
		lblNewLabel.setBounds(107, 26, 220, 30);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Primary");
		lblNewLabel_1.setBounds(107, 67, 92, 30);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Secondary");
		lblNewLabel_1_1.setBounds(289, 67, 92, 30);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Tertiary");
		lblNewLabel_1_2.setBounds(486, 67, 92, 30);
		frame.getContentPane().add(lblNewLabel_1_2);

		JLabel lblNewLabel_1_3 = new JLabel("Quaternary");
		lblNewLabel_1_3.setBounds(729, 67, 92, 30);
		frame.getContentPane().add(lblNewLabel_1_3);

		JSpinner primaryProteinAmountField = new JSpinner();
		primaryProteinAmountField.setModel(new SpinnerNumberModel(1, 0, 50, 1));
		primaryProteinAmountField.setToolTipText("input a number");
		primaryProteinAmountField.setBounds(107, 124, 86, 20);
		frame.getContentPane().add(primaryProteinAmountField);

		JSpinner secondaryProteinAmountField = new JSpinner();
		secondaryProteinAmountField.setModel(new SpinnerNumberModel(1, 0, 50, 1));
		secondaryProteinAmountField.setToolTipText("input a number");
		secondaryProteinAmountField.setBounds(289, 124, 92, 20);
		frame.getContentPane().add(secondaryProteinAmountField);

		JSpinner tertiaryProteinAmountField = new JSpinner();
		tertiaryProteinAmountField.setModel(new SpinnerNumberModel(1, 0, 50, 1));
		tertiaryProteinAmountField.setToolTipText("input a number");
		tertiaryProteinAmountField.setBounds(486, 124, 92, 20);
		frame.getContentPane().add(tertiaryProteinAmountField);

		JSpinner quaternaryProteinAmountField = new JSpinner();
		quaternaryProteinAmountField.setModel(new SpinnerNumberModel(1, 0, 50, 1));
		quaternaryProteinAmountField.setToolTipText("input a number");
		quaternaryProteinAmountField.setBounds(729, 124, 92, 20);
		frame.getContentPane().add(quaternaryProteinAmountField);

		TextArea resultTextArea = new TextArea();
		resultTextArea.setEditable(false);
		resultTextArea.setBounds(107, 267, 714, 216);
		frame.getContentPane().add(resultTextArea);

		JButton simulateBtn = new JButton("Simulate");
		simulateBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int primaryProtein = (int) primaryProteinAmountField.getValue();
				int secondaryProtein = (int) secondaryProteinAmountField.getValue();
				int tertiaryProtein = (int) tertiaryProteinAmountField.getValue();
				int quaternaryProtein = (int) quaternaryProteinAmountField.getValue();

				int[] proteinList = { primaryProtein, secondaryProtein, tertiaryProtein, quaternaryProtein };
				String totalDurationSimulationMP = "";
				String totalDurationSimulationMT = "";
				int proteinToCalculate = 1;
				int processCounter = 0;
				List<Thread> threads = new ArrayList<>();
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
					totalDurationSimulationMP = SimulationUtils.calcDuration(startTimeSimulation,
							endTimeSimulation);
					// reset of proteins to calculate
					proteinToCalculate = 1;
					processCounter = 0;

					startTimeSimulation = System.nanoTime();
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
							//ex.printStackTrace();
						}
					}

					endTimeSimulation = System.nanoTime();
					totalDurationSimulationMT = SimulationUtils.calcDuration(startTimeSimulation,
							endTimeSimulation);

					resultTextArea.setText(
							"For proteins: \nPrimary: " + primaryProtein + " Secondary: "
									+ secondaryProtein + " Tertiary: " + tertiaryProtein
									+ " Quaternary: " + quaternaryProtein + "\n"
									+ "Multiprocess duration: " + totalDurationSimulationMP
									+ "\nMulti thread duration: " + totalDurationSimulationMT);

				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		});
		simulateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		simulateBtn.setBounds(327, 182, 204, 50);
		frame.getContentPane().add(simulateBtn);

	}

	public static Process simulacionMP(int proteinType, int processCounter) throws InterruptedException, IOException {
		File simulationDirectory = new File("src");

		// PROT_[MP]_[proteinToCalculate]_n[process]_[date-time-min-sec-cent].sim
		String startTimeStamp = SimulationUtils.convertTimeStamp();
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
}
