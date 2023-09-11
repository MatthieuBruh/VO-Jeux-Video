package dao.OracleToCSV;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BddOracle {
    private String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private String user = "system";
    private String psswd = "manager";

    private static Connection conn;

    private BddOracle() {
    }

    private static class BddOracleHolder {
        private static final BddOracle uniqueInstance = new BddOracle();
    }

    public static BddOracle getInstance() {
        return BddOracleHolder.uniqueInstance;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPsswd(String psswd) {
        this.psswd = psswd;
    }

    public Connection connect() throws SQLException {
        conn = DriverManager.getConnection(url, user, psswd);
        return conn;
    }

    public static void disconnect() throws SQLException {
        conn.close();
    }

    public static ResultSet query(String rqt) throws SQLException {
        return conn.createStatement().executeQuery(rqt);
    }

}
