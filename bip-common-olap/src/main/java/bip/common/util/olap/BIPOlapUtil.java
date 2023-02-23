package bip.common.util.olap;

import aip.olap.AIPOlapUtil;
import bip.olap.util.collections.Node;
import bip.olap.util.collections.Tree;
import aip.util.NVL;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.Position;
import org.olap4j.metadata.Member;

import java.util.ArrayList;
import java.util.List;

public class BIPOlapUtil extends AIPOlapUtil {

	private BIPOlapUtil(){}
	
	public static Tree<List<String>> convertCellSet2Tree(CellSet cellSet){
		BIPOlapUtil olapUtil = new BIPOlapUtil();
		return olapUtil._convertCellSet2Tree(cellSet);
	}
	private Tree<List<String>> _convertCellSet2Tree(CellSet cellSet){
		Tree<List<String>> tree = new Tree<List<String>>();
		
		List<Node<List<String>>> levelsLastNode=new ArrayList<Node<List<String>>>();
		
		//List<List<String>> values=new ArrayList<List<String>>();
		/* 
		 * rows
		 */
		if(cellSet.getAxes().size()>1){
			
			for (Position row : cellSet.getAxes().get(1)) {
				List<String> row_list = new ArrayList<String>();
				//values.add(row_list);
				/*
				 * row header
				 */
				int level=0;
				String rowheader="";
				for (Member member : row.getMembers()) {
					if(NVL.isEmpty(rowheader) && member.getDepth()>1){
						rowheader=NVL.getStringFixLen("", member.getDepth()-1, '\t');
						if(level==0){
							level=member.getDepth()-1;
						}
					}
					//String value =rowheader+member.getCaption().replace("شهرستان ", "").replace("استان ", "");
					String value =rowheader+member.getCaption();
					row_list.add(value);
				}
				/*
				 * data
				 */
				for (Position column : cellSet.getAxes().get(0)) {
					final Cell cell = cellSet.getCell(column, row);
					row_list.add( cell.getFormattedValue() );
				}
				/*
				 * add tree
				 */
				String text=row_list.get(0);
				Node<List<String>> node = new Node<List<String>>(text,row_list);
				Node<List<String>> parent = null;
				for(int i=level-1;i>=0;i--){
					if(levelsLastNode.size()>i && levelsLastNode.get(i)!=null){
						parent = levelsLastNode.get(i);
						break;
					}
				}
				
				if(parent!=null){
					tree.addChild(parent, node);//levelsLastNode.get(level-1)					
				}else{
					tree.addRoot(node);
				}
				
				/*
				 * levelsLastNode
				 */
				if(levelsLastNode.size()>level){
					levelsLastNode.remove(level);
				}
				for(int i=levelsLastNode.size();i<level;i++){
					levelsLastNode.add(i, null);
				}
				levelsLastNode.add(level, node);
			}
		}else{// cellset has only columns
			List<String> row_list = new ArrayList<String>();
			//values.add(row_list);
			/*
			 * data
			 */
			for (Position column : cellSet.getAxes().get(0)) {
				final Cell cell = cellSet.getCell(column);
				row_list.add( cell.getFormattedValue() );
			}
			/*
			 * add tree
			 */
			Node<List<String>> node = new Node<List<String>>("",row_list);
			tree.addRoot(node);
		}
		
		return tree;
	}
	

}
