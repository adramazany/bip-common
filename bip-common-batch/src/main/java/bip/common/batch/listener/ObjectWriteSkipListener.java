package bip.common.batch.listener;

import bip.common.batch.writer.BatchErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Created by ramezani on 2/1/2018.
 */
public class ObjectWriteSkipListener<T, S> implements SkipListener<T, S>,InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(ObjectWriteSkipListener.class);

    BatchErrorHandler batchErrorHandler;


    public void onSkipInRead(Throwable throwable) {
        logger.error("onSkipInRead exception !!!",throwable);
        batchErrorHandler.handleError(throwable);
    }

    public void onSkipInWrite(S entity, Throwable throwable) {
        logger.error("error in write entity of :"+entity+"!",throwable);
        batchErrorHandler.handleError(entity,throwable);
    }

    public void onSkipInProcess(T entity, Throwable throwable) {
        logger.error("onSkipInProcess entity of :"+entity+"!",throwable);
        batchErrorHandler.handleError(entity,throwable);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.batchErrorHandler, "A batchErrorHandler is required.");
    }

    public void setBatchErrorHandler(BatchErrorHandler batchErrorHandler) {
        this.batchErrorHandler = batchErrorHandler;
    }
}
