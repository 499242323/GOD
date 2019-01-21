package cn.com.egova.openapi.base.helper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSetMetaData;

/**
 * 结果集行映射工具类
 * 说明：暂时只支持MySQL，暂时 T只是java bean
 *
 * @param <T> MySQL常用字段 类型在java类型对应如下列表，请写java bean 时候注意对应
 *            ColumnType:bigint type:BIGINT -5 ClassName:java.lang.Long
 *            ColumnType:bit type:BIT -7 ClassName:java.lang.Boolean
 *            ColumnType:char type:CHAR 1 ClassName:java.lang.String
 *            ColumnType:date type:DATE 91 ClassName:java.sql.Date 只存年月日
 *            ColumnType:datetime type:TIMESTAMP 93 ClassName:java.sql.Timestamp 年月日时分秒 范围为'1000-01-01 00:00:00'到'9999-12-31 23:59:59'
 *            ColumnType:decimal type:DECIMAL 3 ClassName:java.math.BigDecimal
 *            ColumnType:double type:DOUBLE 8 ClassName:java.lang.Double
 *            ColumnType:float type:REAL 7 ClassName:java.lang.Float
 *            ColumnType:int type:INTEGER 4 ClassName:java.lang.Integer
 *            ColumnType:longtext type:LONGVARCHAR -1 ClassName:java.lang.String
 *            ColumnType:mediumint type: INTEGER 4 ClassName:java.lang.Integer
 *            ColumnType:real type:DOUBLE 8 ClassName:java.lang.Double
 *            ColumnType:time type:TIME 92 ClassName:java.sql.Time 只存时分秒
 *            ColumnType:tinyint type:TINYINT -6 ClassName:java.lang.Integer
 *            ColumnType:timestamp type:TIMESTAMP 93 ClassName:java.sql.Timestamp 年月日时分秒 范围为'1970-01-01 00:00:00'到'2037-12-31 23:59:59'
 *            ColumnType:tinytext type:LONGVARCHAR -1 ClassName:java.lang.String
 *            ColumnType:varchar type:VARCHAR 12 ClassName:java.lang.String
 *            ColumnType:year type:DATE 91 ClassName:java.sql.Date 只存年,月日0101
 * @author yindl
 */
public class ObjRowMapper<T> implements RowMapper<T> {

    private Class<T> rowClass;
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    Logger logger = Logger.getLogger(ObjRowMapper.class);

    public ObjRowMapper(Class<T> rowClass) {


        super();
        this.rowClass = rowClass;
        for (Method method : this.rowClass.getMethods()) {

            if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
                methodMap.put(method.getName().toUpperCase(), method);
            }
        }
    }

    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T object = null;
        //获取列数据
        ResultSetWrappingSqlRowSetMetaData wapping = new ResultSetWrappingSqlRowSetMetaData(rs.getMetaData());
        int columnCount = wapping.getColumnCount();
        int columnIndex = 0;
        for (; columnIndex++ != columnCount; ) {

            String columnClassName = wapping.getColumnClassName(columnIndex);
            Object value = rs.getObject(columnIndex);

            // 对于空值,如果不是字符串直接处理下一个字段
            if (value == null && !"java.lang.String".equals(columnClassName)) {
                continue;
            }

            //一般在读取首列数据创建对象
            if (columnIndex == 1 || object == null) {
                try {
                    object = this.rowClass.newInstance();
                } catch (Exception e) {
                    logger.error("未能初始化类的实例，请检查构造函数或者权限！", e);
                }
            }

            //找到和当前字段名称一致的对象属性设置方法，然后赋值,去掉数据库表字段的下划线java使用大小写区别，数据库字段没有大小写，只能使用下划线
            //getColumnName返回的是sql语句中field的原始名字。getColumnLabel是field的SQL AS的值
            String columnName = wapping.getColumnLabel(columnIndex).replaceAll("_", "");
            int type = wapping.getColumnType(columnIndex);

            Method method = methodMap.get("set".concat(columnName).toUpperCase());
            if (method == null) {
                continue;
            }

            String paramterType = method.getParameterTypes()[0].getName();
            //获取列值
            switch (type) {
                case Types.BIGINT:
                    value = rs.getLong(columnIndex);
                    break;
                case Types.CHAR:
                case Types.LONGVARCHAR:
                case Types.VARCHAR:
                case Types.CLOB:
                    value = (value == null ? "" : rs.getString(columnIndex));
                    break;//避免前台处理null
                case Types.TINYINT:
                case Types.INTEGER:
//				value = rs.getInt(columnIndex);
                    if (paramterType.equals("int")) {
                        value = rs.getBigDecimal(columnIndex).intValue();
                    } else if (paramterType.equals("long")) {
                        value = rs.getBigDecimal(columnIndex).longValue();
                    }
                    break;
                case Types.DOUBLE:
                    value = rs.getDouble(columnIndex);

                    break;
                case Types.DECIMAL:
                    value = rs.getBigDecimal(columnIndex);
                    try {
                        if (paramterType.equals("int")) {
                            value = rs.getBigDecimal(columnIndex).intValue();
                        } else if (paramterType.equals("long")) {
                            value = rs.getBigDecimal(columnIndex).longValue();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    break;
                case Types.REAL:
                    value = rs.getFloat(columnIndex);
                    break;
                case Types.BIT:
                    value = rs.getBoolean(columnIndex);
                    break;
                case Types.DATE:
                    value = rs.getDate(columnIndex);
                    break;
                case Types.TIME:
                    value = rs.getTime(columnIndex);
                    break;
                case Types.TIMESTAMP:
                    value = rs.getTimestamp(columnIndex);
                    break;
                case Types.NUMERIC:
                    if (paramterType.equals("int") || paramterType.equals("java.lang.Integer")) {
                        value = rs.getInt(columnIndex);
                    } else if (paramterType.equals("long") || paramterType.equals("java.lang.Long")) {
                        value = rs.getLong(columnIndex);
                    } else if (paramterType.equals("float") || paramterType.equals("java.lang.Float")) {
                        value = rs.getFloat(columnIndex);
                    } else if (paramterType.equals("double") || paramterType.equals("java.lang.Double")) {
                        value = rs.getDouble(columnIndex);
                    } else if (paramterType.equals("boolean") || paramterType.equals("java.lang.Boolean")) {
                        value = rs.getBoolean(columnIndex);
                    }
                    break;
            }

            //执行 setter
            try {
                method.invoke(object, value);
            } catch (Exception e) {

            }
        }
        return object;
    }

}
