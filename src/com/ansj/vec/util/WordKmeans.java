package com.ansj.vec.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ansj.vec.Word2VEC;
import com.word2vec.stopword.ExtractWord;
import com.word2vec.stopword.GetStopWordList;

import love.cq.util.IOUtil;

/**
 * k-means聚类
 * @author zh
 * 缺点：此K-means算法只是实现了迭代上限停止，没有实现中心点不变的时候停止
 * 
 */
public class WordKmeans {
    
    public static void main(String[] args) throws IOException {
        Word2VEC vec = new Word2VEC();
        //vec.loadGoogleModel("vectors.bin");
        vec.loadJavaModel("model/vector.mod") ;
        System.out.println("load model ok!");
       
       ///词语聚类完毕：
        WordKmeans wordKmeans = new WordKmeans(vec.getWordMap(), 10, 50);  //分成10个类，迭代上限50次
        Classes[] explain = wordKmeans.explain();

        for (int i = 0; i < explain.length; i++) 
        {
            System.out.println("--------" + i + "---------");
            System.out.println(explain[i].getTop(10));
        }
        
        ///开始文本聚类：
        GetStopWordList getStopWordList = new GetStopWordList();
        //使用默认的停用词表
        Map<String, List> map=null;
		try {
			map = getStopWordList.getStopWordList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
       
       HashMap<String, float[]> filemap=parserFile(map,explain); 
       System.out.println(filemap.size());
       wordKmeans = new WordKmeans(filemap, 15, 50);
       explain = wordKmeans.explain();

       for (int i = 0; i < explain.length; i++) 
       {
           System.out.println("--------" + i + "---------");
           System.out.println(explain[i].getTop(10));
       }
    }

    private HashMap<String, float[]> wordMap = null;

    private int iter;  //迭代上限次数

    private Classes[] cArray = null;

    public WordKmeans(HashMap<String, float[]> wordMap, int clcn, int iter)  //clcn:是分成的类别数目 ，iter:是迭代上限次数
    {
        this.wordMap = wordMap;
        this.iter = iter;
        cArray = new Classes[clcn];
    }

    public Classes[] explain() {
        //first 取前clcn个点
        Iterator<Entry<String, float[]>> iterator = wordMap.entrySet().iterator();
        for (int i = 0; i < cArray.length; i++) {
            Entry<String, float[]> next = iterator.next();
            cArray[i] = new Classes(i, next.getValue());
        }

        for (int i = 0; i < iter; i++) 
        {
            for (Classes classes : cArray) {
                classes.clean();
            }

            iterator = wordMap.entrySet().iterator();
            while (iterator.hasNext()) 
            {
                Entry<String, float[]> next = iterator.next();
                double miniScore = Double.MAX_VALUE;
                double tempScore;
                int classesId = 0;
                for (Classes classes : cArray) {
                    tempScore = classes.distance(next.getValue());
                    if (miniScore > tempScore) {
                        miniScore = tempScore;
                        classesId = classes.id;
                    }
                }
                cArray[classesId].putValue(next.getKey(), miniScore);
            }

            for (Classes classes : cArray) {
                classes.updateCenter(wordMap);
            }
            System.out.println("iter " + i + " ok!");
        }

        return cArray;
    }
    
    private static HashMap<String, float []>  parserFile(Map<String, List> map, Classes[] explain) 
	{
    	File[] files = new File("bysjzl/wendang/rows/").listFiles();
    	HashMap<String, float []> filemap= new HashMap<String,float []>();
        //构建语料
        for (File file : files)
        {
           if (file.canRead() && file.getName().endsWith(".sample"))
           {
	   			try {
	   				BufferedReader br = IOUtil.getReader(file.getAbsolutePath(), IOUtil.UTF8);
	   				String temp = null;
	   				StringBuffer sBuffer=new StringBuffer();
	   					while ((temp = br.readLine()) != null) 
	   					{
	   						sBuffer.append(temp.split("\t")[0]);
	   						sBuffer.append(" ");
	   						//System.out.println(temp.split("\t")[0]);
	   					}
	   				List<String> list = ExtractWord.extracWord(sBuffer.toString(), map);
	   				float [] fl=new float[explain.length];
	   				//把list转换成向量
	   				for(String liString:list)
	   				{
	   					for (int i = 0; i < explain.length; i++) 
		   		        {
		   		            Set<String> expset=explain[i].values.keySet();
		   		           if(expset.contains(liString))
		   		           {
		   		        	   fl[i]+=1;
		   		           }
		   		        }	
	   				}
	   				filemap.put(file.getName(), fl);  //加入文本向量
	   				br.close();
	   			   } 
	   			catch (Exception e) 
		   		{
		   		 e.printStackTrace();
		   		}
            }
         }
            
		
			return filemap;
	}
    
    
public static class Classes
{
        private int id;

        private float[] center;

        public Classes(int id, float[] center) {
            this.id = id;
            this.center = center.clone();
        }

        Map<String, Double> values = new HashMap<>();

        public double distance(float[] value) {
            double sum = 0;
            for (int i = 0; i < value.length; i++) {
                sum += (center[i] - value[i])*(center[i] - value[i]) ;
            }
            return sum ;
        }

        public void putValue(String word, double score) {
            values.put(word, score);
        }

        /**
         * 重新计算中心点
         * @param wordMap
         */
        public void updateCenter(HashMap<String, float[]> wordMap) {
            for (int i = 0; i < center.length; i++) {
                center[i] = 0;
            }
            float[] value = null;
            for (String keyWord : values.keySet()) {
                value = wordMap.get(keyWord);
                for (int i = 0; i < value.length; i++) {
                    center[i] += value[i];
                }
            }
            for (int i = 0; i < center.length; i++) {
                center[i] = center[i] / values.size();
            }
        }

        /**
         * 清空历史结果
         */
        public void clean() {
            // TODO Auto-generated method stub
            values.clear();
        }

        /**
         * 取得每个类别的前n个结果
         * @param n
         * @return 
         */
        public List<Entry<String, Double>> getTop(int n) {
            List<Map.Entry<String, Double>> arrayList = new ArrayList<Map.Entry<String, Double>>(values.entrySet());
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>() 
            {
                @Override
                public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                    // TODO Auto-generated method stub
                	//System.out.println(o1.getValue() +";"+ o2.getValue());
                	if(o1.getValue() == o2.getValue())
                	{
                		System.out.println(o1.getValue() +";"+ o2.getValue());
                		o2.setValue(o2.getValue()-Double.MIN_VALUE);
                	}
                    return o1.getValue() > o2.getValue() ? 1 : -1;
                }
            });
            int min = Math.min(n, arrayList.size() - 1);
            if(min<=1)
            	return Collections.emptyList();
            return arrayList.subList(0, min);
        }

    }

}
