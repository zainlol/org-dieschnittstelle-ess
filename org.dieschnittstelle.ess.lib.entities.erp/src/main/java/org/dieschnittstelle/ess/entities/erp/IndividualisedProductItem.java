package org.dieschnittstelle.ess.entities.erp;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(namespace = "http://dieschnittstelle.org/ess/entities/erp/ws")
@Entity
public class IndividualisedProductItem extends AbstractProduct implements Serializable {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(IndividualisedProductItem.class);

	private static final long serialVersionUID = 5109263395081656350L;

	private ProductType productType;

	private Integer expirationAfterStocked;
	
	public IndividualisedProductItem() {
		logger.info("<constructor>");
	}
	
	public IndividualisedProductItem(String name,ProductType type,int expirationAfterStocked) {
		super(name);
		this.productType = type;
		this.expirationAfterStocked = expirationAfterStocked;
	}
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	public int getExpirationAfterStocked() {
		return expirationAfterStocked;
	}

	public void setExpirationAfterStocked(int expirationAfterStocked) {
		this.expirationAfterStocked = expirationAfterStocked;
	}
	
	public String toString() {
		return "<IndividualisedProductItem " + this.getId() + ", " + this.getExpirationAfterStocked() + ", " + this.getName() + ", " + this.productType + ">";
	}
	
	public boolean equals(Object other) {
		
		if (other.getClass() != this.getClass()) 
			return false;
		
		return this.getId() == ((IndividualisedProductItem)other).getId();
	}
	
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@PostLoad
	public void onPostLoad() {
		logger.info("@PostLoad: " + this);
	}

	@PostPersist
	public void onPostPersist() {
		logger.info("@PostPersist: " + this);
	}

	@PostRemove
	public void onPostRemove() {
		logger.info("@PostRemove: " + this);
	}

	@PostUpdate
	public void onPostUpdate() {
		logger.info("@PostUpdate: " + this);
	}

	@PrePersist
	public void onPrePersist() {
		logger.info("@PrePersist: " + this);
	}

	@PreRemove
	public void onPreRemove() {
		logger.info("@PreRemove: " + this);
	}

	@PreUpdate
	public void onPreUpdate() {
		logger.info("@PreUpdate: " + this);
	}
}
