import java.sql.*;
import javax.swing.table.AbstractTableModel;

public class accessDB extends AbstractTableModel
{
    private Connection connection;
    private PreparedStatement checkUser;
    private PreparedStatement displayGrade;
    private PreparedStatement displaySuperGrade;
    private PreparedStatement displayMyCourses;
    private PreparedStatement defaultStatement;
    private PreparedStatement newUserEntry;
    private PreparedStatement newSuperUserEntry;
    private PreparedStatement updateGrade;
    private PreparedStatement idArrayStatement;
    private PreparedStatement addSubject;
    private PreparedStatement dropTable;
    private PreparedStatement registerTable;
    private PreparedStatement updateRegisterTable;
    private PreparedStatement addTableStatement;
    private PreparedStatement isSubject;
    private PreparedStatement utilityStatement;
    private PreparedStatement createNewSemester;
    private PreparedStatement currentSemesterStatement;
    private PreparedStatement registerStudentStatement;
    private PreparedStatement setStatus;
    private PreparedStatement getStatus;
    private PreparedStatement getStudentStatus;
    private PreparedStatement deleteStatus;
    private ResultSet resultSet = null;
    private ResultSetMetaData metadata;
    private final String url = "jdbc:mysql://localhost/erp";
    private int nRows;

    private boolean superUser = false;
    private boolean isTableEditable = false;
    private boolean connectedToDatabase = false;

    public accessDB()
    {
        connectedToDatabase = false;
    }

    public void getConnection() throws SQLException
    {
        if(!connectedToDatabase)
        {
            connection = DriverManager.getConnection(url, "javafp", "javafp");

            connectedToDatabase = true;
        }

        defaultStatement = connection.prepareStatement("Select * from utility");
        newUserEntry = connection.prepareStatement("Insert into login(userName, password, name, dob, yearOfAdmission, department, address) values(?, ?, ?, ?, ?, ?, ?)");
        newSuperUserEntry = connection.prepareStatement("Insert into superUserLogin(userName, password, department) values(?, ?, ?)");

        idArrayStatement = connection.prepareStatement("Select SubjectID from allSubjects");

        executeDefault();
    }

    public Class getColumnClass(String columnName) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            return Class.forName(columnName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return Object.class;
    }

