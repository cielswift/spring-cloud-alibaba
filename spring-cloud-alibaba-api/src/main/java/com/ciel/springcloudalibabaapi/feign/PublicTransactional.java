package com.ciel.springcloudalibabaapi.feign;

import com.ciel.springcloudalibabaapi.exception.AlertException;

import java.math.BigDecimal;

public interface PublicTransactional {

    /**
     * 修改余额
     * @param price 修改数目
     * @param sendUserId 转账者
     * @param receiveUserId 接收者
     * @param code 1, 转账者方, -1 接收者方
     * @return 是否成功
     */
    public boolean transactionPrice(BigDecimal price,Long sendUserId, Long receiveUserId,Integer code);


    public boolean hmilyTransaction(BigDecimal price,Long sendUserId,
                                    Long receiveUserId,Integer code) throws AlertException;

}
