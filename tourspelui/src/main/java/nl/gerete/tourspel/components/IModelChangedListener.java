package nl.gerete.tourspel.components;

import org.eclipse.jdt.annotation.*;

public interface IModelChangedListener<T> {
	void onValueAdded(@NonNull T addedEntry) throws Exception;

	void onValueRemoved(@NonNull T removedEntry) throws Exception;
}
