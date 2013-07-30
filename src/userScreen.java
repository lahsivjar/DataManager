import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class userScreen extends JFrame
{
    private JTabbedPane tabbedPane = new JTabbedPane();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu registerMenu = new JMenu("Register");
    private JMenu viewInfoMenu = new JMenu("View Info");
    private JMenu manageTabs = new JMenu("Manage Tabs");
    private JMenu aboutMenu = new JMenu("About");
    private JMenu exitMenu = new JMenu("Exit");

    private JMenuItem register = new JMenuItem("Register for Current Semester");
    private JMenuItem grades = new JMenuItem("Grades");
    private JMenuItem profileInfo = new JMenuItem("Profile Information");
    private JMenuItem courses = new JMenuItem("My Courses");
    private JMenuItem removeAllTabs = new JMenuItem("Remove All Tabs");
    private JMenuItem about = new JMenuItem("About ERP");
    private JMenuItem exit = new JMenuItem("Exit");
    private JMenuItem logout = new JMenuItem("Logout");

    private JPanel registerPanel = new JPanel();
    private JPanel gradeButtonPanel = new JPanel();
    private JPanel gradePanel = new JPanel();
    private JPanel profilePanel = new JPanel();
    private JPanel coursePanel = new JPanel();
    private JScrollPane scrollGradePanel = new JScrollPane(gradePanel);

    private accessDB tableModelGrade;
    private accessDB tableModelCourse;
    private accessDB registerDB;

    private JTable tableForGrades;
    private JTable registerTable;

    private JButton registerButton;

    private String userName = "";

    public userScreen(String user)
    {

	super("Welcome to ERP System");

	setUserName(user);
	tableModelCourse = new accessDB();
	tableModelGrade = new accessDB();
	registerDB = new accessDB();
	try
	    {
		registerDB.getConnection();
	    }
	catch(SQLException s)
	    {
		s.printStackTrace();
	    }

	setJMenuBar(menuBar);

	registerMenu.add(register);

	viewInfoMenu.add(grades);
	viewInfoMenu.add(profileInfo);
	viewInfoMenu.add(courses);

	manageTabs.add(removeAllTabs);

	aboutMenu.add(about);

	exitMenu.add(logout);
	exitMenu.add(exit);
  
	if(!registerDB.getIsRegEnabled()||registerDB.getRegistrationStatus(userName))
	    register.setEnabled(false);

	register.addActionListener(
				   new ActionListener()
				   {
				       public void actionPerformed(ActionEvent e)
				       {
					   tabbedPane.addTab("Register", null, registerPanel, "Tab1");

					   try
					       {
						   registerDB.getConnection();
						   registerTable = new JTable(registerDB);
						   registerButton = new JButton("Register");

						   registerDB.displayRegisterTable();

						   registerButton.addActionListener(
										    new ActionListener()
										    {
											public void actionPerformed(ActionEvent a)
											{
											    try
												{
												    registerDB.registerStudent(userName);
												    JOptionPane.showMessageDialog(userScreen.this, "Registration Successfull", "Congrats", JOptionPane.INFORMATION_MESSAGE);
												    registerDB.setRegistrationStatus(userName);
												    registerButton.setEnabled(false);
												}
											    catch(Exception ex)
												{
												    JOptionPane.showMessageDialog(userScreen.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
												}
											}
										    }
										    );
					       }
					   catch(SQLException s)
					       {
						   JOptionPane.showMessageDialog(userScreen.this, s.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					       }
					   registerPanel.add(registerButton, BorderLayout.NORTH);
					   registerPanel.add(new JScrollPane(registerTable), BorderLayout.CENTER);
					   register.setEnabled(false);
				       }
				   }
				   );

	grades.addActionListener(
				 new ActionListener()
				 {
				     public void actionPerformed(ActionEvent e)
				     {
					 int i = 1;
					 final JButton buttonArray[] = new JButton[20];
					 try
					     {
						 tableModelGrade.getConnection();

						 String yoa = tableModelGrade.getyoa(userName);
						 int year = Integer.parseInt(yoa);
						 String currentSemester = tableModelGrade.getCurrentSemester();
						 Box verticalBox = Box.createVerticalBox();

						 String s1, s2;
						 int temp = currentSemester.indexOf("s");
						 temp = Integer.parseInt(currentSemester.substring(0, temp));
						 
						 do
						     {
							 if(year>temp)
							     break;

							 s1 = year + "s1";
					
							 buttonArray[i] = new JButton(s1);
							 verticalBox.add(buttonArray[i]);
							 verticalBox.add(Box.createVerticalStrut(10));
							 i++;

							 if(currentSemester.compareTo(s1)==0)
							     break;

							 s2 = year + "s2";
				       
							 buttonArray[i] = new JButton(s2);
							 verticalBox.add(buttonArray[i]);
							 verticalBox.add(Box.createVerticalStrut(10));
							 i++;  

							 if(currentSemester.compareTo(s2)==0)
							     break;
			 
							 year++;
						     }while(true);

						 tabbedPane.addTab("Semester", null, gradeButtonPanel, "Tab2");
						 gradeButtonPanel.add(verticalBox, BorderLayout.CENTER);
						 grades.setEnabled(false);

						 for(int count = 1;count<i;count++)
						     {
							 final int tempCount = count;
							 tableForGrades = new JTable(tableModelGrade);
							 buttonArray[count].addActionListener(
											      new ActionListener()
											      {
												  public void actionPerformed(ActionEvent e)
												  {
												      tabbedPane.addTab("Grades", null, scrollGradePanel, "Tab3");

												      try
													  {
													      tableModelGrade.gradeTable(userName, e.getActionCommand());
													      gradePanel.add(new JScrollPane(tableForGrades), BorderLayout.CENTER);
													  }
												      catch(Exception ex)
													  {
													      JOptionPane.showMessageDialog(userScreen.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
													  }
												  }
											      }
											      );
						     }				       
					     }
					 catch(SQLException s)
					     {
						 JOptionPane.showMessageDialog(userScreen.this, s.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
					     }
					 catch(IllegalStateException is)
					     {
						 JOptionPane.showMessageDialog(userScreen.this, is.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
					     }
				     }
				 }
				 );

	profileInfo.addActionListener(
				      new ActionListener()
				      {
					  public void actionPerformed(ActionEvent e)
					  {
					      tabbedPane.addTab("Profile", null, profilePanel, "Tab4");
					      try
						  {
						      accessDB database = new accessDB();
						      database.getConnection();

						      JLabel nameLabel = new JLabel("Name: ");; 
						      JLabel rollLabel = new JLabel("Roll Number: ");
						      JLabel dobLabel = new JLabel("Date Of Birth: ");
						      JLabel yoaLabel = new JLabel("Year Of Admission: ");
						      JLabel depLabel = new JLabel("Department: ");
						      JLabel addLabel = new JLabel("Address: ");

						      Box verticalBox1 = Box.createVerticalBox();
						      verticalBox1.add(Box.createVerticalStrut(20));
						      verticalBox1.add(Box.createVerticalGlue());
						      verticalBox1.add(nameLabel);
						      verticalBox1.add(Box.createVerticalGlue());
						      verticalBox1.add(Box.createVerticalStrut(10));
						      verticalBox1.add(rollLabel);
						      verticalBox1.add(Box.createVerticalGlue());
						      verticalBox1.add(Box.createVerticalStrut(10));
						      verticalBox1.add(dobLabel);
						      verticalBox1.add(Box.createVerticalGlue());
						      verticalBox1.add(Box.createVerticalStrut(10));
						      verticalBox1.add(yoaLabel);
						      verticalBox1.add(Box.createVerticalGlue());
						      verticalBox1.add(Box.createVerticalStrut(10));
						      verticalBox1.add(depLabel);
						      verticalBox1.add(Box.createVerticalGlue());
						      verticalBox1.add(Box.createVerticalStrut(10));
						      verticalBox1.add(addLabel);

						      profilePanel.add(verticalBox1, BorderLayout.CENTER);

						      JLabel name = new JLabel(database.getName(userName));
						      JLabel roll = new JLabel(userName);
						      JLabel dob = new JLabel(database.getdob(userName));
						      JLabel yoa = new JLabel(database.getyoa(userName));
						      JLabel dep = new JLabel(database.getDep(userName));
						      JLabel add = new JLabel(database.getAddress(userName));

						      Box verticalBox2 = Box.createVerticalBox();
						      verticalBox2.add(Box.createVerticalStrut(20));
						      verticalBox2.add(Box.createVerticalGlue());
						      verticalBox2.add(name);
						      verticalBox2.add(Box.createVerticalGlue());
						      verticalBox2.add(Box.createVerticalStrut(10));
						      verticalBox2.add(roll);
						      verticalBox2.add(Box.createVerticalGlue());
						      verticalBox2.add(Box.createVerticalStrut(10));
						      verticalBox2.add(dob);
						      verticalBox2.add(Box.createVerticalGlue());
						      verticalBox2.add(Box.createVerticalStrut(10));
						      verticalBox2.add(yoa);
						      verticalBox2.add(Box.createVerticalGlue());
						      verticalBox2.add(Box.createVerticalStrut(10));
						      verticalBox2.add(dep);
						      verticalBox2.add(Box.createVerticalGlue());
						      verticalBox2.add(Box.createVerticalStrut(10));
						      verticalBox2.add(add);

						      profilePanel.add(verticalBox2, BorderLayout.CENTER);

						      profileInfo.setEnabled(false);
						  }
					      catch(Exception s)
						  {
						      JOptionPane.showMessageDialog(userScreen.this, s.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						  }						  
					  }
				      }
				      );

	courses.addActionListener(
				  new ActionListener()
				  {
				      public void actionPerformed(ActionEvent e)
				      {
					  try
					      {
						  tableModelCourse.getConnection();

						  String currentSemester = tableModelCourse.getCurrentSemester();
						  
						  tabbedPane.addTab("Courses", null, coursePanel, "Tab5");
						  JTable tableForCourses = new JTable(tableModelCourse);

						  try
						      {
							  tableModelCourse.displayCourseTable(userName, currentSemester);
						      }
						  catch(Exception ex)
						      {
							  JOptionPane.showMessageDialog(userScreen.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						      }
						  coursePanel.add(new JScrollPane(tableForCourses), BorderLayout.CENTER);
						  courses.setEnabled(false);
					      }
					  catch(SQLException s)
					      {
						  JOptionPane.showMessageDialog(null, s.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
					      }
					  catch(IllegalStateException is)
					      {
						  JOptionPane.showMessageDialog(null, is.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
						courses.setEnabled(true);
						profileInfo.setEnabled(true);
						grades.setEnabled(true);
					    }
					}
					);
	about.addActionListener(
				new ActionListener()
				{
				    public void actionPerformed(ActionEvent e)
				    {
					String aboutERP = "The ERP Project\n"+"     By GomuNingen";
					JOptionPane.showMessageDialog(userScreen.this, aboutERP, "About ERP", JOptionPane.PLAIN_MESSAGE); 
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

	menuBar.add(registerMenu);
	menuBar.add(viewInfoMenu);
	menuBar.add(manageTabs);
	menuBar.add(aboutMenu);
	menuBar.add(exitMenu);

	add(tabbedPane);

	setSize(500, 500);
	setVisible(true);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	addWindowListener(
			  new WindowAdapter()
			  {
			      public void windowClosed(WindowEvent e)
			      {
				  tableModelGrade.disconnectFromDatabase();
				  tableModelCourse.disconnectFromDatabase();
				  registerDB.disconnectFromDatabase();
			      }
			  }
			  );
    }

    public void setUserName(String user)
    {
	userName = user;
    }
}