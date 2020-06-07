package org.coderdreams.webapp.page;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.dao.CarRepository;
import org.coderdreams.dom.Car;
import org.coderdreams.webapp.BasePage;
import org.coderdreams.wicketfields.FieldArgs.Builder;
import org.coderdreams.wicketfields.event.InitPanelFieldsEvent;
import org.coderdreams.wicketfields.fields.numeric.NumberSpinnerField;
import org.coderdreams.wicketfields.fields.text.TxtField;
import org.coderdreams.wicketfields.form.SingleClickIndicatingAjaxButton;
import org.coderdreams.wicketfields.resources.UiFieldsBehavior;

public class SimpleCreateCarPage extends BasePage implements IBasePage {

    @SpringBean private CarRepository carRepository;

    private FeedbackPanel feedbackPanel;
    private Car newCar;

    public SimpleCreateCarPage() {
        super();
        newCar = new Car();

        Form<Car> carForm = new Form<Car>("carForm");
        add(carForm);

        feedbackPanel = new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(carForm));
        add(feedbackPanel.setOutputMarkupId(true));

        carForm.add(new NumberSpinnerField<Integer>(Builder.of("year", "Year", objModel(newCar::getYear, newCar::setYear)).inputType(Integer.class).min(1920).max(2020).build()));
        carForm.add(new TxtField<String>(Builder.of("make", "Make", objModel(newCar::getMake, newCar::setMake)).build()));
        carForm.add(new TxtField<String>(Builder.of("model", "Model", objModel(newCar::getModel, newCar::setModel)).build()));

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
