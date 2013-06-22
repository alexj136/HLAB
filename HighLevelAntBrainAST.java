class BlockDeclaration {
	public final String blockName;
	public final Statement[] statements;
	public BlockDeclaration(String blockName, Statement[] statements) {
		this.blockName = blockName;
		this.statements = statements;
	}
}

abstract class Statement {}

class While extends Statement {
	public final Condition cond;
	public final SenseDir senseDir;
	public final Statement[] loopBody;
	public While(Condition cond, SenseDir senseDir, Statement[] loopBody) {
		this.cond = cond;
		this.senseDir = senseDir;
		this.loopBody = loopBody;
	}
}

abstract class LinearInstruction extends Statement {}

class Mark extends LinearInstruction {
    public final int markerNo;
    public Mark(int markerNo) {
        if(markerNo < 0 || markerNo > 5) {
            throw new IllegalArgumentException(Integer.toString(markerNo));
        }
        this.markerNo = markerNo;
    }
}

class Unmark extends LinearInstruction {
    public final int markerNo;
    public Unmark(int markerNo) {
        if(markerNo < 0 || markerNo > 5) {
            throw new IllegalArgumentException(Integer.toString(markerNo));
        }
        this.markerNo = markerNo;
    }
}

class Turn extends LinearInstruction {
	public final TurnDir turnDir;
	public Turn(TurnDir turnDir) {
		this.turnDir = turnDir;
	}
}

class BlockCall extends LinearInstruction {
	public final String blockName;
	public BlockCall(String blockName) {
		this.blockName = blockName;
	}
}

class Drop extends LinearInstruction {}

abstract class BranchingInstruction extends Statement {
	public final Statement[] trueBranch;
	public final Statement[] falseBranch;
	public BranchingInstruction(Statement[] trueBranch, Statement[] falseBranch) {
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}
}

class Move extends BranchingInstruction {
	public Move (Statement[] trueBranch, Statement[] falseBranch) {
		super(trueBranch, falseBranch);
	}
}

class PickUp extends BranchingInstruction {
	public PickUp (Statement[] trueBranch, Statement[] falseBranch) {
		super(trueBranch, falseBranch);
	}
}

class Flip extends BranchingInstruction {
	public final int flipNo;
	public Flip (int flipNo, Statement[] trueBranch, Statement[] falseBranch) {
		super(trueBranch, falseBranch);
		this.flipNo = flipNo;
	}
}

class If extends BranchingInstruction {
	public final Condition cond;
	public final SenseDir senseDir;
	public If(Condition cond, SenseDir senseDir, Statement[] trueBranch, Statement[] falseBranch) {
		super(trueBranch, falseBranch);
		this.cond = cond;
		this.senseDir = senseDir;
	}
}

abstract class Condition {}
class Foe extends Condition {}
class Friend extends Condition {}
class FoeWithFood extends Condition {}
class FriendWithFood extends Condition {}
class Food extends Condition {}
class Rock extends Condition {}
class FoeHome extends Condition {}
class Home extends Condition {}
class FoeMarker extends Condition {}
class Marker extends Condition {
    public final int markerNo;
    public Marker(int markerNo) {
        if(markerNo < 0 || markerNo > 5) {
            throw new IllegalArgumentException(Integer.toString(markerNo));
        }
        else {
            this.markerNo = markerNo;
        }
    }
}

enum SenseDir {
    Here,
    Ahead,
    LeftAhead,
    RighAhead
}

enum TurnDir {
    Left,
    Right
}