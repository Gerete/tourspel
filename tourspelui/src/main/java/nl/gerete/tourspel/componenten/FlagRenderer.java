package nl.gerete.tourspel.componenten;

import java.util.function.*;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.dom.html.*;
import to.etc.domui.util.*;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;

/**
 * FlagRenderer voor de vlaggen. Middels het gebruik van een functie wordt de Country meegegeven.
 *
 * @param <T>
 */
@NonNullByDefault
public class FlagRenderer<T> implements INodeContentRenderer<T> {

	private final Function<T, Country> m_converter;

	public FlagRenderer(Function<T, Country> converter) {
		m_converter = converter;
	}

	@Override
	public void renderNodeContent(@NonNull NodeBase component, @NonNull NodeContainer node, @Nullable T o, @Nullable Object parameters) {
		Country c = m_converter.apply(o);
		if(c == null) {
			return;
		}
		Img i = new Img(TourUtil.getFlag(c));
		node.add(i);
		node.setTitle(c.getName());
	}
}
