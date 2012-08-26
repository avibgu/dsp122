package step52;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.HookTargetPair;
import data.Pattern;
import data.Word;

public class Mapper52 extends Mapper<Word, Pattern, HookTargetPair, Pattern> {

	public HookTargetPair mHookTargetPair;
	
	@Override
	protected void setup(Context pContext)
			throws IOException, InterruptedException {
		mHookTargetPair = new HookTargetPair();
	}
		
	protected void map(Word hookWord, Pattern pattern, Context context)
			throws IOException, InterruptedException {
		
		for (Word target : pattern.getTargets()) {
			mHookTargetPair.set(hookWord, target);
			context.write(mHookTargetPair, pattern);
		}
	}
}
