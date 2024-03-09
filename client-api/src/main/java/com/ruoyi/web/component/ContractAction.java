///**
// *
// */
//package com.ruoyi.web.component;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.tron.trident.abi.FunctionReturnDecoder;
//import org.tron.trident.abi.datatypes.Function;
//import org.tron.trident.abi.datatypes.Type;
//import org.tron.trident.core.ApiWrapper;
//import org.tron.trident.core.exceptions.IllegalException;
//import org.tron.trident.core.transaction.TransactionBuilder;
//import org.tron.trident.proto.Chain.Transaction;
//import org.tron.trident.proto.Response.TransactionExtention;
//import org.tron.trident.proto.Response.TransactionInfo;
//import org.tron.trident.utils.Numeric;
//
//import java.util.List;
//
///**
// * @author iven
// *
// */
//@Component
//@Slf4j
//public class ContractAction {
//	@Value("${privateKey}")
//	private String privateKey;
//	@Value("${ownerAddr}")
//	private String ownerAddr;
//	@Value("${contractAddr}")
//	private String contractAddr;
//
//	public String exeFunction(Function function) {
//		// main net, using TronGrid
//		// ApiWrapper wrapper = ApiWrapper.ofMainnet("hex private key", "API key");
//		// Nile test net, using a node from Nile official website
//		ApiWrapper wrapper = ApiWrapper.ofNile(privateKey);
//
//		TransactionBuilder builder = wrapper.triggerCall(ownerAddr, contractAddr, function);
//		builder.setFeeLimit(1000000000L);
//		builder.setMemo("memo test");
//		Transaction signedTxn = wrapper.signTransaction(builder.build());
//
//		log.info("signedTxn is:{}",signedTxn.toString());
//		String ret = wrapper.broadcastTransaction(signedTxn);
//		log.info("ret is:{}",ret);
//		try {
//			Transaction transactionById = wrapper.getTransactionById(ret);
//			log.info("transactionById is:{}",transactionById);
//			TransactionInfo transactionInfoById = wrapper.getTransactionInfoById("24337b660790301a7c7919f11d910fa67db608ff9399a08e804fe067d9ecc91e");
//			log.info("transactionInfoById is:{}",transactionInfoById);
//		} catch (IllegalException e) {
//			log.error("查询交易失败", e);
//		}
//		wrapper.close();
//		return ret;
//	}
//
//	public List<Type> constCallFunction(Function function) {
//		//main net, using TronGrid
//		//ApiWrapper wrapper = ApiWrapper.ofMainnet("hex private key", "API key");
//		//Nile test net, using a node from Nile official website
//        ApiWrapper wrapper = ApiWrapper.ofNile(privateKey);
//
//		TransactionExtention txnExt = wrapper.constantCall(ownerAddr, contractAddr, function);
//		String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
//		List<Type> functionResult = FunctionReturnDecoder.decode(result, function.getOutputParameters());
//		wrapper.close();
//		return functionResult;
//	}
//
//}
