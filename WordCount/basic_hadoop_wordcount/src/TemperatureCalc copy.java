import java.io.IOException;
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
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;


//Data format: 
//11111,bbb1,12-10-1950,1234567890,bbb1@xxx.com,1111111111,M,Diabetes,78
public class TemperatureCalc 

{
	public static class  MapTemp extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> 
	{

		@Override
		public void map(LongWritable name, Text value,
				OutputCollector<Text, IntWritable> output, Reporter arg3)
						throws IOException {
			// TODO Auto-generated method stub
			
			String line = value.toString();
			//System.out.println(line);
			StringTokenizer tokenizer = new StringTokenizer(line,",");
			System.out.print(tokenizer.countTokens());

			while (tokenizer.hasMoreTokens()) {
			//	System.out.println("INSIDE");
				int i =0;
				String temp = null;
				//as the 7th token is disease name I am just looping 
				while (i<=7)
				{
				//System.out.println(i);
				temp = tokenizer.nextToken();
				//System.out.println(temp);
				i++;
				}
				value.set(temp);
				
				//System.out.println(temp);
				output.collect(value, new IntWritable(1));
				temp = tokenizer.nextToken();
				temp = tokenizer.nextToken();
				//System.out.print("EOF");
				
			}
		}
	}
	public static void main(String[] args) throws Exception {

		JobConf conf = new JobConf(TemperatureCalc.class);
		conf.setJobName("diseasecount");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(MapTemp.class);
		//conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		//Healthcare
		//String input  = "///Users//big data resources//tests//Healthcare//healthcare_Sample_dataset2.csv" ;
		//String output = "///Users//Desktop//hjhh//";

		FileInputFormat.setInputPaths(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));

		
//		FileInputFormat.setInputPaths(conf, new Path(args[0]));
//		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);

	}

}
	


