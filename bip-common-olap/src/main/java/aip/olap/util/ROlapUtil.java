package aip.olap.util;

import aip.olap.NoneRolapAggregator;
import mondrian.olap.EnumeratedValues;
import mondrian.rolap.RolapAggregator;

import java.lang.reflect.Field;

/**
 * Created by ramezani on 5/31/2018.
 */
public class ROlapUtil {
    static boolean isRegistered=false;
    public static void registerNoneRolapAggregator(){
        if(isRegistered==false) {
            int index = RolapAggregator.enumeration.getMax();
            registerAggregator(new NoneRolapAggregator(++index));
            isRegistered=true;
        }
    }

    private static void registerAggregator(final RolapAggregator aggregator)
    {
        // reset field 'ordinalToValueMap' in order to make enumeration mutable again
        try
        {
            //final Field f = ReflectionUtil.getField(EnumeratedValues.class, "ordinalToValueMap");
            final Field f = EnumeratedValues.class.getDeclaredField("ordinalToValueMap");
            f.setAccessible(true); // allow setting private fields
            f.set(RolapAggregator.enumeration, null);
        }catch (IllegalArgumentException e){
            throw new RuntimeException(e);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }catch (NoSuchFieldException e){
            throw new RuntimeException(e);
        }

        RolapAggregator.enumeration.register(aggregator);
        RolapAggregator.enumeration.makeImmutable();
    }

}
