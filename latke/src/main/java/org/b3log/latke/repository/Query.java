/*
 * Copyright (c) 2009-2018, b3log.org & hacpai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.latke.repository;

import java.util.*;
import java.util.Map.Entry;

/**
 * Query.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Jun 5, 2018
 * @see Projection
 * @see Filter
 * @see SortDirection
 */
public final class Query {

    /**
     * Current page number.
     */
    private int currentPageNum = 1;

    /**
     * Page count.
     */
    private Integer pageCount;

    /**
     * Page size.
     */
    private int pageSize = Integer.MAX_VALUE;

    /**
     * Sorts.
     */
    private Map<String, SortDirection> sorts = new LinkedHashMap<>();

    /**
     * Filter.
     */
    private Filter filter;

    /**
     * Projections.
     */
    private Set<Projection> projections = new HashSet<>();

    /**
     * Indices.
     */
    private Set<String[]> indexes = new HashSet<>();

    /**
     * Debug flag. https://github.com/b3log/latke/issues/82
     */
    private boolean debug;

    /**
     * Checks whether is debug.
     *
     * @return {@code true} if it is, returns {@code false} otherwise
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets the debug flag.
     *
     * @param debug the specified debug flag
     * @return the currency query object
     */
    public Query setDebug(final boolean debug) {
        this.debug = debug;

        return this;
    }

    /**
     * Adds a projection with the specified property name and value type.
     *
     * @param propertyName the specified property name
     * @param valueType    the specified value type
     * @return the current query object
     */
    public Query addProjection(final String propertyName, final Class<?> valueType) {
        projections.add(new Projection(propertyName, valueType));

        return this;
    }

    /**
     * Gets the projections.
     *
     * @return projections
     */
    public Set<Projection> getProjections() {
        return Collections.unmodifiableSet(projections);
    }

    /**
     * Indexes the specified properties for future queries.
     *
     * @param properties the specified properties
     * @return the current query object
     */
    public Query index(final String... properties) {
        if (null == properties || 0 == properties.length) {
            return this;
        }

        indexes.add(properties);

        return this;
    }

    /**
     * Gets the indices.
     *
     * @return indices
     */
    public Set<String[]> getIndexes() {
        return Collections.unmodifiableSet(indexes);
    }

    /**
     * Adds sort for the specified property with the specified direction.
     *
     * @param propertyName  the specified property name to sort
     * @param sortDirection the specified sort
     * @return the current query object
     */
    public Query addSort(final String propertyName, final SortDirection sortDirection) {
        sorts.put(propertyName, sortDirection);

        return this;
    }

    /**
     * Sets the filter with the specified filter.
     *
     * @param filter the specified filter
     * @return the current query object
     */
    public Query setFilter(final Filter filter) {
        this.filter = filter;

        return this;
    }

    /**
     * Gets the filter.
     *
     * @return filter
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Gets the current page number.
     *
     * <p>
     * <b>Note</b>: The default value of the current page number is @code -1}.
     * </p>
     *
     * @return current page number
     */
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    /**
     * Sets the current page number with the specified current page number.
     *
     * @param currentPageNum the specified current page number
     * @return the current query object
     */
    public Query setCurrentPageNum(final int currentPageNum) {
        this.currentPageNum = currentPageNum;

        return this;
    }

    /**
     * Sets the page size.
     *
     * <p>
     * <b>Note</b>: The default value of the page size {@code -1}.
     * </p>
     *
     * @return page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size with the specified page size.
     *
     * @param pageSize the specified page size
     * @return the current query object
     */
    public Query setPageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * Gets the sorts.
     *
     * @return sorts
     */
    public Map<String, SortDirection> getSorts() {
        return Collections.unmodifiableMap(sorts);
    }

    /**
     * Gets the page count.
     *
     * @return page count
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * Sets the page count with the specified page count.
     *
     * @param pageCount the specified page count
     * @return the current query object
     */
    public Query setPageCount(final int pageCount) {
        this.pageCount = pageCount;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;

        return currentPageNum == query.currentPageNum &&
                pageSize == query.pageSize &&
                debug == query.debug &&
                Objects.equals(pageCount, query.pageCount) &&
                Objects.equals(sorts, query.sorts) &&
                Objects.equals(filter, query.filter) &&
                Objects.equals(projections, query.projections) &&
                Objects.equals(indexes, query.indexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPageNum, pageCount, pageSize, sorts, filter, projections, indexes, debug);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder("currentPageNum=").append(currentPageNum).append(", pageSize=").append(pageSize).append(", pageCount=").append(pageCount).append(
                ", sorts=[");

        final Set<Entry<String, SortDirection>> entrySet = sorts.entrySet();
        final Iterator<Entry<String, SortDirection>> sortsIterator = entrySet.iterator();

        while (sortsIterator.hasNext()) {
            final Entry<String, SortDirection> sort = sortsIterator.next();

            stringBuilder.append("[key=").append(sort.getKey()).append(", direction=").append(sort.getValue().name()).append("]");

            if (sortsIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");
        if (null != filter) {
            stringBuilder.append(", filter=[").append(filter.toString()).append("]");
        }
        stringBuilder.append(", projections=[");

        final Iterator<Projection> projectionsIterator = projections.iterator();

        while (projectionsIterator.hasNext()) {
            final Projection projection = projectionsIterator.next();

            stringBuilder.append('[').append(projection.toString()).append(']');

            if (projectionsIterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
