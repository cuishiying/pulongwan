package com.shanglan.pulongwan.base;

import javax.servlet.http.HttpSession;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;

public class FileUploadProgress implements ProgressListener {

	private long bytesWritten = 0;
	private long totalBytes = -1;
	private boolean succeed = false;
	private HttpSession session;

	public FileUploadProgress(HttpSession session) {
		this.session = session;
	}

	@Override
	public void progressChanged(ProgressEvent progressEvent) {
		long bytes = progressEvent.getBytes();
		ProgressEventType eventType = progressEvent.getEventType();
		switch (eventType) {
		case TRANSFER_STARTED_EVENT:
			System.out.println("Start to upload......");
			session.setAttribute("uploadStatus", new UploadStatus());
			break;

		case REQUEST_CONTENT_LENGTH_EVENT:
			this.totalBytes = bytes;
			UploadStatus status1 = (UploadStatus) session.getAttribute("uploadStatus");
			status1.setTotalBytes(bytes);
			session.setAttribute("uploadStatus", status1);
			System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
			break;

		case REQUEST_BYTE_TRANSFER_EVENT:
			this.bytesWritten += bytes;
			UploadStatus status2 = (UploadStatus) session.getAttribute("uploadStatus");
			status2.setBytesWritten(status2.getBytesWritten() + bytes);
			session.setAttribute("uploadStatus", status2);
			if (this.totalBytes != -1) {
				int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
				System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%("
						+ this.bytesWritten + "/" + this.totalBytes + ")");
			} else {
				System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "("
						+ this.bytesWritten + "/...)");
			}
			break;

		case TRANSFER_COMPLETED_EVENT:
			this.succeed = true;
			UploadStatus status3 = (UploadStatus) session.getAttribute("uploadStatus");
			status3.setSucceed(true);
			session.setAttribute("uploadStatus", status3);
			System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
			break;

		case TRANSFER_FAILED_EVENT:
			System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
			break;

		default:
			break;
		}
	}

	public boolean isSucceed() {
		return succeed;
	}
}
