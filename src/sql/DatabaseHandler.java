package sql;

import helpers.User;
import helpers.Info;
import helpers.SimpleInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:mysql://" + dbHost
                + ":" + dbPort + "/" + dbName
                + "?serverTimezone=Europe/Moscow&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);
        System.out.println("Connected");
        return dbConnection;
    }

    public void signUpUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + " (" +
                Const.USER_FIRSTNAME + "," + Const.USER_SECONDNAME + "," +
                Const.USER_EMAIL + "," + Const.USER_LOGIN + "," +
                Const.USER_PASSWORD + ")" + "VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getFirstname());
            prSt.setString(2, user.getSecondname());
            prSt.setString(3, user.getEmail());
            prSt.setString(4, user.getLogin());
            prSt.setString(5, user.getPassword());

            prSt.executeUpdate();
            prSt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_LOGIN + "=? AND " + Const.USER_PASSWORD + " =?";

        try {
            PreparedStatement prST = getDbConnection().prepareStatement(select);
            prST.setString(1, user.getLogin());
            prST.setString(2, user.getPassword());

            resSet = prST.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void addData(ArrayList<Info> data) {
        String insert = "INSERT INTO " + Const.DATA_TABLE + "(" +
                Const.DATAS_DATE + "," + Const.DATAS_OPEN + "," +
                Const.DATAS_HIGH + "," + Const.DATAS_LOW + ","
                + Const.DATAS_CLOSE + ")" + "VALUES(?, ?, ?, ?, ?);";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            for (Info inf : data) {
                prSt.setString(1, inf.getYear() + "-" + inf.getMonth() + "-" + inf.getDay());
                prSt.setDouble(2, inf.getOpenPrice());
                prSt.setDouble(3, inf.getHighPrice());
                prSt.setDouble(4, inf.getLowPrice());
                prSt.setDouble(5, inf.getClosePrice());

                prSt.executeUpdate();
            }
            prSt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public ObservableList<Info> getData() {
        try {
            Connection conn = getDbConnection();
            ObservableList<Info> obsList = FXCollections.observableArrayList();
            String select = "SELECT * FROM " + Const.DATA_TABLE + ";";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(select);
            while (result.next()) {
                Info info = new Info();
                String[] date = result.getString(Const.DATAS_DATE).split("-");

                info.setDate(date[2] + date[1] + date[0]);
                info.setYear(Integer.parseInt(date[0]));
                info.setMonth(Integer.parseInt(date[1]));
                info.setDay(Integer.parseInt(date[2]));
                info.setOpenPrice(result.getDouble(Const.DATAS_OPEN));
                info.setHighPrice(result.getDouble(Const.DATAS_HIGH));
                info.setLowPrice(result.getDouble(Const.DATAS_LOW));
                info.setClosePrice(result.getDouble(Const.DATAS_CLOSE));

                obsList.add(info);
            }
            System.out.println("Data successfully selected.");
            conn.close();
            return obsList;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public ObservableList<SimpleInfo> getSimpleData(String name, String dateFrom, String dateTo) {

        ObservableList<SimpleInfo> infoList = FXCollections.observableArrayList();
        String sql = "SELECT date, " + name + " FROM " + Const.DATA_TABLE +
                " WHERE date BETWEEN '" + dateFrom + "' AND '" + dateTo + "' ;";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            ResultSet res = prSt.executeQuery();
            while (res.next()) {
                SimpleInfo obj = new SimpleInfo();
                String[] dateArr = res.getString("date").split("-");
                obj.setDate(dateArr[2] + dateArr[1] + dateArr[0]);
                obj.setValue(res.getDouble(name));
                infoList.add(obj);
            }
            prSt.close();
            return infoList;
        } catch (Exception ex) {
            ex.getMessage();
            return null;
        }
    }

    public void addLog(String login, String logInfo) {
        String insert = "INSERT INTO " + Const.LOG_TABLE + "(" +
                Const.LOG_INFO + " , " + Const.LOG_USER_LOGIN + ")" +
                " VALUES(?,?);";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, logInfo);
            prSt.setString(2, login);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
