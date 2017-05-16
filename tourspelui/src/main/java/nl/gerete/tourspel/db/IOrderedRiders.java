package nl.gerete.tourspel.db;

import javax.annotation.*;

public interface IOrderedRiders {
	int getPlace();

	void setPlace(int i);

	@Nullable
	Rider getRider();

	void setRider(@Nonnull Rider r);

}
