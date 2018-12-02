package com.maimaicn.goods.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.dao.FreightTemplateDao;
import com.maimaicn.goods.domain.FreightDetail;
import com.maimaicn.goods.domain.FreightFreeConditions;
import com.maimaicn.goods.domain.FreightTemplate;
import com.maimaicn.utils.RedisLockUtils;
import com.maimaicn.utils.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 运费模板控制类
 */
@RestController
public class FreightTemplateController extends BaseController {

    @Autowired
    private FreightTemplateDao freightTemplateDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @CrossOrigin(allowCredentials = "true",origins = "*")
    @RequestMapping("/freightTemp/save")
    public String save_freight_template(HttpServletRequest request, FreightTemplate ft, String details , String freeConditionsDetail) {
        try{
            Long loginMemberId = this.getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"用户未登录");
            }

            if("".equals(ft.getTemplateName()) || ft.getLocationId() == null || ft.getDeliveryTime() == null){
                return jsonData(request,2,"必要参数不能为空");
            }
            //loginMemberId=2570000000l;
            ft.setMemberId(loginMemberId);
            Integer extpId = ft.getTemplateId();//已经存在模板id 更新
            List<FreightDetail> freightDetails = null;
            List<FreightFreeConditions> freightFreeConditions = null;
            if(FreightTemplate.FRANK_NO.equals(ft.getFrank())){//不包邮的情况需要运费详情数据
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//设置忽略字段
                freightDetails = mapper.readValue(details,new TypeReference<List<FreightDetail>>() {});

                if(freightDetails == null || freightDetails.size() == 0){
                    return jsonData(request,3,"必须设置运费详情信息");
                }
                boolean isdefalut_flag = false;
                //校验运费详情的数据完整度。
                for(FreightDetail fd : freightDetails){
                    if(fd.getFirst() == null || fd.getFirstPrice() == null || fd.getSubsequent() == null || fd.getSubsequentPrice() == null){
                        return jsonData(request,4,"运费详情信息缺失");
                    }
                    if(StringUtils.isEmpty(fd.getLocationIds())){
                        if(isdefalut_flag){
                            return jsonData(request,5,"除默认运费以外都需要设置指定地区");
                        }
                        fd.setIsDefault(FreightDetail.ISDEFAULT_YES);
                        isdefalut_flag = true;
                    }
                }

                //校验指定条件包邮的数据完整度
                isdefalut_flag = false;
                if(FreightTemplate.FREECONDITIONS_YES.equals(ft.getFreeConditions())){
                    freightFreeConditions = mapper.readValue(freeConditionsDetail,new TypeReference<List<FreightFreeConditions>>() {});
                    for(FreightFreeConditions ffc  : freightFreeConditions){
                        if(FreightFreeConditions.PRICETYPEVALUE_PRICETYPE.equals(ffc.getConditionsType()) && ffc.getPriceTypeValue()==null
                                || FreightFreeConditions.PRICETYPEVALUE_AMOUNT.equals(ffc.getConditionsType()) && ffc.getAmount()==null
                                || FreightFreeConditions.PRICETYPEVALUE_MIX.equals(ffc.getConditionsType()) && (ffc.getPriceTypeValue()==null || ffc.getAmount()==null)){
                            return jsonData(request,6,"指定条件包邮信息缺失");
                        }
                        if(StringUtils.isEmpty(ffc.getLocationIds())){
                            if(isdefalut_flag){
                                return jsonData(request,7,"除默认条件外都需要设置指定地区");
                            }
                            ffc.setIsDefault(FreightFreeConditions.ISDEFAULT_YES);
                            isdefalut_flag = true;
                        }else {
                            ffc.setIsDefault(FreightFreeConditions.ISDEFAULT_NO);
                        }
                    }
                }
            }

            //校验该用户下否已经保存了改模板，防止模板的重复插入
            Integer templateId = freightTemplateDao.getTemplateByName(loginMemberId,ft.getTemplateName());
            if(extpId==null&&templateId != null){
                return jsonData(request,8,"该模板名称已存在，请勿重复提交");
            }

            //获取分布式锁，防止并发时同时插入相同的数据
            boolean lock = RedisLockUtils.lock( redisTemplate,"save_freight_template:" + loginMemberId);
            if(lock){
                try{
                    if (extpId == null) {
                        freightTemplateDao.save(ft, freightDetails, freightFreeConditions);
                    } else {
                        freightTemplateDao.update(ft,freightDetails,freightFreeConditions);
                    }

                }finally {
                    RedisLockUtils.unlock(redisTemplate,"save_freight_template:" + loginMemberId);
                }
            }else {
                return jsonData(request,9,"正在在保存，请勿重复提交");
            }

            return jsonData(request,0,"运费模板设置成功");
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 添加商品时获取我的运费模版
     * @param request
     * @return
     */
    @RequestMapping("/freightTemp/myTemp")
    public String getMyTempCombo(HttpServletRequest request) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }

            List<Map<String,Object>> list = freightTemplateDao.getMyTempCombo(loginMemberId);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 运费模板列表
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/freightTemp/list")
    public String myFreightTempList(HttpServletRequest request,Integer pageNo,Integer pageSize) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }
            if(pageNo == null){
                pageNo = 1;
            }
            if(pageSize == null){
                pageSize = 5;
            }

            List<Map<String,Object>> list = freightTemplateDao.myFreightTempList(loginMemberId,pageNo,pageSize);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }
    /**
     * 删除运费模板
     * @param request
     * @param templateId
     * @return
     */
    @RequestMapping("/freightTemp/delete")
    public String delFreightTempById(HttpServletRequest request,Integer templateId) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }
            if(templateId == null){
                return jsonData(request,2,"必要参数不能为空");
            }
            freightTemplateDao.delete(templateId);

            return jsonData(request,0,"删除成功");
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }
    /**
     * 运费模板详情
     * @param request
     * @param templateId
     * @return
     */
    @RequestMapping("/freightTemp/detail")
    public String freightDetail(HttpServletRequest request,Integer templateId) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }
            if(templateId == null){
                return jsonData(request,2,"必要参数不能为空");
            }
            Map<String,Object> freightTemplate= freightTemplateDao.detail(templateId);
            if (freightTemplate == null) {
                return jsonData(request,2,"没有查到该模板");
            }
            return jsonData(request,0,freightTemplate);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }

    }
}
