package study.daydayup.wolf.business.uc.crm.biz.customer.credit.api;

import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import study.daydayup.wolf.business.uc.api.crm.customer.credit.entity.CreditConfig;
import study.daydayup.wolf.business.uc.api.crm.customer.credit.service.CreditConfigService;
import study.daydayup.wolf.business.uc.crm.biz.customer.credit.converter.CreditConfigConverter;
import study.daydayup.wolf.business.uc.crm.biz.customer.credit.dal.dao.CreditConfigDAO;
import study.daydayup.wolf.business.uc.crm.biz.customer.credit.dal.dataobject.CreditConfigDO;
import study.daydayup.wolf.business.uc.crm.biz.customer.credit.service.CreditConfigDomainService;
import study.daydayup.wolf.framework.rpc.RpcService;

import javax.annotation.Resource;

/**
 * study.daydayup.wolf.business.uc.crm.biz.customer.credit.api
 *
 * @author Wingle
 * @since 2020/3/10 1:19 下午
 **/
@RpcService(protocol = "dubbo")
public class CreditConfigServiceImpl implements CreditConfigService {
    @Resource
    private CreditConfigDAO dao;
    @Resource
    private CreditConfigDomainService domainService;

    @Override
    public CreditConfig find(@NonNull Long orgId) {
        return domainService.find(orgId);
    }

    @Override
    public int save(@Validated CreditConfig config) {
        CreditConfigDO configDO = CreditConfigConverter.toDO(config);
        Long orgId = config.getOrgId();

        if (existConfig(orgId)) {
            return editConfig(configDO, orgId);
        }

        return addConfig(configDO);
    }

    private boolean existConfig(Long orgId) {
        CreditConfigDO configDO = dao.selectByOrgId(orgId);
        return configDO != null;
    }

    private int addConfig(@NonNull CreditConfigDO configDO) {
        return dao.insertSelective(configDO);
    }

    private int editConfig(@NonNull CreditConfigDO configDO, @NonNull Long orgId) {
        return dao.updateByOrgId(configDO, orgId);
    }


}
