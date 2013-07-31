import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class login extends JFrame
{
    private JLabel welcome;
    private JLabel userLabel;
    private JLabel passLabel;
    private JButton submit;
    private JButton newEntry;
    private JPasswordField passField;
    private JTextField userField;
    private JCheckBox superUser;
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JPanel panelNorth;
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraints = new GridBagConstraints();
    private accessDB database;

    private String loginTable = "login";

    public login()
    {
        super("Login");
        try
        {
            database = new accessDB();
            database.getConnection();
        }
        catch(SQLException s)
        {
            JOptionPane.showMessageDialog(login.this, s.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            database.disconnectFromDatabase();
            System.exit(1);
        }
        panelCenter = new JPanel(layout);
        panelSouth = new JPanel(new GridLayout(1, 2));
        panelNorth = new JPanel(new GridLayout(2, 1));

        constraints.fill = GridBagConstraints.BOTH;

        welcome = new JLabel("Welcome to ERP System", JLabel.CENTER);
        panelNorth.add(welcome);

        superUser = new JCheckBox("Login as SuperUser");
        panelNorth.add(superUser);

        superUser.addItemListener(
                new ItemListener()
                {
                public void itemStateChanged(ItemEvent e)
                {
                if(superUser.isSelected())
                loginTable = "superUserLogin";
                else
                loginTable = "login";
                }
                }
                );

        userLabel = new JLabel("User Name: ");
        userField = new JTextField(15);
        addComponent(userLabel, 0, 0, 1, 1);
        addComponent(userField, 0, 1, 1, 1);

        passLabel = new JLabel("Password: ");
        passField = new JPasswordField(15);
        addComponent(passLabel, 1, 0, 1, 1);
        addComponent(passField, 1, 1, 1, 1);

        panelCenter.setBorder(new LineBorder(Color.BLACK));

        submit = new JButton("Submit");
        submit.addActionListener(
                new ActionListener()
                {
                public void actionPerformed(ActionEvent k)
                {
                String pass = passField.getText();
                String user = userField.getText();
                if(pass.length()!=0&&user.length()!=0)
                {
                try
                {
                if(database.verifyUser(user, pass, loginTable))
                {
                if(loginTable.compareTo("login")==0)
                {
                userScreen screen = new userScreen(user);
                dispose();
                }
                else
                {
                new superUserScreen();
                dispose();
                }
                }
                else
                    JOptionPane.showMessageDialog(login.this, "Invalid Credentials", "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
                }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(login.this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
                }
                else
                    JOptionPane.showMessageDialog(login.this, "Vacant field", "Incomplete information", JOptionPane.ERROR_MESSAGE);
                }
                }
        );

        newEntry = new JButton("New User");
        newEntry.addActionListener(
                new ActionListener()
                {
                public void actionPerformed(ActionEvent click)
                {
                dispose();

                new newUser();		   
                }
                }
                );
        panelSouth.add(submit);
        panelSouth.add(newEntry);

        add(panelSouth, BorderLayout.SOUTH);
        add(panelCenter, BorderLayout.CENTER);
        add(panelNorth, BorderLayout.NORTH);

        setSize(300, 180);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(
                new WindowAdapter()
                {
                public void windowClosed(WindowEvent close)
                {
                database.disconnectFromDatabase();
                }
                }
                );
    }
    public void addComponent(Component component, int row, int column, int width, int height)
    {
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        layout.setConstraints(component, constraints);
        panelCenter.add(component);
    }

    public static void main(String args[])
    {
        new login();
    }

}