    public int getColumnCount() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            return metadata.getColumnCount();
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }

        return 0;
    }

    public int getRowCount() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        return nRows;
    }

    public String getColumnName(final int column) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            return metadata.getColumnName(column+1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }

        return "";
    }

    public Object getValueAt(final int row, final int column) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            resultSet.absolute(row+1);
            return resultSet.getObject(column+1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }

        return "";
    }

    public boolean isCellEditable(int row, int column)
    {
        if(superUser==true&&getColumnName(column).compareTo("Grade")==0&&getIsTableEditable())
            return true;
        else
            return false;
    }

    public void setValueAt(Object object, int row, int column) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to database");

        try
        {
            String tableName = getCurrentSemester();
            String userName = (String)getValueAt(row, column-2);
            String id = (String)getValueAt(row, column-1);

            updateGrade = connection.prepareStatement("Update "+tableName+" set Grade = '"+(String)object+"' where SubjectID = '"+
                    id+"' and userName = '"+userName+"'");
            updateGrade.execute();

            superGrade(id);

            fireTableCellUpdated(row, column);
        }
        catch(SQLException s)
        {
            //do nothing
        }
    }


    public void disconnectFromDatabase()
    {
        if(connectedToDatabase)
        {
            try
            {
                resultSet.close();
                connection.close();
            }
            catch(Exception s)
            {
                s.printStackTrace();
            }
            finally
            {
                connectedToDatabase = false;
            }
        }
    }

    public void executeDefault() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");
        try
        {
            resultSet = defaultStatement.executeQuery();

            metadata = resultSet.getMetaData();

            resultSet.last();
            nRows = resultSet.getRow();

            fireTableStructureChanged();
        }
        catch(SQLException s){}
    }

    public boolean verifyUser(String userName, String password, String table) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        checkUser = connection.prepareStatement("Select password from "+table+
                " where userName = ?");

        ResultSet temp;
        try
        {
            checkUser.setString(1, userName);
            temp = checkUser.executeQuery();

            temp.next();
            String pass = (String)temp.getObject(1);
            if(password.compareTo(pass)==0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            return false;
        }
    }

    public void gradeTable(String userName, String semester) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        displayGrade = connection.prepareStatement("Select Subject, Grade from allSubjects "+
                "inner join " + semester +
                " on allSubjects.SubjectID = " + semester +".SubjectID "+
                "where userName = ?");

        displayGrade.setString(1, userName);
        resultSet = displayGrade.executeQuery();

        metadata = resultSet.getMetaData();

        resultSet.last();
        nRows = resultSet.getRow();

        fireTableStructureChanged();
    }

    public void superGrade(String subjectID) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        String currentSemester = getCurrentSemester();

        displaySuperGrade = connection.prepareStatement("Select * from "+currentSemester+" where SubjectID = ?");

        displaySuperGrade.setString(1, subjectID);
        resultSet = displaySuperGrade.executeQuery();

        metadata = resultSet.getMetaData();

        resultSet.last();
        nRows = resultSet.getRow();

        fireTableStructureChanged();
    }

    public void deleteTable() throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        dropTable = connection.prepareStatement("Drop table register");
        dropTable.execute();
    }

    public void addTable() throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        addTableStatement = connection.prepareStatement("Create table register (SubjectID varchar(10) not null, "+
                "foreign key(SubjectID) references allSubjects(SubjectID))");

        addTableStatement.execute();
    }

    public void newSemesterTable() throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");
        String currentSemester = getCurrentSemester();
        int i = currentSemester.indexOf("s");

        if("s1".compareTo(currentSemester.substring(i))==0)
            currentSemester = currentSemester.substring(0, i)+"s2";
        else
        {
            int year = Integer.parseInt(currentSemester.substring(0, i));
            year++;
            currentSemester = year+"s1";
        }
        setCurrentSemester(currentSemester);

        createNewSemester = connection.prepareStatement("Create table "+currentSemester+" ("+
                "userName varchar(12) not null,"+
                " SubjectID varchar(10) not null,"+
                " Grade varchar(2),"+
                " foreign key(userName) references login(userName),"+
                " foreign key(SubjectID) references allSubjects(SubjectID))");

        createNewSemester.execute();
    }


    public void displayRegisterTable() throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        registerTable = connection.prepareStatement("Select * from allSubjects "+
                "inner join register "+
                "on allSubjects.SubjectID = register.SubjectID");

        resultSet = registerTable.executeQuery();

        metadata = resultSet.getMetaData();

        resultSet.last();
        nRows = resultSet.getRow();

        fireTableStructureChanged();
    }

    public void addToRegisterTable(String id) throws IllegalStateException, Exception
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");
        if(!isSubjectID(id))
        {
            updateRegisterTable = connection.prepareStatement("Insert into register(SubjectID) values(?)");

            updateRegisterTable.setString(1, id);
            updateRegisterTable.executeUpdate();
        }
        displayRegisterTable();
    }

    public void displayCourseTable(String userName, String semester) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");


        displayMyCourses = connection.prepareStatement("Select Subject, Credits, Teacher "+
                "from allSubjects inner join " + semester +
                " on allSubjects.SubjectID = " + semester + ".SubjectID "+
                "where userName = ?");
        displayMyCourses.setString(1, userName);
        resultSet = displayMyCourses.executeQuery();

        metadata = resultSet.getMetaData();

        resultSet.last();
        nRows = resultSet.getRow();

        fireTableStructureChanged();
    }

    public void setRegistrationStatus(String userName) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        setStatus = connection.prepareStatement("Insert into registrationStatus(userName, status) values(?, 'Y')");
        setStatus.setString(1, userName);
        setStatus.executeUpdate();
    }

    public boolean getRegistrationStatus(String userName) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;

        try
        {
            getStudentStatus = connection.prepareStatement("Select status from registrationStatus where userName = ?");
            getStudentStatus.setString(1, userName);
            temp = getStudentStatus.executeQuery();

            if(temp.next())
            {
                String y = "Y";
                String x = (String)temp.getObject(1);
                if(y.compareTo(x)==0)
                {
                    return true;
                }
            }
        }
        catch(SQLException s)
        {
            System.out.println("dgfhgdghjgjhfdh");
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return false;
    }

    public void getStatusTable() throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        getStatus = connection.prepareStatement("Select * from registrationStatus");
        resultSet = getStatus.executeQuery();

        metadata = resultSet.getMetaData();
        resultSet.last();
        nRows = resultSet.getRow();

        fireTableStructureChanged();
    }

    public void emptyStatusTable() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");
        try
        {
            deleteStatus = connection.prepareStatement("Delete from registrationStatus");
            deleteStatus.executeUpdate();
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
    }


    public void userEntry(String user, String password, String name, String dob, String yoa, String dpt, String add) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        newUserEntry.setString(1, user);
        newUserEntry.setString(2, password);
        newUserEntry.setString(3, name);
        newUserEntry.setString(4, dob);
        newUserEntry.setString(5, yoa);
        newUserEntry.setString(6, dpt);
        newUserEntry.setString(7, add);

        newUserEntry.executeUpdate();
    }

    public void superUserEntry(String user, String password, String department) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        newSuperUserEntry.setString(1, user);
        newSuperUserEntry.setString(2, password);
        newSuperUserEntry.setString(3, department);

        newSuperUserEntry.executeUpdate();
    }

    public void registerStudent(String userName) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        displayRegisterTable();
        resultSet.first();

        String currentSemester = getCurrentSemester();
        for(int i = 0;i<nRows;i++)
        {
            registerStudentStatement = connection.prepareStatement("Insert into "+currentSemester+
                    "(userName, SubjectID, Grade) "+
                    "values(?, ?, ?)");

            registerStudentStatement.setString(1, userName);
            registerStudentStatement.setString(2, (String)resultSet.getObject(1));
            registerStudentStatement.setString(3, "");
            registerStudentStatement.executeUpdate();
            resultSet.next();
        }
    }

    public String getName(String user) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp=null;
        try
        {
            PreparedStatement nameStatement = connection.prepareStatement("Select name from login where userName = ?");
            nameStatement.setString(1, user);
            temp = nameStatement.executeQuery();

            temp.next();
            return (String)temp.getObject(1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }

        return "";
    }

    public String getdob(String user) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;
        try
        {
            PreparedStatement dobStatement = connection.prepareStatement("Select dob from login where userName = ?");
            dobStatement.setString(1, user);
            temp = dobStatement.executeQuery();

            temp.next();
            return (String)temp.getObject(1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return "";
    }

    public String getyoa(String user) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;
        try
        {
            PreparedStatement yoaStatement = connection.prepareStatement("Select yearOfAdmission from login where userName = ?");
            yoaStatement.setString(1, user);
            temp = yoaStatement.executeQuery();

            temp.next();
            return (String)temp.getObject(1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return "";
    }

    public String getDep(String user) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;
        try
        {
            PreparedStatement depStatement = connection.prepareStatement("Select department from login where userName = ?");
            depStatement.setString(1, user);
            temp = depStatement.executeQuery();

            temp.next();
            return (String)temp.getObject(1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return "";
    }

    public String getAddress(String user) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;
        try
        {
            PreparedStatement addStatement = connection.prepareStatement("Select address from login where userName = ?");
            addStatement.setString(1, user);
            temp = addStatement.executeQuery();

            temp.next();
            return (String)temp.getObject(1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return "";
    }

    public String[] getSubjectIDArray() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        String returnArray[] = null;
        ResultSet temp = null;

        try
        {
            temp = idArrayStatement.executeQuery();
            int i = 1;
            temp.last();
            returnArray = new String[temp.getRow()+1];
            temp.beforeFirst();
            while(temp.next())
            {
                returnArray[i] = (String)temp.getObject(1);
                i++;
            }
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return returnArray;
    }

    public boolean isSubjectID(String id) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database"); 
        ResultSet temp = null;
        try
        {
            isSubject = connection.prepareStatement("Select SubjectID from register where SubjectID = ?");
            isSubject.setString(1, id);

            temp = isSubject.executeQuery();

            if(temp.next())
                return true;
            else
                return false;
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return false;
    }

    public String getCurrentSemester() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;
        try
        {
            PreparedStatement semStatement = connection.prepareStatement("Select column2 from utility where column1 = ?");
            semStatement.setString(1, "currentSemester");
            temp = semStatement.executeQuery();

            temp.next();
            return (String)temp.getObject(1);
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        finally
        {
            try
            {
                temp.close();
            }
            catch(SQLException s)
            {
                s.printStackTrace();
            }
        }
        return "";
    }

    public void setCurrentSemester(String sem) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            currentSemesterStatement = connection.prepareStatement("Update utility set column2 = ?");
            currentSemesterStatement.setString(1, sem);

            currentSemesterStatement.executeUpdate();
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
    }


    public void addNewSubject(String subID, String sub, int credits, String teacher) throws IllegalStateException, SQLException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        addSubject = connection.prepareStatement("Insert into allSubjects(SubjectID, Subject, Credits, Teacher) values(?, ?, ?, ?)");

        addSubject.setString(1, subID);
        addSubject.setString(2, sub);
        addSubject.setInt(3, credits);
        addSubject.setString(4, teacher);

        addSubject.executeUpdate();
    }

    public void setSuperUser(boolean value)
    {
        superUser = value;
    }

    public void setIsTableEditable(boolean value)
    {
        isTableEditable = value;
    }

    public boolean getIsTableEditable()
    {
        return isTableEditable;
    }

    public void setIsRegEnabled(boolean value) throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            utilityStatement = connection.prepareStatement("Update utility set isRegEnabled = ?");

            if(value)
                utilityStatement.setString(1, "true");
            else
                utilityStatement.setString(1, "false");

            utilityStatement.executeUpdate();
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
    }

    public boolean getIsRegEnabled() throws IllegalStateException
    {
        if(!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        ResultSet temp = null;

        try
        {
            utilityStatement = connection.prepareStatement("Select isRegEnabled from utility");
            temp = utilityStatement.executeQuery();

            temp.next();
            if("true".compareTo((String)temp.getObject(1))==0)
                return true;

            return false;
        }
        catch(Exception s)
        {
            s.printStackTrace();
        }
        return false;
    }
}

