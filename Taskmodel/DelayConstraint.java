package Taskmodel;

public class DelayConstraint {

	private ConstraintType type;
	private Time upperBound;
	
	public DelayConstraint(Time _upperBound, ConstraintType _type) {
		type = _type;
		upperBound = _upperBound;
	}
	
	public ConstraintType getType() {
		return type;
	}
	
	public Time getUpperBound() {
		return upperBound;
	}
	
	@Override
	public String toString() {
		if(type == ConstraintType.AGE_CONSTRAINT) {
			return "Data-Age Constraint <= " + upperBound.toStringShort();
		}else if (type == ConstraintType.REACTION_CONSTRAINT) {
			return "Reaction Constraint <= " + upperBound.toStringShort();
		}else {
			return null;	//should never reach here...
		}
	}
}
