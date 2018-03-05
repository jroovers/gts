package com.ocs.gts.ui;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.exception.OCSValidationException;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.form.ModelBasedEditForm;
import com.ocs.dynamo.ui.composite.layout.SimpleSearchLayout;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.view.BaseView;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.ocs.gts.domain.Organization;
import com.ocs.gts.service.OrganizationService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Like;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

@UIScope
@SpringView(name = Views.ORGANIZATION_VIEW)
public class OrganizationView extends LazyBaseView {

	@Autowired
	private OrganizationService organizationService;

	private static final long serialVersionUID = 3310122000037867336L;
	
	private SimpleSearchLayout<Integer, Organization> memberLayout;

	@Override
	protected Component build() {
		EntityModel<Organization> em = getModelFactory().getModel(Organization.class);
		FormOptions fo = new FormOptions().setShowEditButton(true).setShowRemoveButton(true).setShowToggleButton(true);
		SimpleSearchLayout<Integer, Organization> layout = new SimpleSearchLayout<Integer, Organization>(
				organizationService, em, QueryType.ID_BASED, fo, null) {

			@Override
			protected Organization createEntity() {
				Organization org = super.createEntity();
				org.setAddress("Daalakkersweg 16");
				return org;
			}

			@Override
			protected void postProcessEditFields(ModelBasedEditForm<Integer, Organization> editForm) {
				Field<?> reputationField = editForm.getField("reputation");
				final Field<?> yearlyMortality = editForm.getField("yearlyMortalityRate");
				reputationField.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 6522564810904403860L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						yearlyMortality.setValue(null);
					}
				});
			}

			@Override
			protected void postProcessButtonBar(Layout buttonBar) {
				// TODO Auto-generated method stub
				Button navigationButton = new Button("Details");
				registerButton(navigationButton);
				navigationButton.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
						GtsUI ui = (GtsUI) UI.getCurrent();
						ui.setSelectedOrganization(getSelectedItem());
						navigate("organizationDetailView");
					}
				});
				buttonBar.addComponent(navigationButton);
			};

		};

		memberLayout = layout;
		return memberLayout;
	}
	
	@Override
	protected void refresh() {
		// TODO Auto-generated method stub
		memberLayout.refresh();
	}
	
}
