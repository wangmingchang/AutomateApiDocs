package com.wmc.automateapidocs;

import java.util.Comparator;
import java.util.TreeMap;  

public class Test {
	public static void main(String[] args) {
		TreeMap<Integer, Integer> map1 = new TreeMap<Integer, Integer>(); // 默认的TreeMap升序排列
		TreeMap<Integer, Integer> map2 = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
			/*
			 * int compare(Object o1, Object o2) 返回一个基本类型的整型， 返回负数表示：o1 小于o2， 返回0
			 * 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Integer a, Integer b) {
				return b - a;
			}
		});
		map2.put(1, 2);
		map2.put(2, 4);
		map2.put(7, 1);
		map2.put(5, 2);
		System.out.println("Map2=" + map2);

		map1.put(1, 2);
		map1.put(2, 4);
		map1.put(7, 1);
		map1.put(5, 2);
		System.out.println("map1=" + map1);
	}
}
