package com.jeesite.modules.shipreport.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.shipreport.entity.ShipReport;
import com.jeesite.modules.shipreport.dao.ShipReportDao;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.config.Global;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.common.utils.excel.ExcelImport;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 船舶报告表Service
 * @author 王浩宇
 * @version 2024-06-21
 */
@SuppressWarnings("ALL")
@Service
public class ShipReportService extends CrudService<ShipReportDao, ShipReport> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ShipReportDao shipReportDao;
	/**
	 * 获取单条数据
	 * @param shipReport
	 * @return
	 */
	@Override
	public ShipReport get(ShipReport shipReport) {
		return super.get(shipReport);
	}
	
	/**
	 * 查询分页数据
	 * @param shipReport 查询条件
	 * @param shipReport page 分页对象
	 * @return
	 */
	@Override
	public Page<ShipReport> findPage(ShipReport shipReport) {
		return super.findPage(shipReport);
	}
	
	/**
	 * 查询列表数据
	 * @param shipReport
	 * @return
	 */
	@Override
	public List<ShipReport> findList(ShipReport shipReport) {
		return super.findList(shipReport);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param shipReport
	 */
	@Override
	@Transactional
	public void save(ShipReport shipReport) {
		super.save(shipReport);
	}

	/**
	 * 导入数据
	 * @param file 导入的数据文件
	 */
	@Transactional
	public String importData(MultipartFile file) {
		if (file == null){
			throw new ServiceException(text("请选择导入的数据文件！"));
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<ShipReport> list = ei.getDataList(ShipReport.class);
			for (ShipReport shipReport : list) {
				try{
					ValidatorUtils.validateWithException(shipReport);
					this.save(shipReport);
					successNum++;
					successMsg.append("<br/>" + successNum + "、编号 " + shipReport.getId() + " 导入成功");
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、编号 " + shipReport.getId() + " 导入失败：";
					if (e instanceof ConstraintViolationException){
						ConstraintViolationException cve = (ConstraintViolationException)e;
						for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
							msg += Global.getText(violation.getMessage()) + " ("+violation.getPropertyPath()+")";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			failureMsg.append(e.getMessage());
			return failureMsg.toString();
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}
	
	/**
	 * 更新状态
	 * @param shipReport
	 */
	@Override
	@Transactional
	public void updateStatus(ShipReport shipReport) {
		super.updateStatus(shipReport);
	}
	
	/**
	 * 删除数据
	 * @param shipReport
	 */
	@Override
	@Transactional
	public void delete(ShipReport shipReport) {
		super.delete(shipReport);
	}

	public List<String> findAllReportingAgencies() {
		return null;
	}

	public List<String> findAllBerths() {
		return null;
	}


	/**
	 * 获取船舶进出港统计数据
	 */
	public Map<String, Object> getStatisticsData(Date startDate, Date endDate) {
		Map<String, Object> stats = new HashMap<>();
		
		// 计算环比时间段
		long timeDiff = endDate.getTime() - startDate.getTime();
		Date prevStartDate = new Date(startDate.getTime() - timeDiff);
		Date prevEndDate = new Date(startDate.getTime() - 1);
		
		// 使用单次SQL查询获取所有统计数据
		String sql = "SELECT " +
				"COUNT(*) as totalCount, " +
				"SUM(CASE WHEN in_out_port LIKE '%进%' THEN 1 ELSE 0 END) as arrivalCount, " +
				"SUM(CASE WHEN in_out_port LIKE '%出%' THEN 1 ELSE 0 END) as departureCount, " +
				"SUM(CASE WHEN sea_river_ship LIKE '%内河船%' THEN 1 ELSE 0 END) as riverCount, " +
				"SUM(CASE WHEN sea_river_ship LIKE '%海船%' THEN 1 ELSE 0 END) as seaCount, " +
				"SUM(CASE WHEN is_hazardous_cargo = '是' THEN 1 ELSE 0 END) as hazardousCount, " +
				"SUM(CASE WHEN ship_length < 80 THEN 1 ELSE 0 END) as smallCount, " +
				"SUM(CASE WHEN ship_length >= 80 AND ship_length < 150 THEN 1 ELSE 0 END) as mediumCount, " +
				"SUM(CASE WHEN ship_length >= 150 THEN 1 ELSE 0 END) as largeCount " +
				"FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ?";
		
		Map<String, Object> currentStats = jdbcTemplate.queryForMap(sql, startDate, endDate);
		
		// 查询环比数据
		Map<String, Object> previousStats = jdbcTemplate.queryForMap(sql, prevStartDate, prevEndDate);

		// 查询各海事机构船舶数量
		String agencySql = "SELECT reporting_agency as name, COUNT(*) as count " +
				"FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ? " +
				"GROUP BY reporting_agency " +
				"ORDER BY count DESC";

		List<Map<String, Object>> agencyData = jdbcTemplate.queryForList(agencySql, startDate, endDate);
		
		// 查询各泊位船舶数量
		String berthSql = "SELECT berth as name, COUNT(*) as count " +
				"FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ? " +
				"GROUP BY berth " +
				"ORDER BY count DESC";

		List<Map<String, Object>> berthData = jdbcTemplate.queryForList(berthSql, startDate, endDate);
		
		// 查询独立的海事机构列表
		String agenciesSql = "SELECT DISTINCT reporting_agency FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ? " +
				"ORDER BY reporting_agency";

		List<String> agencies = jdbcTemplate.queryForList(agenciesSql, String.class, startDate, endDate);
		
		// 查询独立的泊位列表
		String berthsSql = "SELECT DISTINCT berth FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ? " +
				"ORDER BY berth";

		List<String> berths = jdbcTemplate.queryForList(berthsSql, String.class, startDate, endDate);
		
		// 组装返回结果
		stats.put("totalShips", createStatisticsMap(currentStats.get("totalCount"), previousStats.get("totalCount")));
		stats.put("arrivalShips", createStatisticsMap(currentStats.get("arrivalCount"), previousStats.get("arrivalCount")));
		stats.put("departureShips", createStatisticsMap(currentStats.get("departureCount"), previousStats.get("departureCount")));
		stats.put("riverShips", createStatisticsMap(currentStats.get("riverCount"), previousStats.get("riverCount")));
		stats.put("seaShips", createStatisticsMap(currentStats.get("seaCount"), previousStats.get("seaCount")));
		stats.put("hazardousShips", createStatisticsMap(currentStats.get("hazardousCount"), previousStats.get("hazardousCount")));
		stats.put("smallShips", createStatisticsMap(currentStats.get("smallCount"), previousStats.get("smallCount")));
		stats.put("mediumShips", createStatisticsMap(currentStats.get("mediumCount"), previousStats.get("mediumCount")));
		stats.put("largeShips", createStatisticsMap(currentStats.get("largeCount"), previousStats.get("largeCount")));
		
		// 添加图表数据
		stats.put("agencyData", agencyData);
		stats.put("berthData", berthData);
		stats.put("agencies", agencies);
		stats.put("berths", berths);
		
		return stats;
	}

	/**
	 * 获取按筛选条件过滤的数据
	 */
	public Map<String, Object> getFilteredStatistics(Date startDate, Date endDate,
													 String portDirection, String shipType, String hazardous, String lengthRange,
													 String agency, String berth) {
		Map<String, Object> result = new HashMap<>();
		
		// 构建基础SQL
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ");
		sqlBuilder.append("COUNT(*) as totalCount, ");
		sqlBuilder.append("SUM(CASE WHEN in_out_port LIKE '%进%' THEN 1 ELSE 0 END) as arrivalCount, ");
		sqlBuilder.append("SUM(CASE WHEN in_out_port LIKE '%出%' THEN 1 ELSE 0 END) as departureCount, ");
		sqlBuilder.append("SUM(CASE WHEN sea_river_ship LIKE '%内河船%' THEN 1 ELSE 0 END) as riverCount, ");
		sqlBuilder.append("SUM(CASE WHEN sea_river_ship LIKE '%海船%' THEN 1 ELSE 0 END) as seaCount, ");
		sqlBuilder.append("SUM(CASE WHEN is_hazardous_cargo = '是' THEN 1 ELSE 0 END) as hazardousCount, ");
		sqlBuilder.append("SUM(CASE WHEN ship_length < 80 THEN 1 ELSE 0 END) as smallCount, ");
		sqlBuilder.append("SUM(CASE WHEN ship_length >= 80 AND ship_length < 150 THEN 1 ELSE 0 END) as mediumCount, ");
		sqlBuilder.append("SUM(CASE WHEN ship_length >= 150 THEN 1 ELSE 0 END) as largeCount ");
		sqlBuilder.append("FROM ship_report WHERE estimated_arrival_departure_time >= ? AND estimated_arrival_departure_time <= ?");
		
		// 构建参数列表
		List<Object> params = new ArrayList<>();
		params.add(startDate);
		params.add(endDate);
		
		// 添加筛选条件
		if ("in".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%进%'");
		} else if ("out".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%出%'");
		}
		
		if ("river".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%内河船%'");
		} else if ("sea".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%海船%'");
		}
		
		if ("yes".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo = '是'");
		} else if ("no".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo != '是'");
		}
		
		if ("small".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length < 80");
		} else if ("medium".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 80 AND ship_length < 150");
		} else if ("large".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 150");
		}
		
		if (agency != null && !"all".equals(agency)) {
			sqlBuilder.append(" AND reporting_agency = ?");
			params.add(agency);
		}
		
		if (berth != null && !"all".equals(berth)) {
			sqlBuilder.append(" AND berth = ?");
			params.add(berth);
		}
		
		// 执行查询
		Map<String, Object> filteredStats = jdbcTemplate.queryForMap(sqlBuilder.toString(), params.toArray());
		
		// 查询各海事机构船舶数量（带筛选条件）
		StringBuilder agencySqlBuilder = new StringBuilder();
		agencySqlBuilder.append("SELECT reporting_agency as name, COUNT(*) as count ");
		agencySqlBuilder.append("FROM ship_report WHERE estimated_arrival_departure_time >= ? AND estimated_arrival_departure_time <= ?");
		
		List<Object> agencyParams = new ArrayList<>();
		agencyParams.add(startDate);
		agencyParams.add(endDate);
		
		// 添加筛选条件
		if ("in".equals(portDirection)) {
			agencySqlBuilder.append(" AND in_out_port LIKE '%进%'");
		} else if ("out".equals(portDirection)) {
			agencySqlBuilder.append(" AND in_out_port LIKE '%出%'");
		}
		
		if ("river".equals(shipType)) {
			agencySqlBuilder.append(" AND sea_river_ship LIKE '%内河船%'");
		} else if ("sea".equals(shipType)) {
			agencySqlBuilder.append(" AND sea_river_ship LIKE '%海船%'");
		}
		
		if ("yes".equals(hazardous)) {
			agencySqlBuilder.append(" AND is_hazardous_cargo = '是'");
		} else if ("no".equals(hazardous)) {
			agencySqlBuilder.append(" AND is_hazardous_cargo != '是'");
		}
		
		if ("small".equals(lengthRange)) {
			agencySqlBuilder.append(" AND ship_length < 80");
		} else if ("medium".equals(lengthRange)) {
			agencySqlBuilder.append(" AND ship_length >= 80 AND ship_length < 150");
		} else if ("large".equals(lengthRange)) {
			agencySqlBuilder.append(" AND ship_length >= 150");
		}
		
		if (agency != null && !"all".equals(agency)) {
			agencySqlBuilder.append(" AND reporting_agency = ?");
			agencyParams.add(agency);
		}
		
		if (berth != null && !"all".equals(berth)) {
			agencySqlBuilder.append(" AND berth = ?");
			agencyParams.add(berth);
		}
		
		agencySqlBuilder.append(" GROUP BY reporting_agency ORDER BY count DESC");
		
		List<Map<String, Object>> agencyData = jdbcTemplate.queryForList(agencySqlBuilder.toString(), agencyParams.toArray());
		
		// 查询各泊位船舶数量（带筛选条件）
		StringBuilder berthSqlBuilder = new StringBuilder();
		berthSqlBuilder.append("SELECT berth as name, COUNT(*) as count ");
		berthSqlBuilder.append("FROM ship_report WHERE estimated_arrival_departure_time >= ? AND estimated_arrival_departure_time <= ?");
		
		List<Object> berthParams = new ArrayList<>();
		berthParams.add(startDate);
		berthParams.add(endDate);
		
		// 添加筛选条件
		if ("in".equals(portDirection)) {
			berthSqlBuilder.append(" AND in_out_port LIKE '%进%'");
		} else if ("out".equals(portDirection)) {
			berthSqlBuilder.append(" AND in_out_port LIKE '%出%'");
		}

		if ("river".equals(shipType)) {
			berthSqlBuilder.append(" AND sea_river_ship LIKE '%内河船%'");
		} else if ("sea".equals(shipType)) {
			berthSqlBuilder.append(" AND sea_river_ship LIKE '%海船%'");
		}

		if ("yes".equals(hazardous)) {
			berthSqlBuilder.append(" AND is_hazardous_cargo = '是'");
		} else if ("no".equals(hazardous)) {
			berthSqlBuilder.append(" AND is_hazardous_cargo != '是'");
		}

		if ("small".equals(lengthRange)) {
			berthSqlBuilder.append(" AND ship_length < 80");
		} else if ("medium".equals(lengthRange)) {
			berthSqlBuilder.append(" AND ship_length >= 80 AND ship_length < 150");
		} else if ("large".equals(lengthRange)) {
			berthSqlBuilder.append(" AND ship_length >= 150");
		}

		if (agency != null && !"all".equals(agency)) {
			berthSqlBuilder.append(" AND reporting_agency = ?");
			berthParams.add(agency);
		}

		if (berth != null && !"all".equals(berth)) {
			berthSqlBuilder.append(" AND berth = ?");
			berthParams.add(berth);
		}
		
		berthSqlBuilder.append(" GROUP BY berth ORDER BY count DESC");
		
		List<Map<String, Object>> berthData = jdbcTemplate.queryForList(berthSqlBuilder.toString(), berthParams.toArray());
		
		// 组装返回结果
		result.put("totalShips", createStatisticsMap(filteredStats.get("totalCount"), 0));
		result.put("arrivalShips", createStatisticsMap(filteredStats.get("arrivalCount"), 0));
		result.put("departureShips", createStatisticsMap(filteredStats.get("departureCount"), 0));
		result.put("riverShips", createStatisticsMap(filteredStats.get("riverCount"), 0));
		result.put("seaShips", createStatisticsMap(filteredStats.get("seaCount"), 0));
		result.put("hazardousShips", createStatisticsMap(filteredStats.get("hazardousCount"), 0));
		result.put("smallShips", createStatisticsMap(filteredStats.get("smallCount"), 0));
		result.put("mediumShips", createStatisticsMap(filteredStats.get("mediumCount"), 0));
		result.put("largeShips", createStatisticsMap(filteredStats.get("largeCount"), 0));
		
		// 添加图表数据
		result.put("agencyData", agencyData);
		result.put("berthData", berthData);
		
		return result;
	}

	/**
	 * 获取趋势数据
	 */
	public List<Map<String, Object>> getTrendData(Date startDate, Date endDate, String interval,
												 String portDirection, String shipType, String hazardous, String lengthRange,
												 String agency, String berth) {
		// 根据时间间隔确定日期格式和分组格式
		String dateFormat;
		String groupFormat;

		switch (interval) {
			case "day":
				dateFormat = "DATE_FORMAT(estimated_arrival_departure_time, '%Y-%m-%d')";
				groupFormat = "DATE_FORMAT(estimated_arrival_departure_time, '%Y-%m-%d')";
				break;
			case "week":
				dateFormat = "DATE_FORMAT(DATE_SUB(estimated_arrival_departure_time, INTERVAL WEEKDAY(estimated_arrival_departure_time) DAY), '%Y-%m-%d')";
				groupFormat = "DATE_FORMAT(DATE_SUB(estimated_arrival_departure_time, INTERVAL WEEKDAY(estimated_arrival_departure_time) DAY), '%Y-%m-%d')";
				break;
			case "month":
				dateFormat = "DATE_FORMAT(estimated_arrival_departure_time, '%Y-%m')";
				groupFormat = "DATE_FORMAT(estimated_arrival_departure_time, '%Y-%m')";
				break;
			case "quarter":
				dateFormat = "CONCAT(YEAR(estimated_arrival_departure_time), 'Q', QUARTER(estimated_arrival_departure_time))";
				groupFormat = "CONCAT(YEAR(estimated_arrival_departure_time), 'Q', QUARTER(estimated_arrival_departure_time))";
				break;
			case "year":
				dateFormat = "YEAR(estimated_arrival_departure_time)";
				groupFormat = "YEAR(estimated_arrival_departure_time)";
				break;
			default:
				dateFormat = "DATE_FORMAT(estimated_arrival_departure_time, '%Y-%m-%d')";
				groupFormat = "DATE_FORMAT(estimated_arrival_departure_time, '%Y-%m-%d')";
		}

		// 构建SQL查询
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ");
		sqlBuilder.append(dateFormat).append(" as date, ");
		sqlBuilder.append("COUNT(*) as totalCount, ");
		sqlBuilder.append("SUM(CASE WHEN in_out_port LIKE '%进%' THEN 1 ELSE 0 END) as arrivalCount, ");
		sqlBuilder.append("SUM(CASE WHEN in_out_port LIKE '%出%' THEN 1 ELSE 0 END) as departureCount, ");
		sqlBuilder.append("SUM(CASE WHEN sea_river_ship LIKE '%内河船%' THEN 1 ELSE 0 END) as riverCount, ");
		sqlBuilder.append("SUM(CASE WHEN sea_river_ship LIKE '%海船%' THEN 1 ELSE 0 END) as seaCount, ");
		sqlBuilder.append("SUM(CASE WHEN is_hazardous_cargo = '是' THEN 1 ELSE 0 END) as hazardousCount ");
		sqlBuilder.append("FROM ship_report WHERE estimated_arrival_departure_time >= ? AND estimated_arrival_departure_time <= ?");
		
		// 构建参数列表
		List<Object> params = new ArrayList<>();
		params.add(startDate);
		params.add(endDate);
		
		// 添加筛选条件
		if ("in".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%进%'");
		} else if ("out".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%出%'");
		}
		
		if ("river".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%内河船%'");
		} else if ("sea".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%海船%'");
		}
		
		if ("yes".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo = '是'");
		} else if ("no".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo != '是'");
		}
		
		if ("small".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length < 80");
		} else if ("medium".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 80 AND ship_length < 150");
		} else if ("large".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 150");
		}
		
		if (agency != null && !"all".equals(agency)) {
			sqlBuilder.append(" AND reporting_agency = ?");
			params.add(agency);
		}
		
		if (berth != null && !"all".equals(berth)) {
			sqlBuilder.append(" AND berth = ?");
			params.add(berth);
		}
		
		sqlBuilder.append(" GROUP BY ").append(groupFormat).append(" ORDER BY date");
		
		// 执行查询
		List<Map<String, Object>> groupedStats = jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());

		// 准备趋势数据系列
		List<Map<String, Object>> trendData = new ArrayList<>();

		// 初始化各系列数据
		Map<String, Object> totalSeries = new HashMap<>();
		totalSeries.put("name", "总船舶数");
		List<Map<String, Object>> totalData = new ArrayList<>();

		Map<String, Object> arrivalSeries = new HashMap<>();
		arrivalSeries.put("name", "进港船舶");
		List<Map<String, Object>> arrivalData = new ArrayList<>();

		Map<String, Object> departureSeries = new HashMap<>();
		departureSeries.put("name", "出港船舶");
		List<Map<String, Object>> departureData = new ArrayList<>();

		Map<String, Object> riverSeries = new HashMap<>();
		riverSeries.put("name", "内河船");
		List<Map<String, Object>> riverData = new ArrayList<>();

		Map<String, Object> seaSeries = new HashMap<>();
		seaSeries.put("name", "海船");
		List<Map<String, Object>> seaData = new ArrayList<>();

		Map<String, Object> hazardousSeries = new HashMap<>();
		hazardousSeries.put("name", "危化品船");
		List<Map<String, Object>> hazardousData = new ArrayList<>();

		// 处理分组统计数据
		for (Map<String, Object> stat : groupedStats) {
			String date = (String) stat.get("date");
			Long totalCount = ((Number) stat.get("totalCount")).longValue();
			Long arrivalCount = ((Number) stat.get("arrivalCount")).longValue();
			Long departureCount = ((Number) stat.get("departureCount")).longValue();
			Long riverCount = ((Number) stat.get("riverCount")).longValue();
			Long seaCount = ((Number) stat.get("seaCount")).longValue();
			Long hazardousCount = ((Number) stat.get("hazardousCount")).longValue();
			
			// 总船舶数
			Map<String, Object> totalPoint = new HashMap<>();
			totalPoint.put("date", date);
			totalPoint.put("value", totalCount);
			totalData.add(totalPoint);

			// 进港船舶
			Map<String, Object> arrivalPoint = new HashMap<>();
			arrivalPoint.put("date", date);
			arrivalPoint.put("value", arrivalCount);
			arrivalData.add(arrivalPoint);

			// 出港船舶
			Map<String, Object> departurePoint = new HashMap<>();
			departurePoint.put("date", date);
			departurePoint.put("value", departureCount);
			departureData.add(departurePoint);

			// 内河船
			Map<String, Object> riverPoint = new HashMap<>();
			riverPoint.put("date", date);
			riverPoint.put("value", riverCount);
			riverData.add(riverPoint);

			// 海船
			Map<String, Object> seaPoint = new HashMap<>();
			seaPoint.put("date", date);
			seaPoint.put("value", seaCount);
			seaData.add(seaPoint);

			// 危化品船
			Map<String, Object> hazardousPoint = new HashMap<>();
			hazardousPoint.put("date", date);
			hazardousPoint.put("value", hazardousCount);
			hazardousData.add(hazardousPoint);
		}

		// 设置各系列数据
		totalSeries.put("data", totalData);
		arrivalSeries.put("data", arrivalData);
		departureSeries.put("data", departureData);
		riverSeries.put("data", riverData);
		seaSeries.put("data", seaData);
		hazardousSeries.put("data", hazardousData);

		// 添加所有系列到趋势数据
		trendData.add(totalSeries);
		trendData.add(arrivalSeries);
		trendData.add(departureSeries);
		trendData.add(riverSeries);
		trendData.add(seaSeries);
		trendData.add(hazardousSeries);

		return trendData;
	}

	/**
	 * 获取独立的海事机构列表
	 */
	public List<String> getDistinctAgencies(Date startDate, Date endDate) {
		String sql = "SELECT DISTINCT reporting_agency FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ? " +
				"ORDER BY reporting_agency";
		
		return jdbcTemplate.queryForList(sql, String.class, startDate, endDate);
	}
	
	/**
	 * 获取独立的泊位列表
	 */
	public List<String> getDistinctBerths(Date startDate, Date endDate) {
		String sql = "SELECT DISTINCT berth FROM ship_report " +
				"WHERE estimated_arrival_departure_time >= ? " +
				"AND estimated_arrival_departure_time <= ? " +
				"ORDER BY berth";
		
		return jdbcTemplate.queryForList(sql, String.class, startDate, endDate);
	}

	private Map<String, Object> createStatisticsMap(Object current, Object previous) {
		Map<String, Object> map = new HashMap<>();
		map.put("current", current);
		map.put("previous", previous);
		return map;
	}

	// 获取船舶类型分布数据
	public List<Map<String, Object>> getShipTypeDistribution(Date startDate, Date endDate, 
														 String portDirection, String shipType, 
														 String hazardous, String lengthRange, 
														 String agency, String berth) {
		// 构建SQL查询
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ship_type as name, COUNT(*) as value ");
		sqlBuilder.append("FROM ship_report WHERE estimated_arrival_departure_time >= ? AND estimated_arrival_departure_time <= ?");
		
		// 构建参数列表
		List<Object> params = new ArrayList<>();
		params.add(startDate);
		params.add(endDate);
		
		// 添加筛选条件
		if ("in".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%进%'");
		} else if ("out".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%出%'");
		}
		
		if ("river".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%内河船%'");
		} else if ("sea".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%海船%'");
		}
		
		if ("yes".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo = '是'");
		} else if ("no".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo != '是'");
		}
		
		if ("small".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length < 80");
		} else if ("medium".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 80 AND ship_length < 150");
		} else if ("large".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 150");
		}
		
		if (agency != null && !"all".equals(agency)) {
			sqlBuilder.append(" AND reporting_agency = ?");
			params.add(agency);
		}
		
		if (berth != null && !"all".equals(berth)) {
			sqlBuilder.append(" AND berth = ?");
			params.add(berth);
		}
		
		sqlBuilder.append(" GROUP BY ship_type ORDER BY value DESC");
		
		// 执行查询并返回结果
		return jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());
	}

	// 获取货物类型分布数据（按装卸货物吨位累计）
	public List<Map<String, Object>> getCargoTypeDistribution(Date startDate, Date endDate, 
														  String portDirection, String shipType, 
														  String hazardous, String lengthRange, 
														  String agency, String berth) {
		// 构建SQL查询
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT cargo_type as name, SUM(loading_unloading_cargo_volume) as value ");
		sqlBuilder.append("FROM ship_report WHERE estimated_arrival_departure_time >= ? AND estimated_arrival_departure_time <= ?");
		
		// 构建参数列表
		List<Object> params = new ArrayList<>();
		params.add(startDate);
		params.add(endDate);
		
		// 添加筛选条件
		if ("in".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%进%'");
		} else if ("out".equals(portDirection)) {
			sqlBuilder.append(" AND in_out_port LIKE '%出%'");
		}
		
		if ("river".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%内河船%'");
		} else if ("sea".equals(shipType)) {
			sqlBuilder.append(" AND sea_river_ship LIKE '%海船%'");
		}
		
		if ("yes".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo = '是'");
		} else if ("no".equals(hazardous)) {
			sqlBuilder.append(" AND is_hazardous_cargo != '是'");
		}
		
		if ("small".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length < 80");
		} else if ("medium".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 80 AND ship_length < 150");
		} else if ("large".equals(lengthRange)) {
			sqlBuilder.append(" AND ship_length >= 150");
		}
		
		if (agency != null && !"all".equals(agency)) {
			sqlBuilder.append(" AND reporting_agency = ?");
			params.add(agency);
		}
		
		if (berth != null && !"all".equals(berth)) {
			sqlBuilder.append(" AND berth = ?");
			params.add(berth);
		}
		
		sqlBuilder.append(" GROUP BY cargo_type ORDER BY value DESC");
		
		// 执行查询并返回结果
		return jdbcTemplate.queryForList(sqlBuilder.toString(), params.toArray());
	}
}