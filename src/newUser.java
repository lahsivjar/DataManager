import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class newUser extends JFrame
{
    private final JLabel welcome;
    private final JPasswordField passField;
    private final JLabel userLabel;
    private final JLabel passLabel;
    private final JLabel nameLabel;
    private final JLabel addressLabel;
    private final JLabel dobLabel;
    private final JLabel yearOfAdmissionLabel;
    private final JLabel departmentLabel;
    private final JButton submit;
    private final JTextField userField;
    private final JTextField addressField;
    private final JTextField nameField;
    private final JTextField dobField;
    private final JComboBox yearOfAdmissionBox;
    private final JComboBox departmentBox;
    private final String departmentName[] = {"Mathematics", "Information Technology", "Computer Science", "Electrical", "Chemical", "Mechanical", "Metallurgical", "Industrial", "Mining"};
    private final String year[] = {"2008", "2009", "2010", "2011", "2012"};
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JPanel panelNorth;
    private accessDB entry;

    public newUser()
    {
	super("New User Registration");

	entry = new accessDB();

	panelCenter = new JPanel(new GridLayout(7, 2, 5, 5));
	panelSouth = new JPanel();
	panelNorth = new JPanel(new GridLayout(2, 1));

	welcome = new JLabel("Welcome to ERP System", JLabel.CENTER);
	panelNorth.add(welcome);

	userLabel = new JLabel("User Name(Roll Number): ");
	userField = new JTextField();
	panelCenter.add(userLabel);
	panelCenter.add(userField);

	passLabel = new JLabel("Password: ");
	passField = new JPasswordField();
	panelCenter.add(passLabel);
	panelCenter.add(passField);

	nameLabel = new JLabel("Name: ");
	nameField = new JTextField();
	panelCenter.add(nameLabel);
	panelCenter.add(nameField);

	dobLabel = new JLabel("Date Of Birth(dd/mm/yy): ");
	dobField = new JTextField();
	panelCenter.add(dobLabel);
	panelCenter.add(dobField);

	yearOfAdmissionLabel = new JLabel("Year Of Admission: ");
	yearOfAdmissionBox = new JComboBox(year);
	panelCenter.add(yearOfAdmissionLabel);
	panelCenter.add(yearOfAdmissionBox);

	departmentLabel = new JLabel("Department: ");
	departmentBox = new JComboBox(departmentName);
	departmentBox.setMaximumRowCount(10);
	panelCenter.add(departmentLabel);
	panelCenter.add(departmentBox);

	addressLabel = new JLabel("Address: ");
	addressField = new JTextField();
	panelCenter.add(addressLabel);
	panelCenter.add(addressField);

	panelCenter.setBorder(new LineBorder(Color.BLACK));

	submit = new JButton("Submit");

	submit.addActionListener(
				 new ActionListener()
				 {
				     public void actionPerformed(ActionEvent e)
				     {
					 try
					     {
						 entry.getConnection();
						 String user = userField.getText();
						 String pass = passField.getText();
						 String name = nameField.getText();
						 String dob = dobField.getText();
						 String year = (String)yearOfAdmissionBox.getSelectedItem();
						 String dep = (String)departmentBox.getSelectedItem();
						 String add = addressField.getText();

						 if(user.length()!=0&&pass.length()!=0&&name.length()!=0&&dob.length()!=0&&add.length()!=0)
						     {
							 entry.userEntry(user, pass, name, dob, year, dep, add);

							 dispose();
						     }
						 else
						     JOptionPane.showMessageDialog(newUser.this, "Vacant Field(s)", "ERROR", JOptionPane.ERROR_MESSAGE);
					     }
					 catch(SQLException s)
					     {
						 JOptionPane.showMessageDialog(newUser.this, s.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
					     }
					 catch(IllegalStateException i)
					     {
						 JOptionPane.showMessageDialog(newUser.this, i.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
					     }
				     }
				 }
				 );
	panelSouth.add(submit);

	add(panelSouth, BorderLayout.SOUTH);
	add(panelCenter, BorderLayout.CENTER);
	add(panelNorth, BorderLayout.NORTH);

	setSize(400, 300);
	setVisible(true);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	addWindowListener(
			  new WindowAdapter()
			  {
			      public void windowClosed(WindowEvent w)
			      {
				  entry.disconnectFromDatabase();
				  new login();
			      }
			  }
			  );
    }
}