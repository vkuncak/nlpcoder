package statistics;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import rules.Rule;
import symbol.Symbol;
import util.Pair;

public class RuleStatisticsBase {

	private static Map<Symbol, RuleGroupStatistics> headToRuleGroup = new HashMap<Symbol, RuleGroupStatistics>();

	private RuleGroupStatistics getRuleGroupStatistics(Rule rule){
		
		//TODO: Maybe we should group all symbols based on primary head non-terminal, not only based on non-terminal^parent^grandparent.
		//This way, we will have a better distribution.
		//This can be delay this until collecting statistics.
		Symbol head = rule.getHead();
		
		if (!headToRuleGroup.containsKey(head)){
			headToRuleGroup.put(head, new RuleGroupStatistics(head));
		}
		
		return headToRuleGroup.get(head);
	}
	
	public void inc(Rule rule){
		RuleGroupStatistics group = getRuleGroupStatistics(rule);
		group.inc(rule);
	}
	
	//TODO: printToFile
	
	public void print(PrintStream out){
		for(RuleGroupStatistics stat: this.headToRuleGroup.values()){
			stat.print(out);
		}
	}

	public void incCounter(Rule rule) {
		RuleGroupStatistics group = getRuleGroupStatistics(rule);
		group.incCount();
	}
	
	public void releaseUnder(int percentage){
		int released = 0;
		int remaining = 0;
		for(RuleGroupStatistics group: headToRuleGroup.values()){
			Pair<Integer, Integer> pair = group.releaseUnder(percentage);
			released += pair.getFirst();
			remaining += pair.getSecond();
		}
		
		System.out.println("Released: "+released+" ruels.  Remaining: "+remaining+" rules");
	}
}
