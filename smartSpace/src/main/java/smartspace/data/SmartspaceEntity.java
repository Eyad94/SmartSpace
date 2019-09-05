package smartspace.data;

public interface SmartspaceEntity<T> {
	public T getKey();
	public void setKey (T key);
}
