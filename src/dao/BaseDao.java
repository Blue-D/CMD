package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.spec.PSource;

import bean.competition;

/**
 * 数据库底层类
 * 
 * @author 命空
 *
 */
public class BaseDao {

	// 链接数据库所使用的信息
	private static String username;
	private static String password;
	private static String ip;
	private static String port;
	private static String DaoName;
	private static String ChaEncoding;

	private static String url;

	static {
		ini();
	}

	/**
	 * 通过配置文件初始化数据库
	 */
	private static void ini() {
		Properties pro = new Properties();
		try {
			InputStream in = BaseDao.class.getResourceAsStream("Dao.properties");
			pro.load(in);
			username = pro.getProperty("username");
			password = pro.getProperty("password");
			ip = pro.getProperty("ip");
			port = pro.getProperty("port");
			DaoName = pro.getProperty("DaoName");
			ChaEncoding = pro.getProperty("characterEncoding");
			url = "jdbc:mysql://" + ip + ":" + port + "/" + DaoName + "?characterEncoding=" + ChaEncoding;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	protected static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("缺少jar");
		} catch (SQLException e) {
			System.out.println("数据连接失败");
		}
		return conn;
	}

	/**
	 * 向表中插入数据
	 * 
	 * @param tablename
	 *            表名
	 * @param name_data
	 *            数据名与数据的映射
	 * @return
	 */
	protected static boolean insert(String tablename, Map<String, Object> name_data) {
		// 1.获取数据库连接
		Connection conn = getConn();
		String dataname = "("; // 数据名的集合
		String values = "("; // 有几条数据(几个问号)
		// 2.迭代映射生成dataname与values,然后通过这个生成sql
		for (String name : name_data.keySet()) {
			dataname += name + ",";
			values += "?,";
		}
		dataname = dataname.substring(0, dataname.length() - 1) + ")";
		values = values.substring(0, values.length() - 1) + ")";
		// 3.生成sql
		String sql = "insert into " + tablename + " " + dataname + " values" + values;
		// 4.插入数据库
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			int i = 1;
			for (String name : name_data.keySet()) {
				String type = DaoUtil.GetTypeString(name_data.get(name).getClass());
				try {
					Method m = pstmt.getClass().getMethod("set" + type, int.class,
							DaoUtil.getType(name_data.get(name)));
					m.invoke(pstmt, i, name_data.get(name));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				i++;
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			System.out.println("成功写入数据");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 用事务实现简单insert 主义本方法中部提交,需要将传进来的conn在外部手动提交
	 * 
	 * @param t
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static <T> int Insert(T t) {
		// 拼装数据名
		Connection conn = getConn();
		Field[] fs = t.getClass().getDeclaredFields();
		String dataname = "(";
		String values = "(";
		int count = 0;
		List<Object> list = new ArrayList<>();
		for (Field f : fs) {
			try {
				Method get = t.getClass().getMethod(DaoUtil.CreateGetOSet("get", f.getName()));
				Object o = get.invoke(t);
				if (o != null) {
					dataname += f.getName() + ",";
					values += "?,";
					list.add(o);
					count++;
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (count == 0) {
			return 0;
		} else {
			dataname = dataname.substring(0, dataname.length() - 1) + ")";
			values = values.substring(0, values.length() - 1) + ")";
		}
		// 2.拼装SQL
		String SQL = "INSERT INTO " + t.getClass().getSimpleName() + dataname + " VALUES " + values;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(SQL);
			for (int i = 1; i <= count; i++) {
				pstmt.setString(i, list.get(i - 1).toString());
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 用事务实现简单insert 主义本方法中部提交,需要将传进来的conn在外部手动提交
	 * 
	 * @param t
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static <T> PreparedStatement Insert(T t, Connection conn) throws SQLException {
		// 拼装数据名
		Field[] fs = t.getClass().getDeclaredFields();
		String dataname = "(";
		String values = "(";
		int count = 0;
		List<Object> list = new ArrayList<>();
		for (Field f : fs) {
			try {
				Method get = t.getClass().getMethod(DaoUtil.CreateGetOSet("get", f.getName()));
				Object o = get.invoke(t);
				if (o != null) {
					dataname += f.getName() + ",";
					values += "?,";
					list.add(o);
					count++;
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (count == 0) {
			return null;
		} else {
			dataname = dataname.substring(0, dataname.length() - 1) + ")";
			values = values.substring(0, values.length() - 1) + ")";
		}
		// 2.拼装SQL
		String SQL = "INSERT INTO " + t.getClass().getSimpleName() + dataname + " VALUES " + values;
		PreparedStatement pstmt = conn.prepareStatement(SQL);
		for (int i = 1; i <= count; i++) {
			pstmt.setString(i, list.get(i - 1).toString());
		}
		return pstmt;
	}

	/**
	 * 只含有插入操作的事务管理，如果该记录存在唯一标识，且唯一标识重复则为更新对应信息
	 * 
	 * @param t
	 * @return
	 */
	public static boolean Insertcommit(List<Object> list) {
		Connection conn = getConn();
		PreparedStatement[] pstmts = new PreparedStatement[list.size()];
		try {
			conn.setAutoCommit(false);
			int i = 0;
			for (Object obj : list) {
				try {
					// 1.判断是否存在唯一标识组
					Method Pk = obj.getClass().getMethod("GetPrivateKey");
					String PK_name = (String) Pk.invoke(obj);
					if (PK_name == null) {
						pstmts[i] = Insert(obj, conn);
						pstmts[i].executeUpdate();
						continue;
					}
					// 2.条件组装
					String[] pks = PK_name.split(";");
					String[] condition = new String[pks.length];
					for (int p = 0; p <= pks.length - 1; p++) {
						Method Get = obj.getClass().getMethod(DaoUtil.CreateGetOSet("get", pks[p]));
						String value = (String) Get.invoke(obj);
						condition[p] = pks[p] + "='" + value + "'";
					}
					// 3.先更新数据，如果更新失败则插入
					if (PK_name != null) {
						pstmts[i] = Update(obj, condition, conn);
						int x = pstmts[i].executeUpdate();
						if (x == 0) {
							pstmts[i] = Insert(obj, conn);
							pstmts[i].executeUpdate();
						}
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
				i++;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
		} finally {
			try {
				for (PreparedStatement ps : pstmts) {
					if (ps != null)
						ps.close();
				}
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

	/**
	 * 当数据名的数据等于数据时，输出表中的数据
	 * 
	 * @param tablename
	 *            表名
	 * @param dataname
	 *            数据名
	 * @param data
	 *            数据
	 * @return
	 * @throws SQLException
	 */
	protected static void delete(Connection conn, String tablename, Map<String, String> condition) throws SQLException {
		// 1.拼装条件
		String con = DaoUtil.ConditionInstall(condition);
		// 2.瓶装SQL
		String sql = "delete from " + tablename + " where " + con;
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}

	/**
	 * * 只可以执行最简单的查询 Select dataname from tablename where condition dataname为空默认
	 * 全部读取
	 * 
	 * @param tablename
	 * @param dataname
	 * @param condition
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> Select(String tablename, String[] dataname, String[] condtion)
			throws ClassNotFoundException {
		List<T> result = new ArrayList<>();
		// 1.访问数据拼接
		String dn = "";
		if (dataname != null) {
			for (String s : dataname) {
				dn += s + ",";
			}
			dn = dn.substring(0, dn.length() - 1);
		} else {
			dn = "*";
		}
		// 2.条件拼接
		String ct = "";
		if (condtion != null) {
			for (String s : condtion) {
				ct += s + " and ";
			}
			ct = ct.substring(0, ct.length() - 5);
		} else {
			ct = "1=1";
		}
		// 3.sql拼装
		String sql = "SELECT " + dn + " From " + tablename + " WHERE " + ct;
		// 4.获取connection
		Connection conn = getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 5.获取statement对象
			pstmt = conn.prepareStatement(sql);
			// 6.获取结果集
			rs = pstmt.executeQuery();
			int colum = rs.getMetaData().getColumnCount();
			// 7.尝试着装载以tablename为名的bean类若装载失败直接抛出异常
			Class tc = Class.forName("bean." + tablename);
			while (rs.next()) {
				T t = null;
				try {
					t = (T) tc.newInstance();
				} catch (InstantiationException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (int i = 1; i <= colum; i++) {
					String dName = rs.getMetaData().getColumnName(i);
					dName = DaoUtil.BigToSmalle(dName);
					try {
						// 8.创建该属性对应的Get方法
						Class type = DaoUtil.GetType(tc, dName);
						if(type.getSimpleName().equals("Year")==false){
							Method get = rs.getClass().getMethod(DaoUtil.CreateGetOSet("get", type.getSimpleName()),int.class);
							Method Set = tc.getMethod(DaoUtil.CreateGetOSet("set", dName), type);
							Set.invoke(t, get.invoke(rs, i));
						}else{
							Method Set = tc.getMethod(DaoUtil.CreateGetOSet("set", dName), type);
							Set.invoke(t, Year.of(Integer.valueOf(rs.getString(i).substring(0, 4))));
						}
						// 9.创造该属性的对应的Set方法
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				result.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取一个一个表的在某个条件下的指定的信息 最简单的 select ？？？ from ？ where ？
	 * 
	 * @param tablename
	 * @param objclass
	 * @return
	 */
	public static List<Map> getDataWithCondition(String tablename, String[] dataname, String[] condition) {
		// 1.获取数据库连接
		Connection conn = getConn();
		// 2.瓶装SQL
		String dn = "";
		if (dataname != null) {
			for (String s : dataname) {
				dn += s + ",";
			}
			dn = dn.substring(0, dn.length() - 1);
		} else {
			dn = "*";
		}
		// 2.条件拼接
		String ct = "";
		if (condition != null) {
			for (String s : condition) {
				ct += s + ",";
			}
			ct = ct.substring(0, ct.length() - 1);
		} else {
			ct = "1=1";
		}
		String sql = "select " + dn + " from " + tablename + " where " + ct;
		// 3.获取数据
		List<Map> list = new ArrayList();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Map<String, String> map = new HashMap();
				for (int i = 1; i <= col; i++) {
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
				list.add(map);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * 删除表
	 * 
	 * @param tablename
	 * @return
	 */
	public static boolean DeleteTable(String tablename) {
		Connection con = getConn();
		try {
			Statement stmt = (Statement) con.createStatement();
			String sql = "DROP TABLE " + tablename + " ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * 清空表
	 * 
	 * @param tablename
	 * @return
	 */
	public static boolean Truncate(String tablename) {
		Connection con = getConn();
		try {
			Statement stmt = (Statement) con.createStatement();
			String sql = "TRUNCATE TABLE " + tablename + " ";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * 创建表
	 * 
	 * @param tablename
	 * @param colist
	 * @return
	 */
	public static boolean CreateTable(String tablename, String[] colist) {
		Connection con = getConn();
		try {
			Statement stmt = (Statement) con.createStatement();
			String sql = "CREATE TABLE " + tablename + " (";
			for (String co : colist) {
				sql += co + " VARCHAR(30),";
			}
			sql = sql.substring(0, sql.length() - 1) + ")engine=innoDB default charset=utf8";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获得所有的表名
	 * 
	 * @return
	 */
	public static List<String> GetAllTables() {
		Connection conn = getConn();
		List<String> tables = new ArrayList<>();
		try {
			DatabaseMetaData dbmd = (DatabaseMetaData) conn.getMetaData();
			ResultSet rs = dbmd.getTables(conn.getCatalog(), null, null, null);
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
				tables.add(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	/**
	 * 获取一个表里面的键名与属性
	 * 
	 * @param typenum
	 * @return
	 */
	protected static Map<String, String> GetTableAllCol(String TableName) {
		Connection conn = getConn();
		Map<String, String> cols = new HashMap<>();
		try {
			DatabaseMetaData dbmd = (DatabaseMetaData) conn.getMetaData();
			ResultSet rs = dbmd.getColumns(conn.getCatalog(), null, TableName, null);
			while (rs.next()) {
				cols.put(rs.getString("COLUMN_NAME"), DaoUtil.TypesToType(rs.getInt("DATA_TYPE")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cols;
	}

	/**
	 * 获取一个表里面的列名与属性
	 * 
	 * @param TableName
	 * @return
	 */
	protected static Map<String, String> GetTablePrimaryKeys(String TableName) {
		Connection conn = getConn();
		Map<String, String> cols = new HashMap<>();
		try {
			DatabaseMetaData dbmd = (DatabaseMetaData) conn.getMetaData();
			ResultSet rs = dbmd.getColumns(conn.getCatalog(), null, TableName, null);
			while (rs.next()) {
				cols.put(rs.getString("COLUMN_NAME"), DaoUtil.TypesToType(Integer.valueOf(rs.getString("DATA_TYPE"))));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cols;
	}

	/**
	 * 获取表中所有的列名与私有键(类型为JAVA中的类名)
	 * 
	 * @param TableName
	 * @return
	 */
	public static Map<String, String> GetTableFormatAllColAndKey(String TableName) {
		Map<String, String> col = BaseDao.GetTableAllCol(TableName);
		Map<String, String> PriKey = BaseDao.GetTablePrimaryKeys(TableName);
		Map<String, String> map = new HashMap<>();
		map.putAll(col);
		map.putAll(PriKey);
		for (String name : map.keySet()) {
			map.replace(name, DaoUtil.TypeNameFormat(map.get(name)));
		}
		return map;
	}

	/**
	 * JDBC事务处理模型封装
	 * 
	 * @return
	 */
	public static boolean Commit(List<String> sqllist) {
		Connection conn = getConn();
		try {
			conn.setAutoCommit(false);
			for (String sql : sqllist) {
				if (PreExcute(conn, sql) == -1)
					throw new SQLException();
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 预执行SQL语句并返回影响条数，建议用于非查询语句
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected static int PreExcute(Connection conn, String sql) {
		PreparedStatement pstmt = null;
		int i = -1;
		try {
			pstmt = conn.prepareStatement(sql);
			i = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	/**
	 * 获取所有表的名称列表
	 * 
	 * @return
	 */
	public static List<String> GetAllTablesnames() {
		List<String> tables_name = new ArrayList<>();
		Connection conn = getConn();
		try {
			DatabaseMetaData m_DBMetaData = conn.getMetaData();
			ResultSet tableRet = m_DBMetaData.getTables(null, "%", "%", new String[] { "TABLE" });
			while (tableRet.next()) {
				tables_name.add(tableRet.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables_name;
	}

	/**
	 * 
	 * @param t
	 * @param condition
	 * @return
	 */
	public static <T> int Update(T t, String[] condition) {
		// 1.更新拼装
		Field[] fs = t.getClass().getDeclaredFields();
		String data = "";
		int count = 0;
		List<Object> list = new ArrayList<>();
		for (Field f : fs) {
			try {
				Method get = t.getClass().getMethod(DaoUtil.CreateGetOSet("get", f.getName()));
				Object o = get.invoke(t);
				if (o != null) {
					data += f.getName() + "=?,";
					list.add(o);
					count++;
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (count == 0) {
			return 0;
		} else {
			data = data.substring(0, data.length() - 1);
		}
		// 2.条件拼装
		String ct = "";
		if (condition != null) {
			for (String s : condition) {
				ct += s + " and ";
			}
			ct = ct.substring(0, ct.length() - 5);
		} else {
			ct = "1=1";
		}
		// 3.SQL拼装
		String sql = "UPDATE " + t.getClass().getSimpleName() + " Set " + data + " WHERE " + ct;
		// 4.执行
		Connection conn = getConn();
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int x = 1; x <= list.size(); x++) {
				pstmt.setString(x, list.get(x - 1).toString());
			}
			i = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return i;
	}

	/**
	 * 用于事务的update
	 * 
	 * @param t
	 * @param condition
	 * @return
	 * @throws SQLException
	 */
	public static <T> PreparedStatement Update(T t, String[] condition, Connection conn) throws SQLException {
		// 1.更新拼装
		Field[] fs = t.getClass().getDeclaredFields();
		String data = "";
		int count = 0;
		List<Object> list = new ArrayList<>();
		for (Field f : fs) {
			try {
				Method get = t.getClass().getMethod(DaoUtil.CreateGetOSet("get", f.getName()));
				Object o = get.invoke(t);
				if (o != null) {
					data += f.getName() + "=?,";
					list.add(o);
					count++;
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (count == 0) {
			return null;
		} else {
			data = data.substring(0, data.length() - 1);
		}
		// 2.条件拼装
		String ct = "";
		if (condition != null) {
			for (String s : condition) {
				ct += s + " and ";
			}
			ct = ct.substring(0, ct.length() - 5);
		} else {
			ct = "1=1";
		}
		// 3.SQL拼装
		String sql = "UPDATE " + t.getClass().getSimpleName() + " Set " + data + " WHERE " + ct;
		// 4.执行
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		for (int x = 1; x <= list.size(); x++) {
			pstmt.setString(x, list.get(x - 1).toString());
		}
		return pstmt;
	}
}