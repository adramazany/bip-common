package aip.olap.bak;

import java.io.Serializable;
import java.sql.SQLException;

import org.olap4j.OlapConnection;

public interface OlapConnectionProvider extends Serializable
	{
	  public OlapConnection createConnection(final String user, final String password) throws SQLException;
	}
