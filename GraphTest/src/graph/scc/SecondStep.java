package graph.scc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import cc.mallet.examples.TopicModel;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;



public class SecondStep {

	FirstStep step1;
	UserMap umap;
	ArrayList<String> expertPost = new ArrayList<String>();
	ArrayList<String> questionLdaFilter = new ArrayList<String>();
	InstanceList instances = null;
	ParallelTopicModel model = null;
	
	LdaModel tModel;
	
	public SecondStep(FirstStep step1, UserMap umap) {
		this.step1 = step1;
		this.umap = umap;
	}

	public void findExpertPosts(){
		ArrayList<String> expertUsers = step1.getExpertUsers();
		/*for(Map.Entry<String, ArrayList<String>> t : umap.userAnswers.entrySet()){
			String uId = t.getKey();
			for(String pid : t.getValue()){
				if(umap.posts.containsKey(pid)){
					expertPost.add(pid);
				}
			}
		}*/
		for(Map.Entry<String, ArrayList<String>> t : umap.userQuestions.entrySet()){
			String uId = t.getKey();
			for(String pid : t.getValue()){
				if(umap.posts.containsKey(pid)){
					expertPost.add(pid);
				}
			}
		}
		
		System.out.println("Total Expert Posts : " + expertPost.size());
		
	}

	public void runLDA(){
		
		topicMalletFileGeneration();
		loadTopoicModel();
		
	}
	
	
	public void topicMalletFileGeneration() {

		try {
			File f = new File(Properties.lda_corpus_file);
			if (!(f.exists() && !f.isDirectory())) {
				System.err.println("Mallet File Missing.. Generating..");
				BufferedWriter writer = new BufferedWriter(new FileWriter(Properties.lda_corpus_file));

				for (String id : expertPost) {
					String title = Preprocessing.htmlRemove(umap.posts.get(id).getTitle());
					String body = Preprocessing.htmlExtractCodePart(umap.posts.get(id).getBody());
					body = Preprocessing.activateStemmer(body);
					title = Preprocessing.activateStemmer(title);
					writer.write(id + "  X  " + (title + " " + body));
					writer.newLine();
					// writer.writeNext(new String[]{id,"X",body});
				}
				System.out.println("Finish Constructing LDA model files.  " + expertPost.size());
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void questionPrunning(){
		
		double maxi = -1;
		int maxiTopic = -1;
		int numDocs = expertPost.size();
		int numTopics = Properties.num_of_topics;
		
		
		for (int j = 0; j < numDocs; j++) {

			double[] topicDistribution = model.getTopicProbabilities(j);
			maxi = -1;
			maxiTopic = -1;

			for (int topic = 0; topic < numTopics; topic++) {
				if (maxi < topicDistribution[topic]) {
					maxi = topicDistribution[topic];
					maxiTopic = topic;
				}
			}
			
			if(maxi >= Properties.document_topic_threshold){
				String docId = model.getData().get(j).instance.getName().toString();
				questionLdaFilter.add(docId);
			}
	}
	}
	
	public void dimensionReduction(){
		runLDA();
		questionPrunning();
		
		System.out.println("Total : " + expertPost.size() +" After Filtering: " + questionLdaFilter.size());
		
		
	}
	
	
	public void topicModel( ){

		try{
		      
			String corpusFile = Properties.lda_corpus_file;
			int numTopics = Properties.num_of_topics;
			int numThreads = Properties.num_of_threads;
			int numIteration = Properties.num_of_iteration;
			double alpha = Properties.lda_alpha;
			double beta = Properties.lda_beta;
			
			
			long startTime = System.currentTimeMillis();
			
			ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
	        pipeList.add( new CharSequenceLowercase() );
	        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
	        pipeList.add( new TokenSequenceRemoveStopwords(new File(Properties.mallet_en_file), "UTF-8", false, false, false) );
	        pipeList.add( new TokenSequence2FeatureSequence() );
	        
	        this.instances = new InstanceList (new SerialPipes(pipeList));
	        
	        Reader fileReader = new InputStreamReader(new FileInputStream(new File(corpusFile)), "UTF-8");
	        this.instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
	                                               3, 2, 1));
	        
	        this.model = new ParallelTopicModel(numTopics,beta,alpha);

	        this.model.addInstances(instances);
	        
	        this.model.setNumThreads(numThreads);
	        this.model.setNumIterations(numIteration);
	        this.model.estimate();
	        
	        
	        tModel = new LdaModel();
	        tModel.setInstances(instances);
	        tModel.setModel(model);

	        
	        long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			
			
			System.out.println("Topic Model total Time: [ "+totalTime +" ms ]");
			System.out.println("Saving.. LDA MODEL file...");
			saveTopicModel();
			
			
	        
		}catch( Exception e){
			e.printStackTrace();
		}
	}
	
public boolean saveTopicModel(){
		
		if(this.instances == null || this.model == null){
			System.err.println("No model Trained. Can not save!");
			return false;
		}
		
		try{
			
			LdaModel tModel = new LdaModel();
			tModel.setInstances(this.instances);
			tModel.setModel(this.model);
			
			FileOutputStream fout = new FileOutputStream(Properties.lda_model);
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(tModel);
			oos.close();
			System.out.println("Save Complete");
			return true;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public  boolean loadTopoicModel(){
		
		try{

			File f = new File(Properties.lda_model);
			if(!(f.exists() && !f.isDirectory())){
				topicModel();
			}
			
			
			long startTime = System.currentTimeMillis();
			
			FileInputStream fin = new FileInputStream(Properties.lda_model);
			ObjectInputStream ois = new ObjectInputStream(fin);
			tModel = (LdaModel) ois.readObject();
			ois.close();
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			
			System.out.println("Successfully Load the model: Time [ "+totalTime +" ms ]");
			
			model = tModel.getModel();
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
}
