package nl.gerete.tourspel.logic;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.util.*;

public class SystemTask {
	static private final int WAITTIME = 5 * 60 * 1000;

	static private final SystemTask m_instance = new SystemTask();

	private ISystemTask m_currentTask;

	private Thread m_thread;

	private Progress m_progress;

	@NonNull
	private List<ISystemTaskProvider> m_providerList = Collections.EMPTY_LIST;

	private int m_lastProviderIndex;

	private long m_tsNextTry;

	final private Map<String, Ref> m_refMap = new HashMap<String, Ref>();

	private boolean m_terminate;

	static private class Ref {
		final private String m_key;

		final private ISystemTask m_task;

		private String m_result;

		private long m_ts;

		public Ref(ISystemTask task) {
			m_key = StringTool.generateGUID();
			m_task = task;
			m_ts = System.currentTimeMillis();
		}

		public String getKey() {
			return m_key;
		}

		public ISystemTask getTask() {
			return m_task;
		}
	}

	static public SystemTask	getInstance() {
		return m_instance;
	}

	synchronized public ISystemTask getCurrentTask() {
		return m_currentTask;
	}

	/**
	 * Start the task worker. This will ask the "task-to-do" list for new tasks every 5 minutes.
	 */
	synchronized public void initialize() {
		if(m_thread != null)
			throw new IllegalStateException("Already initialized");
		m_terminate = false;
		m_tsNextTry = System.currentTimeMillis() + 60 * 1000;				// First run is one minute after server start
		m_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				taskScheduler();
			}
		});
		m_thread.setDaemon(false);
		m_thread.setName("SystemTask");
		m_thread.start();
	}

	/**
	 * Stop the thingy.
	 */
	synchronized public void	terminate() {
		if(m_thread == null)
			return;
		m_terminate = true;
		try {
			m_thread.notifyAll();
			m_thread.join();
		} catch(InterruptedException x) {
			x.printStackTrace();
		}
	}

	synchronized public void register(ISystemTaskProvider provider) {
		m_providerList = new ArrayList<ISystemTaskProvider>(m_providerList);
		m_providerList.add(provider);
	}

	private synchronized List<ISystemTaskProvider> getProviders() {
		return m_providerList;
	}

	private void taskScheduler() {
		try {
			while(runOnce())
				;
		} catch(Exception x) {
			System.out.println("SystemTask: abnormal termination: "+x);
			x.printStackTrace();
		} catch(Error x) {
			System.out.println("SystemTask: abnormal termination: "+x);
			x.printStackTrace();
			throw x;
		}
		System.out.println("SystemTask: Terminating.");
	}

	private boolean runOnce() throws Exception {
		List<ISystemTaskProvider> list;
		synchronized(this) {
			if(m_terminate) {
				System.out.println("SystemTask: scheduler stopped");
				return false;
			}
			long ts = System.currentTimeMillis();
			if(ts < m_tsNextTry) {
				ts = m_tsNextTry - ts;
				wait((int) ts);
				return true;
			}
			list = m_providerList;
		}

		//-- We actually need to check.... Walk all providers, starting @ the last one
		for(int i = 0; i < list.size(); i++) {
			if(m_lastProviderIndex >= list.size())
				m_lastProviderIndex = 0;
			ISystemTaskProvider p = list.get(m_lastProviderIndex++);

			ISystemTask task = null;
			try {
				task = p.getTask();
			} catch(Exception x) {
				System.out.println(p + ": exception in getTask!!");
				x.printStackTrace();
			}
			if(task != null) {
				//-- We have a task - execute it, then exit -> this will give the next provider a chance immediately after.
				try {
					Progress progress;
					synchronized(this) {
						progress = m_progress = new Progress(task.getName());
						m_currentTask = task;
					}
					System.out.println("SystemTask: starting " + task);
					task.execute(progress);
				} catch(Exception x) {
					x.printStackTrace();
				} finally {
					System.out.println("SystemTask: finished " + task);
					synchronized(this) {
						m_currentTask = null;
						m_progress = null;
					}
				}

				return true;
			}
		}

		//-- None of the providers had any work! Make us sleep for 5 minutes.
		synchronized(this) {
			m_tsNextTry = System.currentTimeMillis() + 5 * 60 * 1000;
		}
		return true;
	}

	@Nullable
	public synchronized Progress getProgress() {
		return m_progress;
	}

	/**
	 * Immediately start the system task thingy, and try to start the specified task.
	 * @param provider
	 */
	public synchronized void punch(@Nullable ISystemTaskProvider provider) {
		m_tsNextTry = System.currentTimeMillis();						// Run asap
		notifyAll();
		for(int i = 0; i < m_providerList.size(); i++) {				// Try to locate the provider to run.
			if(m_providerList.get(i) == provider) {
				m_lastProviderIndex = i;								// Make sure this one's tried 1st
				break;
			}
		}
	}
}
