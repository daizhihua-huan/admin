package com.daizhihua.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    public static void main(String[] args) {
        System.out.println(getData());
    }

    public static Map<String,Object> getData(){
       Map<String,Object> resultMap = new HashMap<>();
        List<Double> valueList=new ArrayList<>();
        valueList.add(1.0);
        List<Object[]> signal = new ArrayList<>();
        Object [] i = new Object[1];
        i[0] = 1;
        signal.add(i);
        List<Object[]> data = new ArrayList<>();
        Object[] s = new Object[1];
        s[0] = 1;
        data.add(s);
        double[] doubles = new double[1];
        doubles[0] = 1.2;
        resultMap.put("dataList",valueList);
        resultMap.put("signal",signal);
        resultMap.put("data",data);
        resultMap.put("arrays",doubles);
        valueList = null;
        signal = null;
        data = null;
        doubles = null;

        return resultMap;
    }
}
