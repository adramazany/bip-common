package aip.util;

import java.util.ArrayList;

public class SyncIds {
	private ArrayList<String> delIds = new ArrayList<String>();
	private ArrayList<String> newIds = new ArrayList<String>();
	public ArrayList<String> getDelIds() {
		return delIds;
	}
	public void setDelIds(ArrayList<String> delIds) {
		this.delIds = delIds;
	}
	public ArrayList<String> getNewIds() {
		return newIds;
	}
	public void setNewIds(ArrayList<String> newIds) {
		this.newIds = newIds;
	}
	
	
}
