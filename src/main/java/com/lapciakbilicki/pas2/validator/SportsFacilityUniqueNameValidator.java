package com.lapciakbilicki.pas2.validator;

import com.lapciakbilicki.pas2.model.sportsfacility.SportsFacility;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.List;

@FacesValidator("sportsFacilityUniqueNameValidator")
public class SportsFacilityUniqueNameValidator implements Validator {

    private final String engMessage = "None unique name";
    private final String plMessage = "Podano nieunikalną nazwa";

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        List<SportsFacility> sportsFacilities = (List<SportsFacility>) uiComponent.getAttributes().get("allFacility");
        String oldName = (String) uiComponent.getAttributes().get("name");
        String name = (String) o;
        SportsFacility findSportsFacility = sportsFacilities.stream()
                .filter(sportsFacility -> sportsFacility.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (findSportsFacility != null) {
            if (oldName == null) {
                throw new ValidatorException(new FacesMessage(facesContext.getViewRoot().getLocale().toString().equals("en") ? engMessage : plMessage));
            } else {
                if (!oldName.equals(name)) {
                    throw new ValidatorException(new FacesMessage(facesContext.getViewRoot().getLocale().toString().equals("en") ? engMessage : plMessage));
                }
            }
        }

    }
}
