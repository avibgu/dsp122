package step51;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.HookTargetPair;
import data.Pattern;

public class Mapper51 extends Mapper<Text, Pattern, HookTargetPair, Pattern> {

	public HookTargetPair mHookTargetPair;
	
	@Override
	protected void setup(Context pContext)
			throws IOException, InterruptedException {
		mHookTargetPair = new HookTargetPair();
	}
		
	protected void map(Text hookWord, Pattern pattern, Context context)
			throws IOException, InterruptedException {
		
		String hook = hookWord.toString();
		
		for (String target : pattern.getTargetsAsSet()) {
			mHookTargetPair.set(hook, target);
			context.write(mHookTargetPair, pattern);
		}
	}
}
