package com.example.springboot;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

/**
 * Redis工具类，封装{@link RedisTemplate}中的一些常用操作。使用方式：作为Spring bean注入即可。
 * 需要注入redisTemplate。
 * @author chenyangguang
 * @date 2017年1月3日 下午4:10:45
 * @version  1.0.0
 * @see RedisTemplate
 *
 */
@Component
public class RedisHelper {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/* --------common---------- */
	
	/**
	 * 设置key的超时时间
	 * @param key key
	 * @param timeout 超时时间，单位：秒
	 */
	public void expire(String key, long timeout){
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 删除key
	 * @param key
	 */
	public void delete(String key){
		redisTemplate.delete(key);
	}
	
	/**
	 * 直接执行redis操作,可执行复杂操作
	 * @param action RedisCallback
	 * @return
	 */
	public <T> T execute(RedisCallback<T> action){
		return redisTemplate.execute(action);
	}
	
	/**
	 * 设置分布式锁，获取到返回true,否则返回false。
	 * <br>为避免死锁，默认超时时间五分钟。
	 * <br>锁用完后需用调用delete()方法释放锁
	 * @param key
	 * @return 获取锁的状态
	 */
	public boolean setnx(final String key){
		return setnx(key, "true");
	}
	
	/**
	 * 设置分布式锁，获取到返回true,否则返回false。
	 * <br>为避免死锁，默认超时时间五分钟。
	 * <br>锁用完后需用调用delete()方法释放锁
	 * @param key
	 * @param value
	 * @return 获取锁的状态
	 */
	public boolean setnx(final String key ,final String value){
		return setnx(key, value, 5 * 60);
	}
	
	/**
	 * 设置分布式锁，获取到返回true,否则返回false。
	 * <br>锁用完后需用调用delete()方法释放锁
	 * @param key
	 * @param value
	 * @param timeout 超时时间，单位：秒
	 * @return
	 */
	public boolean setnx(final String key ,final String value,long timeout){
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(key.getBytes(), value.getBytes());
			}
		});
		expire(key, timeout);
		return result;
	}
	
	
	/* --------string---------- */
	
	/**
	 * 设置缓存
	 * @param key
	 * @param value
	 */
	public void set(String key ,String value){
		redisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * 设置缓存
	 * @param key 
	 * @param value value
	 * @param timeout 超时时间，单位：秒 
	 */
	public void set(String key, String value, long timeout){
		redisTemplate.opsForValue().set(key, value, timeout,TimeUnit.SECONDS);
	}
	
	
	/**
	 * 获取ValueOperations key
	 * @param key
	 * @return
	 */
	public String get(String key){
		return redisTemplate.opsForValue().get(key);
	}
	
	
	/* --------list---------- */
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public int size(String key){
		return redisTemplate.opsForList().size(key).intValue();
	}
	
	/**
	 * list left push，在list头部插入value值
	 * @param key
	 * @param value
	 */
	public Long lpush(String key, String value){
		return redisTemplate.opsForList().leftPush(key, value);
	}
	
	/**
	 * list right push，在list尾部插入value值
	 * @param key
	 * @param value
	 */
	public Long rpush(String key,String value){
		return redisTemplate.opsForList().rightPush(key, value);
	}
	
	/**
	 * 从key 对应list中删除i个和value相同的元素
	 * @param key
	 * @param i
	 * @param value
	 */
	public Long lrem(String key,long i, String value){
		return redisTemplate.opsForList().remove(key, i, value);
	}
	
	/**
	 * 查找缓存list值的一个范围
	 * @param key 
	 * @param start 开始下标
	 * @param end 结束下标
	 * @return
	 */
	public List<String> range(String key ,int start, int end){
		return redisTemplate.opsForList().range(key, start, end);
	}
	
	/**
	 * 从list头部获取元素，并删除
	 * @param key
	 * @return
	 */
	public String lpop(String key){
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * list阻塞队列，如果在timeout时间内取不到数据则一直等待
	 * @param key 
	 * @param timeout 超时时间
	 * @param unit 单位
	 * @return
	 */
	public String lpop(String key, long timeout, TimeUnit unit){
		return redisTemplate.opsForList().leftPop(key, timeout, unit);
	}
	
	/**
	 * 从list底部获取元素，并删除
	 * @param key
	 * @return
	 */
	public String rpop(String key){
		return redisTemplate.opsForList().rightPop(key);
	}
	
	/**
	 * 从list底部获取元素，如果没有，则等待（阻塞）timeout时间
	 * @param key
	 * @param timeout 等待时间
	 * @param unit 时间单位
	 * @return
	 */
	public String rpop(String key, long timeout,TimeUnit unit){
		return redisTemplate.opsForList().rightPop(key, timeout, unit);
	}
	
	/* ---------set--------- */
	
	/**
	 * set集合中添加值
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sadd(String key,String ...values){
		return redisTemplate.opsForSet().add(key, values);
	}
	
	/**
	 * 获取set集合中的值
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key){
		return redisTemplate.opsForSet().members(key);
	}
	
	/**
	 * 判断集合中是否存在value值
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean sisMembers(String key,String value){
		return redisTemplate.opsForSet().isMember(key, value);
	}
	
	/**
	 * 获取set集合的size
	 * @param key
	 * @return
	 */
	public Long ssize(String key){
		return redisTemplate.opsForSet().size(key);
	}
	
	/**
	 * set集合中删除值
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sdel(String key, Object... values){
		return redisTemplate.opsForSet().remove(key, values);
	}
	
	
	/* --------zset---------- */
	
	/**
	 * 在集合中添加值，如果集合不存在则新建。zset为有序集合，按照score排序
	 * @param key
	 * @param value
	 * @param score
	 */
	public void zset(String key, String value, double score){
		redisTemplate.opsForZSet().add(key, value, score);
	}
	
	/**
	 * 在集合中批量添加值，如果集合不存在则新建。
	 * @param key
	 * @param tuples {@link DefaultTypedTuple}
	 */
	public void zset(String key,Set<ZSetOperations.TypedTuple<String>> tuples){
		if (tuples == null || tuples.isEmpty()) {
			return;
		}
		redisTemplate.opsForZSet().add(key, tuples);
	}
	
	/**
	 * 删除集合中的值
	 * @param key
	 * @param values
	 * @return
	 */
	public long zrem(String key ,Object... values){
		return redisTemplate.opsForZSet().remove(key, values);
	}
	
	/**
	 * 查询zset成员的集合个数
	 * @param key
	 * @return
	 */
	public long zcard(String key){
		return redisTemplate.opsForZSet().zCard(key);
	}
	
	/**
	 * 按照score的值从小到大的顺序获取key的value集合
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrange(String key, int start, int end){
		return redisTemplate.opsForZSet().range(key, start, end);
	}
	
	
	/**
	 * 按照score的值从大到小的顺序获取key的value集合
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zreverseRange(String key,int start,int end){
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}
	
	/**
	 * 按照score的值从小到大的顺序获取key的集合
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<TypedTuple<String>> zrangeWithScores(String key,int start,int end){
		return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}
	
	/**
	 * 按照score的值从大到小的顺序获取key的value集合
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<TypedTuple<String>> zreverseRangeWithScores(String key,int start,int end){
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
	}
	
	/**
	 * 获取集合中key值为value的score值
	 * @param key
	 * @param value
	 * @return
	 */
	public Double zscore(String key,String value){
		return redisTemplate.opsForZSet().score(key, value);
	}
	
	/** hash */
	public void hput(String key,String field,Object value){
		redisTemplate.opsForHash().put(key, field, value);
	}
	
	public void hputAll(String key,Map<? extends Object, ? extends Object> map){
		redisTemplate.opsForHash().putAll(key, map);
	}
	
	public Object hget(String key,String field){
		return redisTemplate.opsForHash().get(key, field);
	}
	
	/**
	 * 返回fields
	 * @param key
	 * @return
	 */
	public Set<Object> hfields(String key){
		return redisTemplate.opsForHash().keys(key);
	}
	
	public long hsize(String key){
		return redisTemplate.opsForHash().size(key);
	}
	
	public Map<Object, Object> hentries(String key){
		return redisTemplate.opsForHash().entries(key);
	}
	
	public Long hdel(String key,Object ...fields){
		return redisTemplate.opsForHash().delete(key, fields);
	}
	
	/**
	 * 遍历hash
	 * @param key 
	 * @param options 查询条件，正则匹配
	 * @return
	 */
	public Cursor<Map.Entry<Object, Object>>  scan(String key,ScanOptions options){
		return redisTemplate.opsForHash().scan(key, options);
	}
	
	
	
	/**
	 * 发布订阅消息
	 * @param channel 
	 * @param msg
	 */
	public void convertAndSend(String channel,Serializable msg){
		redisTemplate.convertAndSend(channel, msg);
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}
	
	

}
