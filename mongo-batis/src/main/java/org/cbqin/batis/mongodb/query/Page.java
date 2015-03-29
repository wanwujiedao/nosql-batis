package org.cbqin.batis.mongodb.query;

/**
 * @author qinchuanbao
 * @email cbqin@gmail.com
 * @date 2015/3/25
 * @version 0.1.0
 */

/**
 * 页
 */
public class Page {

    /**
     * 每页条数
     */
    private Integer limit;


    /**
     * 跨过的条数
     * <p>不建议在大集合上使用skip, skip过大时游标的移动时间将较长</p>
     */
    private Integer skip;

    public Page limit(final Integer limit) {
        this.limit = limit;
        return this;
    }

    public Page skip(final Integer skip) {
        this.skip = skip;
        return this;
    }

    public Page() {
    }

    public Page(Integer skip, Integer limit) {
        this.limit = limit;
        this.skip = skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    @Override
    public String toString() {
        return "Page{" +
                "limit=" + limit +
                ", skip=" + skip +
                '}';
    }
}
