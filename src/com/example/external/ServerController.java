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
import com.example.budgetgame.db.DBAdapter;
import com.google.gson.*;



public class ServerController {

	DBAdapter dbAdapter;
	
	public String TAG = "hej";
	String url = "https://budgetgame.azure-mobile.net/";
	String appkey = "zIeRvsVAjXhqWYIUYvefFFENBpvArJ90";
	boolean loginB = false;
	
	private MobileServiceClient mClient;

	private MobileServiceTable<User> mUserTable;
	private MobileServiceTable<Post> mPostTable;

	public boolean logIn(Context context, String username, String password){
		
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
						
						loginB = jsonData.getAsBoolean();
						System.out.println("Greetings from JSON method: " +loginB);
						
					}
				});
		
		
		return loginB;
		
	}
	
	public void doStuff(Context context, String name) {		
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		mClient.invokeApi("Budget?name="+name,
				new ApiJsonOperationCallback() {
					@Override
					public void onCompleted(JsonElement jsonData, Exception error,
							ServiceFilterResponse response) {
						
					}
				});
		
		
		mUserTable = mClient.getTable(User.class);	
		mUserTable.execute(new TableQueryCallback<User>() {
			public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
				System.out.println("BEFORE IF");
				if (exception == null) {
					System.out.println("AFTER IF");
					for (User item : result) {
						System.out.println("Read object with username: " + item.brugernavn);
					}
				}
				else{
					exception.printStackTrace();
				}
			}
		});
		
	}
	
	
	public void syncPosts(Context context, final String name){
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		mClient.invokeApi("Budget?userNameToGetPostsFor="+name,
				new ApiJsonOperationCallback() {
					@Override
					public void onCompleted(JsonElement jsonData, Exception error,
							ServiceFilterResponse response) {
						
					}
				});
		mPostTable = mClient.getTable(Post.class);	
		mPostTable.execute(new TableQueryCallback<Post>() {
			public void onCompleted(List<Post> result, int count, Exception exception, ServiceFilterResponse response) {
				System.out.println("BEFORE IF");
				if (exception == null) {
					System.out.println("AFTER IF");
					for (Post item : result) {
						if (item.username == name){
							dbAdapter.insertPost(item);
						}
					}
				}
				else{
					exception.printStackTrace();
				}
			}
		});
		
	
		
	}
}


