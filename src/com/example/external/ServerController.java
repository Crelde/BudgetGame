package com.example.external;
import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;

import com.example.budgetgame.onTaskCompleted;
import com.example.budgetgame.db.DBAdapter;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceQuery;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;


/**
 * @author Kewin & Christian
 * @summary Class that handles all communication with the server.
 * 
 */
public class ServerController {
	// Initialize values
	DBAdapter dbAdapter;
	String url = "https://budgetgame.azure-mobile.net/";
	String appkey = "zIeRvsVAjXhqWYIUYvefFFENBpvArJ90";
	boolean login = false;
	double saldo;
	onTaskCompleted listener;
	
	private MobileServiceClient mClient;
	private MobileServiceTable<Post> mPostTable;

	// Called from LogInActivity to very a users information, and notify the supplied listener with the response.
	public void logIn(final Context context, final String username, String password, final onTaskCompleted listener){
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
						if(login){
							syncPosts(context, username);
						}
						listener.getLogInTaskCompleted(login);				
					}
				});		
	}
	// Called from OverViewFragment to get a users Saldo, and notify the supplied listener with the result.
	public void getSaldoForUser(Context context, String username, final onTaskCompleted listener)
	{
		this.listener = listener;
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		// invoke getSaldo method from server	
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
	// Method called when a user is successfully logged in, to get load his posts into the phones local database.
	public void syncPosts(Context context, final String name){
		try {
			mClient = new MobileServiceClient(url, appkey, context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		// invoke getPosts method from server		
		dbAdapter = new DBAdapter(context);
		mPostTable = mClient.getTable(Post.class);
		MobileServiceQuery<TableQueryCallback<Post>> msq = mPostTable.top(500);
		msq.endsWith("username", name).execute(new TableQueryCallback<Post>() {
			public void onCompleted(List<Post> result, int count, Exception exception, ServiceFilterResponse response) {
				System.out.println("BEFORE IF");
				if (exception == null) {
					System.out.println("AFTER IF");
					dbAdapter.open();
					dbAdapter.clearPosts();
					for (Post item : result) {					
						dbAdapter.insertPost(item);
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


