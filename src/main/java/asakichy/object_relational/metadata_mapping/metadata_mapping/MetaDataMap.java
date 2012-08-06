package asakichy.object_relational.metadata_mapping.metadata_mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * メタデータのマッピング情報クラス.
 * 
 * テーブルとオブジェクト、テーブルカラムとオブジェクトフィールドをマッピングします.
 */

public class MetaDataMap {
	private Class<? extends DomainObject> domainClass;
	private String tableName;
	private List<ColumnMap> columnMaps = new ArrayList<ColumnMap>();

	public MetaDataMap(Class<? extends DomainObject> domainClass, String tableName) {
		this.domainClass = domainClass;
		this.tableName = tableName;
	}

	public void addColumn(String columnName, String type, String fieldName) {
		columnMaps.add(new ColumnMap(columnName, fieldName, this));
	}

	public Class<? extends DomainObject> getDomainClass() {
		return domainClass;
	}

	public String getTableName() {
		return tableName;
	}

	public List<ColumnMap> getColumns() {
		return columnMaps;
	}

	public String columnList() {
		StringBuilder columnList = new StringBuilder(" id");
		for (ColumnMap colmunMap : columnMaps) {
			columnList.append(", ");
			columnList.append(colmunMap.getColumnName());
		}
		return columnList.toString();
	}

	public String getUpdateList() {
		StringBuilder columnList = new StringBuilder(" SET ");
		for (ColumnMap colmunMap : columnMaps) {
			columnList.append(colmunMap.getColumnName());
			columnList.append("=?,");
		}
		return StringUtils.chop(columnList.toString());
	}

	public String getInsertList() {
		StringBuilder columnList = new StringBuilder("");
		for (int i = 0; i < columnMaps.size(); i++) {
			columnList.append(",?");
		}
		return columnList.toString();
	}
}
