package bip.common.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by ramezani on 8/7/2017.
 */
public class IfOpTasklet implements Tasklet, StepExecutionListener {
    //Injection injection;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // do something
        return RepeatStatus.FINISHED;
    }

    public void beforeStep(StepExecution stepExecution) {
        // no-op
    }

    public ExitStatus afterStep(StepExecution stepExecution) {

//        if (injection==null || injection.getInjectionid()==0) {
//            return new ExitStatus("INSERT");
//        } else if(!injection.isRetry()) {
//            return new ExitStatus("UPDATE");
//        } else {
//            return new ExitStatus("SKIP");
//        }
        return new ExitStatus("SKIP");
    }

//    public Injection getInjection() {
//        return injection;
//    }
//
//    public void setInjection(Injection injection) {
//        this.injection = injection;
//    }
}
