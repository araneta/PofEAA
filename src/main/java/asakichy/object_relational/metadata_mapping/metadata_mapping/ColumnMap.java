package asakichy.object_relational.metadata_mapping.metadata_mapping;

import java.lang.reflect.Field;

import asakichy.object_relational.metadata_mapping.AppRuntimeException;

/**
 * 列レベルのメタデータのマッピング情報クラス.
 * 
 * テーブルカラムとオブジェクトフィールドをマッピングします.
 */

public class ColumnMap {
	private String columnName;
	private String fieldName;
	private Field field;
	private MetaDataMap dataMap;

	public ColumnMap(String columnName, String fieldName, MetaDataMap dataMap) {
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.dataMap = dataMap;
		initField();
	}

	public String getColumnName() {
		return columnName;
	}

	public void setField(Object subject, Object columnValue) {
		try {
			field.set(subject, columnValue);
		} catch (Exception e) {
			throw new AppRuntimeException(e);
		}
	}

	public Object getValue(Object subject) {
		try {
			return field.get(subject);
		} catch (Exception e) {
			throw new AppRuntimeException(e);
		}
	}

	private void initField() {
		try {
			field = dataMap.getDomainClass().getDeclaredField(fieldName);
			field.setAccessible(true);
		} catch (Exception e) {
			throw new AppRuntimeException(e);
		}
	}

}
