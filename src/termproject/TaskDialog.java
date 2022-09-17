package termproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import net.miginfocom.swing.MigLayout;

public class TaskDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel descriptionLabel = new JLabel("Description:");
	private JTextField descriptionTextField = new JTextField(15);
	private JLabel dateSpinnerLabel = new JLabel("Deadline date:");
	private JLabel timeSpinnerLabel = new JLabel("Deadline time:");
	private Calendar calendar = Calendar.getInstance();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private MainWindow parent;
	private JSpinner dateSpinner;
	private JSpinner timeSpinner;
	private JLabel errorLabel = new JLabel("error");
	private int position = -2;
	
	
	// this constructor is called when new task is added
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
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        this.add(dateSpinner, "wrap");
        // label + time spinner
        this.add(timeSpinnerLabel);
        SpinnerModel timeModel = new SpinnerDateModel(initDate, null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        this.add(timeSpinner, "wrap");
        // error label
        this.add(errorLabel, "skip 1, split 2, wrap");
        Font currentFont = errorLabel.getFont();
        errorLabel.setFont(new Font(currentFont.getName(), Font.PLAIN, 11));
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        // ok and cancel buttons
        this.add(okButton, "tag ok");
        this.add(cancelButton, "tag cancel, split 2");
        okButton.addActionListener(new ActionListener() {
        	@Override
    		public void actionPerformed(ActionEvent e) {
    			boolean valid = true;
    			if (descriptionTextField.getText().equals("")) {
    				// if text field is empty
    				TaskDialog.this.errorLabel.setText("Task name cannot be empty.");
    				TaskDialog.this.errorLabel.setVisible(true);
    				valid = false;
    			} // end of empty field if
    			else {
    				try { // adjust data into date format if entered manually
    					dateSpinner.commitEdit();
    					timeSpinner.commitEdit();
    				} catch (java.text.ParseException ex) {
    					valid = false;
    				} // catch end
    				
    				// get the time stored in a string
    				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    				String time = timeFormat.format(timeSpinner.getValue());
    				// get hours and minutes separately
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
    					TaskDialog.this.errorLabel.setText("Due date is too early.");
        				TaskDialog.this.errorLabel.setVisible(true);
    				} // end before if 
    				
    				String taskInfo = descriptionTextField.getText();
    				if (valid) {
    					if (position == -2) // if a new task is created (default value of position)
    						parent.insertRow(new Task(taskInfo, date));
    					else if (position >= 0) // if task is edited
    						parent.updateRow(new Task(taskInfo, date), position);
    					TaskDialog.this.dispose(); 
    					parent.sortByDate();
    				}
    				
    			} // else if field isnt empty end
    			
    		} // end actionPerformed
        });
        
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TaskDialog.this.dispose(); // closes the dialog
			}
        });
        // display size
        this.pack();
	} // end add constructor
	
	// this constructor is called when existing task is edited
	public TaskDialog(Frame owner, String title, int pos) {
		this(owner, title);
		this.position = pos;
		Task editedTask = parent.getATask(pos);
		descriptionTextField.setText(editedTask.getDescription());
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		String stringDateTime = editedTask.getDueDate().format(dateTimeFormat);
		String stringDate = stringDateTime.substring(0, 10) + " 00:00";
		SimpleDateFormat dateClassDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
		Date date = calendar.getTime();
		try {
			date = dateClassDateFormat.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dateSpinner.setValue(date);
		
		SimpleDateFormat dateClassTimeFormat = new SimpleDateFormat("HH:mm");  
		String stringTime = stringDateTime.substring(11);
		try {
			date = dateClassTimeFormat.parse(stringTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timeSpinner.setValue(date);
		
	} // end edit constructor
	
	
} // end taskdialog
