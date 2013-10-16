package com.example.content;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.example.content.ParamUser;

public class TKYApplication extends Application{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private final static String TAG = "TKYApplication";
	public static String APP_FOLDER_NAME = "TKYApp";
	public static TKYApplication instance;
	/**
	 * Ӧ�ó���Ŀ¼����
	 * */
	private String APP_RELATIVE_SD = File.separator + APP_FOLDER_NAME;
	private String SD_CARD_PATH = getSDPath();
	/**
	 * Ӧ�ó�����ļ��о���·��
	 * */
	private String PATH_ROOT = SD_CARD_PATH + APP_RELATIVE_SD;
	/**
	 * ���ݿ���ļ��о���·��
	 * */
	private String FOLDER_DB = PATH_ROOT + File.separator + "Database";
	/**
	 * �����ļ��о���·��
	 * */
	private String FOLDER_DATA = PATH_ROOT + File.separator + "Data";
	/**
	 * ���漰��ʱ�ļ����ļ��о���·��
	 * */
	private String FOLDER_CACHE = PATH_ROOT + File.separator + "Cache";

	/**
	 * ��Ƶ�����ļ���
	 * */
	private String FOLDER_DATA_VIDEO = FOLDER_DATA + File.separator + "Video" + File.separator;
	/**
	 * ��Ƶ�����ļ���
	 * */
	private String FOLDER_DATA_AUDIO = FOLDER_DATA + File.separator + "Audio" + File.separator;
	/**
	 * ͼƬ�����ļ���
	 * */
	private String FOLDER_DATA_PHOTO = FOLDER_DATA + File.separator + "Image" + File.separator;
	public static final int VIDEO = 1;
	public static final int AUDIO = 2;
	public static final int IMAGE = 3;
	
	//socket
	public DataInputStream dis = null;
	public DataOutputStream dos = null;
	private Boolean isContect = false;
	public Socket s = null;
	private int heartSendTime = 0;
	private Timer heartTimer;
	public ArrayList<ParamUser> peopleOnline = new ArrayList<ParamUser>();
	protected boolean isOnChatActivity = false;
	public final static String BROCAST_ACTION = "brocast_action";
	private final static int HEART_DEALY_TIME = 10000;
	private Object heartNumChangeObj = new Object();
	
