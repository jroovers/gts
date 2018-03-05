package com.ocs.gts.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.service.BaseService;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.LazyTabLayout;
import com.ocs.dynamo.ui.composite.layout.ServiceBasedDetailLayout;
import com.ocs.dynamo.ui.composite.layout.SimpleEditLayout;
import com.ocs.dynamo.ui.view.BaseView;
import com.ocs.gts.domain.Delivery;
import com.ocs.gts.domain.Organization;
import com.ocs.gts.domain.Person;
import com.ocs.gts.service.DeliveryService;
import com.ocs.gts.service.OrganizationService;
import com.ocs.gts.service.PersonService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;

@UIScope
@SpringView(name = Views.ORGANIZATION_DETAIL_VIEW)
public class OrganizationDetailView extends BaseView {

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PersonService personService;

	@Autowired
	private DeliveryService deliveryService;

	private static final long serialVersionUID = 5368745112390200786L;

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		Layout main = initLayout();
		GtsUI ui = (GtsUI) UI.getCurrent();
		ui.getSelectedOrganization();
		LazyTabLayout<Integer, Organization> layout = new LazyTabLayout<Integer, Organization>(
				ui.getSelectedOrganization()) {

			@Override
			protected String createTitle() {
				// TODO Auto-generated method stub
				return "View Organization";
			}

			@Override
			protected String[] getTabCaptions() {
				// TODO Auto-generated method stub
				return new String[] { "Organization Details", "Members", "Deliveries" };
			}

			@Override
			protected Component initTab(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					SimpleEditLayout<Integer, Organization> layoutTab0 = new SimpleEditLayout<Integer, Organization>(
							getEntity(), organizationService, getEntityModelFactory().getModel(Organization.class),
							new FormOptions().setOpenInViewMode(true).setShowEditButton(true));
					return layoutTab0;
				case 1:
					ServiceBasedDetailLayout<Integer, Person, Integer, Organization> layoutTab1 = new ServiceBasedDetailLayout<Integer, Person, Integer, Organization>(
							personService, getEntity(), organizationService,
							getEntityModelFactory().getModel("OrganizationPerson", Person.class), new FormOptions(),
							null) {

						private static final long serialVersionUID = -2898632662487449765L;

						protected com.vaadin.data.Container.Filter constructFilter() {
							return new com.vaadin.data.util.filter.Compare.Equal("organization", getEntity());
						};

						protected Person createEntity() {
							Person p = super.createEntity();
							p.setOrganization(getEntity());
							return p;
						};
					};
					return layoutTab1;
				case 2:
					ServiceBasedDetailLayout<Integer, Delivery, Integer, Organization> layoutTab2 = new ServiceBasedDetailLayout<Integer, Delivery, Integer, Organization>(
							deliveryService, getEntity(), organizationService,
							getEntityModelFactory().getModel("OrganizationDelivery", Delivery.class), new FormOptions(),
							null) {

						private static final long serialVersionUID = -2898374836248749765L;

						protected com.vaadin.data.Container.Filter constructFilter() {
							return new com.vaadin.data.util.filter.Compare.Equal("fromPerson.organization",
									getEntity());
						};
					};
					Map<String, Filter> fieldFilter = new HashMap<>();
					fieldFilter.put("fromPerson", new Compare.Equal("organization", getEntity()));
					layoutTab2.setFieldFilters(fieldFilter);

					return layoutTab2;
				default:
					return null;
				}
			}
		};
		main.addComponent(layout);
	}
}