package com.fruit.portal.action.loan;



import com.fruit.portal.action.BaseAction;
import com.fruit.portal.service.loan.LoanContractService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户资金服务电子合同相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-06-14
 * Project        : portal
 */
@Component
@UriMapping("/loan/contract")
public class LoanContractAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanAuthAction.class);


    @Autowired
    private LoanContractService loanContractService;

    /**
     * 合同-安心签---开通安心签账户页面
     * @return
     */
    @UriMapping(value = "/certificate", interceptors = {"userLoginCheckInterceptor"})
    public String authenticationPage()
    {
//        int userId = getLoginUserId();
        try
        {

//            //此处加预埋数据
//            List<BankInfoDTO> bankList = this.baseBankService.loadAll();
//            Map<String, Object> data = new HashMap<String, Object>();
//            data.put("bankList", bankList);//银行列表信息
//            HttpServletRequest request = WebContext.getRequest();
//            request.setAttribute("__DATA", super.toJson(data));

            return "/loan/loan_certificate";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/contract/certificate].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    /**
     * 合同-签合同页面
     * @return
     */
    @UriMapping(value = "/signcontract", interceptors = {"userLoginCheckInterceptor"})
    public String signcontractPage()
    {
        try
        {
            return "/loan/loan_signcontract";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/contract/signcontract].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }



}
