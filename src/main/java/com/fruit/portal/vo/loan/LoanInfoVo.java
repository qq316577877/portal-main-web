package com.fruit.portal.vo.loan;

import com.fruit.loan.biz.dto.LoanInfoDTO;
import java.util.List;

public class LoanInfoVo {

	private List<LoanInfoDTO> loanInfos;

	private List<String> transactionNoList;

	public List<String> getTransactionNoList() {
		return transactionNoList;
	}

	public void setTransactionNoList(List<String> transactionNoList) {
		this.transactionNoList = transactionNoList;
	}

	public List<LoanInfoDTO> getLoanInfos() {
		return loanInfos;
	}

	public void setLoanInfos(List<LoanInfoDTO> loanInfos) {
		this.loanInfos = loanInfos;
	}
}
