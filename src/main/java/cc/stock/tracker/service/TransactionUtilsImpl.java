package cc.stock.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.stock.tracker.repository.TransactionRepository;

@Service
public class TransactionUtilsImpl implements TransactionUtils {

	@Autowired
	private TransactionRepository transactionRepository;	
	
	
}
