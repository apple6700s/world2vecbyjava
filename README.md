# world2vecbyjava

程序清单：
com.word2vec.stopword.ExtractWord.java:从文件读取数据，抽词，包括去除停词。最终形成Google  Wordvec可以接受的输入格式；
com.ansj.vec.Learn.java               :训练特征,skip gram 模型训练
com.ansj.vec.Word2VEC.java            :得到词向量,加载模型
com.ansj.vec.util.WordKmeans.java     :K-means聚类算法的实现，但只是实现了迭代上限停止，没有实现中心点不变的时候停止
test.Test.java                        :测试用例

运行流程：
先是运行ExtractWord.java（提取词语）--> 运行Learn.java（训练模型）--> Word2VEC.java(加载模型，生成词向量)
-->WordKmeans.java（调用K-means聚类算法对词向量进行聚类）-->进一步对文档进行聚类
