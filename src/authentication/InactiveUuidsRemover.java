package authentication;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class InactiveUuidsRemover implements Runnable {

	private static final int INACTIVE_LIMIT_HOURS = 24;
	private static final int SLEEP_MILLIS = 3600000; // 3600000 milliseconds = 1 hour.

	@Override
	public void run() {
		try{
			while(true) {
				System.out.println("LOOKING FOR INACTIVE UUIDS.");
				HashMap<String, Date> latestRequests = Authenticate.getLatestRequestsMap();

				Iterator<String> uuids = latestRequests.keySet().iterator();

				while(uuids.hasNext()) {
					String uuid = uuids.next();
					Date date = latestRequests.get(uuid);

					if(getDateDiff(date, new Date(), TimeUnit.HOURS) >= INACTIVE_LIMIT_HOURS) {
						System.out.println("REMOVING INACTIVE UUID: " + uuid);
						Authenticate.deleteUser(uuid);
						uuids = latestRequests.keySet().iterator();
					}
				}

				System.out.println("REMAINING USERS");
				Iterator<String> uuids2 = latestRequests.keySet().iterator();
				while(uuids2.hasNext()) {
					System.out.println(uuids2.next());
				}

				try {
					Thread.sleep(SLEEP_MILLIS);
				} catch (InterruptedException e) {
					System.err.println("INACTIVE UUIDS REMOVER - THREAD SLEEP ERROR");
				}
			}
		} catch(Exception e) {
			System.err.println("INACTIVE UUIDS REMOVER - FAILED, CONTINUING PROGRAM WITHOUT THIS FUNCTION.");
		}
	}

	private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

}