package com.hectorlopezfernandez.dto;

/**
 * Almacena la información necesaria para mostrar la barra de paginación
 */
public final class PaginationInfo {

	private int currentPage;
	private int firstPage = 1;
	private int lastPage;
	private int previousPage;
	private int nextPage;
	private int itemsPerPage;
	private int totalItemCount;
	private int firstItem;
	private boolean paginationNeeded;


	// constructores

	/**
	 * Crea un objeto con la paginacion deshabilitada
	 */
	public PaginationInfo() {
		this.currentPage = 1;
		this.lastPage = 1;
		this.previousPage = 1;
		this.nextPage = 1;
		this.itemsPerPage = 0;
		this.totalItemCount = 0;
		this.firstItem = 0;
		this.paginationNeeded = false;
	}

	/**
	 * Crea un objeto calculando la paginacion a partir de la pagina actual
	 */
	public PaginationInfo(int page, int numItems, int total) {
		this.itemsPerPage = numItems;
		this.totalItemCount = total;
		this.lastPage = 1 + (totalItemCount / itemsPerPage);
		this.currentPage = page;
		this.currentPage = currentPage < firstPage ? firstPage : currentPage;
		this.currentPage = currentPage > lastPage ? lastPage : currentPage;
		this.previousPage = currentPage <= firstPage ? firstPage : currentPage - 1;
		this.nextPage = currentPage >= lastPage ? lastPage : currentPage + 1;
		this.firstItem = this.itemsPerPage * (this.currentPage - 1);
		this.paginationNeeded = true;
	}


	// getters
	
	public int getCurrentPage() {
		return currentPage;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public int getTotalItemCount() {
		return totalItemCount;
	}

	public boolean isPaginationNeeded() {
		return paginationNeeded;
	}

	public int getFirstItem() {
		return firstItem;
	}

}