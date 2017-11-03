package com.fruit.portal.action.wechat.account;

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
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@UriMapping("/wechat/bank")
public class UserBankWeChatAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBankWeChatAction.class);

    @Autowired
    private BankService bankService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BaseBankService baseBankService;

    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult bankList()
    {
        Map dataMap = new HashMap<String,Object>();
        int userId = getLoginUserId();

        try
        {
            List<UserBankDTO> bankDTOs = bankService.loadAllBank(userId);

            List<BankVo> bankCards = this.bankService.wrapVOs(bankDTOs);
            List<BankInfoDTO> bankList = this.baseBankService.loadAll();

            dataMap.put("bankCards", bankCards);//用户银行卡列表信息

            dataMap.put("bankList", bankList);//银行列表信息

            return new AjaxResult(dataMap);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/bank/list].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"数据库异常!");
        }
    }

    @UriMapping(value = "/add_user_bank_information_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addBank()
    {
        int userId = getLoginUserId();
       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String accountName = (String) requesMap.get("accountName");
        int provinceId = Integer.parseInt(String.valueOf( requesMap.get("provinceId")));
        int cityId = Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId = Integer.parseInt(String.valueOf( requesMap.get("districtId")));
        int bankTypeId = Integer.parseInt(String.valueOf( requesMap.get("bankTypeId")));
        String bankName = (String) requesMap.get("bankName");
        String bankCard = (String) requesMap.get("bankCard");


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
            LOGGER.error("[/wechat/bank/add_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/update_user_bank_information_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult updateBank()
    {

       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        int id = Integer.parseInt(String.valueOf( requesMap.get("id")));

        String accountName =  (String) requesMap.get("accountName");
        int provinceId =   Integer.parseInt(String.valueOf( requesMap.get("provinceId")));
        int cityId =  Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId =  Integer.parseInt(String.valueOf(requesMap.get("districtId")));
        int bankTypeId =  Integer.parseInt(String.valueOf( requesMap.get("bankTypeId")));
        String bankName = (String) requesMap.get("bankName");
        String bankCard = (String) requesMap.get("bankCard");

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
                bankService.updateBank(userModel,bankDto);
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/bank/update_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    @UriMapping(value = "/delete_user_bank_information_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult deleteBank()
    {
        //Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        int id =  Integer.parseInt(String.valueOf( requesMap.get("id")));
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
            LOGGER.error("[/wechat/bank/delete_user_bank_information_ajax].Exception:{}",e);
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
            LOGGER.error("[/wechat/bank/get_user_bank_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/check_user_bank_information_ajax" ,interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult isBankCardExistenceOrNot()
    {
        //Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String bankCard = (String) requesMap.get("bankCard");

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
