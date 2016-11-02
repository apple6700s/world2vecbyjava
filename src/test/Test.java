package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.alibaba.fastjson.JSONObject;
import com.ansj.vec.Learn;
import com.ansj.vec.Word2VEC;

import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

public class Test {
	
	private static final File sportCorpusFile = new File("corpus/result.txt");
	
    public static void main(String[] args) throws IOException {
    	
    	/*Word2VEC w1 = new Word2VEC() ;
        w1.loadGoogleModel("library/corpus.bin") ;
        System.out.println(w1.distance("�����"));        
        System.out.println(w1.distance("ë��"));        
        System.out.println(w1.distance("��Сƽ"));  
        System.out.println(w1.distance("ħ����"));
        System.out.println(w1.distance("ħ��"));
        */
       /* File[] files = new File("corpus/sport/").listFiles();
        
        //��������
        try (FileOutputStream fos = new FileOutputStream(sportCorpusFile)) 
        {
            for (File file : files) {
                if (file.canRead() && file.getName().endsWith(".txt")) {
                    parserFile(fos, file);
                }
            }
        }
        
        */
        //���зִ�ѵ��
        Learn lean = new Learn() ;
        lean.learnFile(sportCorpusFile) ;
        lean.saveModel(new File("model/vector.mod")) ;
        
        //���ز���
        Word2VEC w2v = new Word2VEC() ;
        w2v.loadJavaModel("model/vector.mod") ;
        System.out.println(w2v.distance("����")); 

    }
	private static void parserFile(FileOutputStream fos, File file) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		try (BufferedReader br = IOUtil.getReader(file.getAbsolutePath(), IOUtil.UTF8)) {
			String temp = null;
			JSONObject parse = null;
			while ((temp = br.readLine()) != null) {
				parse = JSONObject.parseObject(temp);
				System.out.println(parse.toString()+"\n  contenttitle:"+parse.getString("contenttitle"));
				paserStr(fos, parse.getString("contenttitle"));
				paserStr(fos, StringUtil.rmHtmlTag(parse.getString("content")));
			}
		}
	}
	
	private static void paserStr(FileOutputStream fos, String title) throws IOException {
		List<Term> parse2 =  ToAnalysis.parse(title).getTerms();
		StringBuilder sb = new StringBuilder();
		for (Term term : parse2) {
			sb.append(term.getName());
			sb.append(" ");
		}
		fos.write(sb.toString().getBytes());
		fos.write("\n".getBytes());
	}
}
