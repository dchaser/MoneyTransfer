package utilities;

import viewmodels.BankViewModel;
import viewmodels.SenderViewModel;
import models.AmountModel;


public interface Communicator {
	
	public boolean ProceccAmount(AmountModel amount);
	
	public void triggerSenderUpdate(SenderViewModel senderVM, int tab);
	
	public void getSenderVMFromReceiverFragment(SenderViewModel senderVM);
	
	public void getDataToConfirmationFragment(String str);

	public void getSenderVMFromBankFragment(BankViewModel bankVM);
	
	
	
	
}
