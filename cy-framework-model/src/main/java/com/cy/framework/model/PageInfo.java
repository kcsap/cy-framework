package com.cy.framework.model;

public class PageInfo {
	public static final Integer DEFAULT_PAGE_SIZE = 10;

	private int pageSize;

	private int currentPage;

	private int totalItem;

	private int startRow;

	private String sort;

	private String order;

	public int getPageSize() {
		if (pageSize <= 0) {
			// 默认10
			return 10;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		setStartEndRow();
	}

	public long getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	private void setStartEndRow() {
		this.startRow = this.getPageSize() * (this.getCurrentPage() - 1);
		if (this.startRow < 0) {
			this.startRow = 0;
		}
	}

	public int getStartRow() {
		if (this.startRow < 0) {
			return 0;
		}

		return startRow;
	}

	public void setStartRow(int startRow) {
		if (this.startRow < 0) {
			startRow = 0;
		}

		this.startRow = startRow;
	}

	public long getTotalPage() {
		long pgSize = pageSize;
		long total = totalItem;
		if (pgSize == 0) {
			return 0;
		}
		long result = total / pgSize;
		if ((total == 0) || ((total % pgSize) != 0)) {
			result++;
		}
		return result;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
