package com.maimaicn.goods.service;

import com.maimaicn.goods.dao.B2BWhiteListDao;
import com.maimaicn.utils.RedisLockUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * b2b白名单service
 */
@Service
public class B2BWhiteListService {
    private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private B2BWhiteListDao b2BWhiteListDao;

    /**
     * 看买家是否是在卖家的白名单中
     * @param sellerMemberId
     * @param buyerMemberId
     * @return
     * @throws Exception
     */
    public boolean isB2BBuyer(Long sellerMemberId,Long buyerMemberId) throws Exception {
        Object b2BBuyer = redisTemplate.opsForValue().get("isB2BBuyer:" + sellerMemberId + ":" + buyerMemberId);

        //如果有缓存，就直接返回
        if(b2BBuyer != null){
            return (boolean) b2BBuyer;
        }
        //没有缓存，就查db层。
        boolean lock = RedisLockUtils.lock(redisTemplate, "isB2BBuyer:lock" + sellerMemberId + ":" + buyerMemberId);
        if(!lock){//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
            int count = 1;
            while (b2BBuyer == null && count < 60){//最多自旋100次，避免死循环
                Thread.sleep(200);
                b2BBuyer = redisTemplate.opsForValue().get("isB2BBuyer:" + sellerMemberId + ":" + buyerMemberId);
                count ++;
            }
            if(b2BBuyer == null){
                log.error("获取b2b白名单，自旋60次未获取数据，"+ sellerMemberId + ":" + buyerMemberId);
                return false;
            }
            return (boolean) b2BBuyer;
        }else {
            try {
                //成功获取锁，加载db数据
                b2BBuyer = b2BWhiteListDao.isB2BBuyer(sellerMemberId, buyerMemberId);
                if(b2BBuyer == null){//空值只缓存5分钟
                    redisTemplate.opsForValue().set("isB2BBuyer:" + sellerMemberId + ":" + buyerMemberId,b2BBuyer,5, TimeUnit.MINUTES);
                    return false;
                }else{
                    redisTemplate.opsForValue().set("isB2BBuyer:" + sellerMemberId + ":" + buyerMemberId,b2BBuyer,2, TimeUnit.HOURS);
                    return (boolean) b2BBuyer;
                }
            }finally {
                RedisLockUtils.unlock(redisTemplate,"isB2BBuyer:lock" + sellerMemberId + ":" + buyerMemberId);
            }
        }
    }


}
