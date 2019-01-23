package com.excelhk.openapi.demoservice.utils;

/**
 *
 * @author anita
 */

public class IdWorker {

	private final long WORKER_ID;
	/**
	 * milliseconds from January 1, 1970
	 * 2018-10-01
	 */
	private final static long TWEPOCH = 1514736000000L;
	private long sequence = 0L;
	private final static long WORKER_ID_BITS = 4L;
	public final static long MAX_WORKER_ID = -1L ^ -1L << WORKER_ID_BITS;
	private final static long SEQ_BITS = 10L;
	private final static long WORKER_ID_SHIFT = SEQ_BITS;
	private final static long  TIMESTAMP_LEFT_SHIFT  = SEQ_BITS + WORKER_ID_BITS;
	public final static long SEQ_MASK = -1L ^ -1L << SEQ_BITS;
	private long lastTimestamp = -1L;

	public IdWorker(final long workerId) {
		super();
		if (workerId > MAX_WORKER_ID || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
		}
		this.WORKER_ID = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & SEQ_MASK;
			if (this.sequence == 0) {
				System.out.println("###########" + SEQ_MASK);
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}
		if (timestamp < this.lastTimestamp) {
			try {
				throw new Exception(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
						this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - TWEPOCH << TIMESTAMP_LEFT_SHIFT)) | (WORKER_ID << WORKER_ID_SHIFT)
				| (this.sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}
