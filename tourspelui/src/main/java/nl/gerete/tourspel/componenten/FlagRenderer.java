package nl.gerete.tourspel.componenten;

import nl.gerete.tourspel.*;
import nl.gerete.tourspel.db.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.util.*;

public class FlagRenderer implements INodeContentRenderer<Object> {
	@Override
	public void renderNodeContent(NodeBase component, NodeContainer node, Object o, Object parameters) throws Exception {
		Country c = null;

		if(o instanceof Rider)
			c = ((Rider) o).getCountry();
		else if(o instanceof Team)
			c = ((Team) o).getCountry();
		else if(o instanceof IOrderedRiders) {
			c = ((IOrderedRiders) o).getRider().getCountry();
		}
		else if(o instanceof Country) {
			c = (Country) o;
		}
		if(c != null) {
			Img i = new Img(TourUtil.getFlag(c));
			node.add(i);
			node.setTitle(c.getName());
		}
	}
}
