package tec.psa.integration;

import static org.junit.Assert.assertTrue;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

/**
 * Test de conexión y obtención de usuarios. Requiere que 
 * la base de datos esté en línea.
 * 
 * @author Joel
 */
public class MySqlConnectionTest {

  @Test
  public void mySqlConnectionTest() throws SQLException {
    
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUser("root");
    dataSource.setPassword("pass123");
    dataSource.setUrl("jdbc:mysql://localhost:3306/psa?useSSL=false");
    
    java.sql.Connection conn = dataSource.getConnection();
    java.sql.Statement stmt = conn.createStatement();
    
    // Hacer consulta a la base de datos
    ResultSet rs = stmt.executeQuery("SELECT 1");
    rs.last();   
    
    assertTrue("El ResultSet es nulo.", rs.getRow() != 0);
    
    rs.close();
    stmt.close();
    conn.close();
  }

}
