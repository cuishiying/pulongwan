package com.shanglan.pulongwan.base;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;

public class AliyunNewOss implements StorageService {

	private static final String TEMP_FOLDER_NAME = "temp";
	private String ossUrl;
	private String bucketName;
	private String cdnName;

	private OSSClient client;

	private SecureRandom ng = new SecureRandom();

	public AliyunNewOss(String accessId, String accessKey, String endpoint, String bucketName, String cdnName) {
		try {
			URL url = new URL(endpoint);
			ossUrl = url.getProtocol() + "://" + bucketName + "." + url.getHost();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Cannot config Aliyun OSS URL!", e);
		}
		this.bucketName = bucketName;
		this.cdnName = cdnName;
		this.client = new OSSClient(endpoint, accessId, accessKey);

		// 判断bucketName是否存在，不存在则创建一个bucket
		boolean exists = client.doesBucketExist(bucketName);
		if (!exists) {
			client.createBucket(bucketName);
		}
	}

	@Override
	public String uploadImg(String folderName, String fileName, String extName, long fileLength,
			InputStream inputStream, boolean isEncod) throws OSSException, ClientException {
		String key = "";
		if (isEncod) {
			key = generateFilePath(folderName, extName); // 生成文件路径
		} else {
			key = handleFolderName(folderName) + fileName + extName;
		}
		client.putObject(bucketName, key, inputStream, creatMetadata(fileName, extName, fileLength));
		return cdnName + "/" + key;
	}

	@Override
	public String uploadFile(String folderName, String fileName, String extName, long fileLength,
			InputStream inputStream, boolean isEncod, HttpSession session) throws OSSException, ClientException {
		String key = "";
		if (isEncod) {
			key = generateFilePath(folderName, extName); // 生成文件路径
		} else {
			key = handleFolderName(folderName) + fileName + extName;
		}
		// client.putObject(bucketName, key, inputStream, creatMetadata(fileName, extName, fileLength));
		// 改为带进度条的上传
		client.putObject(
				new PutObjectRequest(bucketName, key, inputStream, creatMetadata(fileName, extName, fileLength))
						.withProgressListener(new FileUploadProgress(session)));
		return ossUrl + "/" + key;
	}

	@Override
	public String getImgTempUrl(String fileName, String extName, long fileLength, InputStream inputStream)
			throws OSSException, ClientException {
		String key = "";
		ObjectMetadata metadata = new ObjectMetadata();
		Map<String, String> userMetadata = new HashMap<String, String>();
		metadata.setContentLength(fileLength);
		metadata.setCacheControl("max-age=315360000"); // 设置过期时间为10年
		Date expire = new Date(new Date().getTime() + 3600 * 1000);
		metadata.setExpirationTime(expire); // 设置1小时后过期
		userMetadata.put("filename", fileName);
		userMetadata.put("extname", extName);
		metadata.setUserMetadata(userMetadata);
		key = TEMP_FOLDER_NAME + "/" + fileName + extName; // 临时文件url
		client.putObject(bucketName, key, inputStream, metadata);
		return cdnName + "/" + key;
	}

	@Override
	public String getFileTempUrl(String fileName, String extName, long fileLength, InputStream inputStream)
			throws OSSException, ClientException {
		String key = "";
		ObjectMetadata metadata = new ObjectMetadata();
		Map<String, String> userMetadata = new HashMap<String, String>();
		metadata.setContentLength(fileLength);
		metadata.setCacheControl("max-age=315360000"); // 设置过期时间为10年
		Date expire = new Date(new Date().getTime() + 3600 * 1000);
		metadata.setExpirationTime(expire); // 设置1小时后过期
		userMetadata.put("filename", fileName);
		userMetadata.put("extname", extName);
		metadata.setUserMetadata(userMetadata);
		key = TEMP_FOLDER_NAME + "/" + fileName + extName; // 临时文件url
		client.putObject(bucketName, key, inputStream, metadata);
		return ossUrl + "/" + key;
	}

	@Override
	public void delete(String url) {
		client.deleteObject(bucketName, getKey(url));
	}

	@Override
	public String copyImg(String folderName, String tempUrl, boolean isEncod) {
		String key = "";
		String tempKey = getKey(tempUrl);
		OSSObject object = client.getObject(bucketName, tempKey);
		ObjectMetadata metadata = object.getObjectMetadata();
		if (isEncod) {
			key = generateFilePath(folderName, metadata.getUserMetadata().get("extname"));
		} else {
			key = handleFolderName(folderName) + metadata.getUserMetadata().get("filename")
					+ metadata.getUserMetadata().get("extname");
		}
		CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, tempKey, bucketName, key);
		client.copyObject(copyObjectRequest);
		return cdnName + "/" + key;
	}

	@Override
	public String copyFile(String folderName, String tempUrl, boolean isEncod) {
		String key = "";
		String tempKey = getKey(tempUrl);
		OSSObject object = client.getObject(bucketName, tempKey);
		ObjectMetadata metadata = object.getObjectMetadata();
		if (isEncod) {
			key = generateFilePath(folderName, metadata.getUserMetadata().get("extname"));
		} else {
			key = handleFolderName(folderName) + metadata.getUserMetadata().get("filename")
					+ metadata.getUserMetadata().get("extname");
		}
		CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, tempKey, bucketName, key);
		client.copyObject(copyObjectRequest);
		return ossUrl + "/" + key;
	}

	private String generateFilePath(String folderName, String extName) {
		byte[] randomBytes = new byte[16];
		ng.nextBytes(randomBytes);

		return handleFolderName(folderName) + Base64.encodeBase64URLSafeString(randomBytes) + extName;
	}

	private String handleFolderName(String folderName) {
		if (StringUtils.isBlank(folderName)) {
			return "";
		}

		folderName = folderName.trim();
		if (!folderName.endsWith("/")) {
			folderName += "/";
		}

		return folderName;
	}

	private String getKey(String tempUrl) {
		String tempKey = "";
		if (tempUrl.startsWith(ossUrl)) {
			tempKey = tempUrl.replaceFirst(ossUrl + "/", "");
		} else if (tempUrl.startsWith(cdnName)) {
			tempKey = tempUrl.replaceFirst(cdnName + "/", "");
		}
		return tempKey;
	}

	private ObjectMetadata creatMetadata(String fileName, String extName, long fileLength) {
		ObjectMetadata metadata = new ObjectMetadata();
		Map<String, String> userMetadata = new HashMap<String, String>();
		metadata.setContentLength(fileLength);
		metadata.setCacheControl("max-age=315360000"); // 设置过期时间为10年
		userMetadata.put("filename", fileName);
		userMetadata.put("extname", extName);
		metadata.setUserMetadata(userMetadata);
		return metadata;
	}

	@Override
	public ObjectListing listObject(String bucketName, String prefix, String marker, Integer maxKeys) {

		ListObjectsRequest request = new ListObjectsRequest(bucketName, prefix, marker, null, maxKeys);
		return client.listObjects(request);
	}
}
