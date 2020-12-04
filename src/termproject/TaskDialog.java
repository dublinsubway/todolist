package termproject;

import java.awt.Frame;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import net.miginfocom.swing.MigLayout;

public class TaskDialog extends JDialog {
	
	private JLabel descriptionLabel = new JLabel("Description:");
	private JTextField descriptionTextField = new JTextField(100);
	private JLabel dateSpinnerLabel = new JLabel("Deadline date:");
	private JLabel timeSpinnerLabel = new JLabel("Deadline time:");
	private Calendar calendar = Calendar.getInstance();
	
	public TaskDialog(Frame owner, String title) {
		super(owner, title);
		this.setLayout(new MigLayout());
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
        this.add(timeSpinner);
        this.pack();
	}

}
