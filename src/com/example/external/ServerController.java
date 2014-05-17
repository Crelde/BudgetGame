package com.example.external;
import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.*;

import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.onTaskCompleted;
import com.example.budgetgame.db.DBAdapter;
import com.google.gson.*;



public class ServerController {

	DBAdapter dbAdapter;
	
	public String TAG = "hej";
	String url = "https://budgetgame.azure-mobile.net/";
	String appkey = "zIeRvsVAjXhqWYIUYvefFFENBpvArJ90";
	boolean login = false;
	double saldo;
	onTaskCompleted listener;
	
	private MobileServiceClient mClient;

	//private MobileServiceTable<User> mUserTable;
	private MobileServiceTable<Post> mPostTable;

	public void logIn(Context context, String username, String password, final onTaskCompleted listener){
		this.listener = listener;
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		// invoke Log-in method from server
		
		mClient.invokeApi("Budget?username="+username+"&password="+password,
				new ApiJsonOperationCallback() {
					@Override
					public void onCompleted(JsonElement jsonData, Exception error,
							ServiceFilterResponse response) {
						
						login = jsonData.getAsBoolean();
						listener.getLogInTaskCompleted(login);
						
					}
				});		
	}
	
	
	public void getSaldoForUser(Context context, String username, final onTaskCompleted listener)
	{
		this.listener = listener;
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		// invoke Log-in method from server
		
		mClient.invokeApi("Budget?userNameToGetSaldoFor="+username, "get", null,
				new ApiJsonOperationCallback() {
					@Override
					public void onCompleted(JsonElement jsonData, Exception error,
							ServiceFilterResponse response) {
						
						saldo = jsonData.getAsDouble();
						listener.getSaldoTaskCompleted(saldo);
						
					}
				});
	}
	public void syncPosts(Context context, final String name){
		
		dbAdapter = new DBAdapter(context);
		
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		// Doesn't even use ^ method
		mPostTable = mClient.getTable(Post.class);	
		mPostTable.execute(new TableQueryCallback<Post>() {
			public void onCompleted(List<Post> result, int count, Exception exception, ServiceFilterResponse response) {
				String nameToTestAgainst = name;
				System.out.println("BEFORE IF");
				if (exception == null) {
					System.out.println("AFTER IF");
					dbAdapter.open();
					dbAdapter.clearPosts();
					for (Post item : result) {
						if ((item.username.compareTo(nameToTestAgainst) == 0)){						
							dbAdapter.insertPost(item);
						}
					}
					dbAdapter.close();
					
				}
				else{
					exception.printStackTrace();
				}
			}
		});
		
	
		
	}
}


