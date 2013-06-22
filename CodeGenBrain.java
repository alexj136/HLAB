import java.util.HashMap;
import java.util.ArrayList;

public class CodeGenBrain {
	
	private HashMap<String, RelocatableBlock> blocks;
	
	public CodeGenBrain() {
		this.blocks = new HashMap<String, RelocatableBlock>();
	}
	
	public String codeGenBrain(BlockDeclaration[] brain) {
		RelocatableBlock[] blocks = new RelocatableBlock[brain.length];
		for(int i = 0; i < brain.length; i++) {
			blocks[i] = codeGenBlock(brain[i]);
		}
		return "YET TO IMPLEMENT TRANSLATION FROM" +
				"RELOCATABLE BLOCKS TO ABSOLUTE CODE";
	}
	
	private void codeGenBlock(BlockDeclaration block) {
		ArrayList<RelocatableCode> newBlock = new ArrayList<RelocatableCode>();
		for(int i = 0; i < block.code.length; i++) {
			newBlock.add(codeGenStatement(block.code[i]));
		}
		
		this.blocks.put(block.blockName, theBlock);
	}
	
	private RelocatableCode codeGenStatement(Statement statement, int headerState) {
		
		if(statement instanceof Mark) {
			//Write the 'Mark' word, with marker no & next state
			return new RelocatableStatement(
				"Mark " + ((Mark)statement).markerNo,
				headerState);
		}
		else if(statement instanceof Unmark) {
			//Write the 'Unmark' word, with marker no & next state
			return "Unmark " + ((Unmark)statement).markerNo +
					" " + (++this.lineNumber) + "\n";
		}
		else if(statement instanceof Turn) {
			//Write the 'Turn' word, with left/right & next state
			return "Turn " + ((Turn)statement).turnDir.name() +
					" " + (++this.lineNumber) + "\n";
		}
		else if(statement instanceof BlockCall) {
			return "NOT SURE WHAT TO DO HERE YET";
		}
		else if(statement instanceof Drop) {
			//Write the 'Drop' word, with next state
			return "Drop " + (++this.lineNumber) + "\n";
		}
		else if(statement instanceof Move) {
			//Record the line number that this move instruction should go on
			int moveStatementLine = ++this.lineNumber;
			
			//Iterate over each substatement and get them in string form
			String moveStatements = "";
			for(int i = 0; i < ((BranchingInstruction)statement)
					.trueBranch.length; i++) {
				
				moveStatements += codeGenStatement(
						((BranchingInstruction)statement).trueBranch[i]);
			}
			return "Move " + (++this.lineNumber) + " " + (moveStatementLine + 1) + "\n" + moveStatements;
		}
		else if(statement instanceof PickUp) {
			int pickupStatementLine = ++this.lineNumber;
			String pickupStatements = "";
			for(int i = 0; i < ((BranchingInstruction)statement)
					.trueBranch.length; i++) {
				pickupStatements += codeGenStatement(
						((BranchingInstruction)statement).trueBranch[i]);
			}
			return "PickUp " + (++this.lineNumber) + " " + (pickupStatementLine + 1) + "\n" + pickupStatements;
		}
		else if(statement instanceof Flip) {
			int flipStatementLine = ++this.lineNumber;
			String flipStatements = "";
			for(int i = 0; i < ((BranchingInstruction)statement).trueBranch.length; i++) {
				flipStatements += codeGenStatement(((BranchingInstruction)statement).trueBranch[i]);
			}
			return "Flip " + ((Flip)statement).flipNo + " " + (++this.lineNumber) + " " + (flipStatementLine + 1) + "\n" + flipStatements;
		}
		else if(statement instanceof If) {
			int ifStatementLine = ++this.lineNumber;
			String ifStatements = "";
			for(int i = 0; i < ((BranchingInstruction)statement).trueBranch.length; i++) {
				ifStatements += codeGenStatement(((BranchingInstruction)statement).trueBranch[i]);
			}
			return "Sense " + ((If)statement).senseDir.name() + " " + (++this.lineNumber) + " " + (ifStatementLine + 1) + " " + CodeGenBrain.codeGenCond(((If)statement).cond) + "\n" + ifStatements;
		}
		//THIS IS WHERE I GOT TO
		else return "";
	}
	
	public static String codeGenCond(Condition cond) {
		if(cond instanceof Foe) return "Foe";
		else if(cond instanceof Friend) return "Friend";
		else if(cond instanceof FoeWithFood) return "FoeWithFood";
		else if(cond instanceof FriendWithFood) return "FriendWithFood";
		else if(cond instanceof Food) return "Food";
		else if(cond instanceof Rock) return "Rock";
		else if(cond instanceof FoeHome) return "FoeHome";
		else if(cond instanceof Home) return "Home";
		else if(cond instanceof FoeMarker) return "FoeMarker";
		else if(cond instanceof Marker) return "Marker " + ((Marker)cond).markerNo;
		else throw new IllegalArgumentException("Not a valid condition type");
	}
}