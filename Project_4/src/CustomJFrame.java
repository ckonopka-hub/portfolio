import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

public class CustomJFrame extends JFrame
{
	private int FRAME_WIDTH = 500;
	private int FRAME_HEIGHT = 700;
	private JLabel headingLabel;
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel phoneNumberLabel;
	private JLabel emailLabel;
	private JLabel dietaryLabel;
	private JLabel genderLabel;
	private JLabel waterLabel;
	private JLabel mealsLabel;
	private JLabel checkBoxLabel;
	private JLabel walkLabel;
	private JLabel weightLabel;
	
	private static JTextField firstNameTextField;
	private static JTextField lastNameTextField;
	private static JTextField phoneNumberTextField;
	private static JTextField emailTextField;
	
	private JRadioButton maleRadioButton;
	private JRadioButton femaleRadioButton;
	private JRadioButton preferRadioButton;
	
	private ButtonGroup radioButtonGroup;
	
	private JSpinner waterIntakeSpinner;
	
	private JSlider mealSlider;
	
	private JCheckBox wheatCheckBox;
	private JCheckBox sugarCheckBox;
	private JCheckBox dairyCheckBox;
	
	private JComboBox walkComboBox;
	
	private String[] walkOptions;
	private JFormattedTextField weightFormattedTextField;
	private JButton clearButton;
	private JButton submitButton;
	private FileHandler fileHandler;
	
	class InnerActionListener implements ActionListener
	{
		
		/*
		 * - clears data of the form and if submit button was pressed,
		 * data needs to be captured and written to csv file
		 * 
		 * - submitted data needs to be written to csv file using
		 * FileHandler writeResults method
		 * 
		 * - submit and clear buttons both need to register this action
		 * listener
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String surveyResults = "";
			if(e.getActionCommand() == "Submit")
			{
				// capture data
				surveyResults = firstNameTextField.getText() + ",";
				surveyResults = surveyResults + lastNameTextField.getText() + ",";
				surveyResults = surveyResults + phoneNumberTextField.getText() + ",";
				surveyResults = surveyResults + emailTextField.getText() + ",";
				
				if(maleRadioButton.isSelected())
				{
					surveyResults = surveyResults + "Male" + ",";
				}
				else if(femaleRadioButton.isSelected())
				{
					surveyResults = surveyResults + "Female" + ",";
				}
				else
				{
					surveyResults = surveyResults + "Prefer not to say" + ",";
				}
				
				surveyResults = surveyResults + waterIntakeSpinner.getValue() + ",";
				surveyResults = surveyResults + mealSlider.getValue() + ",";
				surveyResults = surveyResults + walkComboBox.getSelectedItem() + ",";
				surveyResults = surveyResults + weightFormattedTextField.getValue();
				
			} // end if statement
			
			// send data to file
			fileHandler.writeResults(surveyResults);
			
			// clear data
			clearForm();
			
			
			
		}
		/*
		 * resets all fields to their default values
		 */
		private void clearForm()
		{
			firstNameTextField.setText("");
			lastNameTextField.setText("");
			phoneNumberTextField.setText("");
			emailTextField.setText("");
			
			maleRadioButton.setSelected(true);
			waterIntakeSpinner.setValue(15);
			mealSlider.setValue(3);
			dairyCheckBox.setSelected(false);
			wheatCheckBox.setSelected(false);
			sugarCheckBox.setSelected(false);
			
			walkComboBox.setSelectedItem(walkOptions[0]);
			weightFormattedTextField.setText("");
		}
		
	}// end InnerActionListener class
	
