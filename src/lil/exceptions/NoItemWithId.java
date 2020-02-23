package src.lil.exceptions;

public class NoItemWithId extends Exception {
	public NoItemWithId(Integer _id){
		super("No item with id "+_id +" found!");
	}
}
