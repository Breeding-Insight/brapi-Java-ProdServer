package org.brapi.test.BrAPITestServer.service;

import java.util.ArrayList;
import java.util.List;

import org.brapi.test.BrAPITestServer.exceptions.BrAPIServerException;
import org.brapi.test.BrAPITestServer.exceptions.InvalidPagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.model.IndexPagination;
import io.swagger.model.Metadata;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PagingUtility {
	@Value("${paging.page-size.default:1000}")
	private int defaultPageSize;
	@Value("${paging.page-size.max-allowed:65000}")
	private int maxAllowablePageSize;

	private static int defaultPageSizeStatic;
	private static int maxAllowablePageSizeStatic;

	@PostConstruct
	private void init() {
		// Java made me do this - JL
		defaultPageSizeStatic = defaultPageSize;
		maxAllowablePageSizeStatic = maxAllowablePageSize;
	}

	public static void calculateMetaData(Metadata metaData) {
		int totalCount = metaData.getPagination().getTotalCount();
		int pageSize = metaData.getPagination().getPageSize();
		metaData.getPagination().setTotalPages((totalCount / pageSize) + Integer.signum( totalCount % pageSize));
	}

	public static void validatePaging(Integer page, Integer pageSize) throws BrAPIServerException {
		boolean pageValid = (page == null) || (page >= 0);
		if (!pageValid)
			throw new InvalidPagingException("page");
		boolean pageSizeValid = (pageSize == null) || (pageSize >= 1);
		if (!pageSizeValid)
			throw new InvalidPagingException("pageSize");
		if (pageSize != null && pageSize > maxAllowablePageSizeStatic) {
			throw new InvalidPagingException("pageSize [Over maximum allowable page size of [" + maxAllowablePageSizeStatic + "]] ");
		}
	}

	public static int getDefaultPageSize() {
		return defaultPageSizeStatic;
	}

	public static Pageable getPageRequest(Metadata metaData) {
		if (metaData == null) {
			metaData = new Metadata();
		}
		if (metaData.getPagination() == null) {
			metaData.setPagination(new IndexPagination());
		}
		return getPageRequest(metaData, null);
	}

	public static Pageable getPageRequest(Metadata metaData, Sort sort) {
		int page = 0;
		if (metaData.getPagination().getCurrentPage() != null && metaData.getPagination().getCurrentPage() >= 0) {
			page = metaData.getPagination().getCurrentPage();
		}

		int pageSize = defaultPageSizeStatic;
		if (metaData.getPagination().getPageSize() != null && metaData.getPagination().getPageSize() > 0) {
			pageSize = metaData.getPagination().getPageSize();
		}

		if (sort == null) {
			return PageRequest.of(page, pageSize);
		}
		return PageRequest.of(page, pageSize, sort);
	}

	public static void calculateMetaData(Metadata metaData, Page<?> page) {
		if (metaData == null) {
			metaData = new Metadata();
		}
		if (metaData.getPagination() == null) {
			metaData.setPagination(new IndexPagination());
		}
		// metaData.getPagination().setPageSize(page.getNumberOfElements());
		metaData.getPagination().setCurrentPage(page.getNumber());
		metaData.getPagination().setTotalCount((int) page.getTotalElements());
		metaData.getPagination().setTotalPages((int) page.getTotalPages());
	}

	public static <T> List<T> paginateSimpleList(List<T> list, Metadata metadata) {
		if (list != null && metadata != null) {
			metadata.getPagination().setTotalCount(list.size());
			calculateMetaData(metadata);
			
			List<T> subList = new ArrayList<>();
			int fromIndex = metadata.getPagination().getCurrentPage() * metadata.getPagination().getPageSize();
			int toIndex = fromIndex + metadata.getPagination().getPageSize();
			if(fromIndex < list.size()) {
				if (toIndex >= list.size()) {
					toIndex = list.size();
				}
				subList = list.subList(fromIndex, toIndex);
			}			
			return subList;
		}
		return list;
	}
}
