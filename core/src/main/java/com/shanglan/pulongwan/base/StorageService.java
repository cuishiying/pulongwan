package com.shanglan.pulongwan.base;

import java.io.InputStream;

import javax.servlet.http.HttpSession;

import com.aliyun.oss.model.ObjectListing;

public interface StorageService {

	/**
	 * 上传一个图片文件到存储服务。与普通文件的区别是目前图片文件会使用CDN加速
	 * 这个接口要求必须知道上传文件的大小。如果无法确定上传文件大小，则需要调用其它接口来把保存。（暂未提供）
	 * 
	 * @param fileName 文件名
	 * @param folderName 上传到的文件夹名称
	 * @param extName 文件扩展名
	 * @param fileLength 文件长度
	 * @param inputStream 上传的文件输入流
	 * @param isEncod 文件名称是否加密
	 * @return 生成的文件访问的URL
	 */
	String uploadImg(String folderName, String fileName, String extName, long fileLength, InputStream inputStream, boolean isEncod);
	
	/**
	 * 上传一个普通文件到存储服务。暂时不使用图片加速
	 * 这个接口要求必须知道上传文件的大小。如果无法确定上传文件大小，则需要调用其它接口来把保存。（暂未提供）
	 * 
	 * @param folderName 上传到的文件夹名称
	 * @param extName 文件扩展名
	 * @param fileLength 文件长度
	 * @param inputStream 上传的文件输入流
	 * @param isEncod 文件名称是否加密
	 * @return 生成的文件访问的URL
	 */
	String uploadFile(String folderName, String fileName, String extName, long fileLength, InputStream inputStream, boolean isEncod, HttpSession session);

	/**
	 * 获取oss临时存储url使用CDN加速
	 * 
	 * @param fileName 文件名
	 * @param extName 文件扩展名
	 * @param fileLength 文件长度
	 * @param inputStream 上传的文件输入流
	 * @return 生成的临时文件访问的URL
	 */
	String getImgTempUrl(String fileName, String extName, long fileLength, InputStream inputStream);
	
	/**
	 * 获取oss临时存储url
	 * 
	 * @param fileName 文件名
	 * @param extName 文件扩展名
	 * @param fileLength 文件长度
	 * @param inputStream 上传的文件输入流
	 * @return 生成的临时文件访问的URL
	 */
	String getFileTempUrl(String fileName, String extName, long fileLength, InputStream inputStream);
	
	/***
	 * 将文件从临时文件夹复制到实际文件夹中
	 * @param folderName 上传到的文件夹名称
	 * @param tempUrl 文件临时URL
	 * @param isEncod 文件名称是否加密
	 * @return 生成的实际文件访问URL
	 */
	String copyImg(String folderName, String tempUrl, boolean isEncod);
	
	/***
	 * 将文件从临时文件夹复制到实际文件夹中
	 * @param folderName 上传到的文件夹名称
	 * @param tempUrl 文件临时URL
	 * @param isEncod 文件名称是否加密
	 * @return 生成的实际文件访问URL
	 */
	String copyFile(String folderName, String tempUrl, boolean isEncod);
	
	/***
	 * 删除存储服务中的object
	 * @param key OSSObject的key（文件URL）
	 */
	void delete(String url);
	
	/**
	 * 获取某个bucket的某个文件夹下的图片(带分页功能)
	 * @param bucketName
	 * @param prefix 前缀（文件夹名）
	 * @param marker 获取list的起始位置
	 * @param maxKeys 返回的object数量
	 * @return ObjectListing对象
	 */
	ObjectListing listObject(String bucketName, String prefix, String marker, Integer maxKeys);
}
