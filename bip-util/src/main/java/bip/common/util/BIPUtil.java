package bip.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bip.common.util.persian.PersianCollator;
import aip.util.AIPUtil;

public class BIPUtil extends AIPUtil{

	public static void sortPersianListOfList(List<List<String>> listOfList,final int column,int fromIndex,int toIndex) {
		Collections.sort(listOfList.subList(fromIndex, toIndex), new Comparator<List>() {
			public int compare(List o1, List o2) {
				return PersianCollator.getPersianInstance().compare(NVL.getString(o1.get(column)),NVL.getString(o2.get(column)));
			}
		});
	}
    
	
}
