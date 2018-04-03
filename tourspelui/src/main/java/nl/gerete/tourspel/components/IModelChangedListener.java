package nl.gerete.tourspel.components;

import javax.annotation.*;

public interface IModelChangedListener<T> {
	void onValueAdded(@Nonnull T addedEntry) throws Exception;

	void onValueRemoved(@Nonnull T removedEntry) throws Exception;
}
