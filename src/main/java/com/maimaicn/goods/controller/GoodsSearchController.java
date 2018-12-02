package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.CategoryDao;
import com.maimaicn.goods.utils.SendMQUtils;
import com.maimaicn.utils.RedisLockUtils;
import com.maimaicn.utils.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 商品搜索
 */
@RestController
public class GoodsSearchController extends BaseController {

    @Autowired
    private SolrClient solrClient;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 商品搜索入口
     * @param keyword 搜索关键字
     * @param cat 商品分类id
     * @param brand 商品品牌id
     * @param smId 商品所属的店铺会员id
     * @param pvid 商品属性值id
     * @param sort 结果排序类型,s-综合排序降序，rq-人气降序（从高到底），new-新品降序（最新上传降序），p-价格升序（由低到高），pd-价格降序（由高到低）
     * @param min_price 价格区间，开始价格
     * @param max_price 价格区间，最大价格
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    public String goodsSearch(HttpServletRequest request, String keyword, Integer cat, Integer brand,Long smId, @RequestParam(value = "pvid",required = false)Integer pvid [], String sort,
                              Double min_price,Double max_price,Integer pageNo, Integer pageSize) {
        try{
            if(pageNo == null){
                pageNo = 1;
            }
            if(pageSize == null){
                pageSize = 60;
            }

            SolrQuery query_params = new SolrQuery();
            //设置查询参数
            StringBuilder sb = new StringBuilder();
            if(!StringUtils.isEmpty(keyword)){
                sb.append("keyword:" + keyword);
                sb.append(" AND ");
            }
            if(cat != null){
                sb.append("catId:" + cat);
                sb.append(" AND ");
            }
            if(brand != null){
                sb.append("brandId:" + brand);
                sb.append(" AND ");
            }
            if(smId != null){
                sb.append("shopMId:" + smId);
                sb.append(" AND ");
            }
            if(min_price != null && max_price != null){
                if(min_price >= max_price){
                    return jsonData(request,1,"价格区间填写有问题");
                }
                sb.append("l_price:[" + min_price + " TO " + max_price + "]");
                sb.append(" AND ");
            }else if(min_price != null && max_price == null) {
                sb.append("l_price:[" + min_price + " TO *]");
                sb.append(" AND ");
            }else if(min_price == null && max_price != null){
                sb.append("l_price:[* TO " + max_price + "]");
                sb.append(" AND ");
            }

            if(pvid != null && pvid.length != 0){
                for(int i = 0,j = pvid.length ;i < j; i++){
                    sb.append("propValIds:" + pvid[i]);
                    sb.append(" AND ");
                }
            }

            String q = sb.toString();
            if(StringUtils.isEmpty(q)){
                q = "*:*";
            }else {
                q = sb.toString().substring(0,sb.length()-4);
            }
            query_params.set("q",q);


            //设置排序规则
            if(!StringUtils.isEmpty(sort) && !"*:*".equals(q)){//查询所有商品时，不能使用排序
                //s-综合排序降序，rq-人气降序（从高到底），new-新品降序（最新上传降序），p-价格升序（由低到高），pd-价格降序（由高到低）
                switch (sort){
                    case "s": query_params.set("sort","sales desc");break;
                    case "rq": query_params.set("sort","sales desc");break;
                    case "new": query_params.set("sort","createTime desc");break;
                    case "p": query_params.set("sort","l_price asc");break;
                    case "pd": query_params.set("sort","l_price desc");break;
                }
            }

            //设置返回的字段,fl - 指定返回那些字段内容，用逗号或空格分隔多个。
            query_params.set("fl","goodsId,title,catId,l_price,mainImg,shopMId,shopName,sales");

            //设置分页
            query_params.setStart((pageNo-1)*pageSize);
            query_params.setRows(pageSize);

            //高亮设置
            query_params.setHighlight(true);
            query_params.addHighlightField("title");//设置高亮的字段
            query_params.setHighlightSimplePre("<span style='color:red;'>");//高亮前缀
            query_params.setHighlightSimplePost("</span");//高亮后缀

            //开始查询
            QueryResponse response = solrClient.query(query_params);
            SolrDocumentList results = response.getResults();
            long total = results.getNumFound();//结果匹配的总数

            Map<String,Object> return_data = new HashMap<>();
            return_data.put("total",total);
            return_data.put("goodsList",results);

            //找出分类导航菜单信息，就是商品搜索页上的筛选条件类表
            //规则，cat参数不空，则直接使用，若为空，则遍历搜索的商品，选取占比最大的分类id
            if(cat == null){
                Map<Integer,Integer> catSelect = new HashMap<>();
                for(Map<String,Object> map : results){
                    cat = (Integer) map.get("catId");
                    Integer count = catSelect.get(cat);
                    if(count == null){
                        catSelect.put(cat,1);
                    }else {
                        catSelect.put(cat,++count);
                    }
                    map.remove("catId");//页面显示不需要这个结果
                }

                //选出值最大的catId
                int max_value = 0;
                for(Map.Entry<Integer,Integer> one_cat : catSelect.entrySet()){
                    if(one_cat.getValue() > max_value){
                        cat = one_cat.getKey();
                        max_value = one_cat.getValue();
                    }
                }
            }

            //现在已有分类id catId，根据分类id获取，该分类下的品牌、分类下的子分类（叶子分类就获取兄弟分类）、该分类下的可搜索参数名，参数所有值
            Object catNavInfo = redisTemplate.opsForValue().get("search:catNavInfo:" + cat);
            if(catNavInfo == null) {//没有缓存，就查db层。
                try {
                    boolean lock = RedisLockUtils.lock(redisTemplate, "search:catNavInfo:lock:" + cat);
                    if (!lock) {//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
                        int count = 1;
                        while (catNavInfo == null && count < 60) {//最多自旋100次，避免死循环
                            Thread.sleep(200);
                            catNavInfo = redisTemplate.opsForValue().get("search:catNavInfo:" + cat);
                            count++;
                        }
                        if (catNavInfo == null) {
                            log.error("获取搜索页分类导航信息错误，自旋60次未获取数据，categoryId=" + cat);
                        }
                    }else {
                        //成功获取锁，加载db数据
                        catNavInfo =  categoryDao.getSearchNavigate(cat);
                        redisTemplate.opsForValue().set("search:catNavInfo:" + cat, catNavInfo, 1, TimeUnit.DAYS);
                    }
                } finally {
                    RedisLockUtils.unlock(redisTemplate, "search:catNavInfo:lock:" + cat);
                }
            }


            return_data.put("navData",catNavInfo);
            return_data.put("pageNo",pageNo);
            return_data.put("pageSize",pageSize);
            return jsonData(request,0,return_data);
        } catch(Exception e){
            log.error("商品搜索异常",e);
            return jsonData(request,110,"服务忙");
        }
    }






    @RequestMapping("/solr/add")
    public String aabb(HttpServletRequest request,Integer goodsId) {
        try{

            SendMQUtils.sendPublishSolrGoodsJms(goodsId);
            return jsonData(request,0,"ok");
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    @RequestMapping("/solr/delete")
    public String de(HttpServletRequest request,Integer goodsId) {
        try{

            SendMQUtils.sendUnloadSolrGoodsJms(goodsId);
            return jsonData(request,0,"ok");
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


}
