//package com.ruoyi.web.utils;
//
//public class TronTransferUsdt {
//    /**
//     * 转账
//     *
//     * @param fromAddress       来源地址
//     * @param fromHexPrivateKey 来源密钥
//     * @param toAddress         对方地址
//     * @param amount            数量
//     * @return 区块链交易id
//     */
//
//    public static String transferUSDT(String fromAddress, String fromHexPrivateKey, String toAddress, BigInteger amount) {
//
//        ApiWrapper client = getApiWrapper(fromHexPrivateKey);
//
//        Function transfer =
//
//                new Function(
//
//                        "transfer",
//
//                        Arrays.asList(new Address(toAddress), new Uint256(amount)),
//
//                        Arrays.asList(new TypeReference() {
//                        }));
//
//        TransactionBuilder builder =
//
//                client.triggerCall(fromAddress, TRC20_USDT_CONTRACT_ADDR, transfer);
//
//        builder.setFeeLimit(50000000);
//
//        Chain.Transaction transaction = client.signTransaction(builder.getTransaction());
//
//        String txid = client.broadcastTransaction(transaction);
//
//        client.close();
//
//        return txid;
//
//    }
//
//
//    /**
//
//     * 转账TRX
//
//     * @param fromAddress 来源地址
//
//     * @param fromHexPrivateKey 来源 秘钥
//
//     * @param toAddress 目标地址
//
//     * @param amount 数量
//
//     * @return
//
//     * @throws IllegalException
//
//     */
//
//    public static String transferTRX( String fromAddress, String fromHexPrivateKey, String toAddress, long amount)
//
//            throws IllegalException {
//
//        ApiWrapper client = getApiWrapper(fromHexPrivateKey);
//
//        Response.TransactionExtention transactionExtention =
//
//                client.transfer(fromAddress, toAddress, amount);
//
//        Chain.Transaction transaction = client.signTransaction(transactionExtention);
//
//        String txid = client.broadcastTransaction(transaction);
//
//        client.close();
//
//        return txid;
//
//    }
//
//3.查询交易状态
//
//    /**
//
//     * 查询交易状态
//
//     *
//
//     * @param txid
//
//     * @return
//
//     * @throws IllegalException
//
//     */
//
//    public String getTransactionStatusById(String txid) throws IllegalException {
//
//        ApiWrapper client = getApiWrapper(tronServiceConfig.getHexPrivateKey());
//
//        Chain.Transaction getTransaction = client.getTransactionById(txid);
//
//        return getTransaction.getRet(0).getContractRet().name();
//
//    }
//}
