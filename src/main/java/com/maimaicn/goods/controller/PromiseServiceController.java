package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.PromiseServiceDao;
import com.maimaicn.goods.domain.PromiseService;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 服务承诺控制类
 */
@RestController
public class PromiseServiceController extends BaseController {

    @Autowired
    private PromiseServiceDao promiseServiceDao;

    @RequestMapping("/promService/indexData")
    public String promService(HttpServletRequest request,String serviceName,Integer page,Integer rows,Integer categoryId) {
        try{
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("serviceName", serviceName);
            params.put("categoryId", categoryId);
            PageVO<Map<String,Object>> pagevo = promiseServiceDao.getIndexData(page,rows,params);

            params.clear();
            params.put("total", pagevo.getRecordCount());
            params.put("rows", pagevo.getRecordList());
            return jsonData(request,0, params);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 添加一个售后服务
     * @param request
     * @return
     */
    @RequestMapping("/promService/add")
    public String add(HttpServletRequest request ,PromiseService ps) {
        try{
            if(ps.getServiceName()==null || "".equals(ps.getServiceName())
                    || ps.getDescription()==null || "".equals(ps.getDescription())
                    || ps.getServiceIcon()==null || "".equals(ps.getServiceIcon()) ){
                return jsonData(request,1,"必要参数不能为空");
            }

            promiseServiceDao.save(ps);
            return jsonData(request,0,"服务添加成功");
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 修改商品售后服务
     * @param request
     * @return
     */
    @RequestMapping("/promService/update")
    public String update(HttpServletRequest request, PromiseService ps) {
        try{
            if(ps.getServiceName()==null || "".equals(ps.getServiceName())
                    || ps.getPromiseId() == null
                    || ps.getDescription()==null || "".equals(ps.getDescription())
                    || ps.getServiceIcon()==null || "".equals(ps.getServiceIcon()) ){
                return jsonData(request,1,"必要参数不能为空");
            }

            promiseServiceDao.update(ps);

            return jsonData(request,0,"服务修改成功");
        } catch(Exception e){
            log.error("服务修改异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping("/promService/delete")
    public String delete(HttpServletRequest request,Integer promiseId) {
        try{
            if(promiseId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            promiseServiceDao.delete(promiseId);

            return jsonData(request,0,"服务删除成功");
        } catch(Exception e){
            log.error("商品售后服务异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 添加服务承诺适用范围
     * @param request
     * @return
     */
    @RequestMapping("/promService/addScope")
    public String addScope(HttpServletRequest request,Integer promiseId,Integer categoryId) {
        try{
            if(promiseId == null || categoryId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            promiseServiceDao.addScope(promiseId,categoryId);

            return jsonData(request,0,"承诺适用范围添加成功");
        } catch(Exception e){
            log.error("承诺适用范围添加异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 列出指定服务适用的所有品类
     * @param request
     * @return
     */
    @RequestMapping("/promService/listScopeCat")
    public String listScopeCat(HttpServletRequest request,Integer promiseId,Integer page,Integer rows) {
        try{
            if(promiseId == null || page == null || rows == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            PageVO<Map<String,Object>> pagevo = promiseServiceDao.listScopeCat(page,rows,promiseId);

            Map<String,Object> data = new HashMap<>();
            data.put("total", pagevo.getRecordCount());
            data.put("rows", pagevo.getRecordList());
            return jsonData(request,0, data);

        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 撤销服务承诺所适用的分类
     * @param request
     * @param promiseId
     * @param categoryId
     * @return
     */
    @RequestMapping("/promService/deleteScopeCat")
    public String deleteScopeCat(HttpServletRequest request,Integer promiseId,Integer categoryId) {
        try{
            if(promiseId == null || categoryId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            promiseServiceDao.deleteScopeCat(promiseId,categoryId);

            return jsonData(request,0,"服务承诺适用分类撤销成功");
        } catch(Exception e){
            log.error("服务承诺适用分类撤销异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 发布商品时需要的商品服务承诺数据
     * 1.适用所有类目的服务
     * 2.自己加入的服务
     * 3、该分类下的共有服务
     * @param request
     * @return
     */
    @RequestMapping("/promService/catService")
    public String publishData(HttpServletRequest request,Integer categoryId) {
        try{
            if(categoryId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,2,"登录超时");
            }

            //需要适用所有类目的服务、自己加入的服务，该分类下的共有服务
            List<Map<String,Object>> list = promiseServiceDao.catService(categoryId,loginMemberId);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }
}
