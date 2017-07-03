package com.province.platform.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.province.platform.commons.ApiResult;
import com.province.platform.commons.GlobalDefine;
import com.province.platform.commons.Pager;
import com.province.platform.constants.NewsCategoryEnum;
import com.province.platform.cookies.CookieUtil;
import com.province.platform.helper.ResourceHelper;
import com.province.platform.pojos.dto.ActivityCalendarDto;
import com.zzh.base.api.NewsRemoteService;
import com.zzh.base.api.SiteRemoteService;
import com.zzh.base.api.ThemedActivityRemoteService;
import com.zzh.base.api.constants.CheckStatusEnum;
import com.zzh.base.api.entity.ListThemedActivityQueryRequest;
import com.zzh.base.api.entity.NewsCategoryItem;
import com.zzh.base.api.entity.NewsCategoryQueryRequest;
import com.zzh.base.api.entity.NewsItem;
import com.zzh.base.api.entity.NewsQueryRequest;
import com.zzh.base.api.entity.activity.ThemedActivityItem;
import com.zzh.base.api.entity.site.SiteModuleItem;
import com.zzh.base.api.entity.site.SiteSectionItem;
import com.zzh.base.api.entity.site.SiteSectionQueryRequest;
import com.zzh.base.api.entity.site.SiteTemplateCompanyRequest;
import com.zzh.common.constants.SiteTemplateConstants;
import com.zzh.common.constants.vendor.ProductCheckStatusEnum;
import com.zzh.common.entity.Pageable;
import com.zzh.course.api.CourseRemoteService;
import com.zzh.course.api.CourseRemoteService.CourseBuilderEnum;
import com.zzh.course.api.entity.CourseItem;
import com.zzh.course.api.enums.CourseStatusEnum;
import com.zzh.course.api.enums.CourseTypeEnum;
import com.zzh.course.api.query.CourseQuery;
import com.zzh.vendor.api.ProductRemoteService;
import com.zzh.vendor.api.VendorRemoteService;
import com.zzh.vendor.api.entity.ProductItem;
import com.zzh.vendor.api.entity.ProductQueryRequest;
import com.zzh.vendor.api.entity.VendorItem;

@Controller
@RequestMapping(value={"","/"})
public class IndexController {
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Resource
	private SiteRemoteService siteRemoteService;
	@Resource
	private NewsRemoteService newsRemoteService;
	@Resource
	private ThemedActivityRemoteService themedActivityRemoteService;
	@Resource
	private CourseRemoteService courseRemoteService;
	@Resource
	private ProductRemoteService productRemoteService;
	@Resource
	private VendorRemoteService vendorRemoteService;
	
	
	/** 
	 * @Description:首页“最新资讯”展示个数
	 */  
	private static final int INDEX_NEW_NEWS_SHOW_SIZE = 9;
	/** 
	 * @Description:首页每个分类资讯展示个数
	 */  
	private static final int INDEX_NEWS_SHOW_NUM = 3;
	/** 
	 * @Description:首页展示课程个数
	 */  
	private static final int INDEX_COURSE_SHOW_NUM = 9;
	/** 
	 * @Description:首页展示服务产品个数
	 */  
	private static final int INDEX_PRODUCT_SHOW_NUM = 5;
	/** 
	 * @Description:首页展示活动个数
	 */  
	private static final int INDEX_ACTIVITY_SHOW_NUM = 9;
	/** 
	 * @Description:活动日历展示活动个数
	 */  
	private static final int INDEX_CALENDOR_ACTIVITY_SHOW_NUM = 20;
	
