import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
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

class TemperatureObject implements WritableComparable <TemperatureObject> {
	
//	private int age;
	private String disease,gender,age;
	
  public int calculateAge(int year, int month, int day) {
	    Calendar dob = Calendar.getInstance();
	    Calendar today = Calendar.getInstance();


	    dob.set(year, month - 1, day);

	    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
	    if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
	        age--;
	    } else if(today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)) {
	        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
	            age--;
	        }
	    }

	   // Integer ageInt = new Integer(age);
	    //String ageS = ageInt.toString();

	    return age;
	}
  
  public  void objectSetter (String value){
		
		String line = value.toString();
		System.out.println(line);
		StringTokenizer tokenizer = new StringTokenizer(line,",");
		System.out.print(tokenizer.countTokens());
		//Text op = new Text();
		if (tokenizer.hasMoreTokens()) {
			System.out.println("INSIDE");
			int i =0;
			while (i<=7)
			{
				try{
					
				
				if (i==2){
					age  = tokenizer.nextToken()  + ",";
				}
				else if (i==6){
					gender = tokenizer.nextToken() + ",";
				}
				else
				{
				disease = tokenizer.nextToken() + ",";
				}
				}
				catch (Exception e){
					e.printStackTrace();
				}
			System.out.println(i);
			//disease = tokenizer.nextToken();
			System.out.println(disease);
			i++;
			}
  }
 }
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("READ");
		String line = arg0.readLine();
		System.out.println(line);
		String [] temp = line.split(",");
		disease = temp [0];
		age = temp [1];
		gender = temp [2];
	//	objectSetter(line);
		
		
	}

	public Text getkey () {
		String temp = disease + gender + age;
		Text temp1 = new Text(temp);
		return temp1;
		
	}
	
	
	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("WRite");
		arg0.writeChars(disease);
		arg0.writeChars(age);
		arg0.writeChars(gender);
	}
	
	private int [] getAge (String age){
		String[] temp = age.split("/");
		int day,month,year;
		month  = convertString(temp [0]);
		day = convertString(temp [1]);
		year  = convertString(temp [2]);
		int a [] = {month,day,year};
		return a;
	}
	
	
	public int convertString (String a){
		StringBuilder sb = new StringBuilder();
 		int v=0;
 		for (char ch : a.toCharArray()) {
 		  if (Character.isDigit(ch)) {
 		    sb.append(ch);
 		  }
 		}
 		if (sb.toString().length() > 0) {
 		   v = Integer.valueOf(sb.toString());
 		}
 		return v;
	}
	
	@Override
	public int compareTo(TemperatureObject arg0) {
		// TODO Auto-generated method stub
		System.out.println("Compareto");
		int currentAge[]  = getAge(age); 
		int incomingAge[] = getAge(arg0.age);

		int currentAge1 = calculateAge(currentAge[2],currentAge[0],currentAge[1]);
		int incomingAge1 = calculateAge(incomingAge[2],incomingAge[0],incomingAge[1]);
		
//		return -1;
	return (currentAge1 > incomingAge1? 1 :-1 );
	}

	
	
}


public class TemperatureCalc 

{
	public static class  MapTemp extends MapReduceBase implements Mapper<LongWritable, Text, TemperatureObject, IntWritable> 
	{

		@Override
		public void map(LongWritable name, Text value,
				OutputCollector<TemperatureObject, IntWritable> output, Reporter arg3)
						throws IOException {
			// TODO Auto-generated method stub
		
			TemperatureObject obj = new TemperatureObject();
			obj.objectSetter(value.toString());
			
			output.collect(obj, new IntWritable(1));
			//	System.out.print("EOF");
			//	tokenizer.nextToken("\\n");
			}
		}
	
			public static class Reduce1 extends MapReduceBase implements
			Reducer<TemperatureObject, IntWritable, Text, IntWritable> {
	
				public void reduce(TemperatureObject key, Iterator<IntWritable> values,
						OutputCollector<Text, IntWritable> output, Reporter reporter)
								throws IOException {
					System.out.println("Reducer");
					int sum = 0;
					while (values.hasNext()) {
						sum += values.next().get();
					}
	
					output.collect(key.getkey(), new IntWritable (sum));
				}
			}
		
	
	public static void main(String[] args) throws Exception {

		JobConf conf = new JobConf(TemperatureCalc.class);
		conf.setJobName("diseasecount");

		conf.setOutputKeyClass(Text.class);
		conf.setMapOutputKeyClass(TemperatureObject.class);
		//conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(MapTemp.class);
		conf.setReducerClass(Reduce1.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		Random rand = new Random();

		//Healthcare
		String input  = "///Users//ramranji//big data resources//tests//Healthcare//healthcare_Sample_dataset1copy.csv" ;
		String output = "///Users//ramranji//Desktop//" + rand.toString();

		FileInputFormat.setInputPaths(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));

		
//		FileInputFormat.setInputPaths(conf, new Path(args[0]));
//		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);

	}

}
	


