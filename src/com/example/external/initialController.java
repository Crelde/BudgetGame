package com.example.external;
import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.*;

import java.net.MalformedURLException;
import java.util.List;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;



public class initialController {

	public String TAG = "hej";
	String url = "https://budgetgame.azure-mobile.net/";
	String appkey = "zIeRvsVAjXhqWYIUYvefFFENBpvArJ90";
	private MobileServiceClient mClient;
	
	
	public void doStuff() throws MalformedURLException{
		mClient = new MobileServiceClient(url, appkey, null);
		MobileServiceTable<User> mUserTable = mClient.getTable(User.class);
		
		mUserTable.execute(new TableQueryCallback<User>() {
			public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					for (User item : result) {
						Log.i(TAG, "Read object with ID " + item.brugernavn);
						// Test change in branch
					}
				}
			}
		});
		
	}
	
	
}
