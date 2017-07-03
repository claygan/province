package com.province.platform.commons;

public class GlobalDefine {
	
	/** 
	 * @ClassName: commonConstants
	 * @Description: 公共常量
	 * 
	 * @author ganshimin@zhongzhihui.com
	 * @date: 2017年3月29日 下午2:34:48
	 */  
	public static class commonConstants{
		public static final int DEFUALT_EXPIRE = 7*24*3600;
		public static final int COMPANY_EXPIRE = 1*24*3600;
	}
	
	/** 
	 * @ClassName: resultCode
	 * @Description:返回状态码规范
	 * 
	 * @author ganshimin@zhongzhihui.com
	 * @date: 2017年3月29日 下午2:33:45
	 */  
	public static class resultCode {
		/** 
		 * @Description:返回成功
		 */  
		public static final int SUCCESS = 0;
		/** 
		 * @Description:后端接口未知错误
		 */  
		public static final int INTERNAL_ERROR = 1;
		/** 
		 * @Description:页面跳转响应
		 */  
		public static final int REDIRECT_ERROR = 2;
		/** 
		 * @Description:登录异常 
		 */  
		public static final int LOGIN_ERROR = -100;
		/** 
		 * @Todo:业务状态码扩展从100开始
		 */
		
	}
	/** 
	 * @ClassName: CourseConstants
	 * @Description: 课程相关常量
	 * 
	 * @author ganshimin@zhongzhihui.com
	 * @date: 2017年6月27日 下午3:32:44
	 */  
	public static class CourseConstants {

		/**
		 * 课程查询排序规则： 首页排序规则
		 */
		public static final String Q_ORDER_INDEX = "INDEX";
		/**
		 * 课程查询排序规则： 按创建时间倒序排列
		 */
		public static final String Q_ORDER_CREATE_DESC = "CREATE_DESC";
		/**
		 * 课程查询排序规则： 按最后更新时间倒序排列
		 */
		public static final String Q_ORDER_UPDATE_DESC = "UPDATE_DESC";
		/** 
		 * @Description: 按学习次数倒叙排列
		 */ 
		public static final String Q_ORDER_STUDY_COUNT_DESC = "STUDY_COUNT_DESC";
		/** 
		 * @Description:按直播课开始时间排序
		 */  
		public static final String Q_ORDER_START_TIME = "START_TIME";
		
		
		/**
		 * @Description: 观看视频是否需要登录：需要
		 */
		public static final int LOGIN_REQUIRE_YES = 0;
		/**
		 * @Description: 观看视频是否需要登录：不需要
		 */
		public static final int LOGIN_REQUIRE_NO = 1;

		/**
		 * @Description: 无标签课程
		 */
		public static final int TAG_FLAG_YES = 1;
		/**
		 * @Description: 有标签课程
		 */
		public static final int TAG_FLAG_NO = 0;

		/**
		 * @Description:免费
		 */
		public static final int COURSE_FEE_FREE = 1;

		/**
		 * @Description:收费
		 */
		public static final int COURSE_FEE_NOT_FREE = 2;

		/**
		 * @Description:会员
		 */
		public static final int COURSE_FEE_VIP = 3;
	}
	
}
