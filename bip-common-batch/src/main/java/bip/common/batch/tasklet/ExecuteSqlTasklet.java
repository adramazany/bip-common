package bip.common.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by ramezani on 8/5/2017.
 */
public class ExecuteSqlTasklet<T> implements Tasklet {

    private DataSource dataSource;
    private String sql;
    private ItemSqlParameterSourceProvider<T> itemSqlParameterSourceProvider;
    private T item;
    private boolean ignoreError=false;


    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            if(itemSqlParameterSourceProvider==null || item ==null){
                new JdbcTemplate(this.dataSource).update(this.sql);
            } else {
                new NamedParameterJdbcTemplate(dataSource).update(this.sql, this.itemSqlParameterSourceProvider.createSqlParameterSource(item));
            }
        }catch(Exception ex){
            if(ignoreError){
                ex.printStackTrace();
            }else{
                throw ex;
            }
        }
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

    public void setItemSqlParameterSourceProvider(ItemSqlParameterSourceProvider<T> itemSqlParameterSourceProvider) {
        this.itemSqlParameterSourceProvider = itemSqlParameterSourceProvider;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setIgnoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
    }
}