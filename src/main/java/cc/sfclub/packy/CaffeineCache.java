/*
 * MIT License
 *
 * Copyright (c) 2021 SaltedFish Club
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cc.sfclub.packy;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.apache.ibatis.cache.Cache;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Cache adapter for Caffeine.
 *
 * @author TODAY
 * @date 2020/9/29 10:52
 */
public final class CaffeineCache implements Cache {

  private final String id;
  private final com.github.benmanes.caffeine.cache.Cache<Object, Object> cache;

  /**
   * Instantiates a new caffeine cache.
   *
   * @param id
   *         the id
   */
  public CaffeineCache(String id) {
    Assert.notNull(id, "二级缓存需要一个ID");
    this.id = id;
    this.cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void putObject(Object key, Object value) {
    if (value == null) {
      value = EmptyObject.INSTANCE;
    }
    this.cache.put(key, value);
  }

  @Override
  public Object getObject(final Object key) {
    final Object value = this.cache.getIfPresent(key);
    if (value == EmptyObject.INSTANCE) {
      return null;
    }
    return value;
  }

  @Override
  public Object removeObject(Object key) {
    return this.cache.asMap().remove(key);
  }

  @Override
  public void clear() {
    this.cache.invalidateAll();
  }

  @Override
  public int getSize() {
    return (int) this.cache.estimatedSize();
  }

  @Override
  public ReadWriteLock getReadWriteLock() {
    return null;
  }

}
