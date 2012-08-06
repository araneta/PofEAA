package asakichy.object_relational.behavioral.identity_map;

/**
 * ドメインオブジェクトの基本クラス.
 */

public class DomainObject {

	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DomainObject other = (DomainObject) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
