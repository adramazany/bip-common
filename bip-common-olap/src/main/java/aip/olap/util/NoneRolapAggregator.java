package aip.olap.util;

/**
 * Created by ramezani on 5/31/2018.
 */

import mondrian.calc.Calc;
import mondrian.calc.TupleList;
import mondrian.olap.Aggregator;
import mondrian.olap.Evaluator;
import mondrian.rolap.RolapAggregator;
import org.olap4j.metadata.Datatype;

/**
 * {@link Aggregator} implementation for performing all aggregation at DB.
 * NOTE: This aggregator does not use segments that are already loaded (into cache).
 * It always queries back to DB for doing the required aggregation.
 */
public class NoneRolapAggregator extends RolapAggregator {

    public NoneRolapAggregator(final int index) {
        super("None", index, false);
    }

    public Aggregator getRollup()  {
        return this;
    }

    public Object aggregate(final Evaluator evaluator, final TupleList members, final Calc exp) {
        // See AggregateFunDef$AggregateCalc.aggregate where logic for distinct-count
        // is implemented (which also queries back to DB for aggregated value, independently from segments).
        // See also http://lists.pentaho.org/pipermail/mondrian/2007-November/000901.html
        final Evaluator evaluator2 = evaluator.pushAggregation(members);
        evaluator2.setNonEmpty(false);
        return evaluator2.evaluateCurrent();
    }

    public String getExpression(final String operand) {
        return operand;
    }

    public boolean supportsFastAggregates(final Datatype dataType)  {
        return false;
    }
}