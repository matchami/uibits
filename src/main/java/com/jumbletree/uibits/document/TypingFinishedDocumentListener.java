package com.jumbletree.uibits.document;

import java.util.Date;
import java.util.HashMap;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * A document listener that fires not when the document is added to or removed from but when a 
 * set of additions or removals are completed.  This is achieved by waiting for a short time of
 * inactivity after the most recent additional/removal before triggering. 
 * 
 * changedUpdate actions are <b>not</b> monitored by this listener
 * @author me
 *
 */
public abstract class TypingFinishedDocumentListener implements DocumentListener {

	private static final String KEY_PROPERTY_KEY = "com.jumbletree.uibits.document.TypeingFinishedDocumentListener.hash.key";
	public class Timer implements Runnable {

		private Double key;
		private Document doc;

		public Timer(Double key, Document doc) {
			this.key = key;
			this.doc = doc;
		}

		@Override
		public void run() {
			while (true) {
				//Four second delay
				long finishAt = lastChangedAts.get(key).getTime() + millisToWait;
				Date now = new Date();
				if (now.getTime() >= finishAt) {
					break;
				}
				try {
					Thread.sleep(finishAt - now.getTime());
				} catch (InterruptedException e) {
				}
			}
			endTimer(key, doc);
		}

	}

	HashMap<Double, Date> lastChangedAts;
	private HashMap<Double, Thread> timerThreads;
	int millisToWait;

	public TypingFinishedDocumentListener() {
		this(500);
	}
	
	public TypingFinishedDocumentListener(int millisToWait) {
		this.millisToWait = millisToWait;
		this.timerThreads = new HashMap<Double, Thread>();
		this.lastChangedAts = new HashMap<Double, Date>();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public final void insertUpdate(final DocumentEvent e) {
		startTimer(e);
	}

	@Override
	public final void removeUpdate(DocumentEvent e) {
		startTimer(e);
	}

	private synchronized void startTimer(DocumentEvent e) {
		//The default implementations of Document implement neither hashCode, nor equals, 
		//so using a HashMap keyed from the document is not going to work.  Instead we store
		//a unique key using the documents custom properties
		Document doc = e.getDocument();
		Double key = (Double)doc.getProperty(KEY_PROPERTY_KEY);
		if (key == null) {
			key = Math.random();
			doc.putProperty(KEY_PROPERTY_KEY, key);
		}
		
		//Now we can actually find out if there is a thread for this document already
		Thread timerThread = timerThreads.get(key);
		this.lastChangedAts.put(key, new Date());
		if (timerThread == null) {
			timerThread = new Thread(new Timer(key, doc));
			this.timerThreads.put(key, timerThread);
			timerThread.start();
		}
	}
	
	synchronized void endTimer(Double key, final Document doc) {
		this.lastChangedAts.put(key, null);
		this.timerThreads.put(key, null);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textChanged(doc);
			}
		});
	}

	public abstract void textChanged(Document doc);
}
