package bip.common.batch.tasklet;

import bip.common.util.NVL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;

/**
 * Created by ramezani on 8/5/2017.
 */
public class ExecuteSqlReturnKeyTasklet<T> implements Tasklet {
    Logger logger = LoggerFactory.getLogger(ExecuteSqlReturnKeyTasklet.class);

    private DataSource dataSource;
    private String sql;
    private String executionContextKeyName;
    private ReturnKeyHolder returnKeyHolder;
    private ItemSqlParameterSourceProvider<T> itemSqlParameterSourceProvider;
    private T item;


    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        NamedParameterJdbcOperations namedParameterJdbcOperations= new NamedParameterJdbcTemplate(dataSource);
        //new JdbcTemplate(this.dataSource).execute(this.sql);

        KeyHolder keyHolder = new GeneratedKeyHolder();


        if(itemSqlParameterSourceProvider==null || item ==null){
            namedParameterJdbcOperations.update(sql, null, keyHolder, new String[]{executionContextKeyName});
        } else {
            namedParameterJdbcOperations.update(sql, this.itemSqlParameterSourceProvider.createSqlParameterSource(item), keyHolder, new String[]{executionContextKeyName});
        }


        int keyValue = keyHolder.getKey().intValue();
        logger.debug(String.format("returnKey=%d,item=%s",keyValue,item));

        if(!NVL.isEmpty(executionContextKeyName)) {
            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(executionContextKeyName, keyValue);
        }
        if(returnKeyHolder!=null){
            returnKeyHolder.setKey(keyHolder.getKey());
        }

        //#{jobExecutionContext['injectionid']}
        //chunkContext.getStepContext().getStepExecution().getJobParameters()..getString("ccReportId");
        //#{jobParameters['theKeyYouWant']}

        return RepeatStatus.FINISHED;
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

    public String getExecutionContextKeyName() {
        return executionContextKeyName;
    }

    public void setExecutionContextKeyName(String executionContextKeyName) {
        this.executionContextKeyName = executionContextKeyName;
    }

    public ReturnKeyHolder getReturnKeyHolder() {
        return returnKeyHolder;
    }

    public void setReturnKeyHolder(ReturnKeyHolder returnKeyHolder) {
        this.returnKeyHolder = returnKeyHolder;
    }

    public void setItemSqlParameterSourceProvider(ItemSqlParameterSourceProvider<T> itemSqlParameterSourceProvider) {
        this.itemSqlParameterSourceProvider = itemSqlParameterSourceProvider;
    }

    public void setItem(T item) {
        this.item = item;
    }


}