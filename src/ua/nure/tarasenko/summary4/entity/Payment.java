package ua.nure.tarasenko.summary4.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Payment {

	private long paymentId;
	private Date operationDate;
	private long sendAccount;
	private long receiveAccount;
	private BigDecimal amount;

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long l) {
		this.paymentId = l;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public long getSendAccount() {
		return sendAccount;
	}

	public void setSendAccount(long sendAccount) {
		this.sendAccount = sendAccount;
	}

	public long getReceiveAccount() {
		return receiveAccount;
	}

	public void setReceiveAccount(long receiveAccount) {
		this.receiveAccount = receiveAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", operationDate=" + operationDate + ", sendAccount=" + sendAccount
				+ ", receiveAccount=" + receiveAccount + ", amount=" + amount + "]";
	}

}