	@RequestMapping({ "", "/index" })
	public String toIndex(HttpServletRequest request,HttpServletResponse response, Model model) {
		Long companyId = CookieUtil.getCompanyId(request);
		//轮播图---------------------------------
		SiteTemplateCompanyRequest siteTemplateCompany = siteRemoteService.selectTemplateCompanyByCompanyId(companyId);
		if (siteTemplateCompany == null) {
			logger.error("companyId:"+companyId+", siteTemplateCompany查询为空");
		}
		long instanceId = siteTemplateCompany.getId();
		SiteModuleItem siteModuleItem = siteRemoteService.selectModuleByInstanceIdAndCode(instanceId, SiteTemplateConstants.MODULE_CODE_CAROUSEL_FIGURE);
		if (siteModuleItem == null) {
			logger.error("轮播图查询为空");
		}
		SiteSectionQueryRequest siteSectionQuery = new SiteSectionQueryRequest();
		siteSectionQuery.setDisabled(true);
		siteSectionQuery.setModuleId(siteModuleItem.getId());
		siteSectionQuery.setOrderBy("sort asc");
		List<SiteSectionItem> siteSectionItems = siteRemoteService.querySiteSection(siteSectionQuery);
		for (SiteSectionItem item : siteSectionItems) {
			item.setImgUrl(ResourceHelper.getDataUrl(item.getImage()));
		}
		model.addAttribute("indexCarousels", siteSectionItems);
		//最新资讯---------------------------------
		NewsQueryRequest newsQuery = new NewsQueryRequest();
		newsQuery.setType(NewsCategoryEnum.GENERAL_NEWS.getCode());//默认资讯动态
		newsQuery.setCompanyId(companyId);
		newsQuery.setDisabled(0);
		newsQuery.setCheckStatus(CheckStatusEnum.CHECK_PASS.getCode());// 已审批通过的
		newsQuery.setOrderBy("update_time desc");
		List<NewsItem> newsItems = newsRemoteService.queryNews(newsQuery, 0, INDEX_NEW_NEWS_SHOW_SIZE);
		model.addAttribute("newNewsItems", newsItems);
		//广告图----------------------------------
		SiteModuleItem adSiteModuleItem = siteRemoteService.selectModuleByInstanceIdAndCode(instanceId, SiteTemplateConstants.PROVINCE_STATIC_PIC);
		if (adSiteModuleItem == null) {
			logger.error("广告图查询为空");
		}
		SiteSectionQueryRequest adSiteSectionQuery = new SiteSectionQueryRequest();
		adSiteSectionQuery.setDisabled(true);
		adSiteSectionQuery.setModuleId(adSiteModuleItem.getId());
		adSiteSectionQuery.setOrderBy("sort asc");
		List<SiteSectionItem> adSiteSectionItems = siteRemoteService.querySiteSection(adSiteSectionQuery);
		for (SiteSectionItem item : adSiteSectionItems) {
			item.setImgUrl(ResourceHelper.getDataUrl(item.getImage()));
			//左上
			if(SiteTemplateConstants.LEFT_TOP_TYPE.equals(item.getType())){
				model.addAttribute("adImages1", item);
			//右上
			}else if(SiteTemplateConstants.RIGHT_TOP_TYPE.equals(item.getType())){
				model.addAttribute("adImages2", item);
			//右上
			}else if(SiteTemplateConstants.LEFT_BOTTOM_TYPE.equals(item.getType())){
				model.addAttribute("adImages3", item);
			//右上
			}else if(SiteTemplateConstants.RIGHT_BOTTOM_TYPE.equals(item.getType())){
				model.addAttribute("adImages4", item);
			}
		}
		//活动沙龙----------------------------------
		ListThemedActivityQueryRequest listThemedActivityQuery = new ListThemedActivityQueryRequest();
		listThemedActivityQuery.setCompanyId(companyId);
		listThemedActivityQuery.setOrderBy("start_time desc");//活动以开始时间来进行排序
		listThemedActivityQuery.setPage(1);
		listThemedActivityQuery.setPageSize(INDEX_ACTIVITY_SHOW_NUM);
		com.zzh.base.api.entity.Pageable<ThemedActivityItem> activityItems = themedActivityRemoteService.queryPage(listThemedActivityQuery);
		model.addAttribute("activityItems", activityItems.getRows());
		return "/views/index";
	}

	
	
