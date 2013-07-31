import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class superUserScreen extends JFrame
{
    private JTabbedPane tabbedPane = new JTabbedPane();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu gradeMenu = new JMenu("Grade");
    private JMenu optionsMenu = new JMenu("Options");
    private JMenu manageTabs = new JMenu("Manage Tabs");
    private JMenu exitMenu = new JMenu("Exit");

    private JButton enableBut;

    private JComboBox subjectID;
    private JComboBox subjectID2;
    private JComboBox departmentBox;

    private JMenuItem allotGrade = new JMenuItem("Grade Allotment");
    private JMenuItem addSubject = new JMenuItem("Add new Subject");
    private JMenuItem register = new JMenuItem("Enable Registration");
    private JMenuItem disable = new JMenuItem("Disable Registration");
    private JMenuItem status = new JMenuItem("Registration Status");
    private JMenuItem newSuperUser = new JMenuItem("Add SuperUser");
    private JMenuItem removeAllTabs = new JMenuItem("Remove All Tabs");
    private JMenuItem logout = new JMenuItem("Logout");
    private JMenuItem exit = new JMenuItem("Exit");

    private JPanel gradePanel = new JPanel();
    private JPanel registerPanel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private GridBagLayout layout = new GridBagLayout();
    private JPanel newUserPanel = new JPanel(layout);
    private JPanel subjectPanel = new JPanel(layout);
    private GridBagConstraints constraints = new GridBagConstraints();

    private JTable tableForGrade;
    private JTable registerTable;
    private JTable statusTable;

    private accessDB gradeAllotment;
    private accessDB registerDB;
    private accessDB statusDB;

    private String currentID;
    private final String departmentName[] = {"Mathematics", "Information Technology", "Computer Science", "Electrical", "Chemical", "Mechanical", "Metallurgical", "Industrial", "Mining"};

    private JLabel subIDLabel;
    private JLabel subLabel;
    private JLabel creditsLabel;
    private JLabel teacherLabel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JLabel confirmLabel;
    private JLabel departmentLabel;

    private JTextField subID;
    private JTextField sub;
    private JTextField credits;
    private JTextField teacher;
    private JTextField userField;
    private JPasswordField passField;
    private JPasswordField confirmField;

    private JButton update;
    private JButton submit;

    public superUserScreen()
    {
	super("Super User Window");

	gradeAllotment = new accessDB();
	registerDB = new accessDB();
	statusDB = new accessDB();

	gradeAllotment.setSuperUser(true);

	try
	    {
		registerDB.getConnection();
	    }
	catch(SQLException s)
	    {
		s.printStackTrace();
	    }

	setJMenuBar(menuBar);

	gradeMenu.add(allotGrade);

	optionsMenu.add(newSuperUser);
	optionsMenu.add(addSubject);
	optionsMenu.add(register);
	optionsMenu.add(status);
	optionsMenu.add(disable);

	manageTabs.add(removeAllTabs);

	exitMenu.add(logout);
	exitMenu.add(exit);

	if(!registerDB.getIsRegEnabled()) {
	    disable.setEnabled(false);
            register.setEnabled(true);
        }else {
	    disable.setEnabled(true);
            register.setEnabled(false);
        }

	newSuperUser.addActionListener(
				       new ActionListener()
				       {
					   public void actionPerformed(ActionEvent e)
					   {
					       try
						   {
						       registerDB.getConnection();

						       tabbedPane.addTab("Add SuperUser", null, newUserPanel, "Add new superUser");

						       userLabel = new JLabel("UserName: ");
						       passLabel = new JLabel("Password: ");
						       confirmLabel = new JLabel("Confirm Password: ");
						       departmentLabel = new JLabel("Department: ");

						       userField = new JTextField(20);
						       passField = new JPasswordField(20);
						       confirmField = new JPasswordField(20);
						       departmentBox = new JComboBox(departmentName);

						       submit = new JButton("Submit");

						       addComponent(userLabel, 0, 0, 1, 1, 1);
						       addComponent(userField, 0, 1, 1, 1, 1);
						       addComponent(passLabel, 1, 0, 1, 1, 1);
						       addComponent(passField, 1, 1, 1, 1, 1);
						       addComponent(confirmLabel, 2, 0, 1, 1, 1);
						       addComponent(confirmField, 2, 1, 1, 1, 1);
						       addComponent(departmentLabel, 3, 0, 1, 1, 1);
						       addComponent(departmentBox, 3, 1, 1, 1, 1);
						       addComponent(submit, 4, 0, 2, 1, 1);

						       submit.addActionListener(
										new ActionListener()
										{
										    public void actionPerformed(ActionEvent e)
										    {								       
											String user = userField.getText();
											String pass = passField.getText();
											String confirm = confirmField.getText();
											String dep = (String)departmentBox.getSelectedItem();
											if(user.length()!=0&&pass.length()!=0&&dep.length()!=0)
											    {
												if(pass.compareTo(confirm)==0)
												    {
													try
													    {
														registerDB.superUserEntry(user, pass, dep);
														JOptionPane.showMessageDialog(superUserScreen.this, "Registration Successfull", "Congrats", JOptionPane.INFORMATION_MESSAGE);
														submit.setEnabled(false);
													    }
													catch(SQLException ex)
													    {
														JOptionPane.showMessageDialog(superUserScreen.this, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
													    }
												    }
												else
												    JOptionPane.showMessageDialog(superUserScreen.this, "Password do not match", "ERROR", JOptionPane.ERROR_MESSAGE);
											    }
											else
											    JOptionPane.showMessageDialog(superUserScreen.this, "Vacant Field(s)", "Error", JOptionPane.ERROR_MESSAGE);
										    }
										}
										);
							   
						   }
					       catch(Exception ex)
						   {
						       JOptionPane.showMessageDialog(superUserScreen.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						   }
					   }
				       }
				       );

	disable.addActionListener(
				  new ActionListener()
				  {
				      public void actionPerformed(ActionEvent e)
				      {
					  try
					      {
						  registerDB.deleteTable();
						  registerDB.setIsRegEnabled(false);
						  disable.setEnabled(false);
                                                  register.setEnabled(true);
					      }
					  catch(Exception ex)
					      {
						  JOptionPane.showMessageDialog(superUserScreen.this, ex.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
					      }
				      }
				  }
				  );

	allotGrade.addActionListener(
				     new ActionListener()
				     {
					 public void actionPerformed(ActionEvent e)
					 {
					     try
						 {
						     gradeAllotment.getConnection();
						     gradeAllotment.setIsTableEditable(true);

						     gradeAllotment.superGrade("");
						     subjectID = new JComboBox(gradeAllotment.getSubjectIDArray());
						     tableForGrade = new JTable(gradeAllotment);

                                                     gradePanel.removeAll();
                                                     String title = "Grade Allotment";
						     tabbedPane.addTab(title, gradePanel);
                                                     int index = tabbedPane.indexOfTab(title);
                                                     tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
                                                     tabbedPane.setSelectedIndex(index);

						     subjectID.addItemListener(
									       new ItemListener()
									       {
										   public void itemStateChanged(ItemEvent e)
										   {
										       currentID = (String)subjectID.getSelectedItem();
										       try
											   {
											       gradeAllotment.superGrade(currentID);
											   }
										       catch(Exception ex)
											   {
											       JOptionPane.showMessageDialog(superUserScreen.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
											   }
										   }
									       }
									       );

						     gradePanel.add(subjectID, BorderLayout.NORTH);
						     gradePanel.add(new JScrollPane(tableForGrade), BorderLayout.CENTER);
						 }
					     catch(SQLException s)
						 {
						     JOptionPane.showMessageDialog(superUserScreen.this, s.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
						 }
					 }
				     }
				     );

	addSubject.addActionListener(
				     new ActionListener()
				     {
					 public void actionPerformed(ActionEvent e)
					 {
                                             subjectPanel.removeAll();
                                             String title = "Add new Subject";
					     tabbedPane.addTab(title, subjectPanel);
                                             int index = tabbedPane.indexOfTab(title);
                                             tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
                                             tabbedPane.setSelectedIndex(index);

					     subIDLabel = new JLabel("SubjectID");
					     subLabel = new JLabel("Subject");
					     creditsLabel = new JLabel("Credits");
					     teacherLabel = new JLabel("Teacher");

					     subID = new JTextField(10);
					     sub = new JTextField(15);
					     credits = new JTextField(4);
					     teacher = new JTextField(10);

					     update = new JButton("UPDATE");

					     addComponent(subIDLabel, 0, 0, 1, 1, 0);
					     addComponent(subLabel, 0, 1, 1, 1, 0);
					     addComponent(creditsLabel, 0, 2, 1, 1, 0);
					     addComponent(teacherLabel, 0, 3, 1, 1, 0);

					     addComponent(subID, 1, 0, 1, 1, 0);
					     addComponent(sub, 1, 1, 1, 1, 0);
					     addComponent(credits, 1, 2, 1, 1, 0);
					     addComponent(teacher, 1, 3, 1, 1, 0);

					     addComponent(update, 2, 1, 2, 1, 0);

					     update.addActionListener(
								      new ActionListener()
								      {
									  public void actionPerformed(ActionEvent a)
									  {
									      try
										  {
										      gradeAllotment.getConnection();

										      gradeAllotment.addNewSubject(subID.getText(), sub.getText(), Integer.parseInt(credits.getText()), teacher.getText());
										      subID.setText("");
										      sub.setText("");
										      credits.setText("");
										      teacher.setText("");
                                                                                      JOptionPane.showMessageDialog(superUserScreen.this, "Subject Added", "Successful", JOptionPane.INFORMATION_MESSAGE);
										  }
									      catch(Exception ex)
										  {
										      JOptionPane.showMessageDialog(superUserScreen.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
										  }
									  }
								      }
								      );
					 }
				     }
				     );

	register.addActionListener(
				   new ActionListener()
				   {
				       public void actionPerformed(ActionEvent e)
				       {
					   try
					       {
						   registerDB.getConnection();
						   subjectID2 = new JComboBox(registerDB.getSubjectIDArray());
                                                   enableBut = new JButton("Enable");
						   subjectID2.setEnabled(true);
						   disable.setEnabled(true);

                                                   registerPanel.removeAll();
                                                   String title = "Enable Registration";
						   tabbedPane.addTab(title, registerPanel);
                                                   int index = tabbedPane.indexOfTab(title);
                                                   tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
                                                   tabbedPane.setSelectedIndex(index);  

						   registerTable = new JTable(registerDB);
						   registerDB.addTable();
						   registerDB.emptyStatusTable();
						   registerDB.displayRegisterTable();
						   registerDB.newSemesterTable();

						   subjectID2.addItemListener(
									      new ItemListener()
									      {
										  public void itemStateChanged(ItemEvent i)
										  {
										      try
											  {
											      registerDB.addToRegisterTable((String)i.getItem());
											  }
										      catch(Exception e)
											  {
											      JOptionPane.showMessageDialog(superUserScreen.this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
											  }
										  }
									      }
									      );
                                                   enableBut.addActionListener(
                                                                               new ActionListener()
                                                                               {
                                                                                    public void actionPerformed(ActionEvent a)
                                                                                    {
                                                                                         registerDB.setIsRegEnabled(true);
                                                                                         enableBut.setEnabled(false);
                                                                                         subjectID2.setEnabled(false);
                                                                                    }
                                                                               }
                                                                               );

                                                   registerPanel.setLayout(new BorderLayout());
                                                   JScrollPane spane = new JScrollPane();
                                                   registerPanel.add(spane);
						   registerPanel.add(subjectID2, BorderLayout.NORTH);
						   registerPanel.add(new JScrollPane(registerTable), BorderLayout.CENTER);
                                                   registerPanel.add(enableBut, BorderLayout.SOUTH);

                                                   register.setEnabled(false);
					       }
					   catch(Exception s)
					       {
						   JOptionPane.showMessageDialog(superUserScreen.this, s.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					       }

				       }
				   }
				   );

	status.addActionListener(
				 new ActionListener()
				 {
				     public void actionPerformed(ActionEvent e)
				     {
					 try
					     {
						 statusDB.getConnection();

                                                 statusPanel.removeAll();
                                                 String title = "Registration Status";
						 tabbedPane.addTab(title, statusPanel);
                                                 int index = tabbedPane.indexOfTab(title);
                                                 tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
                                                 tabbedPane.setSelectedIndex(index);

						 statusTable = new JTable(statusDB);
						 statusDB.getStatusTable();
						 statusPanel.add(new JScrollPane(statusTable), BorderLayout.CENTER);
					     }
					 catch(Exception s)
					     {
						 JOptionPane.showMessageDialog(superUserScreen.this, s.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					     }
				     }
				 }
				 );
				
	removeAllTabs.addActionListener(
					new ActionListener()
					{
					    public void actionPerformed(ActionEvent e)
					    {
						tabbedPane.removeAll();
					    }
					}
					);					  
			
	logout.addActionListener(
				 new ActionListener()
				 {
				     public void actionPerformed(ActionEvent e)
				     {
					 new login();
					 dispose();
				     }
				 }
				 );

	exit.addActionListener(
			       new ActionListener()
			       {
				   public void actionPerformed(ActionEvent e)
				   {
				       dispose();
				   }
			       }
			       );

	menuBar.add(gradeMenu);
	menuBar.add(optionsMenu);
	menuBar.add(manageTabs);
	menuBar.add(exitMenu);

	add(tabbedPane);

	setSize(500, 500);
	setVisible(true);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	addWindowListener(
			  new WindowAdapter()
			  {
			      public void windowClosed(WindowEvent w)
			      {
				  gradeAllotment.disconnectFromDatabase();
				  registerDB.disconnectFromDatabase();
				  statusDB.disconnectFromDatabase();
			      }
			  }
			  );
    }

    public void setSuper()
    {
	accessDB tempDB = new accessDB();
	tempDB.setSuperUser(true);
    }

    public void addComponent(Component component, int row, int column, int width, int height, int identifier)
    {
	constraints.gridx = column;
	constraints.gridy = row;
	constraints.gridwidth = width;
	constraints.gridheight = height;
	layout.setConstraints(component, constraints);
	if(identifier == 0)
	    subjectPanel.add(component);
	else if(identifier == 1)
	    newUserPanel.add(component);
    }
}
