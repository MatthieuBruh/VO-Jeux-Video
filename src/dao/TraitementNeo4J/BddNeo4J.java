package dao.TraitementNeo4J;

import org.neo4j.driver.*;

public class BddNeo4J {

    private static String uri = "neo4j+s://d6b4dec5.databases.neo4j.io";
    private static String user = "neo4j";
    private static String psswd = "Lj2oUMAhuZo-GB80KvWC0PolMZkhnM2rJG7k1d3XxSo";

    private Driver driver;
    private Session session;

    public BddNeo4J() {
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPsswd(String psswd) {
        this.psswd = psswd;
    }

    public void connect() {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, psswd));
        session = driver.session();
    }

    public void close() {
        session.close();
        driver.close();
    }

    public Result run(String instr) {
        Result res = this.session.run(instr);
        return res;
    }
}
