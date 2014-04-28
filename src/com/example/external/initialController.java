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
import com.google.gson.*;



public class initialController {

	public String TAG = "hej";
	String url = "https://budgetgame.azure-mobile.net/";
	String appkey = "zIeRvsVAjXhqWYIUYvefFFENBpvArJ90";

	private MobileServiceClient mClient;

	private MobileServiceTable<User> mUserTable;

	
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
	
	
}
