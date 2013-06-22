import java.util.HashSet;
import java.util.ArrayList;

abstract class Relocatable {
	public abstract String getAbsoluteCode(int headerState);
}

class RelocatableBlock extends Relocatable {
	
	private HashSet<RelocatableBlock> blocks;
	
	private ArrayList<RelocatableCode> code;
	
	private String name;
	
	public RelocatableBlock(HashSet<RelocatableBlock> blocks,
			ArrayList<RelocatableCode> code, String name) {
		
		this.code = code;
		this.blocks = blocks;
		this.name = name;
	}
	
	public RelocatableCode get(int index) {
		return this.code.get(index);
	}
	
	public ArrayList<RelocatableCode> getCode() {
		return this.code;
	}
	
	public int size() {
		return this.code.size();
	}
	
	/**
	 * Inserts a block of code into this block of code, updating all state
	 * references accordingly
	 */
	public void insertBlock(int insertionLocation, RelocatableBlock newBlock) {
		
		// Update the offsets for the statements that come after the insertion
		// location of the new statements
		for(RelocatableCode s : this.code) {
			if(s instanceof RelocatableStatement) {
			
				// If the state pointed before the new block, nothing to do. If
				// not, it has to increase by the length of the new block
				if(((RelocatableStatement)s).state1 >= insertionLocation) {
					((RelocatableStatement)s).state1 += newBlock.size();
				}
				
				// The same applies for state2 if it exists
				if(((RelocatableStatement)s).state2 != -1 &&
						((RelocatableStatement)s).state2
								>= insertionLocation) {
					
					((RelocatableStatement)s).state2 += newBlock.size();
				}
			}
		}
		
		// Update the offsets for the new statements
		for(RelocatableCode s : newBlock.getCode()) {
			if(s instanceof RelocatableStatement) {
				((RelocatableStatement)s).state1 += insertionLocation;
				
				if(((RelocatableStatement)s).state2 != -1) {
					((RelocatableStatement)s).state2 += insertionLocation;
				}
			}
		}
		
		// Put the new code in place
		for(int i = newBlock.size() - 1; i >= 0; i--) {
			this.code.add(insertionLocation, newBlock.get(i));
		}
	}
	
	public String getAbsoluteCode(int headerState) {
		String absoluteblock = "";
		for(int i = 0; i < this.code.size(); i++) {
			absoluteblock += ((RelocatableStatement)this.code.get(i))
					.getAbsoluteCode(i) + "\n";
		}
		return absoluteblock;
	}
}

public abstract class RelocatableCode extends Relocatable {}

class RelocatableBlockCall extends RelocatableCode {
	
	public final String name;
	
	public RelocatableBlockCall(String name) {
		this.name = name;
	}
	
	public String getAbsoluteCode(int headerState) {
		return "UNSUBSTITUDED BLOCK: " + this.name;
	}
}

class RelocatableStatement extends RelocatableCode {
	
	public final String beforeState;
	/*package*/ int state1;
	/*package*/ int state2;
	public final String afterState;
	
	public RelocatableStatement(String beforeState, int state1) {
		this.beforeState = beforeState;
		this.state1 = state1;
		this.state2 = -1;
		this.afterState = "";
	}
	
	public RelocatableStatement(String beforeState, int state1, int state2) {
		this.beforeState = beforeState;
		this.state1 = state1;
		this.state2 = state2;
		this.afterState = "";
	}
	
	public RelocatableStatement(String beforeState, int state1,
			int state2, String afterState) {
		
		this.beforeState = beforeState;
		this.state1 = state1;
		this.state2 = state2;
		this.afterState = afterState;
	}
	
	public String getAbsoluteCode(int headerState) {
		String absoluteStatement = this.beforeState + " " +
				(this.state1 + headerState);
		if(this.state2 != -1) {
			absoluteStatement += " " + (this.state2 + headerState);
		}
		if(this.afterState != "") {
			absoluteStatement += " " + this.afterState;
		}
		return absoluteStatement;
	}
}