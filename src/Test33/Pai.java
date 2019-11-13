package Test33;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.IntWritable;  
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
public class Pai {

	public static List<String> Names=new ArrayList<String>();
	public static  List<String> Values=new ArrayList<String>();
	public static class Sort extends WritableComparator {
		public Sort(){
		//这里就是看你map中填的输出key是什么数据类型，就给什么类型
		super(IntWritable.class,true);
		}
		@Override
		public int compare(WritableComparable a, WritableComparable b) {
		return -a.compareTo(b);//加个负号就是倒序，把负号去掉就是正序。
		}
		}
	public static class Map extends Mapper<Object , Text , IntWritable,Text >{  
	private static Text Name=new Text();
	private static IntWritable num=new IntWritable();
	public void map(Object key,Text value,Context context)throws IOException, InterruptedException
	{
		 String line=value.toString();  
		    String arr[]=line.split("\t");  
		    if(!arr[0].startsWith(" "))
		    {
		    	  num.set(Integer.parseInt(arr[1]));  
		    	  Name.set(arr[0]);
		    	  context.write(num, Name);
		    }
		  
	}
	}
	public static class Reduce extends Reducer< IntWritable, Text, Text, IntWritable>{  
		private static IntWritable result= new IntWritable();  
		int i=0;
		
		 public void reduce(IntWritable key,Iterable<Text> values,Context context) throws IOException, InterruptedException{  
		        for(Text val:values){  
		        	
		        	if(i<10)
		        	{i=i+1;
		        		Names.add(val.toString());
		        		Values.add(key.toString());
		        	}
		        context.write(val,key);  
		        }  
	}
	}

  
	
 
	
	public static int run()throws IOException, ClassNotFoundException, InterruptedException{
		Configuration conf=new Configuration();  
		conf.set("fs.defaultFS", "hdfs://192.168.43.102:9000");
		FileSystem fs =FileSystem.get(conf);
        Job job =new Job(conf,"OneSort");  
        job.setJarByClass(Pai.class);  
        job.setMapperClass(Map.class);  
        job.setReducerClass(Reduce.class);  
        job.setSortComparatorClass(Sort.class);
        job.setOutputKeyClass(IntWritable.class);  
        job.setOutputValueClass(Text.class);  
        job.setInputFormatClass(TextInputFormat.class);  
        job.setOutputFormatClass(TextOutputFormat.class);  
        Path in=new Path("hdfs://192.168.43.102:9000/test/res33/part-r-00000");  
        Path out=new Path("hdfs://192.168.43.102:9000/test/res323");  
        FileInputFormat.addInputPath(job,in);  
        fs.delete(out,true);
        FileOutputFormat.setOutputPath(job,out);  
       return(job.waitForCompletion(true) ? 0 : 1);  
        
       
        }
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{  
	      run();
	       for(String n:Names)
	        {
	        	System.out.println(n);
	           }
	       for(String c:Values)
	       {
	    	   System.out.println(c);
	       }
	      } 
}
