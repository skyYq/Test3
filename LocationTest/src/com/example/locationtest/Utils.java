/**
 * 
 */
package com.example.locationtest;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.amap.api.location.AMapLocation;

import android.text.TextUtils;

/**
 * ����������
 * @����ʱ�䣺 2015��11��24�� ����11:46:50
 * @��Ŀ���ƣ� AMapLocationDemo2.x
 * @author hongming.wang
 * @�ļ�����: Utils.java
 * @��������: Utils
 */
public class Utils {
	/**
	 *  ��ʼ��λ
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * ��λ���
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * ֹͣ��λ
	 */
	public final static int MSG_LOCATION_STOP= 2;
	
	public final static String KEY_URL = "URL";
	public final static String URL_H5LOCATION = "file:///android_asset/location.html";
	/**
	 * ���ݶ�λ������ض�λ��Ϣ���ַ���
	 * @param location
	 * @return
	 */
	public synchronized static String getLocationStr(AMapLocation location){
		if(null == location){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		//errCode����0����λ�ɹ���������Ϊ��λʧ�ܣ�����Ŀ��Բ��չ�����λ������˵��
		if(location.getErrorCode() == 0){
			sb.append("��λ�ɹ�" + "\n");
			sb.append("��λ����: " + location.getLocationType() + "\n");
			sb.append("��    ��    : " + location.getLongitude() + "\n");
			sb.append("γ    ��    : " + location.getLatitude() + "\n");
			sb.append("��    ��    : " + location.getAccuracy() + "��" + "\n");
			sb.append("�ṩ��    : " + location.getProvider() + "\n");

			sb.append("��    ��    : " + location.getSpeed() + "��/��" + "\n");
			sb.append("��    ��    : " + location.getBearing() + "\n");
			// ��ȡ��ǰ�ṩ��λ��������Ǹ���
			sb.append("��    ��    : " + location.getSatellites() + "\n");
			sb.append("��    ��    : " + location.getCountry() + "\n");
			sb.append("ʡ            : " + location.getProvince() + "\n");
			sb.append("��            : " + location.getCity() + "\n");
			sb.append("���б��� : " + location.getCityCode() + "\n");
			sb.append("��            : " + location.getDistrict() + "\n");
			sb.append("���� ��   : " + location.getAdCode() + "\n");
			sb.append("��    ַ    : " + location.getAddress() + "\n");
			sb.append("��Ȥ��    : " + location.getPoiName() + "\n");
			//��λ��ɵ�ʱ��
			sb.append("��λʱ��: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
		} else {
			//��λʧ��
			sb.append("��λʧ��" + "\n");
			sb.append("������:" + location.getErrorCode() + "\n");
			sb.append("������Ϣ:" + location.getErrorInfo() + "\n");
			sb.append("��������:" + location.getLocationDetail() + "\n");
		}
		//��λ֮��Ļص�ʱ��
		sb.append("�ص�ʱ��: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
		return sb.toString();
	}

	private static SimpleDateFormat sdf = null;
	public  static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}
}
