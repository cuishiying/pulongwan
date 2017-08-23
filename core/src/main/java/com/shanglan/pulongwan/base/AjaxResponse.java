package com.shanglan.pulongwan.base;

public class AjaxResponse<R> {

	private boolean success;

	private String message;

	private R data;

	public static AjaxResponse<?> fail(String message) {
		AjaxResponse<?> resp = new AjaxResponse<>();
		resp.success = false;
		resp.message = message;
		return resp;
	}

	public static AjaxResponse<?> fail() {
		AjaxResponse<?> resp = new AjaxResponse<>();
		resp.success = false;
		return resp;
	}

	public static <R> AjaxResponse<R> success(R data) {
		AjaxResponse<R> resp = new AjaxResponse<>();
		resp.success = true;
		resp.data = data;
		return resp;
	}

	public static AjaxResponse<?> success() {
		AjaxResponse<?> resp = new AjaxResponse<>();
		resp.success = true;
		return resp;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public R getData() {
		return data;
	}

	public void setData(R data) {
		this.data = data;
	}
}
