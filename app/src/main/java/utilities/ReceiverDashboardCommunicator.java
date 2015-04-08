package utilities;

import models.BankModel;
import models.ReceiverModel;

public interface ReceiverDashboardCommunicator {

	void GetNewReceiver(ReceiverModel receiverToAdd);

	void GetNewBank(BankModel bankToAdd);

	

}
