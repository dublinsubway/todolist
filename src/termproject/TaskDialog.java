package termproject;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import net.miginfocom.swing.MigLayout;

public class TaskDialog extends JDialog {
	
	private JLabel descriptionLabel = new JLabel("Description:");
	private JTextField descriptionTextField = new JTextField(15);
	private JLabel dateSpinnerLabel = new JLabel("Deadline date:");
	private JLabel timeSpinnerLabel = new JLabel("Deadline time:");
	private Calendar calendar = Calendar.getInstance();
	private JButton okButton = new JButton("Add");
	private JButton cancelButton = new JButton("Cancel");
	private MainWindow parent;
	
	public TaskDialog(Frame owner, String title) {
		super(owner, title);
		parent = (MainWindow) this.getParent();
		this.setLocationRelativeTo(parent);
		this.setLayout(new MigLayout("insets 20"));
		// label + text field
		this.add(descriptionLabel);
		this.add(descriptionTextField, "wrap");
		// label + date spinner
		this.add(dateSpinnerLabel);
		Date initDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 10);
        Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        this.add(dateSpinner, "wrap");
        // label + time spinner
        this.add(timeSpinnerLabel);
        SpinnerModel timeModel = new SpinnerDateModel(initDate, null, null, Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(timeModel);
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        this.add(timeSpinner, "wrap");
        // ok and cancel buttons
        this.add(okButton, "tag ok");
        this.add(cancelButton, "tag cancel, split 2");
        okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean valid = true;
				if (descriptionTextField.getText().equals("")) {
					// if text field is empty
					JOptionPane.showMessageDialog(TaskDialog.this,
							"Task name is empty. Enter the name of the task.",
							"Invalid input",
							JOptionPane.PLAIN_MESSAGE);
				} // empty field if end
				else {
					try {
						dateSpinner.commitEdit();
						timeSpinner.commitEdit();
					} catch (java.text.ParseException ex) {
						valid = false;
					} // catch end
					
					// get the time stored in a string
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
					String time = timeFormat.format(timeSpinner.getValue());
					// get hours and minutes seprartely
					int hours = Integer.parseInt(time.substring(0, 2));
					int minutes = Integer.parseInt(time.substring(3));
					// get the date itself that is given
					Date legacyDate = (Date) dateSpinner.getValue();
					// create an objects with date and add time that user entered
					LocalDateTime date = LocalDateTime.ofInstant(legacyDate.toInstant(), ZoneId.systemDefault());
					date = date.with(LocalTime.of(hours, minutes));
					
					// to not allow user to enter time/date that is in the past/now
					if (date.isBefore(LocalDateTime.now()) || date.equals(LocalDateTime.now())) {
						valid = false;
						JOptionPane.showMessageDialog(TaskDialog.this,
								"Task due date is earlier than current date.",
								"Invalid input",
								JOptionPane.PLAIN_MESSAGE);
					} // end before if 
					
					if (valid) {
						//tableModel.insertRow(new Task(descriptionTextField.getText(), date));
						System.out.println("here we go, date is " + date);
						TaskDialog.this.dispose(); // closes the dialog
					}
					
					
				} // else end
				
			} // actionPerformed end
        });
        
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TaskDialog.this.dispose(); // closes the dialog
			}
        });
        // display size
        this.pack();
	}

}
