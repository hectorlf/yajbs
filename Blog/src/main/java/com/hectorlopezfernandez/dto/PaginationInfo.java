package com.hectorlopezfernandez.dto;

/**
 * Almacena la informacion necesaria para mostrar la barra de paginacion
 */
public final class PaginationInfo {

	public static final PaginationInfo DISABLED = new PaginationInfo();

	private int currentPage;
	private int firstPage = 1;
	private int lastPage;
	private int previousPage;
	private int nextPage;
	private int itemsPerPage;
	private int totalItemCount;
	private int firstItem;
	private boolean enabled;


	// constructores

	/**
	 * Crea un objeto con la paginacion deshabilitada
	 */
	private PaginationInfo() {
		this.currentPage = 1;
		this.lastPage = 1;
		this.previousPage = 1;
		this.nextPage = 1;
		this.itemsPerPage = 0;
		this.totalItemCount = 0;
		this.firstItem = 0;
		this.enabled = false;
	}

	/**
	 * Crea un objeto calculando la paginacion a partir de la pagina actual
	 */
	public PaginationInfo(int page, int numItems, int total) {
		this.totalItemCount = total < 1 ? 1 : total;
		this.itemsPerPage = numItems > total ? total : numItems;
		// itemsPerPage nunca puede ser 0
		if (itemsPerPage < 1) this.itemsPerPage = 1;
		this.lastPage = (totalItemCount / itemsPerPage) + (totalItemCount % itemsPerPage > 0 ? 1 : 0);
		this.currentPage = page;
		this.currentPage = currentPage < firstPage ? firstPage : currentPage;
		this.currentPage = currentPage > lastPage ? lastPage : currentPage;
		this.previousPage = currentPage <= firstPage ? firstPage : currentPage - 1;
		this.nextPage = currentPage >= lastPage ? lastPage : currentPage + 1;
		this.firstItem = itemsPerPage * (currentPage - 1);
		this.enabled = true;
	}

	// getters sinteticos
	
	public boolean isOnFirstPage() {
		return firstPage >= currentPage;
	}
	public boolean isOnLastPage() {
		return lastPage <= currentPage;
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

	public int getFirstItem() {
		return firstItem;
	}

	public boolean isEnabled() {
		return enabled;
	}

}