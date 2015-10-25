package com.tricellsoftware.timetrackertestapp.DTOs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyData 
{	//Array of dummy data
	public static List<DummyItem> Items = new ArrayList<DummyItem>();
	
	//A Map of a simple dummy items aka a Dictionary
	public static Map<String, DummyItem> Item_Map = new HashMap<String, DummyItem>();
	
	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
		public String id;
		public String content;
		public double rate;

		public DummyItem(String id, String content, double rate) {
			this.id = id;
			this.content = content;
			this.rate = rate;
		}

		@Override
		public String toString() {
			return content;
		}
	}
	//
	private static void addItem(DummyItem item) {
		Items.add(item);
		Item_Map.put(item.id, item);
	}

	
	static {
		addItem(new DummyItem("1", "ColorFX", 12.5));
		addItem(new DummyItem("2", "SQA", 14));
		addItem(new DummyItem("3", "DragonFly", 18));
		addItem(new DummyItem("4", "EA", 11.2));
		
	}
	
	

}
