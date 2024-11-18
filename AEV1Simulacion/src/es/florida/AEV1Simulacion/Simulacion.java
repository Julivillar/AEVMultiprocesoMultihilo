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
import java.awt.TextArea;
import javax.swing.SpinnerNumberModel;

public class Simulacion {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulacion window = new Simulacion();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Simulacion() {
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

				int totalProteins = primaryProtein + secondaryProtein + tertiaryProtein + quaternaryProtein;

				SimulacionMP mp = new SimulacionMP(primaryProtein, secondaryProtein, tertiaryProtein,
						quaternaryProtein, 0);

				SimulacionMT mt = new SimulacionMT(primaryProtein, secondaryProtein, tertiaryProtein,
						quaternaryProtein, totalProteins);

				String resultMP = mp.runSimulationMP();
				String resultMT = mt.runSimulationMT();

				resultTextArea.setText(
						"For proteins: \nPrimary: " + primaryProtein + " Secondary: "
								+ secondaryProtein + " Tertiary: " + tertiaryProtein
								+ " Quaternary: " + quaternaryProtein + "\n"
								+ "Multiprocess duration: " + resultMP
								+ "\nMulti thread duration: " + resultMT);
			}
		});
		simulateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		simulateBtn.setBounds(327, 182, 204, 50);
		frame.getContentPane().add(simulateBtn);
	}
}
