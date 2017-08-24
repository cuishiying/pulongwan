package com.shanglan.pulongwan.timetask;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * 数据中心定时任务
 *
 * @author Li Zhenbang
 * @date 创建时间：2017年3月6日 上午9:46:56
 * @version 1.0
 */

public class RecordsCenterTiming {

	@Autowired
	private DbService dbService;


	private DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private Calendar calendar = Calendar.getInstance();

	//当前系统时间
	private String currentTime = LocalDateTime.now().format(formatTime);
	//定时执行时间
	private String timingExecuteTime = LocalDate.parse(LocalDate.now().format(formatDate)).atTime(23, 50, 00).format(formatTime);

	/**
	 * 用户来源定时
	 */
	public void userSourceTiming() {
		AjaxResponse ajaxResponse = dbService.delOldData(getBeforeDate());
	}

	/**
	 * 获取1月前的时间
	 * @return
	 */
	public LocalDateTime getBeforeDate(){
		Date dBefore = new Date();
		calendar.setTime(LocalDateTimeToUdate());
		calendar.add(calendar.MONTH, -1);
		dBefore = calendar.getTime();
		LocalDateTime time = UDateToLocalDateTime(dBefore);
		return time;

	}
	public Date LocalDateTimeToUdate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return Date.from(instant);
	}

	public LocalDateTime UDateToLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return localDateTime;
	}

}
