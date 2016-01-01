import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;



public class DiabeticCounter {

	//11111,bbb1,12-10-1950,1234567890,bbb1@xxx.com,1111111111,M,Diabetes,78	
	public static class DiabeticMapper extends MapReduceBase implements Mapper<LongWritable,Text,Text,LongWritable> {

		@Override
		public void map(LongWritable input_key, Text input_value,
				OutputCollector<Text,LongWritable> output, Reporter arg3)
				throws IOException {
			// TODO Auto-generated method stub
			String line = input_value.toString();
			StringTokenizer tokens = new StringTokenizer(line,",");
			
			int i = 0;
			while (i<=5)
			{
				tokens.nextToken();
				i++;
			}
			Text gender = new Text (tokens.nextToken());
			Text disease = new Text (tokens.nextToken());
			
			Text temp = new Text (gender.toString() + "," + disease.toString());
			output.collect(temp, new LongWritable(1));
		}
		
	}
	
public static class DiabeticReducer extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable>{

		@Override
		public void reduce(Text key, Iterator<LongWritable> values,
				OutputCollector<Text, LongWritable> output, Reporter arg3)
				throws IOException {
			// TODO Auto-generated method stub
			
			String key_line = key.toString();
			StringTokenizer key_tokens = new StringTokenizer(key_line,",");
			String gender = key_tokens.nextToken();
			String disease = key_tokens.nextToken();
			
			System.out.println("values");
			//System.out.println(values.);
			int sum = 0;
			//if (gender.equals ("M")){
				while (values.hasNext()) {
					sum += values.next().get();
				}
			//}
			output.collect(key, new LongWritable(sum));
			
		}
		
	}
		
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("wordcount");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(LongWritable.class);

		conf.setMapperClass(DiabeticMapper.class);
		conf.setReducerClass(DiabeticReducer.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		Random rand = new Random();
		
		//String input  = "///Users//ramranji//Desktop//qs1.rtf" ;
		String input  = "///Users//ramranji//big data resources//tests//Healthcare//healthcare_Sample_dataset1.csv" ;
		String output = "///Users//ramranji//Desktop//" + rand.toString();

		FileInputFormat.setInputPaths(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));

		
//		FileInputFormat.setInputPaths(conf, new Path(args[0]));
//		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}

}
