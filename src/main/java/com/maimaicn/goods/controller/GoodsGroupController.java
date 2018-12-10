package com.maimaicn.goods.controller;

import com.maimaicn.utils.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description 商品分组商家管理
 * @Author 周振
 * @Time 2018/12/9 20:03
 */
@RestController
public class GoodsGroupController extends BaseController {

/*
    @Autowired
    private GoodsGroupDao goodsGroupDao;

    */
/**
     * 后台获取全部商品列表
     *
     * @param request
     * @param page
     * @param rows
     * @return
     *//*

    @RequestMapping("seller/goodsGroupList")
    public String goodsGroupList(HttpServletRequest request, Integer page, Integer rows, Integer groupId, String creatTime, String groupName) {
        try {
            if (page == null) page = 1;
            if (rows == null) rows = 10;
            page = page < 1 ? 1 : page;
            Map<String, Object> params = new HashMap<>();
            if (creatTime != null) {  //条件查询
                params.put("createTime", creatTime);
            }
            if (groupName != null) {
                params.put("groupName", groupName);
            }

            Long loginMemberId = this.getLoginMemberId(request);
            if (loginMemberId == null) {
                return jsonData(request, 1, "用户未登录");
            }
            params.put("memberId", loginMemberId);


            if (groupId != null && groupId != 0) {
                params.put("groupId", groupId);
                List<Map<String, Object>> list = goodsGroupDao.goodsGroupListByPid(params);
                return jsonData(request, 0, list);
            } else {//查询一级分类
                params.put("groupId", 0);
                PageVO<Map<String, Object>> pagevo = goodsGroupDao.goodsGroupList(page, rows, params);
                params.clear();
                params.put("total", pagevo.getRecordCount());
                params.put("rows", pagevo.getRecordList());
                return jsonData(request, 0, params);
            }
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request, 110, "服务忙");
        }

    }

    */
/**
     * 添加商品分组
     *
     * @param request
     * @param
     * @param
     * @return
     *//*

    @RequestMapping("seller/addGoodsGroup")
    public String addGoodsGroup(HttpServletRequest request, String groupName, Integer groupId) {
        try {
            Map<String, Object> params = new HashMap<>();

            Long loginMemberId = this.getLoginMemberId(request);
            if (loginMemberId == null) {
                return jsonData(request, 1, "用户未登录");
            }
            params.put("memberId", loginMemberId);

            if (groupName != null && !groupName.trim().equals("")) {
                params.put("groupName", groupName);
                Integer goodsGroupId = goodsGroupDao.getGoodsGroupByName(loginMemberId, groupName);
                if (goodsGroupId != null) {
                    return jsonData(request, 3, "该商品分组已存在，请勿重复提交");
                }
            } else {
                return jsonData(request, 2, "必要参数不能为空");
            }

            if (groupId != null && groupId != 0) {
                params.put("parentId", groupId);
                //判断  必须只有二级
                boolean threeOrNot = goodsGroupDao.isThreeOrNot(loginMemberId, groupId);
                if (!threeOrNot) {
                    return jsonData(request, 2, "不允许在此选项添加子分类");
                }
            } else {//添加到一级分类
                params.put("parentId", 0);
            }
            goodsGroupDao.addGoodsGroup(params);

            return jsonData(request, 0, "添加成功");
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request, 110, "服务忙");
        }

    }


    */
/**
     * 添加商品分组
     *
     * @param request
     * @param
     * @param
     * @return
     *//*

    @RequestMapping("seller/editGoodsGroup")
    public String editGoodsGroup(HttpServletRequest request, String groupName, Integer groupId) {
        try {
            Map<String, Object> params = new HashMap<>();

            Long loginMemberId = this.getLoginMemberId(request);
            if (loginMemberId == null) {
                return jsonData(request, 1, "用户未登录");
            }
            params.put("memberId", loginMemberId);

            if (groupName != null && !groupName.trim().equals("")) {
                params.put("groupName", groupName);
                Integer goodsGroupId = goodsGroupDao.getGoodsGroupByName(loginMemberId, groupName);
                if (goodsGroupId != null) {
                    return jsonData(request, 3, "该商品分组已存在，请勿重复提交");
                }
            } else {
                return jsonData(request, 2, "必要参数不能为空");
            }

            if (groupId != null ) {
                params.put("groupId", groupId);
                Integer goodsGroupPid = goodsGroupDao.getGoodsGroupById(loginMemberId, groupId);
                if (goodsGroupPid==null) {
                    return jsonData(request, 2, "没有找到该数据");
                }
            }else {
                return jsonData(request, 2, "必要参数不能为空");
            }
            goodsGroupDao.editGoodsGroup(params);
            return jsonData(request, 0, "修改成功");
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request, 110, "服务忙");
        }
    }

    */
/**
     * 删除商品分组
     *
     * @param request
     * @param
     * @param
     * @return
     *//*

    @RequestMapping("seller/delGoodsGroup")
    public String delGoodsGroup(HttpServletRequest request, Integer groupId) {
        try {
            Map<String, Object> params = new HashMap<>();

            Long loginMemberId = this.getLoginMemberId(request);
            if (loginMemberId == null) {
                return jsonData(request, 1, "用户未登录");
            }
            params.put("memberId", loginMemberId);
            if (groupId != null ) {
                params.put("groupId", groupId);
                Integer goodsGroupPid = goodsGroupDao.getGoodsGroupById(loginMemberId, groupId);
                if (goodsGroupPid==null) {
                    return jsonData(request, 2, "没有找到该数据");
                }
            }else {
                return jsonData(request, 2, "必要参数不能为空");
            }
            goodsGroupDao.deleteGoodsGroup(params);
            return jsonData(request, 0, "删除成功");
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request, 110, "服务忙");
        }
    }
*/

}
