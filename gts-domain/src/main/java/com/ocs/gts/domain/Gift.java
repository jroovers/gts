package com.ocs.gts.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.Model;

/**
 * An expensive gift
 * 
 * @author bas.rutten
 *
 */
@Entity
@Table(name = "gift")
@Model(displayName = "Horse head", displayProperty = "name")
public class Gift extends AbstractEntity<Integer> {

	private static final long serialVersionUID = -3436199710873943375L;

	@Id
	@SequenceGenerator(name = "gift_id_gen", sequenceName = "gift_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gift_id_gen")
	private Integer id;

	@Attribute(embedded = true)
	@JoinColumn(name = "logo")
	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	private GiftLogo logo = new GiftLogo();

	@NotNull
	@Size(max = 255)
	private String name;

	@NotNull
	@Size(max = 255)
	private String description;

	@Valid
	@OneToMany(mappedBy = "gift", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST }, orphanRemoval = true)
	@Attribute(complexEditable = true)
	private Set<GiftTranslation> translations = new HashSet<>();

	public void addTranslation(GiftTranslation translation) {
		this.translations.add(translation);
		translation.setGift(this);
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public GiftLogo getLogo() {
		if (logo == null) {
			logo = new GiftLogo();
		}
		return logo;
	}

	public String getName() {
		return name;
	}

	public Set<GiftTranslation> getTranslations() {
		return translations;
	}

	public void removeTranslation(GiftTranslation translation) {
		this.translations.remove(translation);
		translation.setGift(null);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLogo(GiftLogo logo) {
		this.logo = logo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTranslations(Set<GiftTranslation> translations) {
		this.translations = translations;
	}
}
