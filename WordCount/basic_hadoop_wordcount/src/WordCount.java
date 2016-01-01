
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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

//11111,bbb1,12-10-1950,1234567890,bbb1@xxx.com,1111111111,M,Diabetes,78
public class WordCount {

	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			Text temp1 =new Text();
			String line = value.toString();
			System.out.println(line);
			StringTokenizer tokenizer = new StringTokenizer(line,",");
			//System.out.println("---");
			//System.out.println(tokenizer);
			//System.out.println("---");
			while (tokenizer.hasMoreTokens()) {
				System.out.println ("Entering");
				String temp = tokenizer.nextToken();
				String temp12 = "null";
				System.out.println(temp);
				int i = 0;
				while (i<=7)
				{
					try
					{
						System.out.println(i);
						if (i==6){
							temp12 = tokenizer.nextToken();
						}
						else
						{
						temp = tokenizer.nextToken();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				System.out.println(temp);
				i++;
				}
			    temp1.set(temp12);
				temp1.set(temp);
				System.out.print("TOTA:");
			    System.out.println(temp1);

				System.out.println(temp);
				output.collect(temp1, new IntWritable (1));
			}
			System.out.println("EXiting");

		}

		
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> {
	
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			System.out.println("Reducer");
			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			System.out.println(key);

			output.collect(key, new IntWritable (sum));
		}

		 

		
	}

	public static void main(String[] args) throws Exception {

		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("wordcount");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		Random rand = new Random();
		
		//String input  = "///Users//ramranji//Desktop//qs1.rtf" ;
		String input  = "///Users//ramranji//big_data resources//tests//Healthcare//healthcare_Sample_dataset1copy.csv" ;
		String output = "///Users//ramranji//Desktop//" + rand.toString();

		FileInputFormat.setInputPaths(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));

		
//		FileInputFormat.setInputPaths(conf, new Path(args[0]));
//		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);

	}
}