	/** 
	 * @Title: queryIndexNews
	 * @Description: 查询首页资讯热点
	 * @author ganshimin@zhongzhihui.com
	 * @param request
	 * @param response
	 * @return  
	 */  
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/queryIndexNews")
	public ApiResult queryIndexNews(HttpServletRequest request, HttpServletResponse response){
		ApiResult result = new ApiResult();
		Pager pager = new Pager();
		try {
			Long companyId = CookieUtil.getCompanyId(request);
			String showModel = request.getParameter("showModel");//模块
			
			NewsQueryRequest queryRequest = new NewsQueryRequest();
			queryRequest.setCheckStatus(CheckStatusEnum.CHECK_PASS.getCode());
			//获取分类信息
			Map<String, Long> indexCategoryMap = getIndexCategory(companyId);
			if(indexCategoryMap.get(showModel) == null){
				throw new Exception("本平台暂无该分类");
			}
			queryRequest.setCategoryIds(new long[]{indexCategoryMap.get(showModel)});
			queryRequest.setCompanyId(companyId);
			queryRequest.setDisabled(0);
			queryRequest.setOrderBy("create_time desc");
			List<NewsItem> newsItems = newsRemoteService.queryNews(queryRequest, 0, INDEX_NEWS_SHOW_NUM);
			pager.setResultList(newsItems);
			result.setData(pager);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg(e.getMessage()==null?"系统出错":e.getMessage());
			result.setError(GlobalDefine.resultCode.INTERNAL_ERROR);
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("queryIndexCourse")
	@ResponseBody
	public ApiResult queryIndexCourse(HttpServletRequest request, HttpServletResponse response){
		ApiResult result = new ApiResult();
		Pager pager = new Pager();
		try {
			Long companyId = CookieUtil.getCompanyId(request);
			Long userId = CookieUtil.getUserId(request);
			String courseType = request.getParameter("type");
			
			CourseQuery courseQuery = new CourseQuery();
			courseQuery.setCompanyId(companyId);
			courseQuery.setStatus(CourseStatusEnum.ONLINE.getCode());//课程是否下架处理
			if("live".equalsIgnoreCase(courseType)){
				courseQuery.setOrderBy(GlobalDefine.CourseConstants.Q_ORDER_START_TIME);//直播课按开始时间排序
				courseQuery.setTypes(new int[]{CourseTypeEnum.LIVE.getCode()});
			}else if("video".equalsIgnoreCase(courseType)){
				courseQuery.setOrderBy(GlobalDefine.CourseConstants.Q_ORDER_CREATE_DESC);//点播课按创建时间排序
				courseQuery.setTypes(new int[]{CourseTypeEnum.VIDEO.getCode()});
			}
			courseQuery.setStartOffset(0);
			courseQuery.setPageSize(INDEX_COURSE_SHOW_NUM);
			
			Map<CourseBuilderEnum, Object> builderMap = new HashMap<CourseRemoteService.CourseBuilderEnum, Object>();
			builderMap.put(CourseRemoteService.CourseBuilderEnum.COVERIMAGE, null);//封面图片
			builderMap.put(CourseRemoteService.CourseBuilderEnum.TIME_FORMATTER, null);//时间格式化
			List<CourseItem> courseItems = courseRemoteService.queryCourse(courseQuery, companyId, userId, builderMap);
			if("live".equalsIgnoreCase(courseType)){
				for (CourseItem courseItem : courseItems) {
					courseItem.setTime(courseItem.getStartTimeStr());
				}
			}
			pager.setResultList(courseItems);
			result.setData(pager);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("系统出错");
			result.setError(GlobalDefine.resultCode.INTERNAL_ERROR);
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("queryIndexProduct")
	@ResponseBody
	public ApiResult queryIndexProduct(HttpServletRequest request, HttpServletResponse response){
		ApiResult result = new ApiResult();
		Pager pager = new Pager();
		try {
			Long companyId = CookieUtil.getCompanyId(request);
			
			ProductQueryRequest productQueryRequest = new ProductQueryRequest();
			productQueryRequest.setCompanyId(companyId);
			productQueryRequest.setCheckStatus(ProductCheckStatusEnum.PASS.getCode());
			productQueryRequest.setPage(1);
			productQueryRequest.setPageSize(INDEX_PRODUCT_SHOW_NUM);
			Pageable<ProductItem> pageable = productRemoteService.pageProductItems(productQueryRequest);
			List<ProductItem> productItems = new ArrayList<ProductItem>();
			for (ProductItem item : pageable.getRows()) {
				VendorItem vendorItem = vendorRemoteService.findById(item.getVendorId());
				if(vendorItem != null){
					item.setVendorName(vendorItem.getName());
				}
				productItems.add(item);
			}
			if(pageable != null){
				pager.setResultList(productItems);
			}
			result.setData(pager);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("系统出错");
			result.setError(GlobalDefine.resultCode.INTERNAL_ERROR);
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("queryIndexCalendar")
	@ResponseBody
	public ApiResult queryIndexCalendar(HttpServletRequest request, HttpServletResponse response){
		ApiResult result = new ApiResult();
		try {
			Long companyId = CookieUtil.getCompanyId(request);
			ListThemedActivityQueryRequest listThemedActivityQuery = new ListThemedActivityQueryRequest();
			listThemedActivityQuery.setCompanyId(companyId);
			listThemedActivityQuery.setOrderBy("start_time desc");//活动以开始时间来进行排序
			listThemedActivityQuery.setPage(1);
			listThemedActivityQuery.setPageSize(INDEX_CALENDOR_ACTIVITY_SHOW_NUM);
			com.zzh.base.api.entity.Pageable<ThemedActivityItem> themedActivityItems = themedActivityRemoteService.queryPage(listThemedActivityQuery);
			List<ActivityCalendarDto> currentActivityItems = new ArrayList<ActivityCalendarDto>();
			for(ThemedActivityItem themedActivityItem : themedActivityItems.getRows()){
				Date theDate = themedActivityItem.getStartTime();
				Calendar c = Calendar.getInstance();
				c.setTime(theDate);
				int $_year = c.get(Calendar.YEAR);
				int $_month = c.get(Calendar.MONTH)+1;
				int $_day = c.get(Calendar.DAY_OF_MONTH);
				ActivityCalendarDto activityCalendarDto = new ActivityCalendarDto();
				activityCalendarDto.setId(themedActivityItem.getId());
				activityCalendarDto.setName(themedActivityItem.getName());
				activityCalendarDto.setYear($_year);
				activityCalendarDto.setMonth($_month);
				activityCalendarDto.setDay($_day);
				currentActivityItems.add(activityCalendarDto);
			}
			result.setData(currentActivityItems);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("系统出错");
			result.setError(GlobalDefine.resultCode.INTERNAL_ERROR);
		}
		return result;
	}
	
	/** 
	 * @Title: getIndexCategory
	 * @Description: 查询首页展示分类信息放入到暂存区
	 * @author ganshimin@zhongzhihui.com
	 * @param companyId
	 * @throws Exception  
	 */  
	private Map<String, Long> getIndexCategory(Long companyId) throws Exception{
		Map<String, Long> categoryMap = new HashMap<String, Long>();
		NewsCategoryQueryRequest categoryQueryRequest = new NewsCategoryQueryRequest();
		categoryQueryRequest.setCompanyId(companyId);
		categoryQueryRequest.setType(NewsCategoryEnum.GENERAL_NEWS.getCode());
		List<NewsCategoryItem> categoryItems = newsRemoteService.queryCategorys(categoryQueryRequest);
		if(CollectionUtils.isEmpty(categoryItems)){
			throw new Exception("未查询到该平台的资讯分类信息");
		}
		for (NewsCategoryItem item : categoryItems) {
			if("电商动态".equals(item.getName()) || "行业观点".equals(item.getName())){
				categoryMap.put("1", item.getId());
			}else if("数据方向".equals(item.getName()) || "数据透视".equals(item.getName())){
				categoryMap.put("2", item.getId());
			}else if("农村电商".equals(item.getName())){
				categoryMap.put("3", item.getId());
			}
		}
		return categoryMap;
	}
	
	
}
