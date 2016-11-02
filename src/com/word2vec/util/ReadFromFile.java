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
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     */
    public static Map<String, StringBuffer> readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        Map<String, StringBuffer> map=new HashMap<String,StringBuffer>();
        try {
            //����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ����
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
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
    *����map����д�ļ���utf-8�ַ���֧������
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
    	        out=new FileOutputStream(file,false); //���׷�ӷ�ʽ��true        
    	        StringBuffer sb=new StringBuffer();
    	        sb.append(entry.getValue());
    	        out.write(sb.toString().getBytes("utf-8"));//ע����Ҫת����Ӧ���ַ���
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
