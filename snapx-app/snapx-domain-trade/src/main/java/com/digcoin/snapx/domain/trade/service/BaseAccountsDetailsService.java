package com.digcoin.snapx.domain.trade.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.trade.bo.*;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.event.BaseAccountsTransferInEvent;
import com.digcoin.snapx.domain.trade.event.BaseAccountsTransferOutEvent;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsDetailsMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseAccountsDetailsService implements ApplicationEventPublisherAware {

    private static final int DEFAULT_LOCK_SECOND = 10;

    private final BaseAccountsMapper baseAccountsMapper;
    private final BaseAccountsDetailsMapper baseAccountsDetailsMapper;
    private final RedissonClient redissonClient;
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void transfer(BaseAccounts fromAccounts, BaseAccounts toAccounts, Long amount, BaseAccountsTransferDetail transferDetail) {
        this.transferOut(fromAccounts, toAccounts.getId(), amount, transferDetail);
        this.transferIn(toAccounts, fromAccounts.getId(), amount, transferDetail);
    }

    public Long transferIn(BaseAccounts baseAccounts, Long amount, BaseAccountsTransferDetail transferDetail) {
        return this.transferIn(baseAccounts, null, amount, transferDetail);
    }

    public Long transferOut(BaseAccounts baseAccounts, Long amount, BaseAccountsTransferDetail transferDetail) {
        return this.transferOut(baseAccounts, null, amount, transferDetail);
    }

    public Long transferIn(BaseAccounts baseAccounts, Long fromAccountsId, Long amount, BaseAccountsTransferDetail transferDetail) {

        Long baseAccountsId = baseAccounts.getId();
        BaseAccountsDetails detailsEntity = new BaseAccountsDetails();
        detailsEntity.setWalletAccountsId(baseAccountsId);
        detailsEntity.setAccountsType(baseAccounts.getAccountsType());
        detailsEntity.setCategory(baseAccounts.getCategory());
        detailsEntity.setMemberId(baseAccounts.getMemberId());
        detailsEntity.setAmount(amount);
        detailsEntity.setDirection(BaseAccountsFinancialType.DEBIT);
        detailsEntity.setCustomerVisible(transferDetail.getCustomerVisibleMap().getOrDefault(baseAccountsId, true));
        if (Objects.nonNull(fromAccountsId)) {
            detailsEntity.setTransactionAccountsId(fromAccountsId);
        } else {
            detailsEntity.setTransactionAccountsId(null);
        }
        if (Objects.nonNull(transferDetail)) {
            detailsEntity.setSubject(transferDetail.getSubject());
            detailsEntity.setRemarks(transferDetail.getRemarks());
            detailsEntity.setVoucher(transferDetail.getVoucher());
            detailsEntity.setVoucherType(transferDetail.getVoucherType());
        }

        doLock(transferDetail, () -> {

            BaseAccounts accountsEntity = findAccounts(baseAccountsId);
            if (Objects.isNull(accountsEntity)) {
                log.warn("transferIn data not exist baseAccountsId:[{}] fromAccountsId:[{}] amount:[{}] transferDetail:[{}]",
                        baseAccountsId, fromAccountsId, amount, transferDetail);
                throw CommonError.DATA_NOT_EXIST.withMessage("accounts not exist");
            }
            long balanceAfter = accountsEntity.getBalance() + amount;
            detailsEntity.setBalanceBefore(accountsEntity.getBalance());
            detailsEntity.setBalanceAfter(balanceAfter);

            BaseAccountsDetails existDetails = findAccountsDetails(detailsEntity);
            if (Objects.nonNull(existDetails)) {
                log.warn("transferIn 重复的transferIn操作 baseAccountsId:[{}] fromAccountsId:[{}] amount:[{}] transferDetail:[{}]",
                        baseAccountsId, fromAccountsId, amount, transferDetail);
                throw CommonError.PARAMETER_ERROR.withMessage("duplicate operation");
            }

            baseAccountsDetailsMapper.insert(detailsEntity);
            int balanceUpdated = baseAccountsMapper.updateAccountsBalance(baseAccountsId, detailsEntity.getBalanceAfter(), detailsEntity.getBalanceBefore());
            int totalDebitUpdated = baseAccountsMapper.updateAccountsTotalDebit(baseAccountsId, accountsEntity.getTotalDebit() + amount, accountsEntity.getTotalDebit());
            if (balanceUpdated == 0 || totalDebitUpdated == 0) {
                log.warn("transferIn update fail baseAccountsId:[{}] fromAccountsId:[{}] amount:[{}] transferDetail:[{}]",
                        baseAccountsId, fromAccountsId, amount, transferDetail);
                throw CommonError.UNEXPECT_ERROR.withMessage("fail to update accounts balance");
            } else {
                eventPublisher.publishEvent(new BaseAccountsTransferInEvent(detailsEntity.getId()));
            }

        });

        return detailsEntity.getId();
    }

    public Long transferOut(BaseAccounts baseAccounts, Long toAccountsId, Long amount, BaseAccountsTransferDetail transferDetail) {
        Long baseAccountsId = baseAccounts.getId();
        BaseAccountsDetails detailsEntity = new BaseAccountsDetails();
        detailsEntity.setWalletAccountsId(baseAccountsId);
        detailsEntity.setAccountsType(baseAccounts.getAccountsType());
        detailsEntity.setCategory(baseAccounts.getCategory());
        detailsEntity.setMemberId(baseAccounts.getMemberId());
        detailsEntity.setAmount(amount);
        detailsEntity.setDirection(BaseAccountsFinancialType.CREDIT);
        detailsEntity.setCustomerVisible(transferDetail.getCustomerVisibleMap().getOrDefault(baseAccountsId, true));
        if (Objects.nonNull(toAccountsId)) {
            detailsEntity.setTransactionAccountsId(toAccountsId);
        } else {
            detailsEntity.setTransactionAccountsId(null);
        }
        if (Objects.nonNull(transferDetail)) {
            detailsEntity.setSubject(transferDetail.getSubject());
            detailsEntity.setRemarks(transferDetail.getRemarks());
            detailsEntity.setVoucher(transferDetail.getVoucher());
            detailsEntity.setVoucherType(transferDetail.getVoucherType());
        }

        doLock(transferDetail, () -> {

            BaseAccounts accountsEntity = findAccounts(baseAccountsId);
            if (Objects.isNull(accountsEntity)) {
                log.warn("transferOut data not exist baseAccountsId:[{}] toAccountsId:[{}] amount:[{}] transferDetail:[{}]",
                        baseAccountsId, toAccountsId, amount, transferDetail);
                throw CommonError.DATA_NOT_EXIST.withMessage("account not exist");
            }
            long balanceAfter = accountsEntity.getBalance() - amount;
            if (balanceAfter < 0) {
                log.warn("transferOut data not exist baseAccountsId:[{}] toAccountsId:[{}] amount:[{}] transferDetail:[{}]",
                        baseAccountsId, toAccountsId, amount, transferDetail);
                throw CommonError.PARAMETER_ERROR.withMessage("accounts balance not enough, fail to complete operation");
            }
            detailsEntity.setBalanceBefore(accountsEntity.getBalance());
            detailsEntity.setBalanceAfter(balanceAfter);

            BaseAccountsDetails existDetails = findAccountsDetails(detailsEntity);
            if (Objects.nonNull(existDetails)) {
                log.warn("transferOut 重复的transferOut操作 baseAccountsId:[{}] toAccountsId:[{}] amount:[{}] transferDetail:[{}]",
                        baseAccountsId, toAccountsId, amount, transferDetail);
                throw CommonError.PARAMETER_ERROR.withMessage("duplicate operation");
            }

            baseAccountsDetailsMapper.insert(detailsEntity);
            int balanceUpdated = baseAccountsMapper.updateAccountsBalance(baseAccountsId, detailsEntity.getBalanceAfter(), detailsEntity.getBalanceBefore());
            int totalCreditUpdated = baseAccountsMapper.updateAccountsTotalCredit(baseAccountsId, accountsEntity.getTotalCredit() + amount, accountsEntity.getTotalCredit());
            if (balanceUpdated == 0 || totalCreditUpdated == 0) {
                log.warn("transferOut update fail baseAccountsId:[{}] amount:[{}] transferDetail:[{}]", baseAccountsId, amount, transferDetail);
                throw CommonError.UNEXPECT_ERROR.withMessage("fail to update accounts balance");
            } else {
                eventPublisher.publishEvent(new BaseAccountsTransferOutEvent(detailsEntity.getId()));
            }

        });

        return detailsEntity.getId();
    }

    public BaseAccountsDetails findAccountsDetails(Long baseAccountsDetailsId) {
        return baseAccountsDetailsMapper.selectById(baseAccountsDetailsId);
    }

    public BaseAccountsDetails findAccountsDetails(BaseAccountsDetails baseAccountsDetails) {
        Objects.requireNonNull(baseAccountsDetails);
        Long walletAccountId = Objects.requireNonNull(baseAccountsDetails.getWalletAccountsId());
        String subject = Objects.requireNonNull(baseAccountsDetails.getSubject());
        String voucherType = Objects.requireNonNull(baseAccountsDetails.getVoucherType());
        String voucher = Objects.requireNonNull(baseAccountsDetails.getVoucher());
        BaseAccountsFinancialType direction = Objects.requireNonNull(baseAccountsDetails.getDirection());

        return baseAccountsDetailsMapper.selectOne(Wrappers.lambdaQuery(BaseAccountsDetails.class)
                .eq(BaseAccountsDetails::getWalletAccountsId, walletAccountId)
                .eq(BaseAccountsDetails::getSubject, subject)
                .eq(BaseAccountsDetails::getVoucherType, voucherType)
                .eq(BaseAccountsDetails::getVoucher, voucher)
                .eq(BaseAccountsDetails::getDirection, direction));
    }

    public List<BaseAccountsDetails> listBaseAccountsDetails(BaseAccountsDetailsFilter filter) {
        return baseAccountsDetailsMapper.selectList(Wrappers.lambdaQuery(BaseAccountsDetails.class)
                .eq(Objects.nonNull(filter.getBaseAccountsId()), BaseAccountsDetails::getWalletAccountsId, filter.getBaseAccountsId())
                .eq(Objects.nonNull(filter.getAccountsType()), BaseAccountsDetails::getAccountsType, filter.getAccountsType())
                .eq(Objects.nonNull(filter.getDirection()), BaseAccountsDetails::getDirection, filter.getDirection())
                .eq(StringUtils.isNotBlank(filter.getVoucherType()), BaseAccountsDetails::getVoucherType, filter.getVoucherType())
                .eq(StringUtils.isNotBlank(filter.getVoucher()), BaseAccountsDetails::getVoucher, filter.getVoucher())
                .in(CollectionUtils.isNotEmpty(filter.getVouchers()), BaseAccountsDetails::getVoucher, filter.getVouchers())
                .in(CollectionUtils.isNotEmpty(filter.getIds()), BaseAccountsDetails::getId, filter.getIds())
                .orderByDesc(BaseAccountsDetails::getCreateTime));
    }

    public PageResult<BaseAccountsDetails> pageBaseAccountsDetails(BaseAccountsDetailsQuery query) {
        IPage<BaseAccountsDetails> page = PageHelper.getPage(query);
        LambdaQueryWrapper<BaseAccountsDetails> queryWrapper = Wrappers.lambdaQuery(BaseAccountsDetails.class)
                .eq(BaseAccountsDetails::getWalletAccountsId, query.getWalletAccountsId())
                .eq(Objects.nonNull(query.getDirection()), BaseAccountsDetails::getDirection, query.getDirection())
                .eq(Objects.nonNull(query.getCustomerVisible()), BaseAccountsDetails::getCustomerVisible, query.getCustomerVisible())
                .orderByDesc(BaseAccountsDetails::getCreateTime, BaseAccountsDetails::getId);
        IPage<BaseAccountsDetails> pageResult = baseAccountsDetailsMapper.selectPage(page, queryWrapper);
        return PageResult.fromPage(pageResult, Function.identity());
    }

    private BaseAccounts findAccounts(Long baseAccountsId) {
        return baseAccountsMapper.selectById(baseAccountsId);
    }

    private void doLock(BaseAccountsTransferDetail transferDetail, Runnable runnable) {
        doLock(DEFAULT_LOCK_SECOND, transferDetail, runnable);
    }

    private void doLock(int second, BaseAccountsTransferDetail transferDetail, Runnable runnable) {
        String key = String.join(":", transferDetail.getSubject(), transferDetail.getVoucherType(), transferDetail.getVoucher());
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(second, TimeUnit.SECONDS);
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

}
