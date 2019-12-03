package com.udc.master.tfm.tracksports.common.chrono;

/**
 * Estado de un cronometro
 * @author a.oteroc
 *
 */
public enum ChronoStatus {

	STOPPED(1),
	RUNNING(2),
	PAUSED(3);
	
	private int status;
	
	private ChronoStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
