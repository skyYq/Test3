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
	//初始化坐标工具类
	CoordinateConverter converter = null;
	//获取定位
	private Button btn_getLocation;
	//停止定位
	private Button btn_stopLocation;
	//定位信息显示
	private TextView tv_LocationInfo;
	//经度
	private EditText tvJingDu;
	//纬度
	private EditText tvWeiDu;
	//A,B经纬度
	private TextView tvJingDuA,tvWeiDuA,tvJingDuB,tvWeiDuB;
	//获取A,B经纬度,获取AB间的距离
	private Button btnGetA,btnGetB,btnGetABDistance;
	//获取距离
	private Button btnGetDistance;
	//显示距离
	private TextView tvDistance,tvABDistance;
	//定位服务
	private AMapLocationClient locationClient = null;
	//定位控制
	private AMapLocationClientOption locationOption = null;
	//最后一次自己的经度
	private double myJingDu = 0;
	//最后一次自己的纬度
	private double myWeiDu = 0;
	//经度A
	private double jingDuA = 0;
	//经度B
	private double jingDuB = 0;
	//纬度A
	private double weiDuA = 0;
	//纬度B
	private double weiDuB = 0;
	
	/**
	 * 定位监听
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation location) {
			if (null != location) {

				StringBuffer sb = new StringBuffer();
				//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
				if(location.getErrorCode() == 0){
					sb.append("定位成功" + "\n");
					sb.append("定位类型: " + location.getLocationType() + "\n");
					sb.append("经    度    : " + location.getLongitude() + "\n");
					sb.append("纬    度    : " + location.getLatitude() + "\n");
					//保存获取的经纬度
					myJingDu = location.getLongitude();
					myWeiDu = location.getLatitude();
					
					sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
					sb.append("提供者    : " + location.getProvider() + "\n");

					sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
					sb.append("角    度    : " + location.getBearing() + "\n");
					// 获取当前提供定位服务的卫星个数
					sb.append("星    数    : " + location.getSatellites() + "\n");
					sb.append("国    家    : " + location.getCountry() + "\n");
					sb.append("省            : " + location.getProvince() + "\n");
					sb.append("市            : " + location.getCity() + "\n");
					sb.append("城市编码 : " + location.getCityCode() + "\n");
					sb.append("区            : " + location.getDistrict() + "\n");
					sb.append("区域 码   : " + location.getAdCode() + "\n");
					sb.append("地    址    : " + location.getAddress() + "\n");
					sb.append("兴趣点    : " + location.getPoiName() + "\n");
					//定位完成的时间
					sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
				} else {
					//定位失败
					sb.append("定位失败" + "\n");
					sb.append("错误码:" + location.getErrorCode() + "\n");
					sb.append("错误信息:" + location.getErrorInfo() + "\n");
					sb.append("错误描述:" + location.getLocationDetail() + "\n");
				}
				//定位之后的回调时间
				sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
				//解析定位结果，
				String result = sb.toString();
				tv_LocationInfo.setText(result);
			} else {
				tv_LocationInfo.setText("定位失败，loc is null");
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化坐标计算工具
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
		//启动定位
		btn_getLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断是否启动了定位配置
				if (null == locationOption) {
					locationOption = new AMapLocationClientOption();
				}
				// 启动定位
				startLocation();
			}
		});
		//停止定位
		btn_stopLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断是否启动了定位配置
				if (null == locationOption) {
					locationOption = new AMapLocationClientOption();
				}
				// 停止定位
				stopLocation();
			}
		});
		//获取距离
		btnGetDistance.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getUserJuLi();
				
			}
		});
		//获取A的经纬度
		btnGetA.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jingDuA = myJingDu;
				weiDuA = myWeiDu;
				tvJingDuA.setText(""+jingDuA);
				tvWeiDuA.setText(""+weiDuA);
				
			}
		});
		
		//获取B的经纬度
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
				//设置A点位置,纬度,经度
				double aJd = Double.valueOf(tvJingDuA.getText().toString().trim());
				double aWd = Double.valueOf(tvWeiDuA.getText().toString().trim());
				DPoint dPoint1 = new DPoint(aWd, aJd);
				
				//设置B点位置，纬度,经度
				double bJd = Double.valueOf(tvJingDuB.getText().toString().trim());
				double bWd = Double.valueOf(tvWeiDuB.getText().toString().trim());
				DPoint dPoint2 = new DPoint(bWd,bJd);
				
				//获取两者间的距离
				float distance =  converter.calculateLineDistance(dPoint1, dPoint2);
				//设置显示距离
				tvABDistance.setText("AB相距"+distance+"米");
			}
		});
		//加载定位配置
		initLocation();
	}
	/**
	 * 获取用户之间的距离
	 * */
	private void getUserJuLi(){
		if(tvJingDu.getText().toString().trim().equals("") || tvWeiDu.getText().toString().trim().equals("")){
			return;
		}else{
			
			//设置自己的点位置,纬度,经度
			DPoint dPoint1 = new DPoint(myWeiDu, myJingDu);
			double heJd = Double.valueOf(tvJingDu.getText().toString().trim());
			double heWd = Double.valueOf(tvWeiDu.getText().toString().trim());
			//设置他人的点位置，纬度,经度
			DPoint dPoint2 = new DPoint(heWd,heJd);
			//获取两者间的距离
			float distance =  converter.calculateLineDistance(dPoint1, dPoint2);
			//设置显示距离
			tvDistance.setText("输入点与最后定点相距"+distance+"米");
		}
		
	}
	/**
	 * 经纬角度转换为地球弧度
	 * 
	 * */
	private double rad(double d){
		return d * Math.PI / 180.0d;
	}
	/**
	 * 开始定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void startLocation(){
		//根据控件的选择，重新设置定位参数
		resetOption();
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
	}
	/**
	 * 停止定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void stopLocation(){
		// 停止定位
		locationClient.stopLocation();
	}
	// 根据控件的选择，重新设置定位参数
		private void resetOption() {
			// 设置是否需要显示地址信息
			locationOption.setNeedAddress(true);
			/**
			 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
			 * 注意：只有在高精度模式下的单次定位有效，其他方式无效
			 */
			locationOption.setGpsFirst(false);
			// 设置是否开启缓存
			locationOption.setLocationCacheEnable(true);
			// 设置是否单次定位
			locationOption.setOnceLocation(false);
			//设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
			locationOption.setOnceLocationLatest(false);
			//设置是否使用传感器
			locationOption.setSensorEnable(false);
			//设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
			
			//定位间隔 2s
			String strInterval = "2000";
			if (!TextUtils.isEmpty(strInterval)) {
				try{
					// 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
					locationOption.setInterval(Long.valueOf(strInterval));
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
			//超时时间
			String strTimeout = "30000";
			if(!TextUtils.isEmpty(strTimeout)){
				try{
					// 设置网络请求超时时间
				     locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	/**
	 * 初始化定位
	 * 
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void initLocation(){
		//初始化client
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = getDefaultOption();
		//设置定位参数
		locationClient.setLocationOption(locationOption);
		// 设置定位监听
		locationClient.setLocationListener(locationListener);
	}
	/**
	 * 默认的定位参数
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private AMapLocationClientOption getDefaultOption(){
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
		mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
		return mOption;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//销毁定位
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
