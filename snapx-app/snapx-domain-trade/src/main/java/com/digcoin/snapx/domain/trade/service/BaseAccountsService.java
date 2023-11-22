package com.digcoin.snapx.domain.trade.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.trade.bo.*;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType;
import com.digcoin.snapx.domain.trade.entity.AccountsModification;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsAggregationVoucher;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.mapper.AccountsModificationMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsAggregationVoucherMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsDetailsMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class BaseAccountsService {

    protected final BaseAccountsMapper baseAccountsMapper;
    protected final BaseAccountsDetailsMapper baseAccountsDetailsMapper;
    protected final BaseAccountsDetailsService baseAccountsDetailsService;
    protected final BaseAccountsAggregationVoucherMapper baseAccountsAggregationVoucherMapper;
    protected final AccountsModificationMapper accountsModificationMapper;

    public BaseAccountsService(BaseAccountsMapper baseAccountsMapper,
                               BaseAccountsDetailsMapper baseAccountsDetailsMapper,
                               BaseAccountsDetailsService baseAccountsDetailsService,
                               BaseAccountsAggregationVoucherMapper baseAccountsAggregationVoucherMapper,
                               AccountsModificationMapper accountsModificationMapper) {
        this.baseAccountsMapper = baseAccountsMapper;
        this.baseAccountsDetailsMapper = baseAccountsDetailsMapper;
        this.baseAccountsDetailsService = baseAccountsDetailsService;
        this.baseAccountsAggregationVoucherMapper = baseAccountsAggregationVoucherMapper;
        this.accountsModificationMapper = accountsModificationMapper;
    }

    public Map<Long, BaseAccounts> getAccountsMap(Collection<Long> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyMap();
        }
        return baseAccountsMapper.selectList(Wrappers.lambdaQuery(BaseAccounts.class)
                        .in(BaseAccounts::getMemberId, memberIds)
                        .eq(BaseAccounts::getAccountsType, getAccountsType())
                        .eq(BaseAccounts::getCategory, BaseAccountsCategory.AVAILABLE))
                .stream()
                .collect(Collectors.toMap(BaseAccounts::getMemberId, Function.identity()));
    }

    protected abstract BaseAccountsType getAccountsType();

    /**
     * 管理员直接修改账户增加点数
     *
     * @param memberId 账户所属用户id
     * @param amount   增加的点数
     */
    public void increasePointsByAdminModify(Long memberId, BigDecimal amount, String reason) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        long id = createAccountsModification(walletAccounts, BaseAccountsFinancialType.DEBIT, amount, reason);
        increasePoints(walletAccounts,
                BaseAccountsFinancialSubject.ACCOUNTS_MODIFICATION,
                BaseAccountsVoucherType.ACCOUNTS_MODIFICATION,
                String.valueOf(id),
                amount,
                reason);
    }

    /**
     * 管理员直接修改账户减少点数
     *
     * @param memberId 账户所属用户id
     * @param amount   减少的点数
     */
    public void reducePointsByAdminModify(Long memberId, BigDecimal amount, String reason) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        long id = createAccountsModification(walletAccounts, BaseAccountsFinancialType.CREDIT, amount, reason);
        reducePoints(walletAccounts,
                BaseAccountsFinancialSubject.ACCOUNTS_MODIFICATION,
                BaseAccountsVoucherType.ACCOUNTS_MODIFICATION,
                String.valueOf(id),
                amount,
                reason);
    }

    /**
     * 被邀请人获得点数后，邀请人分佣的点数
     *
     * @param memberId     分佣记录所属用户id
     * @param commissionId 分佣记录id
     * @param amount       分佣的点数
     */
    public Long increasePointsByInviterCommission(Long memberId, Long commissionId, BigDecimal amount) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        return increasePoints(walletAccounts,
                BaseAccountsFinancialSubject.INVITER_COMMISSION,
                BaseAccountsVoucherType.INVITER_COMMISSION,
                String.valueOf(commissionId),
                amount);
    }

    /**
     * 获取评价id与金额映射
     *
     * @param evaluateIds 餐馆评价id集合
     * @return
     */
    public Map<Long, BigDecimal> getAccountsVoucherAmountMapForEvaluate(Collection<Long> evaluateIds) {
        // 通过餐馆评价id集合获取相关的因为餐馆评价产生的奖励点数记录集合
        List<String> vouchers = evaluateIds.stream().map(String::valueOf).collect(Collectors.toList());
        List<BaseAccountsDetails> accountsDetailsList = baseAccountsDetailsService.listBaseAccountsDetails(BaseAccountsDetailsFilter.builder()
                .accountsType(getAccountsType())
                .direction(BaseAccountsFinancialType.DEBIT)
                .voucherType(BaseAccountsVoucherType.RESTAURANT_EVALUATE)
                .vouchers(vouchers)
                .build());
        Map<Long, Long> totalAmountMap = accountsDetailsList
                .stream()
                .collect(Collectors.groupingBy(BaseAccountsDetails::getVoucher))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        x -> Long.valueOf(x.getKey()),
                        x -> x.getValue().stream().mapToLong(BaseAccountsDetails::getAmount).sum()));

        // 通过餐馆评价id集合获取后台修改奖励点数的操作记录集合
        List<AccountsModification> accountsModificationList = accountsModificationMapper.selectList(Wrappers.lambdaQuery(AccountsModification.class)
                .eq(AccountsModification::getVoucherType, BaseAccountsVoucherType.RESTAURANT_EVALUATE)
                .in(AccountsModification::getVoucher, vouchers));
        Map<Long, Long> modifyAmountMap = accountsModificationList.stream()
                .collect(Collectors.groupingBy(AccountsModification::getVoucher))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        x -> Long.valueOf(x.getKey()),
                        x -> x.getValue().stream()
                                .mapToLong(y -> y.getAmount() * (BaseAccountsFinancialType.CREDIT.equals(y.getDirection()) ? -1L : 1L))
                                .sum()));

        // 合并奖励记录的点数和修改记录的点数
        Map<Long, BigDecimal> resultMap = new HashMap<>(evaluateIds.size());
        for (Long evaluateId : evaluateIds) {
            Long totalAmount = totalAmountMap.getOrDefault(evaluateId, 0L);
            Long modifyAmount = modifyAmountMap.getOrDefault(evaluateId, 0L);
            resultMap.put(evaluateId, new BigDecimal(totalAmount + modifyAmount));
        }

        return resultMap;
    }


    /**
     * 查询参与活动产生的点数明细列表
     *
     * @param memberId   会员id
     * @param activityId 活动id
     * @return
     */
    public List<SimpleAccountsDetails> listAccountsDetailsIncreaseForActivity(Long memberId, Long activityId) {
        BaseAccounts accounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        return baseAccountsDetailsService.listBaseAccountsDetails(BaseAccountsDetailsFilter.builder()
                        .baseAccountsId(accounts.getId())
                        .direction(BaseAccountsFinancialType.DEBIT)
                        .voucherType(BaseAccountsVoucherType.ACTIVITY)
                        .voucher(String.valueOf(activityId))
                        .build())
                .stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    /**
     * 查询评价餐馆产生的点数明细列表
     *
     * @param memberId   会员id
     * @param evaluateId 活动id
     * @return
     */
    public List<SimpleAccountsDetails> listAccountsDetailsIncreaseForEvaluate(Long memberId, Long evaluateId) {
        BaseAccounts accounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        return baseAccountsDetailsService.listBaseAccountsDetails(BaseAccountsDetailsFilter.builder()
                        .baseAccountsId(accounts.getId())
                        .direction(BaseAccountsFinancialType.DEBIT)
                        .voucherType(BaseAccountsVoucherType.RESTAURANT_EVALUATE)
                        .voucher(String.valueOf(evaluateId))
                        .build())
                .stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    /**
     * 批量查询评价餐馆产生的点数明细列表
     *
     * @param evaluateIds 评价id集合
     * @return
     */
    public List<SimpleAccountsDetails> listAccountsDetailsIncreaseForEvaluate(Collection<Long> evaluateIds) {
        return baseAccountsDetailsService.listBaseAccountsDetails(BaseAccountsDetailsFilter.builder()
                        .accountsType(getAccountsType())
                        .direction(BaseAccountsFinancialType.DEBIT)
                        .voucherType(BaseAccountsVoucherType.RESTAURANT_EVALUATE)
                        .vouchers(evaluateIds.stream().map(String::valueOf).collect(Collectors.toSet()))
                        .build())
                .stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    private SimpleAccountsDetails converter(BaseAccountsDetails entity) {
        SimpleAccountsDetails result = new SimpleAccountsDetails();
        BeanUtils.copyProperties(entity, result);
        result.setAmount(translate(entity.getAmount()));
        return result;
    }

    /**
     * 增加参与活动产生的点数
     * <p>
     * subject枚举
     *
     * @param memberId   会员id
     * @param activityId 活动id
     * @param subject    类型枚举
     * @param amount     数额
     * @return
     * @see com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject
     */
    public Long increasePointsForActivity(Long memberId, Long activityId, String subject, BigDecimal amount) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        return increasePoints(
                walletAccounts,
                subject,
                BaseAccountsVoucherType.ACTIVITY,
                String.valueOf(activityId),
                amount);
    }

    /**
     * 评价餐馆产生的点数
     * <p>
     * subject枚举
     *
     * @param memberId   会员id
     * @param evaluateId 评价id
     * @param subject    类型枚举
     * @param amount     数额
     * @return
     * @see com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject
     */
    public Long increasePointsForEvaluate(Long memberId, Long evaluateId, String subject, BigDecimal amount) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        return increasePoints(
                walletAccounts,
                subject,
                BaseAccountsVoucherType.RESTAURANT_EVALUATE,
                String.valueOf(evaluateId),
                amount);
    }

    public Long increasePoints(BaseAccounts walletAccounts, String subject, String voucherType, String voucher, BigDecimal amount) {
        return increasePoints(walletAccounts, subject, voucherType, voucher, amount, null);
    }

    public Long increasePoints(BaseAccounts walletAccounts, String subject, String voucherType, String voucher, BigDecimal amount, String remarks) {
        long income = translate(amount);
        BaseAccountsTransferDetail transferDetail = new BaseAccountsTransferDetail();
        transferDetail.setSubject(subject);
        transferDetail.setRemarks(remarks);
        transferDetail.setVoucherType(voucherType);
        transferDetail.setVoucher(voucher);
        return baseAccountsDetailsService.transferIn(walletAccounts, income, transferDetail);
    }

    public Long reducePoints(BaseAccounts walletAccounts, String subject, String voucherType, String voucher, BigDecimal amount) {
        return reducePoints(walletAccounts, subject, voucherType, voucher, amount, null);
    }

    public Long reducePoints(BaseAccounts walletAccounts, String subject, String voucherType, String voucher, BigDecimal amount, String remarks) {
        long income = translate(amount);
        BaseAccountsTransferDetail transferDetail = new BaseAccountsTransferDetail();
        transferDetail.setSubject(subject);
        transferDetail.setRemarks(remarks);
        transferDetail.setVoucherType(voucherType);
        transferDetail.setVoucher(voucher);
        return baseAccountsDetailsService.transferOut(walletAccounts, income, transferDetail);
    }

    protected long createAccountsModification(BaseAccounts walletAccounts, BaseAccountsFinancialType direction, BigDecimal amount, String reason) {
        return createAccountsModification(walletAccounts, direction, "", "", amount, reason);
    }

    protected long createAccountsModification(BaseAccounts walletAccounts, BaseAccountsFinancialType direction, String voucherType, String voucher, BigDecimal amount, String reason) {
        AccountsModification accountsModification = new AccountsModification();
        accountsModification.setMemberId(walletAccounts.getMemberId());
        accountsModification.setAccountsId(walletAccounts.getId());
        accountsModification.setAccountsType(walletAccounts.getAccountsType());
        accountsModification.setCategory(walletAccounts.getCategory());
        accountsModification.setVoucherType(voucherType);
        accountsModification.setVoucher(voucher);
        accountsModification.setDirection(direction);
        accountsModification.setAmount(translate(amount));
        accountsModification.setReason(reason);
        accountsModificationMapper.insert(accountsModification);
        return accountsModification.getId();
    }

    public BaseAccounts findAccounts(Long memberId, BaseAccountsCategory category) {
        BaseAccounts baseAccounts = baseAccountsMapper.selectOne(Wrappers.lambdaQuery(BaseAccounts.class)
                .eq(BaseAccounts::getAccountsType, getAccountsType())
                .eq(BaseAccounts::getCategory, category)
                .eq(BaseAccounts::getMemberId, memberId));
        if (Objects.isNull(baseAccounts)) {
            return createAccounts(memberId, category);
        } else {
            return baseAccounts;
        }
    }

    protected BaseAccounts createAccounts(Long memberId, BaseAccountsCategory category) {
        BaseAccounts entity = new BaseAccounts();
        entity.setAccountsType(getAccountsType());
        entity.setCategory(category);
        entity.setFinancialType(category.getFinancialType());
        entity.setMemberId(memberId);
        entity.setBalance(0L);
        baseAccountsMapper.insert(entity);
        return entity;
    }

    public BaseAccounts findAccounts(Long baseAccountsId) {
        return baseAccountsMapper.selectById(baseAccountsId);
    }

    public BaseAccountsBalance findAccountsBalance(Long memberId) {
        List<BaseAccounts> baseAccountsList = baseAccountsMapper.selectList(Wrappers.lambdaQuery(BaseAccounts.class)
                .eq(BaseAccounts::getAccountsType, getAccountsType())
                .eq(BaseAccounts::getMemberId, memberId));
        long total = 0L;
        BaseAccountsBalance baseAccountsBalance = new BaseAccountsBalance();
        for (BaseAccounts baseAccounts : baseAccountsList) {
            total += baseAccounts.getBalance();
            switch (baseAccounts.getCategory()) {
                case AVAILABLE:
                    baseAccountsBalance.setAvailable(translate(baseAccounts.getBalance()));
                    break;
                case FROZEN:
                    baseAccountsBalance.setFrozen(translate(baseAccounts.getBalance()));
                    break;
                default:
            }
        }
        baseAccountsBalance.setTotal(translate(total));
        return baseAccountsBalance;
    }

    public BigDecimal getBalance(Long memberId) {
        List<BaseAccounts> accounts = baseAccountsMapper.selectList(Wrappers.lambdaQuery(BaseAccounts.class)
                .eq(BaseAccounts::getCategory, BaseAccountsCategory.AVAILABLE)
                .eq(BaseAccounts::getAccountsType, getAccountsType())
                .eq(BaseAccounts::getMemberId, memberId));
        return accounts.stream()
                .findAny()
                .map(BaseAccounts::getTotalDebit)
                .map(this::translate)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 获取账户累计收入
     *
     * @param memberId 会员id
     * @return
     */
    public BigDecimal getTotalDebit(Long memberId) {
        List<BaseAccounts> accounts = baseAccountsMapper.selectList(Wrappers.lambdaQuery(BaseAccounts.class)
                .eq(BaseAccounts::getCategory, BaseAccountsCategory.AVAILABLE)
                .eq(BaseAccounts::getAccountsType, getAccountsType())
                .eq(BaseAccounts::getMemberId, memberId));
        return accounts.stream()
                .findAny()
                .map(BaseAccounts::getTotalDebit)
                .map(this::translate)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 获取账户累计支出
     *
     * @param memberId 会员id
     * @return
     */
    public BigDecimal getTotalCredit(Long memberId) {
        List<BaseAccounts> accounts = baseAccountsMapper.selectList(Wrappers.lambdaQuery(BaseAccounts.class)
                .eq(BaseAccounts::getCategory, BaseAccountsCategory.AVAILABLE)
                .eq(BaseAccounts::getAccountsType, getAccountsType())
                .eq(BaseAccounts::getMemberId, memberId));
        return accounts.stream()
                .findAny()
                .map(BaseAccounts::getTotalCredit)
                .map(this::translate)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getAmountSum(BaseAmountSumParameter baseAmountSumParameter) {
        baseAmountSumParameter.setAccountsType(getAccountsType());
        return translate(baseAccountsDetailsMapper.getAmountSum(baseAmountSumParameter));
    }

    public Map<Long, BigDecimal> getRecentDaysEarnAmountSum(Collection<Long> memberIds, Integer days) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        List<BaseAmountSumByDatetime> resultList = baseAccountsDetailsMapper.getAmountSumByDatetime(BaseAmountSumByDatetimeParameter.builder()
                .memberIds(memberIds)
                .accountsType(getAccountsType())
                .startTime(startTime)
                .endTime(endTime)
                .build());
        return resultList.stream()
                .collect(Collectors.toMap(BaseAmountSumByDatetime::getMemberId, x -> translate(x.getAmount())));
    }

    public List<BaseAccountRecentDayRanking> pageRecentDayRanking(Long offset, Long limit, Integer days) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        List<BaseAccountRecentDayRanking> result = baseAccountsDetailsMapper.pageRecentDayRanking(offset, limit, startTime, getAccountsType());
        for (BaseAccountRecentDayRanking item : result) {
            item.setAmountBigDecimal(translate(item.getAmount()));
        }
        return result;
    }

    public BigDecimal getTotalAmountSum() {
        Long result = baseAccountsDetailsMapper.getTotalAmountSum(BaseAmountSumByDatetimeParameter.builder()
                .accountsType(getAccountsType())
                .build());
        return translate(result);
    }

    public Map<Long, Long> getRawBalanceMap(BaseAccountsCategory category, Collection<Long> memberIds) {
        List<BaseAccounts> accounts = baseAccountsMapper.selectList(Wrappers.lambdaQuery(BaseAccounts.class)
                .eq(BaseAccounts::getAccountsType, getAccountsType())
                .eq(BaseAccounts::getCategory, category)
                .in(BaseAccounts::getMemberId, memberIds));
        return accounts
                .stream()
                .collect(Collectors.toMap(BaseAccounts::getMemberId, BaseAccounts::getBalance));
    }

    public BigDecimal translate(Long amount) {
        if (Objects.isNull(amount) || amount == 0) {
            return BigDecimal.ZERO;
        }
        return doTranslate(amount);
    }

    public Long translate(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.equals(BigDecimal.ZERO)) {
            return 0L;
        }
        return doTranslate(amount);
    }

    public BigDecimal getEvaluateAmountAggregation(Long memberId) {
        return getAggregationByVoucherType(memberId, BaseAccountsVoucherType.RESTAURANT_EVALUATE, BaseAccountsFinancialType.DEBIT);
    }

    public BigDecimal getActivityAmountAggregation(Long memberId) {
        return getAggregationByVoucherType(memberId, BaseAccountsVoucherType.ACTIVITY, BaseAccountsFinancialType.DEBIT);
    }

    private BigDecimal getAggregationByVoucherType(Long memberId, String voucherType, BaseAccountsFinancialType direction) {
        Optional<BaseAccountsAggregationVoucher> result = Optional.ofNullable(baseAccountsAggregationVoucherMapper
                .selectOne(Wrappers.lambdaQuery(BaseAccountsAggregationVoucher.class)
                        .eq(BaseAccountsAggregationVoucher::getAccountsType, getAccountsType())
                        .eq(BaseAccountsAggregationVoucher::getMemberId, memberId)
                        .eq(BaseAccountsAggregationVoucher::getFinancialType, direction)
                        .eq(BaseAccountsAggregationVoucher::getVoucherType, voucherType)));
        return result
                .map(BaseAccountsAggregationVoucher::getAmount)
                .map(this::translate)
                .orElse(BigDecimal.ZERO);
    }

    protected abstract BigDecimal doTranslate(Long amount);

    protected abstract Long doTranslate(BigDecimal amount);

}
