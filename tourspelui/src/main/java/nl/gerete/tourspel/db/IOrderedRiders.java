package nl.gerete.tourspel.db;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IOrderedRiders {
	int getPlace();

	void setPlace(int i);

	@Nullable
	Rider getRider();

	void setRider(@NonNull Rider r);

}
