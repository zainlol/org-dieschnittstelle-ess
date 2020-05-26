package org.dieschnittstelle.ess.entities.erp;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "bundle")
public class ProductBundle implements Serializable {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProductBundle.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1501911067906145681L;

	@Id
	@GeneratedValue
	private long id;

	@Id
	@ManyToOne
	private IndividualisedProductItem product;

	private int units;

	public ProductBundle() {
		logger.info("<constructor>");
	}

	public ProductBundle(IndividualisedProductItem product, int units) {
		this.product = product;
		this.units = units;
	}

	public IndividualisedProductItem getProduct() {
		return this.product;
	}

	public void setProduct(IndividualisedProductItem product) {
		this.product = product;
	}

	public int getUnits() {
		return this.units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public String toString() {
		return "<ProductBundle " + this.product + " (" + this.units + ")>";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, new String[] { "id" });
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
