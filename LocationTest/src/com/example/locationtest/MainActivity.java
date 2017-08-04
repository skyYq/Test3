package com.example.locationtest;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends CheckPermissionsActivity {
	//��ʼ�����깤����
	CoordinateConverter converter = null;
	//��ȡ��λ
	private Button btn_getLocation;
	//ֹͣ��λ
	private Button btn_stopLocation;
	//��λ��Ϣ��ʾ
	private TextView tv_LocationInfo;
	//����
	private EditText tvJingDu;
	//γ��
	private EditText tvWeiDu;
	//A,B��γ��
	private TextView tvJingDuA,tvWeiDuA,tvJingDuB,tvWeiDuB;
	//��ȡA,B��γ��,��ȡAB��ľ���
	private Button btnGetA,btnGetB,btnGetABDistance;
	//��ȡ����
	private Button btnGetDistance;
	//��ʾ����
	private TextView tvDistance,tvABDistance;
	//��λ����
	private AMapLocationClient locationClient = null;
	//��λ����
	private AMapLocationClientOption locationOption = null;
	//���һ���Լ��ľ���
	private double myJingDu = 0;
	//���һ���Լ���γ��
	private double myWeiDu = 0;
	//����A
	private double jingDuA = 0;
	//����B
	private double jingDuB = 0;
	//γ��A
	private double weiDuA = 0;
	//γ��B
	private double weiDuB = 0;
	
	/**
	 * ��λ����
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation location) {
			if (null != location) {

				StringBuffer sb = new StringBuffer();
				//errCode����0����λ�ɹ���������Ϊ��λʧ�ܣ�����Ŀ��Բ��չ�����λ������˵��
				if(location.getErrorCode() == 0){
					sb.append("��λ�ɹ�" + "\n");
					sb.append("��λ����: " + location.getLocationType() + "\n");
					sb.append("��    ��    : " + location.getLongitude() + "\n");
					sb.append("γ    ��    : " + location.getLatitude() + "\n");
					//�����ȡ�ľ�γ��
					myJingDu = location.getLongitude();
					myWeiDu = location.getLatitude();
					
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
					sb.append("��λʱ��: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
				} else {
					//��λʧ��
					sb.append("��λʧ��" + "\n");
					sb.append("������:" + location.getErrorCode() + "\n");
					sb.append("������Ϣ:" + location.getErrorInfo() + "\n");
					sb.append("��������:" + location.getLocationDetail() + "\n");
				}
				//��λ֮��Ļص�ʱ��
				sb.append("�ص�ʱ��: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
				//������λ�����
				String result = sb.toString();
				tv_LocationInfo.setText(result);
			} else {
				tv_LocationInfo.setText("��λʧ�ܣ�loc is null");
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��ʼ��������㹤��
		converter = new CoordinateConverter(
				getApplicationContext());
		btn_getLocation = (Button)findViewById(R.id.getLocation);
		btn_stopLocation = (Button)findViewById(R.id.stopLocation);
		tvJingDu = (EditText)findViewById(R.id.jingDu);
		tvWeiDu = (EditText)findViewById(R.id.weiDu);
		tvDistance = (TextView)findViewById(R.id.juLi);
		btnGetDistance = (Button)findViewById(R.id.getDistance);
		tv_LocationInfo = (TextView)findViewById(R.id.locationText);
		tvJingDuA = (TextView)findViewById(R.id.jinDuA);
		tvWeiDuA = (TextView)findViewById(R.id.weiDuA);
		tvJingDuB = (TextView)findViewById(R.id.jinDuB);
		tvWeiDuB = (TextView)findViewById(R.id.weiDuB);
		btnGetA = (Button)findViewById(R.id.getA);
		btnGetB = (Button)findViewById(R.id.getB);
		btnGetABDistance = (Button)findViewById(R.id.getABDistance);
		tvABDistance = (TextView)findViewById(R.id.juLiAB);
		//������λ
		btn_getLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж��Ƿ������˶�λ����
				if (null == locationOption) {
					locationOption = new AMapLocationClientOption();
				}
				// ������λ
				startLocation();
			}
		});
		//ֹͣ��λ
		btn_stopLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж��Ƿ������˶�λ����
				if (null == locationOption) {
					locationOption = new AMapLocationClientOption();
				}
				// ֹͣ��λ
				stopLocation();
			}
		});
		//��ȡ����
		btnGetDistance.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getUserJuLi();
				
			}
		});
		//��ȡA�ľ�γ��
		btnGetA.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jingDuA = myJingDu;
				weiDuA = myWeiDu;
				tvJingDuA.setText(""+jingDuA);
				tvWeiDuA.setText(""+weiDuA);
				
			}
		});
		
		//��ȡB�ľ�γ��
		btnGetB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jingDuB = myJingDu;
				weiDuB = myWeiDu;
				tvJingDuB.setText(""+jingDuB);
				tvWeiDuB.setText(""+weiDuB);
				
			}
		});
		btnGetABDistance.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//����A��λ��,γ��,����
				double aJd = Double.valueOf(tvJingDuA.getText().toString().trim());
				double aWd = Double.valueOf(tvWeiDuA.getText().toString().trim());
				DPoint dPoint1 = new DPoint(aWd, aJd);
				
				//����B��λ�ã�γ��,����
				double bJd = Double.valueOf(tvJingDuB.getText().toString().trim());
				double bWd = Double.valueOf(tvWeiDuB.getText().toString().trim());
				DPoint dPoint2 = new DPoint(bWd,bJd);
				
				//��ȡ���߼�ľ���
				float distance =  converter.calculateLineDistance(dPoint1, dPoint2);
				//������ʾ����
				tvABDistance.setText("AB���"+distance+"��");
			}
		});
		//���ض�λ����
		initLocation();
	}
	/**
	 * ��ȡ�û�֮��ľ���
	 * */
	private void getUserJuLi(){
		if(tvJingDu.getText().toString().trim().equals("") || tvWeiDu.getText().toString().trim().equals("")){
			return;
		}else{
			
			//�����Լ��ĵ�λ��,γ��,����
			DPoint dPoint1 = new DPoint(myWeiDu, myJingDu);
			double heJd = Double.valueOf(tvJingDu.getText().toString().trim());
			double heWd = Double.valueOf(tvWeiDu.getText().toString().trim());
			//�������˵ĵ�λ�ã�γ��,����
			DPoint dPoint2 = new DPoint(heWd,heJd);
			//��ȡ���߼�ľ���
			float distance =  converter.calculateLineDistance(dPoint1, dPoint2);
			//������ʾ����
			tvDistance.setText("���������󶨵����"+distance+"��");
		}
		
	}
	/**
	 * ��γ�Ƕ�ת��Ϊ���򻡶�
	 * 
	 * */
	private double rad(double d){
		return d * Math.PI / 180.0d;
	}
	/**
	 * ��ʼ��λ
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void startLocation(){
		//���ݿؼ���ѡ���������ö�λ����
		resetOption();
		// ���ö�λ����
		locationClient.setLocationOption(locationOption);
		// ������λ
		locationClient.startLocation();
	}
	/**
	 * ֹͣ��λ
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void stopLocation(){
		// ֹͣ��λ
		locationClient.stopLocation();
	}
	// ���ݿؼ���ѡ���������ö�λ����
		private void resetOption() {
			// �����Ƿ���Ҫ��ʾ��ַ��Ϣ
			locationOption.setNeedAddress(true);
			/**
			 * �����Ƿ����ȷ���GPS��λ��������30����GPSû�з��ض�λ�����������綨λ
			 * ע�⣺ֻ���ڸ߾���ģʽ�µĵ��ζ�λ��Ч��������ʽ��Ч
			 */
			locationOption.setGpsFirst(false);
			// �����Ƿ�������
			locationOption.setLocationCacheEnable(true);
			// �����Ƿ񵥴ζ�λ
			locationOption.setOnceLocation(false);
			//�����Ƿ�ȴ��豸wifiˢ�£��������Ϊtrue,���Զ���Ϊ���ζ�λ��������λʱ��Ҫʹ��
			locationOption.setOnceLocationLatest(false);
			//�����Ƿ�ʹ�ô�����
			locationOption.setSensorEnable(false);
			//�����Ƿ���wifiɨ�裬�������Ϊfalseʱͬʱ��ֹͣ����ˢ�£�ֹͣ�Ժ���ȫ������ϵͳˢ�£���λλ�ÿ��ܴ������
			
			//��λ��� 2s
			String strInterval = "2000";
			if (!TextUtils.isEmpty(strInterval)) {
				try{
					// ���÷��Ͷ�λ�����ʱ����,��СֵΪ1000�����С��1000������1000��
					locationOption.setInterval(Long.valueOf(strInterval));
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
			//��ʱʱ��
			String strTimeout = "30000";
			if(!TextUtils.isEmpty(strTimeout)){
				try{
					// ������������ʱʱ��
				     locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	/**
	 * ��ʼ����λ
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void initLocation(){
		//��ʼ��client
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = getDefaultOption();
		//���ö�λ����
		locationClient.setLocationOption(locationOption);
		// ���ö�λ����
		locationClient.setLocationListener(locationListener);
	}
	/**
	 * Ĭ�ϵĶ�λ����
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private AMapLocationClientOption getDefaultOption(){
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//��ѡ�����ö�λģʽ����ѡ��ģʽ�и߾��ȡ����豸�������硣Ĭ��Ϊ�߾���ģʽ
		mOption.setGpsFirst(false);//��ѡ�������Ƿ�gps���ȣ�ֻ�ڸ߾���ģʽ����Ч��Ĭ�Ϲر�
		mOption.setHttpTimeOut(30000);//��ѡ��������������ʱʱ�䡣Ĭ��Ϊ30�롣�ڽ��豸ģʽ����Ч
		mOption.setInterval(2000);//��ѡ�����ö�λ�����Ĭ��Ϊ2��
		mOption.setNeedAddress(true);//��ѡ�������Ƿ񷵻�������ַ��Ϣ��Ĭ����true
		mOption.setOnceLocation(false);//��ѡ�������Ƿ񵥴ζ�λ��Ĭ����false
		mOption.setOnceLocationLatest(false);//��ѡ�������Ƿ�ȴ�wifiˢ�£�Ĭ��Ϊfalse.�������Ϊtrue,���Զ���Ϊ���ζ�λ��������λʱ��Ҫʹ��
		AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//��ѡ�� �������������Э�顣��ѡHTTP����HTTPS��Ĭ��ΪHTTP
		mOption.setSensorEnable(false);//��ѡ�������Ƿ�ʹ�ô�������Ĭ����false
		mOption.setWifiScan(true); //��ѡ�������Ƿ���wifiɨ�衣Ĭ��Ϊtrue���������Ϊfalse��ͬʱֹͣ����ˢ�£�ֹͣ�Ժ���ȫ������ϵͳˢ�£���λλ�ÿ��ܴ������
		mOption.setLocationCacheEnable(true); //��ѡ�������Ƿ�ʹ�û��涨λ��Ĭ��Ϊtrue
		return mOption;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//���ٶ�λ
		if (null != locationClient) {
			/**
			 * ���AMapLocationClient���ڵ�ǰActivityʵ�����ģ�
			 * ��Activity��onDestroy��һ��Ҫִ��AMapLocationClient��onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
