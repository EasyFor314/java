import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Лев on 10.10.2019.
 */
public class MainForm extends JFrame{
    private JButton создатьButton;
    private JButton удалитьButton;
    private JButton Подключиться;
    private JPanel panel;
    private JButton closeConnectButton;
    private JTable table1;
    private JButton показатьButton;
    private JTextField textID;
    private JTextField Value;
    public  Connection con = null; // соединение с БД
    public Statement stmt = null; // оператор
    public MainForm() {


        this.getContentPane().add(panel);
        Подключиться.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                connectDb();
            }
        });
        closeConnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeDb();
            }
        });
        показатьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            ShowTable();
            }

        });
        удалитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id= Integer.parseInt(textID.getText());
                String sql = "DELETE FROM `table` WHERE `id` = '"+id+"';";
                try {
                    stmt.executeUpdate(sql);
                    ShowTable();
                }
                catch (Exception ex)
                {
                    ShowMsg("Строка НЕ удалена с id ="+id + ex.toString());
                }
            }
        });
        создатьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = Value.getText();
                String sql = "INSERT INTO `table`(`name`) VALUES ('"+s+"');";
                try {
                    stmt.executeUpdate(sql);
                    ShowTable();
                }
                catch (Exception ex)
                {
                    ShowMsg("Строка не добавлена " + ex.toString());
                }
            }
        });

    }
    public  boolean closeDb(){
        try {
            con.close();
            ShowMsg("Connection closed");
            return true;
        }
        catch (Exception exx)
        {
            ShowMsg( "Connection dont close"+exx.toString());
        }
    return  false;
    }
    public  boolean connectDb()
    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bibl", "root", "");
            // работа с базой данных
            stmt = con.createStatement();
            ShowMsg("Connection success");
            return true;
        }
        catch ( Exception ex)
        {
            ShowMsg("Error connection " + ex.toString());
        }
    return false;
    }
    public  void ShowTable()
    {
        String sql = "SELECT * FROM `table`;";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            int id;
            String name;
            int m=10;

            table1.setModel(new DefaultTableModel(new Integer[m][2], new String[2]));
            int i=0;

            while (rs.next())
            {
                id = rs.getInt("id");
                name = rs.getString("name");
                table1.setValueAt(id,i,0);
                table1.setValueAt(name,i,1);
                i++;
            }
            rs.close();
        }
        catch ( Exception ex)
        {
            ShowMsg("Отображение не удалось" + ex.toString());
        }

    }
    public void ShowMsg(String st)
    {
        System.out.println(st);
        JOptionPane.showMessageDialog(panel, st);
    }

}