	/*
	 * Initializes all class variable components, configures them, and
	 * adds them to the frame using a layout manager (GridBagLayout 
	 * recommended)
	 */
	public CustomJFrame()
	{
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		// panels
			JPanel panel = new JPanel();
			panel.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			
		// file handler
		fileHandler = new FileHandler();
				
	// initializing all components
				
			// title1Panel component
			headingLabel = new JLabel("Personal Information");
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			c.ipady = 10;
			panel.add(headingLabel,c);
				
			// personal information components
			firstNameLabel = new JLabel("First Name: ");
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0;
			c.gridy = 1;
			panel.add(firstNameLabel,c);
			
			lastNameLabel = new JLabel("Last Name: ");
			c.gridx = 0;
			c.gridy = 2;
			panel.add(lastNameLabel,c);
			
			phoneNumberLabel = new JLabel("Phone Number: ");
			c.gridx = 0;
			c.gridy = 3;
			panel.add(phoneNumberLabel,c);
			
			emailLabel = new JLabel("Email: ");
			c.gridx = 0;
			c.gridy = 4;
			panel.add(emailLabel,c);
				
			// personInfoTextFields components
			final int FIELD_WIDTH = 10;
			firstNameTextField = new JTextField(FIELD_WIDTH);
			c.gridx = 1;
			c.gridy = 1;
			panel.add(firstNameTextField,c);
			
			lastNameTextField = new JTextField(FIELD_WIDTH);
			c.gridx = 1;
			c.gridy = 2;
			panel.add(lastNameTextField,c);
			
			phoneNumberTextField = new JTextField(FIELD_WIDTH);
			c.gridx = 1;
			c.gridy = 3;
			panel.add(phoneNumberTextField,c);
			
			emailTextField = new JTextField(FIELD_WIDTH);
			c.gridx = 1;
			c.gridy = 4;
			panel.add(emailTextField,c);
			
				
			// genderLabel components
			genderLabel = new JLabel("Sex: ");
			c.gridx = 0;
			c.gridy = 5;
			panel.add(genderLabel,c);
				
			// genderRadioButtons components
			maleRadioButton = new JRadioButton("Male");
			maleRadioButton.setSelected(true);
			c.gridx = 1;
			c.gridy = 5;
			panel.add(maleRadioButton,c);
			
			femaleRadioButton = new JRadioButton("Female");
			c.gridx = 1;
			c.gridy = 6;
			panel.add(femaleRadioButton,c);
			
			preferRadioButton = new JRadioButton("Prefer not to say");
			c.gridx = 1;
			c.gridy = 7;
			panel.add(preferRadioButton,c);
			
			// add radio buttons to button group
			ButtonGroup group = new ButtonGroup();
			group.add(maleRadioButton);
			group.add(femaleRadioButton);
			group.add(preferRadioButton);
				
			// title2Panel component
			dietaryLabel = new JLabel("Dietary Questions");
			c.gridx = 0;
			c.gridy = 8;
			panel.add(dietaryLabel,c);
				
			// bigGrid components
			waterLabel = new JLabel("How many cups of water on average do you drink a day?");
			c.fill = GridBagConstraints.NONE;
			c.gridwidth = 3;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0;
			c.gridy = 9;
			panel.add(waterLabel,c);
			
			waterIntakeSpinner = new JSpinner(new SpinnerNumberModel(15,0,50,1));
			c.gridx = 0;
			c.gridy = 10;
			panel.add(waterIntakeSpinner,c);
			
			mealsLabel = new JLabel("How many meals on average do you eat a day?");
			c.gridx = 0;
			c.gridy = 11;
			panel.add(mealsLabel,c);
			
			mealSlider = new JSlider(JSlider.HORIZONTAL,0,10,3);
			mealSlider.setMajorTickSpacing(100);
			mealSlider.setMinorTickSpacing(10);
			
			mealSlider.setPaintLabels(true);
			
			c.gridx = 0;
			c.gridy = 12;
			panel.add(mealSlider,c);
			
			
			checkBoxLabel = new JLabel("Do any of these meals regularly contain: ");
			c.gridx = 0;
			c.gridy = 13;
			panel.add(checkBoxLabel,c);
			
			JPanel checkBoxPanel = new JPanel();
			dairyCheckBox = new JCheckBox("Dairy");
			checkBoxPanel.add(dairyCheckBox);
			
			wheatCheckBox = new JCheckBox("Wheat");
			checkBoxPanel.add(wheatCheckBox);
			
			sugarCheckBox = new JCheckBox("Sugar");
			c.anchor = GridBagConstraints.CENTER;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 14;
			checkBoxPanel.add(sugarCheckBox);
			panel.add(checkBoxPanel,c);
			
			
			
			walkLabel = new JLabel("On average how many miles do you walk in a day?");
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 15;
			panel.add(walkLabel,c);
			
			// put strings in combo box array
			walkOptions = new String[4];
			walkOptions[0] = "Less than 1 Mile";
			walkOptions[1] = "More than 1 mile but less than 2 miles";
			walkOptions[2] = "More than 2 miles but less than 3 miles";
			walkOptions[3] = "More than 3 miles";
			
			walkComboBox = new JComboBox();
			walkComboBox.addItem(walkOptions[0]);
			walkComboBox.addItem(walkOptions[1]);
			walkComboBox.addItem(walkOptions[2]);
			walkComboBox.addItem(walkOptions[3]);
			walkComboBox.setEditable(false);
			c.gridx = 0;
			c.gridy = 16;
			panel.add(walkComboBox,c);
			
			weightLabel = new JLabel("How much do you weigh?");
			c.gridx = 0;
			c.gridy = 17;
			panel.add(weightLabel,c);
			
			weightFormattedTextField = new JFormattedTextField(new DecimalFormat("###,###"));
			weightFormattedTextField.setColumns(FIELD_WIDTH);
			weightFormattedTextField.setMinimumSize(getPreferredSize());
			weightFormattedTextField.setValue(0);
			c.gridx = 0;
			c.gridy = 18;
			panel.add(weightFormattedTextField,c);
			
			// create listener
			ActionListener listener = new InnerActionListener();
				
			// clearAndSubmitPanel components
			clearButton = new JButton("Clear");
			clearButton.addActionListener(listener);
			c.anchor = GridBagConstraints.LAST_LINE_START;
			c.gridx = 0;
			c.gridy = 19;
			panel.add(clearButton,c);
			
			submitButton = new JButton("Submit");
			submitButton.addActionListener(listener);
			c.anchor = GridBagConstraints.LAST_LINE_END;
			c.gridx = 1;
			c.gridy = 19;
			panel.add(submitButton,c);
				
			// add panel to frame
			add(panel);
		
		
	}
	
	
} // end CustomJFrame class
