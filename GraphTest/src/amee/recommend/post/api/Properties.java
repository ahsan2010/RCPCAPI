package graph.scc;

public class Properties {
	public static String root = "/home/amee/Documents/RecommendingPostAPIIssues";
	public static String tag = "";
	public static String msr_post_file = "/home/amee/Documents/MSR2015/Posts.xml";
    public static String stop_word_path = root+"/stopList.csv";
    public static String post_ser_file = root+"/postSer/"+tag+"Post.ser";
  
    public static String lda_model = root+"/lda.model";
    public static String lda_corpus_file = root+"/ldaCorpur.mal";
    public static String mallet_en_file = root+"/en.txt";
    public static String mallet_ap_file = root+"/ap.txt";
    public static int num_of_topics = 40;
    public static int num_of_threads = 2;
    public static int num_of_iteration = 40;
    public static double lda_alpha = 0.01;
    public static double lda_beta = 0.1;
    public static double document_topic_threshold = 0.1;

}
