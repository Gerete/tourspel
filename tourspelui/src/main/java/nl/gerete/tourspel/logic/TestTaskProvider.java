package nl.gerete.tourspel.logic;

import to.etc.util.*;

public class TestTaskProvider implements ISystemTaskProvider {
	@Override
	public ISystemTask getTask() throws Exception {
		return new ISystemTask() {
			@Override
			public void execute(Progress p) throws Exception {
				p.setTotalWork(100.0);
				for(int i = 0; i < 100; i++) {
					Thread.sleep(1000);
					p.increment(1);
				}
			}

			@Override
			public String getName() {
				return "Test job";
			}
		};
	}

}
