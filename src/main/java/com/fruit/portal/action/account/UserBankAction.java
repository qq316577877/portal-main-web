package com.fruit.portal.action.account;

import com.fruit.account.biz.dto.UserBankDTO;
import com.fruit.base.biz.dto.BankInfoDTO;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserBankRequest;
import com.fruit.portal.service.BaseBankService;
import com.fruit.portal.service.account.BankService;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.vo.account.BankVo;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户银行卡相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-05-18
 * Project        : portal
 */
@Component
@UriMapping("/member/bank")
public class UserBankAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBankAction.class);

    @Autowired
    private BankService bankService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BaseBankService baseBankService;

    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String bankList()
    {
        int userId = getLoginUserId();
        try
        {
            List<UserBankDTO> bankDTOs = bankService.loadAllBank(userId);

            List<BankVo> bankCards = this.bankService.wrapVOs(bankDTOs);
            List<BankInfoDTO> bankList = this.baseBankService.loadAll();

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("bankCards", bankCards);//用户银行卡列表信息

            data.put("bankList", bankList);//银行列表信息

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("__DATA", super.toJson(data));
            return "/account/bank_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/bank/list].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/add_user_bank_information_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addBank()
    {
        int userId = getLoginUserId();
        Map<String, Object> params = getParamsValidationResults();
        String accountName = (String) params.get("accountName");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        int bankTypeId = (Integer) params.get("bankTypeId");
        String bankName = (String) params.get("bankName");
        String bankCard = (String) params.get("bankCard");
//        int isDefault = (Integer) params.get("isDefault");//20170525产品经理：银行卡去掉是否默认功能


        try
        {
            UserBankRequest userBankRequest = new UserBankRequest();

            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            userBankRequest.setUserId(userId);
            userBankRequest.setAccountName(accountName);
            userBankRequest.setProvinceId(provinceId);
            userBankRequest.setCityId(cityId);
            userBankRequest.setDistrictId(districtId);
            userBankRequest.setBankTypeId(bankTypeId);
            userBankRequest.setBankName(bankName);
            userBankRequest.setBankCard(bankCard);
//            userBankRequest.setIsDefault(isDefault);//20170525产品经理：银行卡去掉是否默认功能

            bankService.addBank(userModel,userBankRequest);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/bank/add_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/update_user_bank_information_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult updateBank()
    {
        Map<String, Object> params = getParamsValidationResults();
        int id = (Integer) params.get("id");
        String accountName = (String) params.get("accountName");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        int bankTypeId = (Integer) params.get("bankTypeId");
        String bankName = (String) params.get("bankName");
        String bankCard = (String) params.get("bankCard");
//        int isDefault = (Integer) params.get("isDefault");//20170525产品经理：银行卡去掉是否默认功能

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserBankDTO bankDto = bankService.loadBankById(id);
            if (null != bankDto)
            {
                bankDto.setAccountName(accountName);
                bankDto.setProvinceId(provinceId);
                bankDto.setCityId(cityId);
                bankDto.setDistrictId(districtId);
                bankDto.setBankTypeId(bankTypeId);
                bankDto.setBankName(bankName);
                bankDto.setBankCard(bankCard);
//                bankDto.setIsDefault(isDefault);//20170525产品经理：银行卡去掉是否默认功能
                bankService.updateBank(userModel,bankDto);
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/bank/update_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    //20170525产品经理：银行卡去掉是否默认功能
//    @UriMapping(value = "/set_default_bank_ajax", interceptors = "userLoginCheckInterceptor")
//    public AjaxResult setDefaultBank()
//    {
//        int userId = getLoginUserId();
//        int id = super.getIntParameter("id", 0);
//        try
//        {
//            Validate.isTrue(id > 0, "id must bigger then 0.");
//
//            UserModel userModel = super.loadUserModel(userId);
//            Validate.isTrue(null != userModel);
//
//            bankService.setDefaultBank(userModel,userId, id);
//            return new AjaxResult();
//        }
//        catch (IllegalArgumentException e)
//        {
//            LOGGER.error("[/member/bank/set_default_bank_ajax].Exception:{}",e);
//            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
//        }
//    }

    @UriMapping(value = "/delete_user_bank_information_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult deleteBank()
    {
        int id = super.getIntParameter("id", 0);
        try
        {
            Validate.isTrue(id > 0, "id must bigger then 0.");

            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            bankService.deleteBank(userModel,id);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/bank/delete_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询会员银行卡信息
     * @return
     */
    @UriMapping(value = "/get_user_bank_information_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult getUserBanksInformation()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;
        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);


            List<UserBankDTO> bankDTOs = bankService.loadAllBank(userId);

            List<BankVo> bankVoList = this.bankService.wrapVOs(bankDTOs);

            Map<String, Object> dataMap = new HashMap<String, Object>();

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            dataMap.put("bankCards", bankVoList);
            ajaxResult.setData(dataMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/bank/get_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/check_user_bank_information_ajax" ,interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult isBankCardExistenceOrNot()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        String bankCard = (String) validationResults.get("bankCard");

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);


            UserBankDTO bankDTOs = bankService.loadBankByUserIdAndBankCard(userId,bankCard);

            if (null != bankDTOs)
            {
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "该银行卡号已存在!");
            }
            return new AjaxResult(AjaxResultCode.OK.getCode(), "可以添加该银行卡号!");
        }
        catch (IllegalArgumentException e)
        {
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

}
