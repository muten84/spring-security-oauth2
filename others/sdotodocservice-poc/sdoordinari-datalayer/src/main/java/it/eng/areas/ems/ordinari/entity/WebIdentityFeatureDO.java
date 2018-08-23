package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the WEB_IDENTITY_FEATURES database table.
 * 
 */
@Entity
@Table(name="WEB_IDENTITY_FEATURES")
public class WebIdentityFeatureDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="WEB_FEATURES_ID")
	private String webFeaturesId;

	@Column(name="WEB_IDENTITY_ID")
	private String webIdentityId;

	public WebIdentityFeatureDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWebFeaturesId() {
		return this.webFeaturesId;
	}

	public void setWebFeaturesId(String webFeaturesId) {
		this.webFeaturesId = webFeaturesId;
	}

	public String getWebIdentityId() {
		return this.webIdentityId;
	}

	public void setWebIdentityId(String webIdentityId) {
		this.webIdentityId = webIdentityId;
	}

}