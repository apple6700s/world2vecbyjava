package com.word2vec.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ReadFromFile {
	public static void main(String [] args)
	{
		Map<String, StringBuffer> map=readFileByLines("bysjzl/data/SogouQ.sample");
		String path="bysjzl/wendang";
		if (writeAndtraverseMap(map,path)) {
			System.out.println("success");
		}else {
			System.out.println("failed");
		}
		
	}
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static Map<String, StringBuffer> readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        Map<String, StringBuffer> map=new HashMap<String,StringBuffer>();
        try {
            //以行为单位读取文件内容，一次读一整行
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	tempString=tempString.replaceAll("\\[", "").replaceAll("\\]", "");
            	String [] temp=tempString.split("\t");
            	if(!map.containsKey(temp[1]))
            	{
            		line++;
            		map.put(temp[1], new StringBuffer(temp[2]+"\t"+temp[4]));
            	}
            	else
            	{
            		
            		map.put(temp[1], map.get(temp[1]).append("\n"+temp[2]+"\t"+temp[4]));
            	}
                //System.out.println("line " + line + ": " + tempString);
                
            }
            reader.close();
            System.out.println(line);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        return map;
        		
    }
    /*
    *遍历map并且写文件：utf-8字符集支持中文
    */
    public static boolean writeAndtraverseMap(Map<String, StringBuffer> map,String path)
    {
    	boolean check=true;
    	String basepath=path;
    	try
        {
    		 FileOutputStream out=null;
	    	for (Map.Entry<String, StringBuffer> entry : map.entrySet()) 
	    	{  
	    		//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
    	        path=basepath+"/"+entry.getKey()+".sample";
    	        //System.out.println(path);
    	        File file=new File(path);
    	        if(!file.exists())
    	            file.createNewFile();
    	        out=new FileOutputStream(file,false); //如果追加方式用true        
    	        StringBuffer sb=new StringBuffer();
    	        sb.append(entry.getValue());
    	        out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
    	    }     	
    	    out.close();
        }
        catch(IOException ex)
        {
        	check=false;
            System.out.println(ex.toString());
        }
    	return check;
    }

}
