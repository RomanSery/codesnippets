package org.coderdreams.webapp.page;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dao.CarRepository;
import org.coderdreams.dom.Car;
import org.coderdreams.enums.CarCondition;
import org.coderdreams.util.UIHelpers;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.wicketfields.FieldArgs.Builder;
import org.coderdreams.wicketfields.event.InitPanelFieldsEvent;
import org.coderdreams.wicketfields.fields.bool.CheckBoxField;
import org.coderdreams.wicketfields.fields.dropdown.AjaxDropdownField;
import org.coderdreams.wicketfields.fields.numeric.AjaxNumberSpinnerField;
import org.coderdreams.wicketfields.fields.text.AjaxTxtField;
import org.coderdreams.wicketfields.fields.text.TxtField;
import org.coderdreams.wicketfields.form.SingleClickIndicatingAjaxButton;
import org.coderdreams.wicketfields.resources.UiFieldsBehavior;

public class ComplexCreateCarPage extends BasePage implements IBasePage {

    @SpringBean private CarRepository carRepository;

    private FeedbackPanel feedbackPanel;
    private Car newCar;
    private WebMarkupContainer rareCar;
    private CheckBoxField engineStartsField;

    public ComplexCreateCarPage() {
        super();
        newCar = new Car();

        Form<Car> carForm = new Form<Car>("carForm");
        add(carForm);

        feedbackPanel = new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(carForm));
        add(feedbackPanel.setOutputMarkupId(true));

        rareCar = new WebMarkupContainer("rareCar") {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(isRareCar(newCar));
            }
        };
        carForm.add(rareCar.setOutputMarkupPlaceholderTag(true));

        carForm.add(new AjaxNumberSpinnerField<Integer>(Builder.of("year", "Year", objModel(newCar::getYear, newCar::setYear)).inputType(Integer.class).min(1920).max(2020).build()) {
            public void onFieldChanged(AjaxRequestTarget target) { target.add(rareCar); }
        });
        carForm.add(new AjaxTxtField<String>(Builder.of("make", "Make", objModel(newCar::getMake, newCar::setMake)).build()) {
            public void onFieldChanged(AjaxRequestTarget target) { target.add(rareCar); }
        });
        carForm.add(new AjaxTxtField<String>(Builder.of("model", "Model", objModel(newCar::getModel, newCar::setModel)).build()) {
            public void onFieldChanged(AjaxRequestTarget target) { target.add(rareCar); }
        });

        rareCar.add(new TxtField<Integer>(Builder.of("mileage", "Mileage", objModel(newCar::getMileage, newCar::setMileage)).build()));
        rareCar.add(new AjaxDropdownField<CarCondition>(Builder.of("condition", "Condition", objModel(newCar::getCondition, newCar::setCondition))
                .choiceList(new ListModel<CarCondition>(CarCondition.VALUES)).cr(UIHelpers.getEnumChoiceRenderer()).build()) {
            public void onFieldChanged(AjaxRequestTarget target) { target.add(engineStartsField); }
        });
        rareCar.add(engineStartsField = new CheckBoxField(Builder.of("engineStarts", "Engine Starts?", objModel(newCar::getEngineStarts, newCar::setEngineStarts)).build()) {
            @Override
            public void onConfigure() {
                super.onConfigure();
                setVisible(newCar.getCondition() == CarCondition.BAD);
            }
        });



        carForm.add(new SingleClickIndicatingAjaxButton("saveButton", carForm, true, null) {
            @Override protected void onSubmit(AjaxRequestTarget target) {
                String errMsg = NewCarValidator.validate(newCar);
                if(errMsg != null) {
                    error(errMsg);
                    target.add(feedbackPanel);
                    return;
                }
                carRepository.save(newCar);
            }
        });

        add(new UiFieldsBehavior());
        send(this, Broadcast.BREADTH, new InitPanelFieldsEvent(null));
    }

    private boolean isRareCar(Car car) {
        if(car == null) {
            return false;
        }
        if(Objects.equals(car.getYear(), 1952) && "Chevy".equals(car.getMake()) && "Super".equals(car.getModel())) {
            return true;
        }
        return false;
    }

    private static class NewCarValidator {
        static String validate(Car car) {
            if(car.getYear() == null) {
                return "Enter year";
            }
            if(StringUtils.isBlank(car.getMake())) {
                return "Enter make";
            }
            if(StringUtils.isBlank(car.getModel())) {
                return "Enter model";
            }
            return null;
        }
    }
}
