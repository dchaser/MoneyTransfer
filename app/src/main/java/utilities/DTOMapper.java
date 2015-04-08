package utilities;

import java.util.ArrayList;
import java.util.List;

import models.AddressModel;
import models.AmountModel;
import models.BankModel;
import models.ReceiverModel;
import models.SenderModel;
import viewmodels.SenderViewModel;
import dtos.Address;
import dtos.Amount;
import dtos.Bank;
import dtos.Receiver;
import dtos.Sender;
import dtos.SenderVerification;
import dtos.Transaction;

public class DTOMapper {

	private SenderViewModel senderVM;

	public DTOMapper(SenderViewModel senderVM) {
		this.senderVM = senderVM;
	}

	public Transaction GetTransactionDTO() {

		Sender senderDTO = createSenderDTO();

		Amount amountDTO = GetAmountDTO(this.senderVM.SelectedAmount);

		// sender,receiver,amount,bank,status
		Transaction transactionDTO = new Transaction(senderDTO, null,
				amountDTO, null, "PENDING");

		return transactionDTO;
	}

	private Sender createSenderDTO() {
		SenderVerification senderVerification;
		SenderModel senderModel = this.senderVM.SelectedSender;

		if (this.senderVM.SenderIdentity != null) {
			senderVerification = new SenderVerification(
					this.senderVM.SenderIdentity);
		} else {
			senderVerification = new SenderVerification();
		}

		Sender senderDTO = new Sender(senderModel.GetFirstName(),
				senderModel.GetLastName(), senderModel.GetMobile(),
				senderModel.GetEmail(),
				GetReceiverDTO(this.senderVM.ReceiverVM.GetSelectedReceiver()),
				GetAddressDTO(senderModel.GetAddress()),
				senderModel.GetVerified(), senderVerification);

		// setting the cloudId(sender's id in the cloud database) if exist to
		// check if this is an existing sender.
		// if so this record will update the existing sender in the cloud db
		// else insert a new one.
		senderDTO.Id = this.senderVM.SelectedSender.GetCloudRefCode();
		return senderDTO;
	}
	
	public Sender createSenderUpdateDTOwithoutReceivers() {
		
		SenderVerification senderVerification;
		SenderModel senderModel = this.senderVM.SelectedSender;

		if (this.senderVM.SenderIdentity != null) {
			senderVerification = new SenderVerification(
					this.senderVM.SenderIdentity);
		} else {
			senderVerification = new SenderVerification();
		}

		Sender senderDTO = new Sender(senderModel.GetFirstName(),
				senderModel.GetLastName(), senderModel.GetMobile(),
				senderModel.GetEmail(),
				GetAddressDTO(senderModel.GetAddress()),
				senderModel.GetVerified(), senderVerification);

		// setting the cloudId(sender's id in the cloud database) if exist to
		// check if this is an existing sender.
		// if so this record will update the existing sender in the cloud db
		// else insert a new one.
		senderDTO.Id = this.senderVM.SelectedSender.GetCloudRefCode();
		return senderDTO;
	}

	private List<Receiver> GetReceiverDTO(ReceiverModel selectedReceiver) {

		List<Receiver> receiversDTO = new ArrayList<Receiver>();

		Receiver receiverDTO = new Receiver(
				selectedReceiver.GetReceiverFullName(),
				selectedReceiver.GetReceiverMobile(),
				selectedReceiver.GetReceiverEmail(),
				selectedReceiver.GetReceiverAddress(),
				GetBanksDTO(this.senderVM.ReceiverVM.bankVM.GetSelectedBank()));

		receiverDTO.Id = selectedReceiver.GetCloudId();
		receiversDTO.add(receiverDTO);

		return receiversDTO;
	}

	private Amount GetAmountDTO(AmountModel amountModel) {
		Amount amount = new Amount(amountModel.GetSrcCode(),
				amountModel.GetDestCode(), amountModel.GetConvertRate(),
				amountModel.GetAmtSend(), amountModel.GetAmtReceived());
		// amount.Id = amountModel.getCloudId();
		return amount;

	}

	private Address GetAddressDTO(AddressModel addressModel) {

		return new Address(addressModel.GetCloudId(),addressModel.GetStNo(), addressModel.GetSuburb(),
				addressModel.GetPostCode(), addressModel.GetState());
	}

	private List<Bank> GetBanksDTO(BankModel bankModel) {

		List<Bank> banksDTO = new ArrayList<Bank>();

		Bank bank = new Bank(bankModel.GetBankName(),
				bankModel.GetAcountName(), bankModel.GetBankCode(),
				bankModel.GetAccountID(), bankModel.GetBranchName());
		bank.Id = bankModel.GetCloudId();

		banksDTO.add(bank);
		return banksDTO;
	}

}
