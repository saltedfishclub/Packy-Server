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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Setter;

/**
 * @author TODAY 2020/9/10 11:05
 */
@Setter
public class Pagination<T> implements ListableResult<T>, IPage<T> {

  public Pagination(Pageable pageable) {
    this(pageable.getCurrent(), pageable.getSize());
  }

  /*
   * 分页构造函数
   *
   * @param current
   *            当前页
   * @param size
   *            每页显示条数
   */
  public Pagination(long current, long size) {
    this(current, size, 0);
  }

  public Pagination(long current, long size, long total) {
    this(current, size, total, true);
  }

  public Pagination(long current, long size, boolean isSearchCount) {
    this(current, size, 0, isSearchCount);
  }

  public Pagination(long current, long size, long total, boolean isSearchCount) {
    if (current > 1) {
      this.current = current;
    }
    this.size = size;
    this.total = total;
    this.isSearchCount = isSearchCount;
  }

  public static <T> Pagination<T> pageable(Pageable pageable) {
    final Pagination<T> ok = ok();
    ok.size = pageable.getSize();
    ok.current = pageable.getCurrent();
    return ok;
  }

  public static <T> Pagination<T> page(int current, int size) {
    return new Pagination<>(current, size);
  }

  public static <T> Pagination<T> ok() {
    return new Pagination<>();
  }

  public static <T> Pagination<T> ok(List<T> data) {
    return new Pagination<T>().setRecords(data);
  }

  public static <T> Pagination<T> ok(List<T> data, long total, Pageable pageable) {
    final Pagination<T> ret = pageable(pageable);
    ret.setTotal(total);
    ret.setRecords(data);
    return ret;
  }

  @Override
  public List<T> getData() {
    return getRecords();
  }

  // ------------------

  /**
   * 查询数据列表
   */
  protected List<T> records = Collections.emptyList();

  /**
   * 总数
   */
  protected long total = 0;
  /**
   * 每页显示条数，默认 10
   */
  private long size = DEFAULT_LIST_SIZE;
  /**
   * 当前页
   */
  protected long current = 1;

  /**
   * 排序字段信息
   */
  protected List<OrderItem> orders = new ArrayList<>();

  /**
   * 自动优化 COUNT SQL
   */
  protected boolean optimizeCountSql = true;
  /**
   * 是否进行 count 查询
   */
  @JsonIgnore
  private boolean isSearchCount = true;
  /**
   * 是否命中count缓存
   */
  @JsonIgnore
  protected boolean hitCount = false;

  public static <T> Pagination<T> ascByCreateTime(Pageable pageable) {
    return new Pagination<T>(pageable).addOrder(OrderItem.asc("create_time"));
  }

  public static <T> Pagination<T> descByCreateTime(Pageable pageable) {
    return new Pagination<T>(pageable).addOrder(OrderItem.desc("create_time"));
  }

  public Pagination() {
  }

  /**
   * 是否存在上一页
   *
   * @return true / false
   */
  public boolean hasPrevious() {
    return this.current > 1;
  }

  /**
   * 是否存在下一页
   *
   * @return true / false
   */
  public boolean hasNext() {
    return this.current < this.getPages();
  }

  @Override
  @JsonIgnore
  public List<T> getRecords() {
    return this.records;
  }

  @Override
  public Pagination<T> setRecords(List<T> records) {
    this.records = records;
    return this;
  }

  @Override
  public long getTotal() {
    return this.total;
  }

  @Override
  public Pagination<T> setTotal(long total) {
    this.total = total;
    return this;
  }

  @Override
  public long getSize() {
    return this.size;
  }

  @Override
  public Pagination<T> setSize(long size) {
    this.size = size;
    return this;
  }

  @Override
  public long getCurrent() {
    return this.current;
  }

  @Override
  public Pagination<T> setCurrent(long current) {
    this.current = current;
    return this;
  }

  /**
   * 添加新的排序条件，构造条件可以使用工厂：
   *
   * @param items
   *         条件
   *
   * @return 返回分页参数本身
   */
  public Pagination<T> addOrder(OrderItem... items) {
    Collections.addAll(orders, items);
    return this;
  }

  /**
   * 添加新的排序条件，构造条件可以使用工厂：
   *
   * @param items
   *         条件
   *
   * @return 返回分页参数本身
   */
  public Pagination<T> addOrder(List<OrderItem> items) {
    orders.addAll(items);
    return this;
  }

  @Override
  @JsonIgnore
  public List<OrderItem> orders() {
    return orders;
  }

  public void setOrders(List<OrderItem> orders) {
    this.orders = orders;
  }

  @Override
  @JsonIgnore
  public boolean optimizeCountSql() {
    return optimizeCountSql;
  }

  @Override
  @JsonIgnore
  public boolean isSearchCount() {
    if (total < 0) {
      return false;
    }
    return isSearchCount;
  }

  public Pagination<T> setSearchCount(boolean isSearchCount) {
    this.isSearchCount = isSearchCount;
    return this;
  }

  public Pagination<T> setOptimizeCountSql(boolean optimizeCountSql) {
    this.optimizeCountSql = optimizeCountSql;
    return this;
  }

  @Override
  public void hitCount(boolean hit) {
    this.hitCount = hit;
  }

  public void setHitCount(boolean hit) {
    this.hitCount = hit;
  }

  @Override
  @JsonIgnore
  public boolean isHitCount() {
    return hitCount;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj instanceof Pagination) {
      final Pagination<?> other = (Pagination<?>) obj;

      return size == other.size
              && total == other.total
              && current == other.current
              && isSearchCount == other.isSearchCount
              && Objects.equals(records, other.records)
              && optimizeCountSql == other.optimizeCountSql;
    }
    return false;
  }
}
