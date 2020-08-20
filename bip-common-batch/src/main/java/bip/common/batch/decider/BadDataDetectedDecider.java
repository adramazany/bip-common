package bip.common.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

/**
 * Created by ramezani on 8/13/2017.
 */
public class BadDataDetectedDecider implements JobExecutionDecider {

    private DataSource dataSource;
    private String message;
    private String sql;

    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        SqlRowSet rs = new JdbcTemplate(this.dataSource).queryForRowSet(this.sql);
        if(rs.next()){
            //return FlowExecutionStatus.FAILED;
            throw new RuntimeException("bad data detected!:"+message+" ,sql="+this.sql);
        }else{
            return FlowExecutionStatus.COMPLETED;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
