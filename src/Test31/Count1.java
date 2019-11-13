package Test31;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import  org.apache.hadoop.io.Text;

/*
 * 对清洗好的数据进行统计
 */

public class Count1 {
	Configuration conf=new Configuration();
	/*public static void main(String a[]) throws ClassNotFoundException, IOException, InterruptedException
	{
		int result =-1;
		result=new Count1().run();
		if(result==0)
			System.out.println("成功");
		else
			System.out.println("失败");
	}*/
	public  int run() throws IOException, ClassNotFoundException, InterruptedException
	{
		conf.set("fs.defaultFS", "hdfs://192.168.43.102:9000");
		//conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
		FileSystem fs =FileSystem.get(conf);
		Job job=new Job();
		job.setJobName("Count1");
		job.setJarByClass(Count1.class);
		job.setMapperClass(doMapper.class);
		job.setReducerClass(doReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		Path in =new Path("hdfs://192.168.43.102:9000/test/res2/part-r-00000");
		Path out  =new Path("hdfs://192.168.43.102:9000/test/res31");
		FileInputFormat.addInputPath(job, in);
		fs.delete(out,true);
		FileOutputFormat.setOutputPath(job, out);
		System.out.println(job.waitForCompletion(true)?0:1);
		return job.waitForCompletion(true)?0:1;
	}
}


 class doMapper extends Mapper<Object, Text,Text, IntWritable> {
	public  final IntWritable one =new IntWritable(1);
	public  Text word=new Text();
	@Override
	protected void map(Object key, Text value,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//StringTokenizer tokenizer =new StringTokenizer(value.toString(), ",");
		//word.set(tokenizer.nextToken());
		String[] arr=value.toString().split("\t"); //根据\t进行切分
		
			word.set(arr[0] );
		
		
		context.write(word, one);
	}
}
class doReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	private IntWritable result=new IntWritable();
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int sum=0;
		for(IntWritable value: values)
		{
			sum+=value.get();
			
		}
		result.set(sum);
		context.write(key, result);
	}
}