	public static ParamUser user;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public String getNewFilePath(int FileType) {
		String path = null;
		switch (FileType) {
		case VIDEO:
			getFolderWithCreate(FOLDER_DATA_VIDEO);
			path = FOLDER_DATA_VIDEO + System.currentTimeMillis() + ".3gp";
			break;
		case AUDIO:
			getFolderWithCreate(FOLDER_DATA_AUDIO);
			path = FOLDER_DATA_AUDIO + System.currentTimeMillis() + ".amr";
			break;
		case IMAGE:
			getFolderWithCreate(FOLDER_DATA_PHOTO);
			path = FOLDER_DATA_PHOTO + System.currentTimeMillis() + ".jpg";
			break;
		}
		return path;
	}
	private static boolean isFileOrFolderExistsWithCreate(String path, boolean isFolder) {
		File file = new File(path);
		if (!file.exists()) {
			if (isFolder && file.mkdirs()) {
				return true;
			} else if (!isFolder) {
				String temppath = path.substring(0, path.lastIndexOf("/"));
				File filedir = new File(temppath);
				if (!filedir.exists()) {
					if (!filedir.mkdirs())
						return false;
				}
				try {
					if (file.createNewFile())
						return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			} else
				return false;
		} else
			return true;
	}
	/**
	 * @author HJ �ж��ļ����Ƿ���ڣ�������ڣ���ֱ�ӷ���true,
	 *         ��������ڣ��򴴽��ļ��У���������ɹ����򷵻�true,�������ʧ���򷵻�false
	 * */
	public static boolean getFolderWithCreate(String folderPath) {
		return isFileOrFolderExistsWithCreate(folderPath, true);
	}
	
	/**
	 * @author HJ ��ȡSD����Ӧ��Ŀ¼
	 * */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = isSDCardExist();
		System.out.println(Environment.getExternalStorageDirectory());
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ
			return sdDir.toString();
		} else {
			return null;
		}
	}
	public static boolean isSDCardExist(){
		return  Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
	}
	
	//����socket
	public Thread connectSocket = new Thread(new Runnable() {

			@Override
			public void run() {
				Log.i("HomeActivity", "001");
//				if (Tools.isNet(TKYApplication.instance.getApplicationContext())) {
					try {
						Log.i(TAG, "connect;");
						Log.i("HomeActivity", "002");
						s = new Socket(Constants.BASE_IP, Constants.SOCKET_PORT);
						if (s.isConnected()) {
							Log.i("HomeActivity", "003");
							dos = new DataOutputStream(s.getOutputStream());
							dis = new DataInputStream(s.getInputStream());
							String headStr = basicObj("online").toString();//+ "\n\r";
							Log.i(TAG, "online,headStr=" + headStr);
							dos.writeUTF(headStr);
							dos.flush();
							new Thread(null, doThread, "Message").start();
							Log.i(TAG, "connect;connect success;headStr="+headStr);
							isContect = true;
							
							//��������
							heartTimer = new Timer();
							heartTimer.schedule(new socketHeartTask(), HEART_DEALY_TIME, HEART_DEALY_TIME);
							Log.i("HomeActivity", "004");
						}
					} catch (UnknownHostException e) {
						Log.i(TAG, "connect;����ʧ��");
						e.printStackTrace();
					} catch (SocketTimeoutException e) {
						Log.i(TAG, "connect;���ӳ�ʱ��������δ������IP����");
						e.printStackTrace();
					} catch (IOException e) {
						Log.i(TAG, "IOException;����ʧ��");
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
//				} else {
//					Log.i(TAG, "����δ����");
//				}
			}

		});

	private Runnable doThread = new Runnable() {
		public void run() {
			Log.i(TAG, "doThread;running!");
			ReceiveMsg();
		}
	};

	/**
	 * �̼߳���Server��Ϣ
	 */
	private void ReceiveMsg() {
		if (isContect) {
			try {
				String reMsg = null;
				while ((reMsg = dis.readUTF()) != null) {
					Log.i(TAG, "ReceiveMsg" + reMsg);
					if (reMsg != null) {

						try {
							JSONObject reObj = new JSONObject(reMsg);
							String flag = reObj.getString(JSONKey.FLAG);
							Log.i(TAG, "ReceiveMsg,reObj=" + reObj.toString()+";flag="+flag);
							if (flag.equals("online")) { // ����
								String name = reObj.getString("username");
								int id = -1;
								id = reObj.getInt("userid");
								if(id != user.getId()){
									boolean isAlreadyIn = false;
									for(int i = 0; i<peopleOnline.size();i++){
										if(id == peopleOnline.get(i).getId()){
											isAlreadyIn = true;
										}
									}
									if(!isAlreadyIn){
										ParamUser onePeople = new ParamUser();
										onePeople.setName(name);
										onePeople.setId(id);
										peopleOnline.add(onePeople);
									}
								}
								//TODO֪ͨ�ֽ��յ���������Ա��Ϣ
							} else if (flag.equals("sendmsg")) { // ����
								Log.i(TAG,"���յ�������Ϣ");
								
								String fromUserName = reObj.getString("username");
								int fromUserId = reObj.getInt("userid");
								String msg = reObj.getString("msg");
								String time = reObj.getString("time");
								int chattype = reObj.getInt("chattype");
								
								//TODO֪ͨ���յ�������Ϣ
								if(isOnChatActivity){ //��ǰ��ʾҳ��Ϊ����ҳ��������ʾ֪ͨ
									Intent msgIntent = new Intent();
									msgIntent.setAction(BROCAST_ACTION);
									Bundle data = new Bundle();
									data.putString(JSONKey.FLAG,"sendmsg");
									data.putString("msg", msg);
									data.putString("time", time);
									data.putString("username", fromUserName);
									data.putInt("userid", fromUserId);
									data.putInt("chattype", chattype);
									msgIntent.putExtras(data);
							        sendBroadcast(msgIntent);
								}else{ //��ǰ��ʾΪ������ҳ����ʾ֪ͨ
									
								}
								
							} else if (flag.equals("heart_check")) { // �������
								changHeartNum(-1);
							} else if (flag.equals("offline")) { // ����
								disConnect(false);
								break;
							}

						} catch(Exception e){
							e.printStackTrace();
						}

					}
				}
			} catch (SocketException e) {
				// TODO: handle exception
				System.out.println("exit!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}



	public void disConnect(boolean isNeedStartAgain) {
		Log.i(TAG, "disConnect;");
		if (dos != null) {
			try {
				// dos.writeUTF(chatKey + "offline:" + name);
				String headStr = basicObj("offline").toString();
				Log.i(TAG, "disConnect;headStr=" + headStr);
				dos.writeUTF(headStr);// (headStr.getBytes());
				if(connectSocket != null && connectSocket.isAlive()){
					connectSocket.stop();
					connectSocket.destroy();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(s != null){
					s.close();
					s = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(heartTimer != null){
			heartTimer.cancel();
			heartTimer = null;
		}
		if(isNeedStartAgain){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					connectSocket.start();
				}
			}).start();
		}
	}

	public JSONObject basicObj(String flag){
		JSONObject myObject = new JSONObject();
		try {
			myObject.put(JSONKey.FLAG, flag);
			if(user != null){
				myObject.put("username", user.getName());
				myObject.put("userid", user.getId());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return myObject;
	}
	
	//socket����
	class socketHeartTask extends TimerTask{
		@Override
		public void run() {
			if (s.isConnected() && dos != null ) {
				// dos.writeUTF(chatKey + "online:" + name);
				if(heartSendTime > 3){ //����N�κ�Ͽ�����
					disConnect(true);
				}else{
					heartCheckSend();
					changHeartNum(1);
				}
				
			}
		}
	}
	
	private void heartCheckSend(){
		JSONObject obj = basicObj("heart_check");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = df.format(new Date());
		try {
			obj.put("time", date);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			dos.writeUTF(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void changHeartNum(int addNum){
		synchronized (heartNumChangeObj) {
			heartSendTime = heartSendTime + addNum;
		}
	}
}
