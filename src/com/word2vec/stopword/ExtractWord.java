package com.word2vec.stopword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.alibaba.fastjson.JSONObject;
import com.word2vec.stopword.GetStopWordList;
import com.word2vec.stopword.MacroDef;

import love.cq.domain.Branch;
import love.cq.util.IOUtil;
import love.cq.util.StringUtil;
 
/**
 * @author:zh
 * @E-mail: zhanghaonh@foxmail.com
 * @Version:1.0
 * @Description:抽词，包括去除停词
 * 从文件读取数据，抽词，包括去除停词。最终形成Google  Wordvec可以接受的输入格式；
 * 
 */
 
public class ExtractWord {
	private static final File sportCorpusFile = new File("bysjzl/wendang/result.txt");
    // extract word
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> extracWord(String article, Map<String, List> map) throws Exception {
		List<String> list = new ArrayList<String>();
		List<String> list_c = map.get(MacroDef.STOP_CHINESE);
	    List<String> list_e = map.get(MacroDef.STOP_ENGLISH);
		List<Term> parse = NlpAnalysis.parse(article).getTerms();
		for (Term term : parse) {
			boolean flag = true;
			String str = term.getName().trim();
			for (String str_c : list_c) {
				if (str_c.equals(str))
					flag = false;
			}
		 for (String str_e : list_e) {
		 if (str_e.equals(str))
			 flag = false;
		   }

			if (str == "")
				flag = false;
			if (flag)
				list.add(str);
		}

		return list;
	}
 
    public static void main(String[] args) throws Exception {
        long start=System.currentTimeMillis();
        ExtractWord extractWord = new ExtractWord();
        GetStopWordList getStopWordList = new GetStopWordList();
        //使用默认的停用词表
        Map<String, List> map = getStopWordList.getStopWordList();   
        File[] files = new File("bysjzl/wendang/rows/").listFiles();
        //构建语料
        try (FileOutputStream fos = new FileOutputStream(sportCorpusFile)) 
        {
            for (File file : files) {
                if (file.canRead() && file.getName().endsWith(".sample")) {
                    parserFile(fos,file,map);
                }
            }
        }
        System.out.println(System.currentTimeMillis()-start);
    }

	private static void parserFile(FileOutputStream fos, File file,Map<String, List> map) 
	{
		List<String> list = null;
			try {
				BufferedReader br = IOUtil.getReader(file.getAbsolutePath(), IOUtil.UTF8);
				String temp = null;
				//JSONObject parse = null;
				StringBuffer sBuffer=new StringBuffer();
					while ((temp = br.readLine()) != null) 
					{
						sBuffer.append(temp.split("\t")[0]);
						sBuffer.append(" ");
						//parse = JSONObject.parseObject(temp);
						//System.out.println(parse.toString()+"\n  contenttitle:"+parse.getString("contenttitle"));
						System.out.println(temp.split("\t")[0]);
						//list = ExtractWord.extracWord(parse.getString("contenttitle")+" "+StringUtil.rmHtmlTag(parse.getString("content")), map);
						//paserStr(fos, list);
					}
				list = ExtractWord.extracWord(sBuffer.toString(), map);
				paserStr(fos, list);
				br.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private static void paserStr(FileOutputStream fos, List<String> list) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			sb.append(str);
			sb.append(" ");
		}
		fos.write(sb.toString().getBytes());
		fos.write("\n".getBytes());
	}
	
 
}