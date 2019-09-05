package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import smartspace.dao.rdb.MapAttributesConverter;


@Entity
@Table(name="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {
	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private Date creationTimestamp;
	private boolean expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Map<String, Object> moreAttributes;
	

	public ElementEntity() {
	}
	
	@Transient
	public String getElementSmartspace() {
		return elementSmartspace;
	}
	

	public void setElementSmartspace(String elementSmartSpace) {
		this.elementSmartspace = elementSmartSpace;
	}
	
	@Transient
	public String getElementId() {
		return this.elementId;
	}
	
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	@Lob
	@Convert(converter=MapAttributesConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	@Id
	@Column(name="ELEMENT_KEY")
	public String getKey() {
		return elementSmartspace + "#" + elementId;
	}

	@Override
	public void setKey(String key) {
		String s[] = key.split("#");
		setElementSmartspace(s[0]);
		setElementId(s[1]);
		
	}



	@Override
	public String toString() {
		return "ElementEntity [elementSmartSpace=" + elementSmartspace + ", elementId=" + elementId + ", location="
				+ location + ", name=" + name + ", type=" + type + ", creationTimestamp=" + creationTimestamp
				+ ", expired=" + expired + ", creatorSmartspace=" + creatorSmartspace + ", "
						+ "creatorEmail=" + creatorEmail + "]";
	}
	
}
