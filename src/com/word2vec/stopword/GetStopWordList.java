package com.word2vec.stopword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.word2vec.stopword.MacroDef;
 
/**
 * @author:blogchong
 * @E-mail: blogchong@163.com
 * @Version:1.0
 * @blog  www.blogchong.com
 * @CreateTime：2014年12月16日 下午3:12:26
 * @Description:获取系统的停用词表
 */
 
public class GetStopWordList {
 
    // get stopword 1
    @SuppressWarnings("rawtypes")
    public Map<String, List> getStopWordList() throws Exception {
 
        Map<String, List> map = new HashMap<String, List>();
 
        String path_chinese = "bysjzl/stopword/chinese_stopword.txt";
        String path_english = "bysjzl/stopword/english_stopword.txt";
 
        GetStopWordList getStopWordList = new GetStopWordList();
 
        List<String> list_c = getStopWordList.readStopWord(path_chinese);
        List<String> list_e = getStopWordList.readStopWord(path_english);
 
        map.put(MacroDef.STOP_CHINESE, list_c);
        map.put(MacroDef.STOP_ENGLISH, list_e);
 
        return map;
    }
 
    // get stopword 2
    @SuppressWarnings("rawtypes")
    public Map<String, List> getStopWordList(String path_chinese,
            String path_english) throws Exception {
 
        Map<String, List> map = new HashMap<String, List>();
 
        GetStopWordList getStopWordList = new GetStopWordList();
 
        List<String> list_c = getStopWordList.readStopWord(path_chinese);
        List<String> list_e = getStopWordList.readStopWord(path_english);
 
        map.put(MacroDef.STOP_CHINESE, list_c);
        map.put(MacroDef.STOP_ENGLISH, list_e);
 
        return map;
    }
 
    // read stopword from file
    @SuppressWarnings("resource")
    public List<String> readStopWord(String path) throws Exception {
 
        List<String> list = new ArrayList<String>();
 
        File file = new File(path);
        InputStreamReader isr = new InputStreamReader(
                new FileInputStream(file), MacroDef.ENCODING);
        BufferedReader bf = new BufferedReader(isr);
 
        String stopword = null;
        while ((stopword = bf.readLine()) != null) {
            stopword = stopword.trim();
            list.add(stopword);
        }
 
        return list;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void main(String[] args) throws Exception {
        GetStopWordList getStopWordList = new GetStopWordList();
        Map<String, List> map = getStopWordList.getStopWordList();
        List<String> list = map.get(MacroDef.STOP_CHINESE);
 
        for (String str : list) {
            System.out.println(str);
        }
    }
 
}